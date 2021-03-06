package de.fau.fablab.app.server.core.toolusage;

import de.fau.fablab.app.rest.core.FabTool;
import de.fau.fablab.app.rest.core.ToolUsage;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.List;

/**
 * DAO to handle machine/tool usage.
 */
public class ToolUsageDAO extends AbstractDAO<ToolUsage> {

    private static final String TABLE_NAME = "ToolUsage";

    // offset in milliseconds when finished machine usage entries should be deleted
    private static final long DELETE_OFFSET = 15 * 60 * 1000;

    public ToolUsageDAO (SessionFactory factory) {
        super(factory);
    }

    /**
     * Add a usage item to a specific tool, takes care about the sets new usage item as ancestor of first item without
     * successor
     *
     * @param usage
     * @return
     */
    ToolUsage create(ToolUsage usage) {

        // look for tool and connect it to Usage
        List<FabTool> toolList = super.currentSession().createQuery("FROM FabTool WHERE id = :tool_id")
                .setParameter("tool_id", usage.getToolId())
                .list();

        if (toolList.size() != 1)
            return null;

        usage.setTool(toolList.get(0));

        // persist!
        ToolUsage newUsage = persist(usage);

        // look for ancestor and set (me) new successor
        // the one without successor is the last one
        List<ToolUsage> usageList = super.currentSession().createQuery("FROM " + TABLE_NAME + " WHERE tool_id = :tool_id AND successor_id IS NULL AND id != :usage_id")
                .setParameter("tool_id", usage.getToolId())
                .setParameter("usage_id", newUsage.getId())
                .list();

        long now = new Date().getTime();
        if (usageList.size() >= 1) {

            ToolUsage ancestor = usageList.get(0);
            ancestor.setSuccessor(newUsage);

            currentSession().update(ancestor);

            newUsage.setStartTime(ancestor.getStartTime() + ancestor.getDuration() * 60 * 1000);

            if (newUsage.getStartTime() < now)
                newUsage.setStartTime(now);
        }
        else {
            newUsage.setStartTime(now);
        }

        currentSession().update(newUsage);

        return newUsage;
    }

    /**
     * get a list of all usage items for a specific tool
     * @param id if of the tool
     * @return list of usage items
     */
    List<ToolUsage> getUsageForTool(long id) {

        // auto delete usage entries, delete all entries, older than half an hour
        long now = new Date().getTime() - DELETE_OFFSET;
        List<ToolUsage> deleteList = super.currentSession().
                createQuery("FROM " + TABLE_NAME + " WHERE tool_id = :toolId AND (starttime + duration * 60 * 1000) < :now")
                .setParameter("toolId", id)
                .setParameter("now", now)
                .list();

        for (ToolUsage del : deleteList) {
            delete(del.getToolId(), del.getId());
        }

        // return current list of usage entries
        return super.currentSession().
                createQuery("FROM " + TABLE_NAME + " WHERE tool_id = :toolId")
                .setParameter("toolId", id)
                .list();
    }

    /**
     * get a usage item from database
     * @param id id of item
     * @return
     */
    ToolUsage getUsage(long id) {
        return get(id);
    }

    /**
     * Delete specific usage item for specific tool.
     * @param toolId
     * @param usageId item which should be removed
     * @return true on success
     */
    boolean delete(long toolId, long usageId) {
        ToolUsage usage = get(usageId);

        if (usage == null)
            return false;

        if (usage.getTool().getId() == toolId) {

            //find ancestor and set new successor
            ToolUsage ancestor = findAncestor(usage);
            if (ancestor != null) {
                ancestor.setSuccessor(usage.getSuccessor());
                currentSession().update(ancestor);

                // only check start times if deleted task is not in past
                if (usage.getStartTime() + usage.getDuration() * 60 * 1000 > new Date().getTime())
                    updateStartTimes(ancestor);
            }

            currentSession().delete(usage);
            return true;
        }

        return false;
    }

    /**
     * Remove all usage items for specific tool
     * @param toolId
     */
    void clearUsageForTool(long toolId) {
        super.currentSession().createQuery("DELETE FROM " + TABLE_NAME + " WHERE tool_id = :tool_id")
                .setParameter("tool_id", toolId)
                .executeUpdate();
    }

    /**
     * Change order of ToolUsage items. In this case we want to move "usage" to be directly behind "after".
     *
     * Example: (move 5 after 2)
     * 1 -> 1
     * 2 -> 2
     * 3 -> 5
     * 4 -> 3
     * 5 -> 4
     * 6 -> 6
     *
     * @param usage The item which should be moved
     * @param after The item, which should be the new ancestor of "usage!
     * @return true on success
     */
    boolean moveAfter(ToolUsage usage, ToolUsage after) {

        // identical item?
        if (usage.getId() == after.getId())
            return false;

        // is "usage" already successor of "after"
        if (after.getSuccessor()!= null && after.getSuccessor().getId() == usage.getId())
            return false;

        // the successor of "usage" has to be the new successor of the ancestor of "usage"
        // maybe "usage" does not have an ancestor
        ToolUsage ancestor = findAncestor(usage);
        if (ancestor != null) {
            ancestor.setSuccessor(usage.getSuccessor());
            currentSession().update(ancestor);
        }

        // successor of "after" has to be successor of "usage"
        usage.setSuccessor(after.getSuccessor());

        // "usage" has to successor of "after"
        after.setSuccessor(usage);

        // start current task if it has no ancestor
        if (findAncestor(after) == null)
            after.setStartTime(new Date().getTime());

        currentSession().update(usage);
        currentSession().update(after);

        updateStartTimes(after);

        return true;
    }

    public ToolUsage findAncestor(ToolUsage usage) {

        if (usage == null)
            return null;

        List<ToolUsage> usageList = super.currentSession().createQuery("FROM " + TABLE_NAME + " WHERE successor_id = :successor_id AND tool_id = :tool_id")
                .setParameter("successor_id", usage.getId())
                .setParameter("tool_id", usage.getTool().getId())
                .list();

        if (usageList.size() == 1)
            return  usageList.get(0);

        return null;
    }

    public List<FabTool> getEnabledTools() {
        return super.currentSession().
                createQuery("FROM FabTool WHERE enabledForMachineUsage = :flag")
                .setParameter("flag", true)
                .list();
    }

    public boolean setToolEnabled(long toolId, boolean flag) {
        FabTool tool = getTool(toolId);

        if (tool == null)
            return false;

        tool.setEnabledForMachineUsage(flag);
        currentSession().update(tool);

        return true;
    }

    private void updateStartTimes(ToolUsage first) {
        ToolUsage ancestor = first;
        ToolUsage item;
        while ((item = ancestor.getSuccessor()) != null) {
            item.setStartTime(ancestor.getStartTime() + ancestor.getDuration() * 60 * 1000);
            currentSession().update(item);
            ancestor = item;
        }
    }

    public FabTool getTool(long toolId) {
        List<FabTool> toolList = super.currentSession().
                createQuery("FROM FabTool WHERE id = :toolId")
                .setParameter("toolId", toolId)
                .list();

        if (toolList.size() != 1)
            return null;

        return toolList.get(0);
    }
}

package de.fau.fablab.app.server;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.fau.fablab.app.rest.core.User;
import de.fau.fablab.app.server.configuration.*;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Configuration class containing environment specific parameters
 * specified in the given YAML file when starting the application
 * The mapping between this class and the used yaml file is done by Jackson
 */
class ServerConfiguration extends Configuration
{
    @NotNull
    @Valid
    @JsonProperty
    private SpaceApiConfiguration spaceapi;
    public SpaceApiConfiguration getSpaceApiConfiguration(){ return spaceapi; }

    @Valid
    @NotNull
    @JsonProperty
    private DoorStateConfiguration doorState;
    public DoorStateConfiguration getDoorStateConfiguration(){ return doorState; }

    @Valid
    @NotNull
    @JsonProperty
    private DataSourceFactory database = new DataSourceFactory();
    public DataSourceFactory getDatabaseConfiguration() {
        return database;
    }

    @Valid
    @NotNull
    @JsonProperty
    private AdminInterfaceConfiguration adminInterface = new AdminInterfaceConfiguration();
    public AdminInterfaceConfiguration getAdminInterfaceConfiguration() { return adminInterface; }

    @Valid
    @NotNull
    @JsonProperty
    private OpenErpConfiguration openerp = new OpenErpConfiguration();
    public OpenErpConfiguration getOpenErpConfiguration() { return openerp; }

    @Valid
    @NotNull
    @JsonProperty
    private EventsConfiguration events = new EventsConfiguration();
    public EventsConfiguration getEventsConfiguration() { return events; }

    @Valid
    @NotNull
    @JsonProperty
    private NewsConfiguration news = new NewsConfiguration();
    public NewsConfiguration getNewsConfiguration() { return news; }

    @Valid
    @NotNull
    @JsonProperty
    private ToolsConfiguration tools = new ToolsConfiguration();
    public ToolsConfiguration getToolsConfiguration() { return tools; }

    @Valid
    @NotNull
    @JsonProperty
    private GeneralConfiguration general = new GeneralConfiguration();
    public GeneralConfiguration getGeneralConfiguration() { return general; }

    @Valid
    @NotNull
    @JsonProperty
    private ProjectsConfiguration projects = new ProjectsConfiguration();
    public ProjectsConfiguration getProjectsConfiguration() { return projects; }

    @Valid
    @NotNull
    @JsonProperty
    private AndroidPushConfiguration androidPush = new AndroidPushConfiguration();
    public AndroidPushConfiguration getAndroidPushConfiguration() { return androidPush; }

    @Valid
    @NotNull
    @JsonProperty
    private ApplePushConfiguration applePush = new ApplePushConfiguration();
    public ApplePushConfiguration getApplePushConfiguration() { return applePush; }

    @NotNull
    @JsonProperty
    private VersionCheckConfiguration versionCheck = new VersionCheckConfiguration();
    public VersionCheckConfiguration geVersionCheckConfiguration() { return versionCheck; }

    @NotNull
    @JsonProperty
    private List<User> users = new ArrayList<>();
    public List<User> getUserList() { return users; }

    @NotNull
    @JsonProperty
    private CheckOutApiKeyConfiguration checkoutApiKey = new CheckOutApiKeyConfiguration();
    public CheckOutApiKeyConfiguration getCheckoutApiKeyConfiguration() { return checkoutApiKey; }
}

package de.fau.fablab.app.server.core.openerp;


import com.thetransactioncompany.jsonrpc2.JSONRPC2Request;
import com.thetransactioncompany.jsonrpc2.JSONRPC2Response;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2Session;
import com.thetransactioncompany.jsonrpc2.client.JSONRPC2SessionException;
import de.fau.fablab.app.rest.core.Location;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.net.URL;
import java.util.*;

public class LocationClient {

    static final String METHOD = "call";
    static final String FIELD_ID = "id";
    static final String FIELD_CODE = "code";
    static final String FIELD_NAME = "name";

    private static JSONArray fields = new JSONArray();
    static {
        fields.add(0, FIELD_ID);
        fields.add(1, FIELD_NAME);
        fields.add(2, FIELD_CODE);
    }

    private JSONRPC2Session mJSONRPC2Session;
    private URL mSearchReadUrl;
    private String mJSONSessionId;
    private JSONObject mUsercontext;

    public LocationClient(OpenERPConnector aOpenERPConnector , URL aSearchReadURL){
        mJSONRPC2Session = aOpenERPConnector.getOpenERPJsonSession();
        mSearchReadUrl = aSearchReadURL;
        mJSONSessionId = aOpenERPConnector.getOpenERPSessionId();
        mUsercontext = aOpenERPConnector.getOpenERPUserContext();
    }

    public List<Location> getLocations() throws OpenErpException {
        List<Location> locations = new ArrayList<>();

        JSONRPC2Response jsonRPC2Response = null;
        try {
            mJSONRPC2Session.setURL(mSearchReadUrl);

            JSONArray domain = new JSONArray();

            Map<String, Object> categoryParams = new HashMap<>();
            categoryParams.put("session_id", mJSONSessionId);
            categoryParams.put("context", mUsercontext);
            categoryParams.put("domain", domain);
            categoryParams.put("model", "stock.location");
            categoryParams.put("sort", "");
            categoryParams.put("fields", fields);

            jsonRPC2Response = mJSONRPC2Session.send(new JSONRPC2Request(METHOD, categoryParams, OpenERPUtil.generateRequestID()));
            JSONObject result = (JSONObject) jsonRPC2Response.getResult();
            JSONArray records = (JSONArray) result.get("records");
            for(Object categoryObject : records) {
                JSONObject productJson = (JSONObject) categoryObject;
                Location newLocation = new Location();

                long id = ((Long) productJson.get(FIELD_ID));
                String locName = (String) productJson.get(FIELD_NAME);
                String locCode = OpenERPConst.UNKNOW_CODE;
                if(!(productJson.get(FIELD_CODE) instanceof Boolean)) {
                    locCode = (String) productJson.get(FIELD_CODE);
                }
                newLocation.setLocationId(id);
                newLocation.setName(locName);
                newLocation.setCode(locCode);
                locations.add(newLocation);
            }

        } catch (JSONRPC2SessionException e) {
            throw new OpenErpException(e.getMessage(), "");
        }

        return locations;
    }




}

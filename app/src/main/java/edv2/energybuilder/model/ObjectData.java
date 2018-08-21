package edv2.energybuilder.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ObjectData {
    private String key = "";
    private String id = "";
    private String type = "";
    private String name = "";
    private String inputFreq = "";
    private String eventPhases = "";

    public ObjectData(String key, JSONObject jsonObject) {
        this.key = key;
        try {
            id = jsonObject.getString("id");
        } catch (JSONException e) {
        }try {
                type = jsonObject.getString("type");
        } catch (JSONException e) {
        }

        try {
            name = jsonObject.getString("name");
        } catch (JSONException e) {
        }

        try {
            inputFreq = jsonObject.getString("input_freq");
        } catch (JSONException e) {
        }


    }

    public ObjectData(String key, JSONObject jsonObject,JSONObject jsonEventPhase) {
        this.key = key;
        try {
            id = jsonObject.getString("id");
        } catch (JSONException e) {
        }try {
                type = jsonObject.getString("type");
        } catch (JSONException e) {
        }

        try {
            name = jsonObject.getString("name");
        } catch (JSONException e) {
        }
        try {
            JSONObject object = jsonObject.getJSONObject("event_phases");
            JSONArray jsonArray = object.getJSONArray("1");
            name = jsonObject.getString("event_phases");
        } catch (JSONException e) {
        }


    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInputFreq() {
        return inputFreq;
    }

    public void setInputFreq(String inputFreq) {
        this.inputFreq = inputFreq;
    }

    public String getEventPhases() {
        return eventPhases;
    }

    public void setEventPhases(String eventPhases) {
        this.eventPhases = eventPhases;
    }
}



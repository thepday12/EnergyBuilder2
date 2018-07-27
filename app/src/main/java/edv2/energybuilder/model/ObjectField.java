package edv2.energybuilder.model;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class ObjectField {
    private String key = "";
    private String name = "";
    private String dataType = "";
    private String controlType = "";
    private boolean enable = false;
    private boolean mandatory = false;
    private String value = "";
    private List<String> list = new ArrayList<>();
    private List<EventPhase> eventPhases = new ArrayList<>();

    public ObjectField(String key,String name, String dataType, String controlType) {
        this.key = key;
        this.name = name;
        this.dataType = dataType;
        this.controlType = controlType;
        this.enable = true;
    }

    /***
     * Ap dung cho EU
     * @param name
     * @param dataType
     * @param controlType
     * @param eventPhases list du lieu day du
     * @param list list du lieu hien thi
     */
    public ObjectField(String key,String name, String dataType, String controlType,List<EventPhase> eventPhases,List<String> list) {
        this.key = key;
        this.name = name;
        this.dataType = dataType;
        this.controlType = controlType;
        this.enable = true;
        this.eventPhases = eventPhases;
        this.list = list;
    }

    /***
     * Add thang tu JSOn
     * @param jsonObject
     * @param jsonList dung trong truong hop field = "l"
     */
    public ObjectField(JSONObject jsonObject, JSONObject jsonList) {

        try {
            key = jsonObject.getString("field");
        } catch (JSONException e) {
        }

        try {
            name = jsonObject.getString("name");
        } catch (JSONException e) {
        }

        try {
            dataType = jsonObject.getString("data_type");
        } catch (JSONException e) {
        }

        try {
            controlType = jsonObject.getString("control_type");

            if(controlType.equals("l")){
                JSONObject json = jsonList.getJSONObject(jsonObject.getString("list"));
                Iterator<String> keys = json.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    String value = json.getString(key);
                    list.add(value);
                }

            }
        } catch (JSONException e) {
        }
        try {
            enable = jsonObject.getBoolean("enable");
        } catch (JSONException e) {
        }

        try {
            mandatory = jsonObject.getBoolean("mandatory");
        } catch (JSONException e) {
        }





    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getControlType() {
        return controlType;
    }

    public void setControlType(String controlType) {
        this.controlType = controlType;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<EventPhase> getEventPhases() {
        return eventPhases;
    }

    public void setEventPhases(List<EventPhase> eventPhases) {
        this.eventPhases = eventPhases;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public String getFieldData(){
        String jsonString = "";
        if(!value.isEmpty()){
            jsonString = "\""+key+"\":\""+ value+"\"";
        }
        return jsonString;
    }
}



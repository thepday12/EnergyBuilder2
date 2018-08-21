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

import edv2.energybuilder.utils.MyUtils;


public class ObjectField {
    private String key = "";
    private String name = "";
    private String dataType = "";
    private String controlType = "";
    private boolean enable = false;
    private boolean mandatory = false;
    private String value = "";
    private String format = "";
    private int decimals = 0;

    private List<ObjectList> list = new ArrayList<>();
    private List<EventPhase> eventPhases = new ArrayList<>();

    public ObjectField(String key, String name, String dataType, String controlType) {
        this.key = key;
        this.name = name;
        this.dataType = dataType;
        this.controlType = controlType;
        this.enable = true;
        this.value = MyUtils.getCurrentDate();
    }

    /***
     * Ap dung cho EU
     * @param name
     * @param dataType
     * @param controlType
     * @param eventPhases list du lieu day du
     */
    public ObjectField(String key, String name, String dataType, String controlType, List<EventPhase> eventPhases) {
        this.key = key;
        this.name = name;
        this.dataType = dataType;
        this.controlType = controlType;
        this.enable = true;
        this.eventPhases = eventPhases;
        List<ObjectList> tmpLists = new ArrayList<>();
        for (EventPhase eventPhase : eventPhases) {
            tmpLists.add(new ObjectList(eventPhase.getPhaseEvent(), eventPhase.getName()));
        }
        this.value = tmpLists.get(0).getId();
        this.list = tmpLists;
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

            if (controlType.equals("l")) {
                JSONArray json = jsonList.getJSONArray(jsonObject.getString("list"));
                for (int i = 0; i < json.length(); i++) {
                    JSONObject values = json.getJSONObject(i);
                    String key = values.getString("value");
                    String value = values.getString("text");
                    list.add(new ObjectList(key, value));
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
        try {
            format = jsonObject.getString("format");
        } catch (JSONException e) {
        }
        try {
            decimals = jsonObject.getInt("decimals");
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

    public List<ObjectList> getList() {
        return list;
    }

    public void setList(List<ObjectList> list) {
        this.list = list;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getDecimals() {
        return decimals;
    }

    public void setDecimals(int decimals) {
        this.decimals = decimals;
    }

    public String getFieldData() {
        String jsonString = "";
        if (!value.isEmpty()) {
            if (controlType.equals("l")) {
                String tmpValue = "";
                for (ObjectList objectList : list) {
                    if (objectList.getId().equals(value)) {
                        tmpValue = objectList.getId();
                        break;
                    }
                }
                jsonString = "\"" + key + "\":\"" + tmpValue + "\"";
                Log.e("VLLL", jsonString);
            } else if (controlType.equals("n")) {
                jsonString = "\"" + key + "\":\"" + MyUtils.formatDecimalValue(value) + "\"";//Format cac quoc gia la khac nhau
            } else {
                jsonString = "\"" + key + "\":\"" + value + "\"";
            }
        }
        return jsonString;
    }
}



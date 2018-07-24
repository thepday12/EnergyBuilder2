package edv2.energybuilder.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Route {
    private String key = "";
    private String id = "";
    private String name = "";
    private int complete = 0;
    private int total = 0;

    public Route(String key, JSONObject jsonObject,JSONObject jsonPoints) {
        this.key = key;
        try {
            id = jsonObject.getString("id");
        } catch (JSONException e) {
        }

        try {
            name = jsonObject.getString("name");
        } catch (JSONException e) {
        }


        Iterator<String> keys = jsonPoints.keys();
        total = 0;
        complete = 0;

        while (keys.hasNext()) {
            String keyJson = keys.next();
            try {
                JSONObject object = jsonPoints.getJSONObject(keyJson);
                if(object.getString("route_id").equals(id)){
                    total+=1;
                    if(object.getBoolean("complete")){
                        complete+=1;
                    }
                }
            } catch (JSONException e) {

            }
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}

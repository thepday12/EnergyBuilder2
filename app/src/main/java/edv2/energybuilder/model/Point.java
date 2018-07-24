package edv2.energybuilder.model;

import org.json.JSONException;
import org.json.JSONObject;


public class Point {
    private String key = "";
    private String id = "";
    private String routeId = "";
    private String name = "";
    private boolean complete = false;
    private String objects ="";
    DetailPoint fl = new DetailPoint();
    DetailPoint eu = new DetailPoint();
    DetailPoint ta =  new DetailPoint();
    DetailPoint eq =  new DetailPoint();

    public Point(String key, JSONObject jsonObject) {
        this.key = key;
        try {
            id = jsonObject.getString("id");
        } catch (JSONException e) {
        }try {
            routeId = jsonObject.getString("route_id");
        } catch (JSONException e) {
        }

        try {
            name = jsonObject.getString("name");
        } catch (JSONException e) {
        }
        try {
            complete = jsonObject.getBoolean("complete");
        } catch (JSONException e) {
        }


        try {
            fl = new DetailPoint("FLOW",Integer.valueOf(jsonObject.getString("FL")),"FL");
        } catch (JSONException e) {
        }try {
            eu = new DetailPoint("ENERGY UNIT",Integer.valueOf(jsonObject.getString("EU")),"EU");
        } catch (JSONException e) {
        }try {
            ta = new DetailPoint("TANK",Integer.valueOf(jsonObject.getString("TA")),"TA");
        } catch (JSONException e) {
        }

        try {
            eq = new DetailPoint("EQUIPMENT",Integer.valueOf(jsonObject.getString("EQ")),"EQ");
        } catch (JSONException e) {
        }

        try {
//            JSONArray jsonArray = jsonObject.getJSONArray("objects");
//            for(int i = 0;i<jsonArray.length();i++){
//                objects.add(jsonArray.getString(i));
//            }

            objects =  jsonObject.getString("objects");
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

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isComplete() {
        return complete;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public String getObjects() {
        return objects;
    }

    public void setObjects(String objects) {
        this.objects = objects;
    }

    public DetailPoint getFl() {
        return fl;
    }

    public void setFl(DetailPoint fl) {
        this.fl = fl;
    }

    public DetailPoint getEu() {
        return eu;
    }

    public void setEu(DetailPoint eu) {
        this.eu = eu;
    }

    public DetailPoint getTa() {
        return ta;
    }

    public void setTa(DetailPoint ta) {
        this.ta = ta;
    }

    public DetailPoint getEq() {
        return eq;
    }

    public void setEq(DetailPoint eq) {
        this.eq = eq;
    }
}



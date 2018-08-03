package edv2.energybuilder.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.jaredrummler.materialspinner.MaterialSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edv2.energybuilder.R;
import edv2.energybuilder.adapter.ObjectFieldAdapter;
import edv2.energybuilder.adapter.UpdateFieldListener;
import edv2.energybuilder.model.EventPhase;
import edv2.energybuilder.model.ObjectData;
import edv2.energybuilder.model.ObjectField;
import edv2.energybuilder.model.ObjectList;
import edv2.energybuilder.model.Point;
import edv2.energybuilder.model.Route;
import edv2.energybuilder.utils.Global;
import edv2.energybuilder.utils.MyUtils;
import edv2.energybuilder.utils.VerticalSpaceItemDecoration;
import edv2.energybuilder.utils.animation.SlideInUpAnimator;

public class UpdateObjectActivity extends BaseActivity {
    private RecyclerView rvContent;
    private MaterialSpinner spinnerObject;
    private int currentObjectPosition = 0;
    private int currentEventPhasePosition = 0;
    private Button btReset,btSave;
    private Point point;
    private String type;
    private ObjectFieldAdapter objectFieldAdapter;

    private String currentObjectId;

    private List<String> visibleObjects = new ArrayList<>();
    private List<ObjectData> detailObjects = new ArrayList<>();
    private             List<ObjectField> fields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {



        super.onResume();
    }

    @Override
    public int myView() {
        return R.layout.activity_update_object;
    }

    @Override
    public void findView() {
        spinnerObject = findViewById(R.id.spinnerObject);
        btReset = findViewById(R.id.btReset);
        btSave = findViewById(R.id.btSave);
        rvContent = findViewById(R.id.rvContent);

    }

    @Override
    public void configView() {


        Intent intent = getIntent();
        point = new Gson().fromJson(intent.getStringExtra(Global.EX_DATA),Point.class);
        type = intent.getStringExtra(Global.EX_ACTION);

        createSpinerSelectObject();
        getSupportActionBar().setTitle(visibleObjects.get(currentObjectPosition));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvContent.setLayoutManager(layoutManager);
        rvContent.setItemAnimator(new SlideInUpAnimator());
        VerticalSpaceItemDecoration dividerItemDecoration = new VerticalSpaceItemDecoration((int) MyUtils.convertDpToPixel(8,this));

        rvContent.addItemDecoration(dividerItemDecoration);


    }

    private void createSpinerSelectObject() {
        //Tao danh sach object de lua chon (spiner dau tien)
        try {
            JSONObject jsonObject = new JSONObject(mySharedPreferences.getDataConfig());
            JSONObject objects = jsonObject.getJSONObject("objects");
            //Danh sach id
            JSONArray jsonArray = new JSONArray( point.getObjects());
            for(int i = 0;i<jsonArray.length();i++){
                String item = jsonArray.getString(i);
                if(item.startsWith(type)){
                    ObjectData objectData = new ObjectData(item,objects.getJSONObject(item));
                    detailObjects.add(objectData);
                    visibleObjects.add(objectData.getName());
                }

            }
        } catch (JSONException e) {
        }
        if(visibleObjects.size()>0) {
            spinnerObject.setItems(visibleObjects);
            currentObjectId = detailObjects.get(currentObjectPosition).getKey();
        }
        getObjectStruct();
    }

    @Override
    public void initListener() {
        btReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              resetAllField();
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isValid = true;
                for(ObjectField field:fields){
                    if(field.isMandatory() && field.getValue().isEmpty()){
                        isValid =false;
                        break;
                    }
                }
                if(isValid) {
                    try {
                        JSONObject jsonObject = new JSONObject(mySharedPreferences.getDataConfig());
                        JSONObject jsonObjectDetail = new JSONObject();
                        try {
                            jsonObjectDetail = jsonObject.getJSONObject("object_details");
                        } catch (Exception e) {

                        }

                        String jsonString = "";
                        List<ObjectField> tmp = new ArrayList<>();
                        int i = 1;//Khong lay orcur date
                        if(type.equals("EU")){
                            i =2;//Khong lay orcur date + operation
                        }
                        for(;i<fields.size();i++){
                            tmp.add(fields.get(i));
                        }
                        for (ObjectField field : tmp) {
                            if (!field.getFieldData().isEmpty()) {
                                jsonString += field.getFieldData() + ",";
                            }
                        }
                        if (!jsonString.isEmpty()) {
                            jsonString += "\"editted\":\"1\"";
                            jsonString = "{" + jsonString + "}";
                            jsonObjectDetail.put(getCurrentKey(), new JSONObject(jsonString));
                            jsonObject.put("object_details", jsonObjectDetail);
                            mySharedPreferences.setDataConfig(jsonObject.toString());
                            Toast.makeText(UpdateObjectActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {

                    }
                }else{
                    Toast.makeText(UpdateObjectActivity.this, "You must fill in all of the fields (*).", Toast.LENGTH_SHORT).show();
                }

            }
        });

        spinnerObject.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                currentObjectPosition = position;
                ObjectData objectData = detailObjects.get(currentObjectPosition);
                currentObjectId = objectData.getKey();
                getSupportActionBar().setTitle(objectData.getName());
                getObjectStruct();
            }
        });
    }

    private void resetAllField() {
//        currentEventPhasePosition = 0;
//        for(int i =0;i<fields.size();i++){
//            ObjectField field = fields.get(i);
//            field.setValue("");
//        }
//        objectFieldAdapter.changeDataset(fields);
        int from =1;
        if(type.equals("EU")){
            from = 2;
        }
        resetAllField(from);
    }
    private void resetAllField(int start) {
        for(int i =start;i<fields.size();i++){
            ObjectField field = fields.get(i);
            if(i==0) {
                field.setValue(MyUtils.getCurrentDate());
            }else if(field.getControlType().equals("l")){
                field.setValue(field.getList().get(0).getId());
            }else{
                field.setValue("");
            }
        }
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject = new JSONObject(mySharedPreferences.getDataConfig());
//        } catch (JSONException e) {
//
//        }
        try {
            objectFieldAdapter.notifyItemRangeChanged(start, fields.size()-start);
        }catch (Exception e){

        }

    }

    private void getObjectStruct(){
        try {
            JSONObject jsonObject = new JSONObject(mySharedPreferences.getDataConfig());
            JSONObject jsonObjects = jsonObject.getJSONObject("objects");
            JSONObject jsonAttrs = jsonObject.getJSONObject("object_attrs");
            JSONObject jsonList = jsonObject.getJSONObject("lists");

            JSONArray jsonType = jsonAttrs.getJSONArray(type);

            fields = new ArrayList<>();


            //add Occur date
            fields.add(new ObjectField("occur_date","Occur date","d","d"));
            if(type.equals("EU")){
                //Lay thong tin de tao name cho event_phase
                JSONArray jsonEvent = jsonList.getJSONArray("CODE_EVENT_TYPE");
                JSONArray jsonPhase = jsonList.getJSONArray("CODE_FLOW_PHASE");
                //Tao danh sach event  - phase tu Json
                List<ObjectList> eventList = new ArrayList<>();
                List<ObjectList> phaseList = new ArrayList<>();
                for(int i = 0;i<jsonEvent.length();i++){
                    JSONObject values = jsonEvent.getJSONObject(i);
                    String key = values.getString("value");
                    String value = values.getString("text");
                    eventList.add(new ObjectList(key,value));
                }
                for(int i = 0;i<jsonPhase.length();i++){
                    JSONObject values = jsonPhase.getJSONObject(i);
                    String key = values.getString("value");
                    String value = values.getString("text");
                    phaseList.add(new ObjectList(key,value));
                }



                List<EventPhase> eventPhases = new ArrayList<>();


                try {

                    JSONObject jsonEventPhase = jsonObjects.getJSONObject(currentObjectId).getJSONObject("event_phases");
//                    jsonEventPhase = new JSONObject("{\"1\":[\"1\",\"3\"],\"2\":[\"2\",\"6\"]}");
                    Iterator<String> keys = jsonEventPhase.keys();


                    while (keys.hasNext()) {//event - key  & con cua event la danh sach phase
                        String key = keys.next();
                        if(jsonEventPhase.get(key) instanceof JSONArray) {
                            JSONArray innerJObject = jsonEventPhase.getJSONArray(key);
                            for(int i=0;i<innerJObject.length();i++) {
                                String phase = innerJObject.getString(i);
                                //Lay name dua vao event_id = phase_id
                                String name = "";
                                try{
                                    //Tim kiem ten event trong danh sach event
                                    for(ObjectList item:eventList){
                                        if(item.getId().equals(key)) {
                                            name = item.getName() + " ";
                                            break;
                                        }
                                    }

                                    //Tim kiem ten phase trong danh sach phase
                                    for(ObjectList item2:phaseList){
                                        if(item2.getId().equals(phase)) {
                                            name += item2.getName();
                                           break;
                                        }
                                    }

                                    eventPhases.add(new EventPhase(key, phase, name));
//                                    name = jsonEvent.getString(key)+" "+jsonPhase.getString(phase);
                                }catch (Exception e){

                                }

                            }
                        }
                    }
                } catch (JSONException e) {
                }

                //Neu la EU add rieng operation
                fields.add(new ObjectField("operation","Operation","l","l",eventPhases));
            }

            //add cac field trong object_attrs
            for (int i = 0; i < jsonType.length(); i++) {
                ObjectField field= new ObjectField(jsonType.getJSONObject(i),jsonList);
                if(!field.getControlType().isEmpty()&&!field.getControlType().equals("null")) {
                    fields.add(field);
                }
            }
            //Cap nhat value cho cac field


            updateFieldValue(jsonObject,0);
            updateValue();


        } catch (JSONException e) {

        }
    }

    private void updateFieldValue(JSONObject jsonObject, int resetFromPosition){
        try {
            resetFromPosition+=1;
            //Du lieu da luu tru
            JSONObject jsonObjectDetail = jsonObject.getJSONObject("object_details");
            JSONObject object = null;
            try {
                object = jsonObjectDetail.getJSONObject(getCurrentKey());

            }catch (Exception e){

            }
            if(object!=null){
                for(int i =resetFromPosition;i<fields.size();i++){
                    ObjectField field = fields.get(i);
                    String value = "";
                    try{
                        value =  object.getString(field.getKey());

                    }catch (Exception e){

                    }
                    //Neu gia tri  = rong cho list thi value  = gia tri dau tien
                    if(value.isEmpty() && field.getControlType().equals("l")){
                       value = field.getList().get(0).getId();
                    }
                    field.setValue(value);
                }
                try {
                    objectFieldAdapter.notifyItemRangeChanged(resetFromPosition, fields.size() - resetFromPosition);
                }catch (Exception e){

                }
            }else{

                resetAllField(resetFromPosition);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void updateValue(){

        objectFieldAdapter = new ObjectFieldAdapter(this,type, currentObjectId,fields, new UpdateFieldListener() {
            @Override
            public void changeValue(int position, ObjectField objectField) {

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject = new JSONObject(mySharedPreferences.getDataConfig());
                } catch (JSONException e) {
                }



                fields.set(position,objectField);
                if(position==0){//Change orcurdate
//                    updateFieldValue(jsonObject,1);
                    if(type.equals("EU")) {
                        updateFieldValue(jsonObject, 1);
                    }else{
                        updateFieldValue(jsonObject, 0);
                    }
//                    currentEventPhasePosition = 0;
                }else if(type.equals("EU")&&position==1){
                    //Neu La EU thi xac dinh vi tri (value = phase_event)
                    String value = objectField.getValue();
                    List<EventPhase> eventPhases = objectField.getEventPhases();
                    for(int i=0;i<eventPhases.size();i++){
                        EventPhase eventPhase = eventPhases.get(i);
                        if(eventPhase.getPhaseEvent().equals(value)){
                            currentEventPhasePosition = i;
                        }
                    }
                    updateFieldValue(jsonObject,position);
                }
            }
        });
        rvContent.setAdapter(objectFieldAdapter);

    }

    private String getCurrentKey(){
        String date = fields.get(0).getValue();
        if(date.isEmpty()){
            date = MyUtils.getCurrentDate();
        }
        String result = currentObjectId+"_"+date;
        if(type.equals("EU")){
            result+="_"+fields.get(1).getEventPhases().get(currentEventPhasePosition).getPhaseEvent();
        }
        return result;
    }
}

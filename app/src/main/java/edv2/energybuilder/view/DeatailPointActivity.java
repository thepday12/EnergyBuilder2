package edv2.energybuilder.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edv2.energybuilder.R;
import edv2.energybuilder.adapter.DetailPointAdapter;
import edv2.energybuilder.adapter.PointsAdapter;
import edv2.energybuilder.model.DetailPoint;
import edv2.energybuilder.model.EventPhase;
import edv2.energybuilder.model.ObjectField;
import edv2.energybuilder.model.Point;
import edv2.energybuilder.utils.Global;
import edv2.energybuilder.utils.MyUtils;
import edv2.energybuilder.utils.VerticalSpaceItemDecoration;
import edv2.energybuilder.utils.animation.SlideInUpAnimator;

public class DeatailPointActivity extends BaseActivity {
    private RecyclerView rvContent;
    private Button btResetRoute,btCompletePoint;
    private Point point;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {



        rvContent.setAdapter(new DetailPointAdapter(this,point));



        super.onResume();
    }

    @Override
    public int myView() {
        return R.layout.activity_detail_point;
    }

    @Override
    public void findView() {
        rvContent = findViewById(R.id.rvContent);
        btResetRoute = findViewById(R.id.btResetRoute);
        btCompletePoint = findViewById(R.id.btCompletePoint);
    }

    @Override
    public void configView() {
        point = new Gson().fromJson(getIntent().getStringExtra(Global.EX_DATA),Point.class);
        getSupportActionBar().setTitle(point.getName());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvContent.setLayoutManager(layoutManager);
        rvContent.setItemAnimator(new SlideInUpAnimator());
        VerticalSpaceItemDecoration dividerItemDecoration = new VerticalSpaceItemDecoration((int) MyUtils.convertDpToPixel(8,this));

        rvContent.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void initListener() {
        btCompletePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject jsonObject = new JSONObject(mySharedPreferences.getDataConfig());
                    JSONArray jsonPoints = jsonObject.getJSONArray("points");
                    JSONObject jsonPoint = new JSONObject();
                    int position = 0;
                    for(int i=0;i<jsonPoints.length();i++){
                        JSONObject object = jsonPoints.getJSONObject(i);
                        if(object.getString("key").equals(point.getKey())){
                            jsonPoint = object;
                            position =i;
                            break;
                        }
                    }
                    //Cap nhat trang thai complete cho Point
                    jsonPoint.put("complete",true);
                    jsonPoints.put(position,jsonPoint);
                    jsonObject.put("points",jsonPoints);
                    mySharedPreferences.setDataConfig(jsonObject.toString());
                    Toast.makeText(DeatailPointActivity.this, "Point completed", Toast.LENGTH_SHORT).show();
                    finish();
                } catch (JSONException e) {

                }
            }
        });
        btResetRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogConfirm("Warning", "All data of this point will be removed. Do you really want to continue?", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        resetPoint(point);
                        Toast.makeText(DeatailPointActivity.this, "Point reset", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }
}

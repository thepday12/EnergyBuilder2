package edv2.energybuilder.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edv2.energybuilder.R;
import edv2.energybuilder.adapter.PointsAdapter;
import edv2.energybuilder.model.Point;
import edv2.energybuilder.utils.Global;
import edv2.energybuilder.utils.MyUtils;
import edv2.energybuilder.utils.VerticalSpaceItemDecoration;
import edv2.energybuilder.utils.animation.SlideInUpAnimator;

public class PointsActivity extends BaseActivity {
    private RecyclerView rvContent;
    private Button btResetRoute;
    private String ROUTE_ID;
    List<Point> points= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        loadData();
        super.onResume();
    }

    private void loadData() {
        try {
            JSONObject jsonObject = new JSONObject(mySharedPreferences.getDataConfig());
            JSONObject jsonPoints = jsonObject.getJSONObject("points");
            Iterator<String> keys = jsonPoints.keys();
            points = new ArrayList<>();
            while (keys.hasNext()) {
                String key = keys.next();
                if(jsonPoints.get(key) instanceof JSONObject) {
                    JSONObject innerJObject = jsonPoints.getJSONObject(key);
                    String routeId = innerJObject.getString("route_id");
                    if(!routeId.equals(ROUTE_ID)){
                        continue;
                    }else{
                        points.add(new Point(key,innerJObject));
                    }
                    Log.v("details", " lastUpdate");
                } else if (jsonPoints.get(key) instanceof String){
                    String value = jsonPoints.getString("type");
                    Log.v("key = type", "value = " + value);
                }
            }

            if(points.size()>0) {
                rvContent.setAdapter(new PointsAdapter(this,points));
            }

        } catch (JSONException e) {
        }
    }

    @Override
    public int myView() {
        return R.layout.activity_points;
    }

    @Override
    public void findView() {
        rvContent = findViewById(R.id.rvContent);
        btResetRoute = findViewById(R.id.btResetRoute);
    }

    @Override
    public void configView() {
        Intent intent = getIntent();
        ROUTE_ID = intent.getStringExtra(Global.EX_ID);
        getSupportActionBar().setTitle(intent.getStringExtra(Global.EX_NAME));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvContent.setLayoutManager(layoutManager);
        rvContent.setItemAnimator(new SlideInUpAnimator());
        VerticalSpaceItemDecoration dividerItemDecoration = new VerticalSpaceItemDecoration((int) MyUtils.convertDpToPixel(8,this));

        rvContent.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void initListener() {
        btResetRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogConfirm("Warning", "All data of this route will be removed. Do you want to continue?", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        for(Point point:points){
                            point.setComplete(false);
                            resetPoint(point);
                        }
                        rvContent.setAdapter(new PointsAdapter(PointsActivity.this,points));
                    }
                });

            }
        });
    }
}

package edv2.energybuilder.view;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import edv2.energybuilder.R;
import edv2.energybuilder.adapter.RoutesAdapter;
import edv2.energybuilder.model.Route;
import edv2.energybuilder.utils.MyUtils;
import edv2.energybuilder.utils.VerticalSpaceItemDecoration;
import edv2.energybuilder.utils.animation.SlideInUpAnimator;

public class RoutesActivity extends BaseActivity {
    private RecyclerView rvContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        try {
            JSONObject jsonObject = new JSONObject(mySharedPreferences.getDataConfig());
            JSONArray jsonRoutes = jsonObject.getJSONArray("routes");
            JSONArray jsonPoints = jsonObject.getJSONArray("points");

            List<Route> routes = new ArrayList<>();
            for(int i =0;i<jsonRoutes.length();i++){
                JSONObject innerJObject = jsonRoutes.getJSONObject(i);
                String total = innerJObject.getString("total");
                if(total.equals("0")){
                    continue;
                }else{
                    routes.add(new Route(innerJObject,jsonPoints));
                }
            }


            if(routes.size()>0) {

                rvContent.setAdapter(new RoutesAdapter(RoutesActivity.this,routes));
            }

        } catch (JSONException e) {
        }
        super.onResume();
    }

    @Override
    public int myView() {
        return R.layout.activity_routes;
    }

    @Override
    public void findView() {
        rvContent = findViewById(R.id.rvContent);
    }

    @Override
    public void configView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvContent.setLayoutManager(layoutManager);
        rvContent.setItemAnimator(new SlideInUpAnimator());
        VerticalSpaceItemDecoration dividerItemDecoration = new VerticalSpaceItemDecoration((int) MyUtils.convertDpToPixel(16,this));

        rvContent.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void initListener() {

    }
}

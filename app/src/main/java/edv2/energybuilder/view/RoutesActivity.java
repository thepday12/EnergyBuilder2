package edv2.energybuilder.view;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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
            JSONObject jsonRoutes = jsonObject.getJSONObject("routes");
            JSONObject jsonPoints = jsonObject.getJSONObject("points");
            Iterator<String> keys = jsonRoutes.keys();
            List<Route> routes = new ArrayList<>();
            while (keys.hasNext()) {
                String key = keys.next();
                if(jsonRoutes.get(key) instanceof JSONObject) {
                    JSONObject innerJObject = jsonRoutes.getJSONObject(key);
                    String total = innerJObject.getString("total");
                    if(total.equals("0")){
                        continue;
                    }else{
                        routes.add(new Route(key,innerJObject,jsonPoints));
                    }
                    Log.v("details", " lastUpdate");
                } else if (jsonRoutes.get(key) instanceof String){
                    String value = jsonRoutes.getString("type");
                    Log.v("key = type", "value = " + value);
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

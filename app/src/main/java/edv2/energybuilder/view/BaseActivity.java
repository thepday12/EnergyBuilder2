package edv2.energybuilder.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import edv2.energybuilder.R;
import edv2.energybuilder.model.Point;
import edv2.energybuilder.utils.Global;
import edv2.energybuilder.utils.MyConnection;
import edv2.energybuilder.utils.MySharedPreferences;

public abstract class BaseActivity extends AppCompatActivity {
    private ActionBar actionbar;
    protected final int REQ_LOGIN = 1;
    private ProgressDialog progressDialog;
    private boolean isLoading = false;
    private String progressTitle = null;
    protected MyConnection myConnection;
    protected MySharedPreferences mySharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Slide());
            getWindow().setExitTransition(new Slide());

        }
        actionbar = getSupportActionBar();
        if (actionbar != null) {
            actionbar.setDisplayHomeAsUpEnabled(true);
        }else{
        }
        setContentView(myView());

        myConnection = new MyConnection(BaseActivity.this);
        mySharedPreferences = new MySharedPreferences(BaseActivity.this);
        progressDialog = new ProgressDialog(this);

        findView();
        configView();
        initListener();


    }

    @Override
    protected void onResume() {
        if(isLoading){
            showProgressDialog(progressTitle);
        }
        super.onResume();
    }

    @Override
    protected void onStop() {
        if(progressDialog!=null) {
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
        super.onStop();
    }


    protected void showProgressDialog() {
        showProgressDialog(null);
    }

    protected void showProgressDialog(String title) {
        if (title == null) {
            title = getString(R.string.loading_title);
        }
        progressTitle = title;
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
            }catch (Exception e){

            }
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(title);
        progressDialog.show();

        isLoading = true;
    }

    protected void dismissProgressDialog() {
        if (progressDialog != null) {
            if (progressDialog.isShowing()) {
                isLoading = false;
                progressDialog.dismiss();
                progressTitle = null;
            }
        }
    }


    public void startSettingActivity() {
        startActivity(new Intent(BaseActivity.this, SettingsActivity.class));
    }
    public void startRoutesActivity() {
        startActivity(new Intent(BaseActivity.this, RoutesActivity.class));
    }

    public void startLoginActivity(int action) {
        Intent intent = new Intent(BaseActivity.this, LoginActivity.class);
        intent.putExtra(Global.EX_ACTION, action);
        startActivityForResult(intent, REQ_LOGIN);
    }

    public void startMainActivity() {
        Intent intent = new Intent(BaseActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (actionbar != null) {
            getMenuInflater().inflate(R.menu.menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                startMainActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    /***
     * 0- Tra ve view hien thi cua activity
     * @return
     */
    public abstract int myView();

    /***
     * 1- Anh xa view tu layout -> bien trong java
     */
    public abstract void findView();

    /***
     * 2- Thiet lap hien thi mac dinh cho view
     */
    public abstract void configView();

    /***
     * Xu ly su kien
     */
    public abstract void initListener();


    protected void handleDownloadConfig() {
        if (mySharedPreferences.getDataConfig().isEmpty()) {
            requestDownloadConfig();
        } else {
            showDialogConfirm("Warning", "All your data and configurations will be replaced", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    mySharedPreferences.setDataConfig("");
                    requestDownloadConfig();
                }
            });
        }
    }
    protected void handleUploadData() {
        String objectDetails = new JSONObject().toString();
        String defaultValue = objectDetails;
        try {
            JSONObject jsonObject =  new JSONObject(mySharedPreferences.getDataConfig());
            objectDetails = jsonObject.getJSONObject("object_details").toString();
        } catch (JSONException e) {

        }
        if(!objectDetails.equals(defaultValue)){
            requestUploadData(objectDetails);
        }



    }

    protected void requestDownloadConfig() {
        showProgressDialog("Download data");
        myConnection.downloadConfig(new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("MY_RESPONSE", response.toString());
                dismissProgressDialog();

                boolean isTokenExpired = false;
                try {
                    if (response.getString("message").equals("Unauthorized")) {
                        isTokenExpired = true;
                    }
                } catch (JSONException e) {

                }
                if (isTokenExpired) {
                    mySharedPreferences.userLogout();
                    startLoginActivity(Global.ACTION_DOWNLOAD);
                } else {
                    mySharedPreferences.setDataConfig(response.toString());
                }
                Toast.makeText(BaseActivity.this, "Download completed!", Toast.LENGTH_SHORT).show();
                startRoutesActivity();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
                Toast.makeText(BaseActivity.this, "Failed to download data. Internal error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    protected void requestUploadData(String objectDetails) {
        showProgressDialog("Upload data");
        myConnection.uploadData(objectDetails,new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("MY_RESPONSE", response.toString());
                dismissProgressDialog();

                try {
                    if (response.getString("message").equals("ok")) {
                        Toast.makeText(BaseActivity.this, "Upload completed!", Toast.LENGTH_SHORT).show();
                    }else if (response.getString("message").equals("Unauthorized")) {
                        mySharedPreferences.userLogout();
                        startLoginActivity(Global.ACTION_UPLOAD);
                    }else{
                        Toast.makeText(BaseActivity.this, "Failed to download data. Internal error", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {

                }


        }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dismissProgressDialog();
                Toast.makeText(BaseActivity.this, "Failed to download data. Internal error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void showDialogConfirm(String title, String message, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(BaseActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(BaseActivity.this);
        }
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, listener)
                .setNegativeButton(android.R.string.cancel, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    protected void resetPoint(Point point){
        try {

            JSONObject jsonObject = new JSONObject(mySharedPreferences.getDataConfig());

            //Du lieu da luu tru
            JSONObject jsonObjectDetail = null;
            try {
                jsonObjectDetail = jsonObject.getJSONObject("object_details");
            }catch (Exception e){

            }
//
            if(jsonObjectDetail!=null) {
                JSONArray jsonArray = new JSONArray(point.getObjects());
                JSONObject newJsonObjectDetail = new JSONObject();

                Iterator<String> keys = jsonObjectDetail.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    boolean isValid = true;
                    //Kiem tra key co thuoc danh sach can xoa ko
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if(key.startsWith(jsonArray.getString(i))){
                            isValid = false;
                            break;
                        }
                    }
                    //Neu ko thuoc them vao danh sach moi
                    if(isValid) {
                        JSONObject innerJObject = jsonObjectDetail.getJSONObject(key);
                        newJsonObjectDetail.put(key,innerJObject);
                    }
                }
                jsonObject.put("object_details",newJsonObjectDetail);
                //Cap nhat trang thai complete
                try {
                    JSONObject jsonPoints = jsonObject.getJSONObject("points");
                    JSONObject jsonPoint = jsonPoints.getJSONObject(point.getKey());
                    //Cap nhat trang thai complete cho Point
                    jsonPoint.put("complete",false);
                    jsonPoints.put(point.getKey(),jsonPoint);
                    jsonObject.put("points",jsonPoints);
                } catch (JSONException e) {

                }


                mySharedPreferences.setDataConfig(jsonObject.toString());

            }
        } catch (JSONException e) {
        }
    }
}

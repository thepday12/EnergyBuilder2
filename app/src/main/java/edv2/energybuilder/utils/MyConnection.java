package edv2.energybuilder.utils;

import android.app.Application;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import edv2.energybuilder.MyApplication;


public class MyConnection {
    private Context mContext;
    private MySharedPreferences mySharedPreferences;

    public MyConnection(Context context) {
        mContext = context;
        mySharedPreferences = new MySharedPreferences(mContext);
    }

    public  void requestLogin(final String userName, final String password, Response.Listener listener, Response.ErrorListener errorListener){
        String url  = mySharedPreferences.getServerUrl()+ "/dclogin";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url,null,
                listener,errorListener){
//            @Override
//            protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("username", userName);
//                    params.put("password", password);
//                    return params;
//            }

            @Override
            public byte[] getBody() {
                Map<String, String> params = new HashMap<>();
                params.put("username", userName);
                params.put("password", password);
                return new JSONObject(params).toString().getBytes();
            }
        };
        MyApplication.getInstance().addToRequestQueue(stringRequest, "req_login");

    }

    public  void downloadConfig(Response.Listener listener, Response.ErrorListener errorListener){
        String url  = mySharedPreferences.getServerUrl()+ "/dcloadconfig";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url,null,
                listener,errorListener){

            @Override
            public byte[] getBody() {
                Map<String, String> params = new HashMap<>();
                String userToken = mySharedPreferences.getUserToken();
//                try {
//                    userToken = URLEncoder.encode(userToken, "utf-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                params.put("token",userToken );
                params.put("data_type", mySharedPreferences.getDataCaptureType());
                params.put("days", mySharedPreferences.getStringHistoryDays("7"));
                return new JSONObject(params).toString().getBytes();
            }
        };


        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Global.MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.getInstance().addToRequestQueue(stringRequest, "req_download");
    }

    public  void uploadData(final String objectDetails, Response.Listener listener, Response.ErrorListener errorListener){
        String url  = mySharedPreferences.getServerUrl()+ "/dcsavedata";
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, url,null,
                listener,errorListener){

            @Override
            public byte[] getBody() {
                Map<String, String> params = new HashMap<>();
                String userToken = mySharedPreferences.getUserToken();
//                try {
//                    userToken = URLEncoder.encode(userToken, "utf-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
                params.put("token",userToken );
                params.put("data_type", mySharedPreferences.getDataCaptureType());
                params.put("object_details", objectDetails);
                return new JSONObject(params).toString().getBytes();
            }
        };


        MyApplication.getInstance().addToRequestQueue(stringRequest, "req_download");
    }
}

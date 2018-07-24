package edv2.energybuilder.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import edv2.energybuilder.R;
import edv2.energybuilder.utils.Global;

public class LoginActivity extends BaseActivity {
    private EditText etUserName,etPassword;
    private Button btLogin;
    private int action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public int myView() {
        return R.layout.activity_login;
    }

    @Override
    public void findView() {
        etUserName = findViewById(R.id.etUserName);
        etPassword = findViewById(R.id.etPassword);
        btLogin = findViewById(R.id.btLogin);
    }

    @Override
    public void configView() {
        action = getIntent().getIntExtra(Global.EX_ACTION,-1);
    }

    @Override
    public void initListener() {
        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btLogin.setEnabled(false);
                String userName = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                if(!userName.isEmpty()&&!password.isEmpty()){
                    showProgressDialog("Login");
                    myConnection.requestLogin( userName, password, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("MY_RESPONSE",response.toString());
                            completeRequest();
                            try {
                                if(response.getString("message").equals("ok")){
                                    String userToken = response.getString("access_token");
                                    mySharedPreferences.setUserToken(userToken);
                                    new Runnable() {
                                        @Override
                                        public void run() {
//                                            try {
//                                                Thread.sleep(500);
//                                            } catch (InterruptedException e) {
//
//                                            }
                                            Intent returnIntent = new Intent();
                                            returnIntent.putExtra(Global.EX_ACTION,action);
                                            setResult(Activity.RESULT_OK,returnIntent);
                                            finish();
                                        }
                                    }.run();

                                }

                            } catch (JSONException e) {

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            completeRequest();
                        }
                    });
                }
            }
        });
    }

    private void completeRequest(){
        dismissProgressDialog();
        btLogin.setEnabled(true);
    }
}

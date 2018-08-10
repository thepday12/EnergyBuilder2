package edv2.energybuilder.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import edv2.energybuilder.R;
import edv2.energybuilder.utils.Global;

public class MainActivity extends BaseActivity {
    private Button btRoutes,btDownload,btUpload,btClear,btSettings,btLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int myView() {
        return R.layout.activity_main;
    }

    @Override
    public void findView() {
        btRoutes = findViewById(R.id.btRoutes);
        btDownload = findViewById(R.id.btDownload);
        btUpload = findViewById(R.id.btUpload);
        btClear = findViewById(R.id.btClear);
        btSettings = findViewById(R.id.btSettings);
        btLogin = findViewById(R.id.btLogin);
    }

    @Override
    public void configView() {

    }

    @Override
    public void initListener() {
        btRoutes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btLogin.getText().toString().equals("LOGIN")) {
                    startLoginActivity(Global.ACTION_DOWNLOAD);
                }else {
                    if(mySharedPreferences.getDataConfig().isEmpty()){
                        handleDownloadConfig();
                    }else {
                        startRoutesActivity();
                    }
                }
            }
        });

        btDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btLogin.getText().toString().equals("LOGIN")) {
                    startLoginActivity(Global.ACTION_DOWNLOAD);
                }else {
                    handleDownloadConfig();
                }
            }
        });
        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btLogin.getText().toString().equals("LOGIN")) {
                    startLoginActivity(Global.ACTION_UPLOAD);
                }else {
                    handleUploadData();
                }
            }
        });

        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mySharedPreferences.getDataConfig().isEmpty()) {
                    showDialogConfirm("Warning", "Do you really want clear all offline data?", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            mySharedPreferences.clearData();
                        }
                    });
                }
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSettingActivity();
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btLogin.getText().toString().equals("LOGIN")) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    showDialogConfirm("Login", "Are you sure you want to Logout?", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            mySharedPreferences.userLogout();
                            btLogin.setText("LOGIN");
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Neu co du lieu userToken -> da login
        if(mySharedPreferences.getUserToken().isEmpty()){
            btLogin.setText("LOGIN");
        }else{
            btLogin.setText("LOGOUT");
        }
        //Neu chua co du lieu server yeu cau hoan thanh settings
        if(mySharedPreferences.getServerUrl().isEmpty()){
            startSettingActivity();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQ_LOGIN:
                if(resultCode == Activity.RESULT_OK){
                    int action = data.getIntExtra(Global.EX_ACTION,-1);
                    if(action == Global.ACTION_DOWNLOAD){
                        handleDownloadConfig();
                    }
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

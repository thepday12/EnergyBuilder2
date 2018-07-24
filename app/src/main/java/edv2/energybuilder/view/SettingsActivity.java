package edv2.energybuilder.view;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import edv2.energybuilder.R;
import edv2.energybuilder.utils.MySharedPreferences;

public class SettingsActivity extends BaseActivity {
    private EditText etServer,etDays;
    private Button btFDC,btStandard,btSave;
    private MySharedPreferences mySharedPreferences;
    private boolean currentDataCaptureValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int myView() {
        return R.layout.activity_settings;
    }

    @Override
    public void findView() {
        etServer = findViewById(R.id.etServer);
        etDays = findViewById(R.id.etDays);
        btFDC =findViewById(R.id.btFDC);
        btStandard =findViewById(R.id.btStandard);
        btSave =findViewById(R.id.btSave);
    }

    @Override
    public void configView() {
        mySharedPreferences = new MySharedPreferences(SettingsActivity.this);
        etServer.setText(mySharedPreferences.getServerUrl());
        etDays.setText(mySharedPreferences.getStringHistoryDays(""));
        updateSegmentDataCaptureValue(mySharedPreferences.isFDC());
    }

    @Override
    public void initListener() {
        btFDC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSegmentDataCaptureValue(true);
            }
        });
        btStandard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSegmentDataCaptureValue(false);
            }
        });

        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String serverUrl = etServer.getText().toString();
                String historyDay = etDays.getText().toString();
                if(!serverUrl.isEmpty()){
                    if(historyDay.isEmpty()) {
                        mySharedPreferences.setSettingsData(serverUrl, currentDataCaptureValue);
                    }else{
                        mySharedPreferences.setSettingsData(serverUrl, currentDataCaptureValue,Integer.valueOf(historyDay));
                    }
                    finish();
                }
            }
        });
    }

    /**
     * Cap nhat hien thi va gia tri cua <b>currentDataCaptureValue</b>
     * @param isFDC <b>true</b> - button FDC duoc chon
     */
    private void updateSegmentDataCaptureValue(boolean isFDC){
        currentDataCaptureValue = isFDC;
        if(isFDC){
            btFDC.setTextColor(Color.WHITE);
            btFDC.setBackgroundResource(R.drawable.segment_selected_bg_l);
            btStandard.setTextColor(Color.parseColor("#157efb"));
            btStandard.setBackgroundResource(R.drawable.segment_bg_r);
        }else{
            btFDC.setTextColor(Color.parseColor("#157efb"));
            btFDC.setBackgroundResource(R.drawable.segment_bg_l);
            btStandard.setTextColor(Color.WHITE);
            btStandard.setBackgroundResource(R.drawable.segment_selected_bg_r);
        }
    }
}

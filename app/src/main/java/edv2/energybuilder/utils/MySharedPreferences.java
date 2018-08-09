package edv2.energybuilder.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    SharedPreferences sharedpreferences;
    final String SERVER_KEY = "SERVER";
    final String DATA_CAPTURE_KEY = "DATA_CAPTURE";
    final String HISTORY_DAYS_KEY = "HISTORY_DAYS";
    final String USER_TOKEN_KEY = "USER_TOKEN_KEY";
    final String USER_NAME_KEY = "USER_NAME_KEY";
    final String DATA_CONFIG_KEY = "DATA_CONFIG";

    public MySharedPreferences(Context context) {
        sharedpreferences = context.getSharedPreferences("evd2.energybuilder", Context.MODE_PRIVATE);
    }

    public void setSettingsData(String serverUrl, boolean isFDC, int historyDays){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        if(historyDays<=0){
            historyDays = 7;
        }
        editor.putString(SERVER_KEY, serverUrl);
        editor.putBoolean(DATA_CAPTURE_KEY, isFDC);//fdc = true standard = false
        editor.putInt(HISTORY_DAYS_KEY, historyDays);
        editor.commit();
    }

    public void setSettingsData(String serverUrl, boolean isFDC){
        setSettingsData(serverUrl,isFDC,7);
    }

    public String getServerUrl(){
        return sharedpreferences.getString(SERVER_KEY,"");
    }
    public boolean isFDC(){
        return sharedpreferences.getBoolean(DATA_CAPTURE_KEY,true);
    }
    public String getDataCaptureType(){
        if(isFDC()){
            return "fdc";
        }else{
            return "std";
        }

    }

    public int getHistoryDays(){
        return sharedpreferences.getInt(HISTORY_DAYS_KEY,0);
    }
    public String getStringHistoryDays(String defaultValue){
        int value = getHistoryDays();
        if(value>0){
            return String.valueOf(value);
        }else{
            return defaultValue;
        }

    }


    public String getUserToken(){
        return sharedpreferences.getString(USER_TOKEN_KEY,"");
    }

    public void setUserToken(String userToken){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USER_TOKEN_KEY, userToken);
        editor.apply();
    }

    public String getUserName(){
        return sharedpreferences.getString(USER_NAME_KEY,"");
    }

    public void setUserName(String userName){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USER_NAME_KEY, userName);
        editor.apply();
    }

    public String getDataConfig(){
        return sharedpreferences.getString(DATA_CONFIG_KEY,"");
    }

    public void setDataConfig(String data){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(DATA_CONFIG_KEY, data);
        editor.apply();
    }


    public void userLogout(){
        setUserToken("");
    }


    public void clearData(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(DATA_CONFIG_KEY, "");
        editor.apply();
    }

    public void clearAllData(){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USER_TOKEN_KEY, "");
        editor.putString(SERVER_KEY, "");
        editor.putString(DATA_CONFIG_KEY, "");
        editor.putBoolean(DATA_CAPTURE_KEY, true);//fdc = true standard = false
        editor.putInt(HISTORY_DAYS_KEY, 0);
        editor.apply();
    }

}

package com.graha.purchasingapps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.graha.purchasingapps.global.Config;
import com.graha.purchasingapps.global.EventCompleted;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    String JSON_STRING, type, msg;
    EditText value_username, value_password;
    Button btnLogin;
    Config pConfig;
    public  boolean pMain = false;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Gson gson = new Gson();
        String objCon = getIntent().getStringExtra("pConfig");
        pConfig = gson.fromJson(objCon, Config.class);

        value_username = (EditText) findViewById(R.id.value_username);
        value_password = (EditText) findViewById(R.id.value_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(this);
        value_username.setFocusable(true);
        value_password.setFocusable(true);
    }

    @Override
    public void onClick(View view) {
        String username = value_username.getText().toString();
        String password = value_password.getText().toString();
        if (username != null && password != null && !username.equalsIgnoreCase("") && !password.equalsIgnoreCase("")) {
            pConfig.username = username;
            pConfig.password = password;

            new SvrLogin(new EventCompleted() {
                @Override
                public void onTaskCompleted(Config vConfig) {
                    if (vConfig != null) {
                        pMain = true;
                        pConfig = vConfig;
                        setTlog();
                        Gson gson = new Gson();
                        String jSonCon = gson.toJson(pConfig);
                        Intent gotoMain = new Intent(getApplicationContext(), MainActivity.class);
                        gotoMain.putExtra("pConfig", jSonCon);
                        startActivity(gotoMain);
                        finish();
                    }else{
                        pConfig.username = "";
                        value_username.setText("");
                        value_password.setText("");
                        value_username.requestFocus();
                    }
                }
            }).execute();

        }else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert );
            alertDialogBuilder.setTitle("Error");
            alertDialogBuilder
                    .setMessage("Username / Password Harus diisi..!")
                    .setIcon(R.drawable.warning)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.cancel();
                            //finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            //Toast.makeText(getApplicationContext(), "Username / Password Harus diisi.!", Toast.LENGTH_SHORT).show();
            value_username.requestFocus();
        }
    }

    class SvrLogin extends AsyncTask<Void, Void, String> {

        public EventCompleted delegate = null;

        public SvrLogin(EventCompleted delegate) {
            this.delegate = delegate;
        }



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(LoginActivity.this, "Proses Login", "Mohon Tunggu...", false, false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            JSON_STRING = s;
            try {
                setConfig();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            RequestHandler sh = new RequestHandler();

            HashMap<String, String> hashMap = new HashMap();
            hashMap.put("username", pConfig.username);
            hashMap.put("password", pConfig.password);
            hashMap.put("id_app", pConfig.pId_app);

            String s = sh.sendPostRequest(pConfig.urlLogin, hashMap);
            return s;
        }

        public void setConfig()throws JSONException{
            JSONObject jsonObject = null;
            JSONArray lreturn = null;
            JSONArray lresult = null;

            try {
                jsonObject = new JSONObject(JSON_STRING);
                lresult = jsonObject.getJSONArray("result");
                lreturn = jsonObject.getJSONArray("return");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for(int i = 0; i<lreturn.length(); i++){
                JSONObject jo = lreturn.getJSONObject(i);
                type = jo.getString("type");
                msg = jo.getString("msg");
            }

            if (type.equalsIgnoreCase("E")){
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(LoginActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert );
                alertDialogBuilder.setTitle("Error");
                alertDialogBuilder
                        .setMessage(msg)
                        .setIcon(R.drawable.warning)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.cancel();
                                //finish();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }else {
                JSONObject vjo = lresult.getJSONObject(0);
                pConfig.pId_user = vjo.getString("id_user");
                pConfig.username = vjo.getString("username");
                pConfig.pPlant = vjo.getString("plant");
                pConfig.pId_conn = vjo.getString("id_conn");
                pConfig.pUser_sap = vjo.getString("user_sap");
                pConfig.pPass_sap = vjo.getString("password");
                pConfig.pAshost = vjo.getString("ashost");
                pConfig.pSysnr = vjo.getString("sysnr");
                pConfig.pClient = vjo.getString("client");
                Date todayDate = Calendar.getInstance().getTime();
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                pConfig.pLast_in = formatter.format(todayDate);

                delegate.onTaskCompleted(pConfig);
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loading != null) {
            loading.dismiss();
            loading = null;
        }
    }
    private void setTlog(){
        Raising vRaising = new Raising(getApplicationContext(), pConfig.pId_app, pConfig.pImei, pConfig.pGlobalPath, pConfig.pDBName);
        TLog vTLog = new TLog(pConfig.pId, pConfig.pId_app, pConfig.pId_user, pConfig.pId_conn, pConfig.pImei, pConfig.pIp_webser,  pConfig.pLast_in);
        vRaising.insertTLog(vTLog);
        ArrayList<TLog> arrTLog = vRaising.getTLog();
        if (arrTLog.size() != 0) {
            TLog vTLog2 = arrTLog.get(0);
            pConfig.pId = vTLog2.getId();
        }
    }
}
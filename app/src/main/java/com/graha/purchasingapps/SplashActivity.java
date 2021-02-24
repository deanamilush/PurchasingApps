package com.graha.purchasingapps;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.graha.purchasingapps.global.Config;
import com.graha.purchasingapps.global.EventCompleted;
import com.graha.purchasingapps.global.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SplashActivity extends AppCompatActivity {

    private TextView textView;
    public Config pConfig = new Config();
    private String JSON_STRING, type, msg;
    public boolean pMain = false;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        File fileEvents = new File(pConfig.pInitAppl);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                pConfig.setBaseURL(text.toString());
            }
            br.close();
        } catch (IOException ignored) {
        }

        if (pConfig.baseURL != null) {
            //getJSON();
            new GetJSON((EventCompleted) vConfig -> {
                pConfig.pAppname = vConfig.pAppname;
                pConfig.pVer = vConfig.pVer;
                pConfig.pDev = vConfig.pDev;
                pConfig.pImei = deviceId();
                pConfig.pIp_webser = pConfig.baseURL;
                textView = (TextView) findViewById(R.id.textView);
                textView.setText(pConfig.pVer + "." + pConfig.pDev);
                getTlog();
            }).execute();
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
            alertDialogBuilder.setTitle("Error");
            alertDialogBuilder
                    .setMessage("File INITAPPL Tidak ditemukan, Hubungi segera Administrator..!")
                    .setIcon(R.drawable.warning)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.cancel();
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    class GetJSON extends AsyncTask<Void, Void, String> {

        public EventCompleted delegate = null;

        public GetJSON(EventCompleted delegate) {
            this.delegate = delegate;
        }

        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(SplashActivity.this, "Cek Version", "Mohon Tunggu...", false, false);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            JSON_STRING = s;
            try {
                cekVersion();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... params) {
            RequestHandler rh = new RequestHandler();
            HashMap<String, String> hashMap = new HashMap();
            hashMap.put("id_app", pConfig.pId_app);

            String s = rh.sendPostRequest(pConfig.urlCekVer, hashMap);
            return s;
        }

        private void cekVersion() throws JSONException {
            JSONObject jsonObject = null;
            JSONArray lreturn = null;
            JSONArray lresult = null;
            Config vConfig;

            try {
                jsonObject = new JSONObject(JSON_STRING);
                lresult = jsonObject.getJSONArray("result");
                lreturn = jsonObject.getJSONArray("return");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < lreturn.length(); i++) {
                JSONObject jo = lreturn.getJSONObject(i);
                type = jo.getString("type");
                msg = jo.getString("msg");
            }

            if (type.equalsIgnoreCase("E")) {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            } else {
                JSONObject vjo = lresult.getJSONObject(0);
                pConfig.pAppname = vjo.getString("appname");
                pConfig.pVer = vjo.getString("version");
                pConfig.pDev = vjo.getString("dev");

                int versionCode = BuildConfig.VERSION_CODE;
                String versionName = BuildConfig.VERSION_NAME;
                String sVerCode = String.valueOf(versionCode);
                if (sVerCode.equalsIgnoreCase(pConfig.pVer) && versionName.equalsIgnoreCase(pConfig.pDev)) {
                    delegate.onTaskCompleted(pConfig);

                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                    alertDialogBuilder.setTitle("Error");
                    alertDialogBuilder
                            .setMessage("Segera Perbaharui Aplikasi versi Terbaru..!")
                            .setIcon(R.drawable.warning)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        }
    }

    private void getTlog() throws JSONException {
        if (pConfig.pVer != null) {
            Raising vRaising = new Raising(getApplicationContext(), pConfig.pId_app, pConfig.pImei, pConfig.pGlobalPath, pConfig.pDBName);
            ArrayList<TLog> arrTLog = vRaising.getTLog();
            if (arrTLog.size() != 0) {
                TLog vTLog = arrTLog.get(0);
                pConfig.pId = vTLog.getId();
                pConfig.pId_user = vTLog.getId_user();
                pConfig.pId_conn = vTLog.getId_conn();

                pConfig.pIp_webser = vTLog.getIp_webser();
                pConfig.pImei = vTLog.getImei();
                pConfig.pLast_in = vTLog.getLast_in();
                String result = verifLog();

                if (result != null || !result.equalsIgnoreCase("")) {
                    JSONObject jsonObject = null;
                    JSONArray lreturn = null;
                    JSONArray lresult = null;

                    try {
                        jsonObject = new JSONObject(result);
                        lresult = jsonObject.getJSONArray("result");
                        lreturn = jsonObject.getJSONArray("return");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < lreturn.length(); i++) {
                        JSONObject jo = lreturn.getJSONObject(i);
                        type = jo.getString("type");
                        msg = jo.getString("msg");
                    }

                    if (type.equalsIgnoreCase("E")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                        alertDialogBuilder.setTitle("Error");
                        alertDialogBuilder
                                .setMessage(msg)
                                .setIcon(R.drawable.warning)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int arg1) {
                                        dialog.cancel();
                                        finish();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                    } else {
                        JSONObject vjo = lresult.getJSONObject(0);
                        pConfig.username = vjo.getString("username");
                        pConfig.password = vjo.getString("pass");
                        pConfig.pPlant = vjo.getString("plant");
                        pConfig.pUser_sap = vjo.getString("user_sap");
                        pConfig.pPass_sap = vjo.getString("password");
                        pConfig.pAshost = vjo.getString("ashost");
                        pConfig.pSysnr = vjo.getString("sysnr");
                        pConfig.pClient = vjo.getString("client");
                        Date todayDate = Calendar.getInstance().getTime();
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
                        pConfig.pLast_in = formatter.format(todayDate);

                        TLog vUpTLog = new TLog(pConfig.pId, pConfig.pId_app, pConfig.pId_user, pConfig.pId_conn, pConfig.pImei, pConfig.pIp_webser, pConfig.pLast_in);
                        vRaising.updateTLog(vUpTLog);

                        pMain = true;
                        //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                    alertDialogBuilder.setTitle("Error");
                    alertDialogBuilder
                            .setMessage("Invalid data Log, harap lakukan logout dahulu.!")
                            .setIcon(R.drawable.warning)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int arg1) {
                                    dialog.cancel();
                                    finish();
                                }
                            });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    //Toast.makeText(getApplicationContext(), "Invalid data Log, harap lakukan logout dahulu.!", Toast.LENGTH_SHORT).show();
                }

                if (pMain) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Gson gson = new Gson();
                            String jSonCon = gson.toJson(pConfig);
                            Intent gotoMain = new Intent(getApplicationContext(), MainActivity.class);
                            gotoMain.putExtra("pConfig", jSonCon);
                            startActivity(gotoMain);
                            finish();
                        }
                    }, 3000);
                }

            } else {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Gson gson = new Gson();
                        String jSonCon = gson.toJson(pConfig);
                        Intent gotologin = new Intent(SplashActivity.this, LoginActivity.class);
                        gotologin.putExtra("pConfig", jSonCon);
                        startActivity(gotologin);
                        finish();
                    }
                }, 3000);
            }
        } else {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SplashActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
            alertDialogBuilder.setTitle("Error");
            alertDialogBuilder
                    .setMessage("Invalid Version, hubungi administrator..!")
                    .setIcon(R.drawable.warning)
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int arg1) {
                            dialog.cancel();
                            finish();
                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    }

    private String verifLog() {
        ServiceLogin serviceLogin = new ServiceLogin(pConfig);
        String result = new String();
        try {
            result = serviceLogin.execute().get();
        } catch (Exception e) {
            System.out.println("Gagal");
        }
        return result;
    }

    // GET IMEI
    TelephonyManager telephonyManager;

    private String deviceId() {
        String vImei = null;
        telephonyManager = (TelephonyManager) getSystemService(SplashActivity.this.TELEPHONY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return "";
            }
            vImei = telephonyManager.getImei();
        } else {
            vImei = telephonyManager.getDeviceId();
        }
        return vImei;
    }
}
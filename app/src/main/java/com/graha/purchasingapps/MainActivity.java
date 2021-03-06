package com.graha.purchasingapps;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.gson.Gson;
import com.graha.purchasingapps.global.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;

    private Config pConfig;
    private final ArrayList<UserData> list = new ArrayList<>();
    private ProgressBar progressBar;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ListAdapter adapter;
    private int jobId = 0;
    private Timer autoUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new Gson();
        String objCon = getIntent().getStringExtra("pConfig");
        getMyData();
        pConfig = gson.fromJson(objCon, Config.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration
                (recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ListAdapter(list);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onResume() {
        super.onResume();
        autoUpdate = new Timer();
        autoUpdate.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    jobId++;
                    Toast.makeText(getApplicationContext(), "Loading For Refresh Data ", Toast.LENGTH_SHORT).show();
                    getListPr();
                });
            }
        }, 0, 20000); // updates each 40 secs
    }

    @Override
    public void onPause() {
        autoUpdate.cancel();
        super.onPause();
    }


   public void getListPr(){
       list.clear();
       progressBar.setVisibility(View.VISIBLE);
       final int DEFAULT_TIMEOUT = 20 * 1000;
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(DEFAULT_TIMEOUT);
        String url = "http://192.168.1.8/GlobalInc/valPrPO.php";
        RequestParams params = new RequestParams();
        params.put("ashost", pConfig.pAshost);
        params.put("sysnr", pConfig.pSysnr);
        params.put("client", pConfig.pClient);
        params.put("usap", pConfig.pUser_sap);
        params.put("psap", pConfig.pPass_sap);
        client.post(url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                progressBar.setVisibility(View.INVISIBLE);
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray jsonArray = responseObject.getJSONArray("return");
                    JSONArray tPurc = responseObject.getJSONArray("t_purc");
                    JSONArray tablePr = responseObject.getJSONArray("t_pr");
                    JSONArray tablePo = responseObject.getJSONArray("t_po");
                    JSONObject type = jsonArray.getJSONObject(0);
                    String typeReturn = type.getString("type");
                    String messageReturn = type.getString("msg");

                        if (typeReturn.equalsIgnoreCase("E")) {
                            AlertDialog.Builder alertDialogBuilder =
                                    new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                            alertDialogBuilder.setTitle("Error");
                            alertDialogBuilder
                                    .setMessage(messageReturn)
                                    .setIcon(R.drawable.warning)
                                    .setCancelable(false)
                                    .setPositiveButton("OK", (dialog, arg1) -> {
                                        dialog.cancel();
                                        //finish();
                                    });
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                        else {
                            String lBednr;
                            for (int i = 0; i < tPurc.length(); i++) {
                                JSONObject user = tPurc.getJSONObject(i);
                                UserData userData = new UserData();
                                userData.setName(user.getString("BEDNR"));
                                lBednr = user.getString("BEDNR");
                                userData.setPrThisMonth(0);
                                userData.setPrLastMonth(0);
                                userData.setPrMonthAgo(0);
                                userData.setPoThisMonth(0);
                                userData.setPoLastMonth(0);
                                userData.setPoMonthAgo(0);

                                for (int k = 0; k < tablePr.length(); k++) {
                                    JSONObject dtPr = tablePr.getJSONObject(k);
                                    if (lBednr.equalsIgnoreCase(dtPr.getString("BEDNR"))){
                                        userData.setPrThisMonth(dtPr.getInt("QCUR_MT"));
                                        userData.setPrLastMonth(dtPr.getInt("QPREV_MT"));
                                        userData.setPrMonthAgo(dtPr.getInt("QLAST_MT"));
                                        break;
                                    }
                                }

                                for (int k = 0; k < tablePo.length(); k++) {
                                    JSONObject dtPo = tablePo.getJSONObject(k);
                                    if (lBednr.equalsIgnoreCase(dtPo.getString("BEDNR"))){
                                        userData.setPoThisMonth(dtPo.getInt("QCUR_MT"));
                                        userData.setPoLastMonth(dtPo.getInt("QPREV_MT"));
                                        userData.setPoMonthAgo(dtPo.getInt("QLAST_MT"));
                                        break;
                                    }
                                }

                                list.add(userData);
                                adapter.notifyDataSetChanged();
                            }
                    }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.INVISIBLE);
                String errorMessage;
                switch (statusCode) {
                    case 401:
                        errorMessage = statusCode + " : Bad Request";
                        break;
                    case 403:
                        errorMessage = statusCode + " : Forbiden";
                        break;
                    case 404:
                        errorMessage = statusCode + " : Not Found";
                        break;
                    default:
                        errorMessage =  statusCode + " : " + error.getMessage();
                        break;
                }
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_logout:
                androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder =
                        new androidx.appcompat.app.AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert );
                alertDialogBuilder.setTitle("Information");
                alertDialogBuilder
                        .setMessage("Apakah anda yakin untuk logout.?")
                        .setIcon(R.drawable.information)
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            // do something when the button is clicked
                            public void onClick(DialogInterface dialog, int arg1) {
                                setLogOut();
                                Intent gotoSplash = new Intent(MainActivity.this, SplashActivity.class);
                                startActivity(gotoSplash);
                                finish();
                            }
                        })
                        .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int arg1) {
                                dialog.cancel();
                            }
                        })
                        .show();
        }
        return false;
    }

    private void setLogOut(){
        Raising vRaising = new Raising(getApplicationContext(), pConfig.pId_app, pConfig.pImei, pConfig.pGlobalPath, pConfig.pDBName);
        TLog vTLog = new TLog(pConfig.pId, pConfig.pId_app, pConfig.pId_user, pConfig.pId_conn, pConfig.pImei, pConfig.pIp_webser,  pConfig.pLast_in);
        vRaising.deleteTLog(vTLog);
    }
    public String getMyData(){
        Gson gson = new Gson();
        String jSonCon = gson.toJson(pConfig);
        return  jSonCon;
    }
}
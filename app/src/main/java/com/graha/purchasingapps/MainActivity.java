package com.graha.purchasingapps;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.graha.purchasingapps.global.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    Config pConfig;
    private final ArrayList<UserData> list = new ArrayList<>();
    RecyclerView recyclerView;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new Gson();
        String objCon = getIntent().getStringExtra("pConfig");
        getMyData();
        pConfig = gson.fromJson(objCon, Config.class);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        getListPr();

        /*list.addAll(getListUser());
        showList();*/
    }

    private void getListPr(){
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://192.168.1.8/GlobalInc/valPrPO.php";
        RequestParams params = new RequestParams();
        params.put("ashost", pConfig.pAshost);
        params.put("sysnr", pConfig.pSysnr);
        params.put("client", pConfig.pClient);
        params.put("usap", pConfig.pUser_sap);
        params.put("psap", pConfig.pPass_sap);
        client.get(url,params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("return");
                    JSONObject object = jsonArray.getJSONObject(0);
                    String typeReturn = object.getString("type");
                    String messageReturn = object.getString("msg");
                    if (typeReturn.equalsIgnoreCase("E")) {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
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
                    } else{
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
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
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }

    private void showList(){
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ListAdapter cardAdapter = new ListAdapter(list);
        recyclerView.setAdapter(cardAdapter);
    }

    public ArrayList<UserData> getListUser() {
        String[] dataName = getResources().getStringArray(R.array.data_name);
        int[] dataThisMonth = getResources().getIntArray(R.array.this_month);
        int[] dataLastMonth = getResources().getIntArray(R.array.last_month);
        int[] dataMonthAgo = getResources().getIntArray(R.array.month_ago);
        ArrayList<UserData> listUser = new ArrayList<>();
        for (int i = 0; i < dataName.length; i++) {
            UserData list = new UserData();
            list.setName(dataName[i]);
            list.setPrThisMonth(dataThisMonth[i]);
            list.setPrLastMonth(dataLastMonth[i]);
            list.setPrMonthAgo(dataMonthAgo[i]);
            list.setPoThisMonth(dataMonthAgo[i]);
            list.setPoLastMonth(dataMonthAgo[i]);
            list.setPoMonthAgo(dataMonthAgo[i]);
            listUser.add(list);
        }
        return listUser;
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
                androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Light_Dialog_Alert );
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
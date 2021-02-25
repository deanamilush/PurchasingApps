package com.graha.purchasingapps;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
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
    TextView textPic;
    private final ListAdapter adapter = new ListAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new Gson();
        String objCon = getIntent().getStringExtra("pConfig");
        getMyData();
        pConfig = gson.fromJson(objCon, Config.class);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        textPic = findViewById(R.id.tv_item_name);

        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        getListPr();
    }



   public void getListPr(){
       final ArrayList<UserData> listItems = new ArrayList<>();
        AsyncHttpClient client = new AsyncHttpClient();
        UserData userData = new UserData();
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
                ArrayList<UserData> listData = new ArrayList<>();
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {

                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.getJSONArray("return");
                        JSONObject type = jsonArray.getJSONObject(0);
                        String typeReturn = type.getString("type");
                        String messageReturn = type.getString("msg");
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
                        }else {
                            JSONArray tPr = jsonObject.getJSONArray("t_pr");
                            for (int j = 0; j < tPr.length(); j++) {
                                JSONObject user = tPr.getJSONObject(j);
                                userData.setName(user.getString("BEDNR"));
                                userData.setPrThisMonth(user.getInt("QCUR_MT"));
                                userData.setPrLastMonth(user.getInt("QPREV_MT"));
                                userData.setPrMonthAgo(user.getInt("QLAST_MT"));
                                listItems.add(userData);
                                adapter.setData(listItems);
                               // adapter.notifyDataSetChanged();
                            }
                        }
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
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
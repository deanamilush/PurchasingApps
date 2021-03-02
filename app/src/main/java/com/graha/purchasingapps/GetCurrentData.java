package com.graha.purchasingapps;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.google.gson.Gson;
import com.graha.purchasingapps.global.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/*public class GetCurrentData extends JobService {

    public static final String TAG = GetCurrentData.class.getSimpleName();
    private final ArrayList<UserData> listUpdate = new ArrayList<>();
    private final ListAdapter adapter = new ListAdapter(listUpdate);
    Config pConfig;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.d(TAG, "onStartJob() Executed");
        jobParameters.getExtras().getString("ashost");
        jobParameters.getExtras().getString("sysnr");
        jobParameters.getExtras().getString("client");
        jobParameters.getExtras().getString("usap");
        jobParameters.getExtras().getString("psap");
        getCurrentList(jobParameters);

        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob() Executed");
        return true;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Gson gson = new Gson();
        String objCon = intent.getStringExtra("pConfig");
        getMyData();
        pConfig = gson.fromJson(objCon, Config.class);
        return super.onStartCommand(intent, flags, startId);
    }

    private void getCurrentList(final JobParameters job){
        AsyncHttpClient client = new AsyncHttpClient();
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
                String result = new String(responseBody);
                Log.d(TAG, result);
                try {
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray jsonArray = responseObject.getJSONArray("return");
                    JSONArray tablePr = responseObject.getJSONArray("t_pr");
                    JSONObject type = jsonArray.getJSONObject(0);
                    String typeReturn = type.getString("type");
                    String messageReturn = type.getString("msg");

                    if (typeReturn.equalsIgnoreCase("E")) {
                        AlertDialog.Builder alertDialogBuilder =
                                new AlertDialog.Builder(getApplicationContext(), android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
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
                        for (int i = 0; i < tablePr.length(); i++) {
                            JSONObject user = tablePr.getJSONObject(i);
                            UserData userData = new UserData();
                            userData.setName(user.getString("BEDNR"));
                            userData.setPrThisMonth(user.getInt("QCUR_MT"));
                            userData.setPrLastMonth(user.getInt("QPREV_MT"));
                            userData.setPrMonthAgo(user.getInt("QLAST_MT"));
                            listUpdate.add(userData);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(getApplicationContext(), "Data Update!", Toast.LENGTH_SHORT).show();
                            jobFinished(job, false);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    jobFinished(job, true);
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                jobFinished(job, true);
            }
        });
    }
    public String getMyData(){
        Gson gson = new Gson();
        String jSonCon = gson.toJson(pConfig);
        return  jSonCon;
    }
}*/

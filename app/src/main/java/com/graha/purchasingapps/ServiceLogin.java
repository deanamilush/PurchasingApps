package com.graha.purchasingapps;

import android.os.AsyncTask;

import com.graha.purchasingapps.global.Config;

import java.util.HashMap;

public class ServiceLogin extends AsyncTask<String, String, String> {
    String result = new String();
    Config vConfig;

    public ServiceLogin(Config vConfig) {
        this.vConfig = vConfig;
    }

    @Override
    protected String doInBackground(String... param) {
        RequestHandler sh = new RequestHandler();

        HashMap<String, String> hashMap = new HashMap();
        hashMap.put("id_user", vConfig.pId_user);
        hashMap.put("id_app", vConfig.pId_app);
        hashMap.put("id_conn", vConfig.pId_conn);

        String s = sh.sendPostRequest(vConfig.urlVerifLog, hashMap);
        return s;
    }
}
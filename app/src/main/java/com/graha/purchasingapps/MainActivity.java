package com.graha.purchasingapps;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    Config pConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Gson gson = new Gson();
        String objCon = getIntent().getStringExtra("pConfig");
        getMyData();
        pConfig = gson.fromJson(objCon, Config.class);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_pr, R.id.navigation_po)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_view);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
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
                                Intent gotoSplash = new Intent(MainActivity.this, LoginActivity.class);
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
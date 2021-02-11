package com.graha.purchasingapps;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class Raising {

    private DBManager dbManager;
    private Context context;
    private String id_app;
    private String lImei;
    private TLog vTLog;
    private String pGlobalPath ;
    private String pDBName;
    private String tbLOg = "T_LOG";
    private String cmId = "id";
    private String cmIdUser = "id_user";
    private String cmIdApp = "id_app";
    private String cmIdConn = "id_conn";
    private String cmImei = "imei";
    private String cmIp = "ip_webser";
    private String cmLast = "last_in";
    private String createTable = "CREATE TABLE "+tbLOg+" ("+
            cmId +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
            cmIdApp+" TEXT NOT NULL, " +
            cmIdUser+" TEXT NOT NULL, " +
            cmIdConn+" TEXT NOT NULL," +
            cmImei+" TEXT NOT NULL," +
            cmIp+" TEXT NOT NULL," +
            cmLast+" TEXT NOT NULL" +
            ")";
    private String dropTable = "DROP TABLE IF EXISTS "+tbLOg;

    public Raising(Context context, String id_app, String lImei, String pGlobalPath, String pDBName) {
        this.pGlobalPath = pGlobalPath;
        this.pDBName = pDBName;
        String myDb = this.pGlobalPath + this.pDBName;
        dbManager = new DBManager(context, myDb, null, 1, createTable, dropTable);
        this.context = context;
        this.id_app = id_app;
        this.lImei = lImei;
    }

    public ArrayList<TLog> getTLog() {
        ArrayList<TLog> arrTLog = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + tbLOg + " WHERE " + cmIdApp + " = '" + id_app.trim() + "' AND " + cmImei + " = '" + lImei.trim() + "'";
        SQLiteDatabase database = dbManager.getThis().getWritableDatabase();
        Cursor cursor = database.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                vTLog = new TLog(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6));
                arrTLog.add(vTLog);
            } while (cursor.moveToNext());
        }

        Log.e("select data ", "" + arrTLog);
        database.close();

        return arrTLog;
    }

    public void insertTLog(TLog vTLog) {
        String insertQuery = "INSERT INTO " + tbLOg +" VALUES (null, '"+vTLog.getId_app()+"', '"+vTLog.getId_user()+"', " +
                "'"+vTLog.getId_conn()+"', '"+vTLog.getImei()+"', '"+vTLog.getIp_webser()+"','"+vTLog.getLast_in()+"')";
        /*String insertQuery = "INSERT INTO " + tbLOg +" VALUES (null, 'G191205001', 'id_user', " +
                "'id_conn', 'ip_webser', 'imei', 'last_in')";*/
        SQLiteDatabase database = dbManager.getThis().getWritableDatabase();

        Log.e("insert TLog ", "" + insertQuery);
        database.execSQL(insertQuery);
        database.close();
    }

    public void updateTLog(TLog vTLog) {
        String updatetQuery = "UPDATE " + tbLOg + " SET "
                + cmIp + " = '" + vTLog.getIp_webser() + "',"
                + cmImei + " = '" + vTLog.getImei() + "',"
                + cmLast + " = '" + vTLog.getLast_in()
                + "' WHERE " + cmId + " = "+vTLog.getId();
        SQLiteDatabase database = dbManager.getThis().getWritableDatabase();

        Log.e("update data ", "" + updatetQuery);
        database.execSQL(updatetQuery);
        database.close();
    }

    public void deleteTLog(TLog vTLog) {
        //String deleteQuery = "DELETE * FROM " + tbLOg + " WHERE " + cmId + " = " + vTLog.getId() + " OR TRIM(" + cmIdApp + ") = " + vTLog.getId_app().trim();
        String deleteQuery = "DELETE FROM " + tbLOg + " WHERE " + cmIdApp + " = '"+vTLog.getId_app()+"' AND " + cmIdUser + " = '" +vTLog.getId_user()+ "'" +
                " AND " + cmImei + " = '" +vTLog.getImei()+ "' AND " + cmId+ " = " +vTLog.getId() ;
        SQLiteDatabase database = dbManager.getThis().getWritableDatabase();

        database.execSQL(deleteQuery);
        database.close();
    }
}
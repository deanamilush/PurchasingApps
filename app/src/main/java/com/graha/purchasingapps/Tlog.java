package com.graha.purchasingapps;

class TLog {
    private int id;
    private String id_app, id_user, id_conn, ip_webser, imei, last_in ;

    public TLog(int id, String id_app, String id_user, String id_conn, String imei, String ip_webser, String last_in) {
        this.id = id;
        this.id_app = id_app;
        this.id_user = id_user;
        this.id_conn = id_conn;
        this.imei = imei;
        this.ip_webser = ip_webser;
        this.last_in = last_in;
    }

    public int getId() {
        return id;
    }

    public String getId_app() {
        return id_app;
    }

    public String getId_user() {
        return id_user;
    }

    public String getId_conn() {
        return id_conn;
    }

    public String getIp_webser() {
        return ip_webser;
    }

    public String getImei() {
        return imei;
    }

    public String getLast_in() {
        return last_in;
    }
}
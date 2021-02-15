package com.graha.purchasingapps.global;

public class Config {
    public String pGlobalPath = "/storage/emulated/0/init/";
    public String pInitAppl = pGlobalPath + "initappl.txt";
    public String pDBName = "dashappdb";
    public String pDBApp = "stockopdb";
    public String pId_app = "A200706002"; //id_app dari t_app dashboard

    public String baseURL;
    public String urlLogin;
    public String urlCekVer;
    public String urlVerifLog;
    public  String urlChangePas;
    public String urlScanBarang;
    public String urlVerMatnr;
    public String urlRfcRes;
    private String pDirGlobal = "GlobalInc";

    // global T_LOG
    public int pId;
    public String pId_user;
    public String pId_conn;
    public String pImei;
    public String pIp_webser;
    public String pLast_in;
    public String pAppname;
    public String pVer;
    public String pDev;
    public String pPlant;
    public String pAshost;
    public String pSysnr;
    public String pClient;

    // global user SAP
    public String pUser_sap;
    public String pPass_sap;

    // global variable untuk login
    public String username = "username";
    public String password = "password";

    //global Variabel Aplikasi Stock Opname
    public String pDate;
    public String pLgnum;
    public String pLgpla;
    public String pPic;
    public Integer pMenu;

/*    public Config() {
    }*/

    public void setBaseURL(String baseURL) {
        // Set URL for PHP WebService
        this.baseURL = baseURL;
        this.urlLogin = baseURL+pDirGlobal+"/loginService.php";
        this.urlCekVer = baseURL+pDirGlobal+"/verService.php";
        this.urlVerifLog = baseURL+pDirGlobal+"/verifLog.php";
        this.urlChangePas = baseURL+pDirGlobal+"/changePass.php";
        this.urlScanBarang = baseURL+pDirGlobal+"/valBin.php";
        this.urlVerMatnr = baseURL+pDirGlobal+"/valMatnr.php";
        this.urlRfcRes = baseURL+pDirGlobal+"/valRfcRes.php";
//        this.urlRfcRes = baseURL+pDirGlobal+"/valBin.php";
    }
}

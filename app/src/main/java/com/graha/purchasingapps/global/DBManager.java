package com.graha.purchasingapps.global;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBManager extends SQLiteOpenHelper {

    private String createTable, dropTable, addTable;

    public DBManager(@Nullable Context varcontext, @Nullable String vardbname, @Nullable SQLiteDatabase.CursorFactory varfactory, int varversion, String createTable, String dropTable) {
        super(varcontext,vardbname,varfactory,varversion);
        this.createTable = createTable;
        this.dropTable = dropTable;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(dropTable);
        onCreate(sqLiteDatabase);
    }

    public DBManager getThis() {
        return this;
    }
}
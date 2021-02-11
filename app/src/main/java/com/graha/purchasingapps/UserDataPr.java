package com.graha.purchasingapps;

import android.os.Parcel;
import android.os.Parcelable;

public class UserDataPr implements Parcelable {

    String name;
    Integer thisMonth;
    Integer lastMonth;
    Integer monthAgo;

    public UserDataPr(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0) {
            thisMonth = null;
        } else {
            thisMonth = in.readInt();
        }
        if (in.readByte() == 0) {
            lastMonth = null;
        } else {
            lastMonth = in.readInt();
        }
        if (in.readByte() == 0) {
            monthAgo = null;
        } else {
            monthAgo = in.readInt();
        }
    }

    public static final Creator<UserDataPr> CREATOR = new Creator<UserDataPr>() {
        @Override
        public UserDataPr createFromParcel(Parcel in) {
            return new UserDataPr(in);
        }

        @Override
        public UserDataPr[] newArray(int size) {
            return new UserDataPr[size];
        }
    };

    public UserDataPr() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getThisMonth() {
        return thisMonth;
    }

    public void setThisMonth(int thisMonth) {
        this.thisMonth = thisMonth;
    }

    public Integer getLastMonth() {
        return lastMonth;
    }

    public void setLastMonth(Integer lastMonth) {
        this.lastMonth = lastMonth;
    }

    public Integer getMonthAgo() {
        return monthAgo;
    }

    public void setMonthAgo(Integer monthAgo) {
        this.monthAgo = monthAgo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        if (thisMonth == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(thisMonth);
        }
        if (lastMonth == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(lastMonth);
        }
        if (monthAgo == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(monthAgo);
        }
    }
}

package com.graha.purchasingapps;

import android.os.Parcel;
import android.os.Parcelable;

public class UserData implements Parcelable{

    String name;
    Integer poThisMonth;
    Integer poLastMonth;
    Integer poMonthAgo;
    Integer prThisMonth;
    Integer prLastMonth;
    Integer prMonthAgo;

    protected UserData(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0) {
            poThisMonth = null;
        } else {
            poThisMonth = in.readInt();
        }
        if (in.readByte() == 0) {
            poLastMonth = null;
        } else {
            poLastMonth = in.readInt();
        }
        if (in.readByte() == 0) {
            poMonthAgo = null;
        } else {
            poMonthAgo = in.readInt();
        }
        if (in.readByte() == 0) {
            prThisMonth = null;
        } else {
            prThisMonth = in.readInt();
        }
        if (in.readByte() == 0) {
            prLastMonth = null;
        } else {
            prLastMonth = in.readInt();
        }
        if (in.readByte() == 0) {
            prMonthAgo = null;
        } else {
            prMonthAgo = in.readInt();
        }
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            return new UserData(in);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    public UserData() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPoThisMonth() {
        return poThisMonth;
    }

    public void setPoThisMonth(Integer poThisMonth) {
        this.poThisMonth = poThisMonth;
    }

    public Integer getPoLastMonth() {
        return poLastMonth;
    }

    public void setPoLastMonth(Integer poLastMonth) {
        this.poLastMonth = poLastMonth;
    }

    public Integer getPoMonthAgo() {
        return poMonthAgo;
    }

    public void setPoMonthAgo(Integer poMonthAgo) {
        this.poMonthAgo = poMonthAgo;
    }

    public Integer getPrThisMonth() {
        return prThisMonth;
    }

    public void setPrThisMonth(Integer prThisMonth) {
        this.prThisMonth = prThisMonth;
    }

    public Integer getPrLastMonth() {
        return prLastMonth;
    }

    public void setPrLastMonth(Integer prLastMonth) {
        this.prLastMonth = prLastMonth;
    }

    public Integer getPrMonthAgo() {
        return prMonthAgo;
    }

    public void setPrMonthAgo(Integer prMonthAgo) {
        this.prMonthAgo = prMonthAgo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        if (poThisMonth == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(poThisMonth);
        }
        if (poLastMonth == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(poLastMonth);
        }
        if (poMonthAgo == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(poMonthAgo);
        }
        if (prThisMonth == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(prThisMonth);
        }
        if (prLastMonth == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(prLastMonth);
        }
        if (prMonthAgo == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(prMonthAgo);
        }
    }
}

package com.cfwin.cfwinblockchain.beans.mail;

import android.os.Parcel;
import android.os.Parcelable;

import javax.mail.Store;

public class StoreBean implements Parcelable {
    private String[] parmas;
    private Store store;
    public StoreBean(String[] parmas, Store store){
        this.parmas = parmas;
        this.store = store;
    }

    protected StoreBean(Parcel in) {
        parmas = in.createStringArray();
        store = (Store) in.readValue(this.getClass().getClassLoader());
    }

    public static final Creator<StoreBean> CREATOR = new Creator<StoreBean>() {
        @Override
        public StoreBean createFromParcel(Parcel in) {
            return new StoreBean(in);
        }

        @Override
        public StoreBean[] newArray(int size) {
            return new StoreBean[size];
        }
    };

    public String[] getParmas() {
        return parmas;
    }

    public Store getStore() {
        return store;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(parmas);
        dest.writeValue(store);
    }
}

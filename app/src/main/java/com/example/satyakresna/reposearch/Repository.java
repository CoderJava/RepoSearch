package com.example.satyakresna.reposearch;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by satyakresna on 02-Jul-17.
 */

public class Repository implements Parcelable {

    private String name;
    private Owner owner;

    public Repository() {

    }

    public Repository(String name, Owner owner) {
        setName(name);
        setOwner(owner);
    }

    public Repository(Parcel in) {
        String[] data = new String[2];
        in.readStringArray(data);
        this.name = data[0];
        this.owner = new Owner(data[1]);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.name, this.owner.getLogin()});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Repository createFromParcel(Parcel in) {
            return new Repository(in);
        }

        public Repository[] newArray(int size) {
            return new Repository[size];
        }
    };

    public class Owner {

        public Owner() {

        }

        public Owner(String login) {
            setLogin(login);
        }

        private String login;

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

    }
}


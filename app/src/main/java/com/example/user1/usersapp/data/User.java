package com.example.user1.usersapp.data;

import android.content.ContentValues;
import android.database.Cursor;

import com.example.user1.usersapp.data.UsersContract.UserEntry;

import java.util.UUID;

/**
 * Created by User 1 on 28/12/2017.
 */

public class User {
    private String id;
    private String name;
    private String specialty;
    private String phoneNumber;
    private String bio;
    private String avatarUri;

    public User(String name, String specialty, String phoneNumber, String bio, String avatarUri) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.specialty = specialty;
        this.phoneNumber = phoneNumber;
        this.bio = bio;
        this.avatarUri = avatarUri;
    }

    public User (){

    }

    public User(Cursor cursor) {
        id = cursor.getString(cursor.getColumnIndex(UserEntry.ID));
        name = cursor.getString(cursor.getColumnIndex(UserEntry.NAME));
        specialty = cursor.getString(cursor.getColumnIndex(UserEntry.SPECIALTY));
        phoneNumber = cursor.getString(cursor.getColumnIndex(UserEntry.PHONE_NUMBER));
        bio = cursor.getString(cursor.getColumnIndex(UserEntry.BIO));
        avatarUri = cursor.getString(cursor.getColumnIndex(UserEntry.AVATAR_URI));
    }

    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put(UserEntry.ID,id);
        values.put(UserEntry.NAME, name);
        values.put(UserEntry.SPECIALTY, specialty);
        values.put(UserEntry.PHONE_NUMBER, phoneNumber);
        values.put(UserEntry.BIO, bio);
        values.put(UserEntry.AVATAR_URI, avatarUri);
        return values;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getAvatarUri() {
        return avatarUri;
    }

    public void setAvatarUri(String avatarUri) {
        this.avatarUri = avatarUri;
    }
}

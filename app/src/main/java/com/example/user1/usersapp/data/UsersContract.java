package com.example.user1.usersapp.data;

import android.provider.BaseColumns;

/**
 * Created by User 1 on 28/12/2017.
 */

public class UsersContract {
    public static abstract class UserEntry implements BaseColumns{
        public static final String TABLE_NAME = "user";
        public static final String ID = "id";
        public static final String NAME = "name";
        public static final String SPECIALTY = "specialty";
        public static final String PHONE_NUMBER ="phoneNumber";
        public static final String AVATAR_URI = "avatarUri";
        public static final String BIO="bio";
    }
}

package com.example.user1.usersapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.user1.usersapp.data.UsersContract.UserEntry;

/**
 * Created by User 1 on 28/12/2017.
 */

public class UsersDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Users.db";

    public UsersDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + UserEntry.TABLE_NAME + " ("
                + UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + UserEntry.ID + " TEXT NOT NULL,"
                + UserEntry.NAME + " TEXT NOT NULL,"
                + UserEntry.SPECIALTY + " TEXT NOT NULL,"
                + UserEntry.PHONE_NUMBER + " TEXT NOT NULL,"
                + UserEntry.BIO + " TEXT NOT NULL,"
                + UserEntry.AVATAR_URI + " TEXT,"
                + "UNIQUE (" + UserEntry.ID + "))");

        mockData(sqLiteDatabase);
    }

    private void mockData(SQLiteDatabase sqLiteDatabase) {
        mockUser(sqLiteDatabase, new User("Carlos Perez", "Abogado penalista",
                "300 200 1111", "Gran profesional con experiencia de 5 años en casos penales.",
                "carlos_perez.jpg"));
        mockUser(sqLiteDatabase, new User("Daniel Samper", "Abogado accidentes de tráfico",
                "300 200 2222", "Gran profesional con experiencia de 5 años en accidentes de tráfico.",
                "daniel_samper.jpg"));
        mockUser(sqLiteDatabase, new User("Lucia Aristizabal", "Abogado de derechos laborales",
                "300 200 3333", "Gran profesional con más de 3 años de experiencia en defensa de los trabajadores.",
                "lucia_aristizabal.jpg"));
        mockUser(sqLiteDatabase, new User("Marina Acosta", "Abogado de familia",
                "300 200 4444", "Gran profesional con experiencia de 5 años en casos de familia.",
                "marina_acosta.jpg"));
        mockUser(sqLiteDatabase, new User("Olga Ortiz", "Abogado de administración pública",
                "300 200 5555", "Gran profesional con experiencia de 5 años en casos en expedientes de urbanismo.",
                "olga_ortiz.jpg"));
        mockUser(sqLiteDatabase, new User("Pamela Briger", "Abogado fiscalista",
                "300 200 6666", "Gran profesional con experiencia de 5 años en casos de derecho financiero",
                "pamela_briger.jpg"));
        mockUser(sqLiteDatabase, new User("Rodrigo Benavidez", "Abogado Mercantilista",
                "300 200 1111", "Gran profesional con experiencia de 5 años en redacción de contratos mercantiles",
                "rodrigo_benavidez.jpg"));
        mockUser(sqLiteDatabase, new User("Tom Bonz", "Abogado penalista",
                "300 200 1111", "Gran profesional con experiencia de 5 años en casos penales.",
                "tom_bonz.jpg"));
    }

    public long mockUser(SQLiteDatabase sqLiteDatabase, User user){
        return sqLiteDatabase.insert(UserEntry.TABLE_NAME,null,user.toContentValues());
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public Cursor getAllUsers(){
            return getReadableDatabase().query(UserEntry.TABLE_NAME,null,null,null,null,null,null);
    }

    public Cursor getUserById(String userId){
        Cursor c = getReadableDatabase().query(
                UserEntry.TABLE_NAME,null,UserEntry.ID + " LIKE ?",new String[]{userId},null,null,null
        );
        return  c;
    }

    public long saveUser(User user){
        return getWritableDatabase().insert(UserEntry.TABLE_NAME,null,user.toContentValues());
    }

    public int deleteUser(String userId){
        return getWritableDatabase().delete(UserEntry.TABLE_NAME,UserEntry.ID + " LIKE ?",new String[]{userId});
    }

    public int updateUser(User user, String userId){
        return getWritableDatabase().update(UserEntry.TABLE_NAME,user.toContentValues(),UserEntry.ID + " LIKE ?",new String[]{userId});
    }
}

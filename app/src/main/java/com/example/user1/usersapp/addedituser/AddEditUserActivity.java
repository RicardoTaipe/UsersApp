package com.example.user1.usersapp.addedituser;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.user1.usersapp.R;
import com.example.user1.usersapp.data.User;
import com.example.user1.usersapp.data.UsersContract;
import com.example.user1.usersapp.data.UsersDbHelper;
import com.example.user1.usersapp.users.UsersActivity;

public class AddEditUserActivity extends AppCompatActivity {
    public static final int REQUEST_ADD_EDIT_USER =1;

    private String mUserId;
    private UsersDbHelper mUsersDbHelper;
    private TextInputEditText mNameField, mPhoneNumberField, mSpecialtyField,mBioField;
    private TextInputLayout mNameLabel,mPhoneNumberLabel,mSpecialtyLabel,mBioLabel;
    private FloatingActionButton mSaveButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_user);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Referencias
        mSaveButton = (FloatingActionButton) findViewById(R.id.fab);
        mNameField = (TextInputEditText)findViewById(R.id.et_name);
        mPhoneNumberField = (TextInputEditText)findViewById(R.id.et_phone_number);
        mSpecialtyField = (TextInputEditText)findViewById(R.id.et_specialty);
        mBioField = (TextInputEditText)findViewById(R.id.et_bio);
        mNameLabel = (TextInputLayout) findViewById(R.id.til_name);
        mPhoneNumberLabel = (TextInputLayout)findViewById(R.id.til_phone_number);
        mSpecialtyLabel = (TextInputLayout)findViewById(R.id.til_specialty);
        mBioLabel = (TextInputLayout)findViewById(R.id.til_bio);

        mUserId = getIntent().getStringExtra(UsersActivity.EXTRA_USER_ID);

        setTitle(mUserId == null ? "Añadir abogado" : "Editar abogado");

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEditUser();
            }
        });

        mUsersDbHelper = new UsersDbHelper(this);

        if(mUserId != null){
            loadUser(mUserId);
        }
    }

    //region Cargar usuario en forma asincrona

    private void loadUser(String mUserId) {
        new GetUserById().execute(mUserId);
    }

    private class GetUserById extends AsyncTask<String,Void,Cursor>{
        @Override
        protected Cursor doInBackground(String... strings) {
            return mUsersDbHelper.getUserById(strings[0]);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if(cursor != null && cursor.moveToLast()){
                User user = new User(cursor);
                mNameField.setText(user.getName());
                mPhoneNumberField.setText(user.getPhoneNumber());
                mSpecialtyField.setText(user.getSpecialty());
                mBioField.setText(user.getBio());
            }else{
                Toast.makeText(getApplicationContext(), "Error al editar abogado", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_CANCELED);
                finish();
            }
        }
    }

    //endregion

    private void addEditUser() {
        boolean error = false;

        String name = mNameField.getText().toString();
        String phoneNumber = mPhoneNumberField.getText().toString();
        String specialty = mSpecialtyField.getText().toString();
        String bio = mBioField.getText().toString();

        if(TextUtils.isEmpty(name)){
            mNameLabel.setError(getString(R.string.error));
            error = true;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            mPhoneNumberLabel.setError(getString(R.string.error));
            error = true;
        }
        if(TextUtils.isEmpty(specialty)){
            mSpecialtyLabel.setError(getString(R.string.error));
            error = true;
        }
        if(TextUtils.isEmpty(bio)){
            mBioLabel.setError(getString(R.string.error));
            error = true;
        }

        if(error){
            return;
        }

        User user = null;

        if(mUserId == null){
             user = new User(name,specialty,phoneNumber,bio,"");
        }else{
            Cursor cursor = (mUsersDbHelper.getUserById(mUserId));
            if(cursor!= null && cursor.moveToLast()){
                //Mostrar informacion del usuario en el scrollview
                user = (new User(cursor));
                String avatarUri = user.getAvatarUri();
                user = new User(name,specialty,phoneNumber,bio,avatarUri);
            }            
        }

        new AddEditUserTask().execute(user);

    }

    private class AddEditUserTask extends AsyncTask<User,Void,Boolean> {
        @Override
        protected Boolean doInBackground(User... users) {
            if(mUserId!=null){
                return mUsersDbHelper.updateUser(users[0],mUserId)>0;
            }else{
                return mUsersDbHelper.saveUser(users[0])> 0;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(!aBoolean){
                Toast.makeText(getApplicationContext(),"Error al agregar nueva información", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_CANCELED);
            }else {
                Toast.makeText(getApplicationContext(),"Actualización exitosa", Toast.LENGTH_SHORT).show();
                setResult(Activity.RESULT_OK);
            }
            finish();
        }
    }
}

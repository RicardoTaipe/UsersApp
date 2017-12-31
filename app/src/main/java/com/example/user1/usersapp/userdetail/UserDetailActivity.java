package com.example.user1.usersapp.userdetail;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.user1.usersapp.R;
import com.example.user1.usersapp.addedituser.AddEditUserActivity;
import com.example.user1.usersapp.data.User;
import com.example.user1.usersapp.data.UsersDbHelper;
import com.example.user1.usersapp.users.UsersActivity;

public class UserDetailActivity extends AppCompatActivity {
    public static final String ARG_USER_ID = "userId";

    private String userId;
    private CollapsingToolbarLayout mCollapsingView;
    private ImageView mAvatar;
    private TextView mPhoneNumber,mSpecialty,mBio;

    private UsersDbHelper mUsersDbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsingView = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mAvatar = (ImageView) findViewById(R.id.iv_avatar);
        mPhoneNumber =(TextView) findViewById(R.id.tv_phone);
        mSpecialty = (TextView) findViewById(R.id.tv_specialty);
        mBio = (TextView) findViewById(R.id.tv_bio);

        mUsersDbHelper = new UsersDbHelper(this);

        String userId = getIntent().getStringExtra(UsersActivity.EXTRA_USER_ID);
        loadUser(userId);
    }

    //region Carga un usuario en base al id en forma asincronica
    private void loadUser(String userId) {
        new GetUserByIdTask().execute(userId);
    }

    private class GetUserByIdTask extends AsyncTask<String, Void, Cursor> {

        @Override
        protected Cursor doInBackground(String... strings) {
            return  mUsersDbHelper.getUserById(strings[0]);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if(cursor!= null && cursor.moveToLast()){
                //Mostrar informacion del usuario en el scrollview
                showUser(new User(cursor));
            }else {
                Toast.makeText(getApplicationContext(),"Error al carga la informacion",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showUser(User user) {
        mCollapsingView.setTitle(user.getName());
        Glide.with(this)
                .load(Uri.parse("file:///android_asset/" + user.getAvatarUri()))
                .centerCrop()
                .into(mAvatar);
        mPhoneNumber.setText(user.getPhoneNumber());
        mSpecialty.setText(user.getSpecialty());
        mBio.setText(user.getBio());
    }
    //endregion

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        String userId = getIntent().getStringExtra(UsersActivity.EXTRA_USER_ID);

        switch (item.getItemId()){
            case R.id.action_edit:
                Intent intent = new Intent(this, AddEditUserActivity.class);
                intent.putExtra(UsersActivity.EXTRA_USER_ID,userId);
                startActivityForResult(intent,UsersActivity.REQUEST_UPDATE_DELETE_USER);
                break;
            case R.id.action_delete:
                new DeleteUserById().execute(userId);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private class DeleteUserById extends AsyncTask<String,Void,Integer>{

        @Override
        protected Integer doInBackground(String... strings) {
            return mUsersDbHelper.deleteUser(strings[0]);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Boolean query = integer> 0;
            if(!query){
                Toast.makeText(getApplicationContext(),"Error al eliminar abogado", Toast.LENGTH_SHORT).show();
            }
            setResult(query ? Activity.RESULT_OK : Activity.RESULT_CANCELED);
            finish();
        }
    }

    //Procesa si se elimino o edito un usuario
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == UsersActivity.REQUEST_UPDATE_DELETE_USER){
            if(Activity.RESULT_OK == resultCode){
                setResult(Activity.RESULT_OK);
                this.finish();
            }
        }
    }
}

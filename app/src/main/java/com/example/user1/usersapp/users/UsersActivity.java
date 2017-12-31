package com.example.user1.usersapp.users;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.user1.usersapp.R;
import com.example.user1.usersapp.addedituser.AddEditUserActivity;
import com.example.user1.usersapp.data.UsersContract;
import com.example.user1.usersapp.data.UsersContract.UserEntry;
import com.example.user1.usersapp.data.UsersDbHelper;
import com.example.user1.usersapp.userdetail.UserDetailActivity;

public class UsersActivity extends AppCompatActivity {

    public static final String EXTRA_USER_ID = "extra_user_id";
    public static final int REQUEST_UPDATE_DELETE_USER = 2;
    private UsersDbHelper mUsersDbHelper;

    private ListView mUserListView;
    private UserCursorAdapter mUserAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mUserListView = (ListView)findViewById(R.id.users_list);
        mUserAdapter = new UserCursorAdapter(this,null);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        mUserListView.setAdapter(mUserAdapter);

        mUserListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Cursor currentItem = (Cursor)mUserAdapter.getItem(i);
                String currentId = currentItem.getString(currentItem.getColumnIndex(UserEntry.ID));
                showDetailScreen(currentId);
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddScreen();
            }
        });

        getApplication().deleteDatabase(UsersDbHelper.DATABASE_NAME);

        mUsersDbHelper = new UsersDbHelper(this);

        loadUsers();
    }

    //Muestra la pantalla para anadir un nuevo Usuario
    private void showAddScreen() {
        Intent intent = new Intent(getApplicationContext(), AddEditUserActivity.class);
        startActivityForResult(intent,AddEditUserActivity.REQUEST_ADD_EDIT_USER);
    }

    //Actualiza la lista de usuario en caso de que se haya creado nuevos usuarios
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(Activity.RESULT_OK == resultCode){
            switch (requestCode){
                case AddEditUserActivity.REQUEST_ADD_EDIT_USER:
                    Toast.makeText(this,"Usuario ingresado",Toast.LENGTH_SHORT).show();
                    loadUsers();
                    break;
                case REQUEST_UPDATE_DELETE_USER:
                    loadUsers();
                    break;
            }
        }

    }

    //Abre la actividad con el detalle del usuario seleccionado
    private void showDetailScreen(String currentId) {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra(UsersActivity.EXTRA_USER_ID,currentId);
        startActivityForResult(intent,UsersActivity.REQUEST_UPDATE_DELETE_USER);
    }

    //region  Cargar datos de formar asincrona
    private void loadUsers() {
        new UsersLoadTask().execute();
    }

    private class UsersLoadTask extends AsyncTask<Void,Void,Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            return mUsersDbHelper.getAllUsers();
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            if(cursor!= null && cursor.getCount()>0){
                mUserAdapter.swapCursor(cursor);
            }else {

            }
        }
    }
    //endregion


}

package com.example.user1.usersapp.users;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.user1.usersapp.R;
import com.example.user1.usersapp.data.UsersContract;

/**
 * Created by User 1 on 28/12/2017.
 */

public class UserCursorAdapter extends CursorAdapter {

    public UserCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(R.layout.list_item_user,viewGroup,false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        TextView nameText = (TextView)view.findViewById(R.id.tv_name);
        final ImageView  avatarImage= (ImageView)view.findViewById(R.id.iv_avatar);

        //Obtener valores de la DB
        String name = cursor.getString(cursor.getColumnIndex(UsersContract.UserEntry.NAME));
        String avatarUri = cursor.getString(cursor.getColumnIndex(UsersContract.UserEntry.AVATAR_URI));

        //Setear valores en los views
        nameText.setText(name);

        Glide.with(context)
                .load(Uri.parse("file:///android_asset/" + avatarUri))
                .asBitmap()
                .error(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(new BitmapImageViewTarget(avatarImage){
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(context.getResources(),resource);
                        drawable.setCircular(true);
                        avatarImage.setImageDrawable(drawable);
                    }
                });
    }
}

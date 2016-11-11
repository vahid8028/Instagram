package com.parse.starter;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserFeed extends AppCompatActivity {

    LinearLayout linearLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_feed);

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        Intent i = getIntent();
        String activeUserName = i.getStringExtra("username");
        Log.i("AppInfo", activeUserName);
        setTitle(activeUserName + " 's Feed");

        // get information about the user's feed

        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("images");
        query.whereEqualTo("username", activeUserName);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        System.out.println("there is " + String.valueOf(objects.size() + " images."));
                        int count = 0;
                        for (ParseObject object: objects) {
                            count += 1;
                            ParseFile file = (ParseFile) object.get("image");
                            final int finalCount = count;
                            file.getDataInBackground(new GetDataCallback() {
                                @Override
                                public void done(byte[] data, ParseException e) {
                                    if (e == null) {

                                        //System.out.println("no exception here");
                                        Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
                                        ImageView imageView = new ImageView(getApplicationContext());
                                        imageView.setImageBitmap(image);

                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(
                                                ViewGroup.LayoutParams.MATCH_PARENT,
                                                ViewGroup.LayoutParams.WRAP_CONTENT


                                        ));

                                        linearLayout.addView(imageView);
                                        TextView tv = new TextView(getApplicationContext());
                                        tv.setText("this is added");
                                        linearLayout.addView(tv);
                                        System.out.println("this is " + String.valueOf(finalCount) + "times adding image view");



                                    }
                                }
                            });




                        }
                    } else {

                        Toast.makeText(getApplicationContext(), "There is no image about this user", Toast.LENGTH_LONG).show();


                    }
                }
            }
        });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.back) {
            Intent i = new Intent(getApplicationContext(), UserList.class);
            startActivity(i);

        }

        return super.onOptionsItemSelected(item);
    }

}


package com.anioncode.workpower;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.anioncode.workpower.Model.ModelEvent;
import com.anioncode.workpower.UI.RecyclerAdapter;
import com.anioncode.workpower.database.DatabaseHelper;
import com.anioncode.workpower.fragment.Push_Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences sharedPref;


    RecyclerView recyclerView;
    DatabaseHelper mDatabaseHelper;
    ArrayList<ModelEvent> events;
    RecyclerAdapter recyclerViewx;
    FloatingActionButton actionButton;
    RelativeLayout relativeLayout;
    FrameLayout frameLayout;

    TextView surname,places;
    Boolean isOpen=false;

    CircleImageView circleImageView;
    private static final int  PICK_IMAGE_REQUEST= 415;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        surname=findViewById(R.id.Surname);
        places=findViewById(R.id.Places);
        circleImageView=findViewById(R.id.profile_image);

        Bundle extras = getIntent().getExtras();
        sharedPref = getSharedPreferences("0", Activity.MODE_PRIVATE);
        if (extras != null) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("Nazwa", extras.getString("Nazwa"));
            editor.putString("Stanowisko", extras.getString("Stanowisko"));

            editor.commit();

            // and get whatever type user account id is
        }

        circleImageView.setOnClickListener(this);

        SharedPreferences.Editor preferencesEditor = sharedPref.edit();
        String textFromPreferencesS = sharedPref.getString("Nazwa", "");
        String textFromPreferencesP = sharedPref.getString("Stanowisko", "");
      //  String textFromPreferencesP = preferences.getString("1", "");

        if(!textFromPreferencesS.equals("")){
            surname.setText(textFromPreferencesS);
            places.setText(textFromPreferencesP);

        }else{
            surname.setText("Witaj");
            places.setText("Wykonaj swój dzienny plan , powodzenia !");
        }
        String imageS = sharedPref.getString("imagePreferance", "");
        Bitmap imageB ;
        if(!imageS.equals("")) {
            imageB = decodeToBase64(imageS);
            circleImageView.setImageBitmap(imageB);
        }
    ///FIND
        recyclerView=findViewById(R.id.RecyclerView);
        actionButton=findViewById(R.id.fab);
        relativeLayout=findViewById(R.id.RelativeLayout);
        frameLayout=findViewById(R.id.Frame);


        frameLayout.setTranslationY(-getResources().getDimension(R.dimen.stan_300));
        frameLayout.bringToFront();



        getSupportFragmentManager().beginTransaction().replace(R.id.Frame,
                new Push_Fragment()).commit();

        //Convert DPD To PX
        final float scale =  getResources().getDisplayMetrics().density;
        int px = (int) (300 * scale + 0.5f);

        frameLayout.getLayoutParams().height = px;

    ///DATABASE
        mDatabaseHelper = new DatabaseHelper(this);
    ///RECYCLER
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        events= new ArrayList<>();
        events = mDatabaseHelper.getAllData();
        recyclerViewx = new RecyclerAdapter(this, events);

        recyclerView.setAdapter(recyclerViewx);
    ///BUTTON CLICK
        actionButton.setOnClickListener(this);
        relativeLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.fab: /** Start a new Activity MyCards.java */
            {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(view.getContext());

                LayoutInflater inflater1 = (LayoutInflater)  getSystemService(LAYOUT_INFLATER_SERVICE);
                final View view1 = inflater1.inflate(R.layout.todo_picker, null);

                final EditText txt1 = view1.findViewById(R.id.text);
                final Switch imortant = view1.findViewById(R.id.important);


                builder1.setView(view1);
                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(!txt1.getText().toString().trim().equals("")){
                                    String important;
                                    if(imortant.isChecked()){
                                        important="1";
                                    }else {
                                        important="0";
                                    }

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                                    String currentDateandTime = sdf.format(new Date());

                                    AddData(txt1.getText().toString(), currentDateandTime, important);
                                    events = mDatabaseHelper.getAllData();

                                    recyclerViewx = new RecyclerAdapter(view1.getContext(), events);
                                    recyclerView.setAdapter(recyclerViewx);

                                }
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

                break;
            }
            case R.id.RelativeLayout: /** Start a new Activity MyCards.java */
            {
                if(!isOpen){
                    relativeLayout.animate().translationY(getResources().getDimension(R.dimen.stan_300));
                    frameLayout.animate().translationY(0);
                    relativeLayout.bringToFront();
                    isOpen=true;
                }else{
                    relativeLayout.animate().translationY(0);
                    frameLayout.animate().translationY(-getResources().getDimension(R.dimen.stan_300));
                    isOpen=false;
                }


                break;
            }
            case R.id.profile_image:{
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

                break;
            }


        }
    }
    public void AddData(String Name, String Date, String Pr) {
        boolean insertData = false;

        insertData = mDatabaseHelper.addData(Name, Date, Pr);

        if (insertData) {
            //Snackbar.make(getWindow().getDecorView(), "Dodane :D", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(getWindow().getDecorView(), "Coś poszło nie tak :'( ", Snackbar.LENGTH_SHORT).show();
        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                // Log.d(TAG, String.valueOf(bitmap));
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("imagePreferance", encodeToBase64(bitmap));
                editor.commit();
                circleImageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static String encodeToBase64(Bitmap image) {
        Bitmap immage = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }
    public static Bitmap decodeToBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }
}

package com.anioncode.workpower.fragment;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.anioncode.workpower.MainActivity;
import com.anioncode.workpower.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;


public class Push_Fragment extends Fragment {

    private AdView mAdView;
    private Button save;

    CheckBox checkBox;
    EditText ed1, ed2;

    private static final int GALLERY_REQUEST = 763;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        MobileAds.initialize(getActivity(), "ca-app-pub-3788232558823244~2641832795");

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        // Toast.makeText(getApplicationContext(),sharedPref.getString("dane", ""),Toast.LENGTH_SHORT).show();
        String txt = sharedPref.getString("dane", "");


        checkBox = view.findViewById(R.id.check);

        if (txt.equals("Yes")) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                @Override
                                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                    if (isChecked) {

                                                        SharedPreferences.Editor editor = sharedPref.edit();
                                                        editor.putString("dane", "Yes");
                                                        editor.commit();
                                                        //     Toast.makeText(getApplicationContext(),sharedPref.getString("dane", ""),Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        SharedPreferences.Editor editor = sharedPref.edit();
                                                        editor.putString("dane", "NO");
                                                        editor.commit();
                                                        //     Toast.makeText(getApplicationContext(),sharedPref.getString("dane", ""),Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
        );
        ed1 = view.findViewById(R.id.editText);
        ed2 = view.findViewById(R.id.editText2);


        save = view.findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = getActivity().getIntent();
                intent.putExtra("Nazwa", ed2.getText().toString());
                intent.putExtra("Stanowisko", ed1.getText().toString());

                getActivity().finish();
                startActivity(intent);
            }
        });


        return view;
    }


    public void sendNotification() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Spojrzyj";
            String description = "Spojrzyj";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Spojrzyj", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getActivity(), "Spojrzyj")
                .setSmallIcon(R.drawable.ic_done)
                .setContentTitle("Pamiętaj!")
                .setContentText(" Otwórz aplikacje i wróć do nas ")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0,
                new Intent(getActivity(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());


// notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, mBuilder.build());
    }

    @Override
    public void onPause() {
        final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String txt = sharedPref.getString("dane", "");
        if (txt.equals("Yes")) {

            sendNotification();
        }
        super.onPause();
    }

}

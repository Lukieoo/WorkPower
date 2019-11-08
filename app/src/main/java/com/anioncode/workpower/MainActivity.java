package com.anioncode.workpower;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.anioncode.workpower.Model.ModelEvent;
import com.anioncode.workpower.UI.RecyclerAdapter;
import com.anioncode.workpower.database.DatabaseHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    DatabaseHelper mDatabaseHelper;
    ArrayList<ModelEvent> events;
    RecyclerAdapter recyclerViewx;
    FloatingActionButton actionButton;
    RelativeLayout relativeLayout;
    RelativeLayout relativeLayout2;

    ImageButton graph;

    Boolean isOpen=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    ///FIND
        recyclerView=findViewById(R.id.RecyclerView);
        actionButton=findViewById(R.id.fab);
        relativeLayout=findViewById(R.id.RelativeLayout);
        relativeLayout2=findViewById(R.id.RL);
        graph=findViewById(R.id.Graph);

        relativeLayout2.setTranslationY(-getResources().getDimension(R.dimen.stan_300));
        relativeLayout2.bringToFront();
        final float scale =  getResources().getDisplayMetrics().density;
        int px = (int) (300 * scale + 0.5f);

        relativeLayout2.getLayoutParams().height = px;

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
        graph.setOnClickListener(this);
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
            case R.id.Graph: /** Start a new Activity MyCards.java */
            {
                if(!isOpen){
                    relativeLayout.animate().translationY(getResources().getDimension(R.dimen.stan_300));
                    relativeLayout2.animate().translationY(0);
                    relativeLayout.bringToFront();
                    isOpen=true;
                }else{
                    relativeLayout.animate().translationY(0);
                    relativeLayout2.animate().translationY(-getResources().getDimension(R.dimen.stan_300));
                    isOpen=false;
                }


                break;
            }
            case R.id.Contacts: /** Start a new Activity MyCards.java */
            {

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

}

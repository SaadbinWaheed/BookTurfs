package com.potato.saad.bookturfs;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.brouding.blockbutton.BlockButton;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainMenu extends AppCompatActivity {

    ArrayList tempTimings = new ArrayList<>();
    ArrayList tempVenues = new ArrayList<>();
    ArrayList tempStatus = new ArrayList<>();
    ArrayList tempKeys = new ArrayList<>();

    ArrayList Timings = new ArrayList<>();
    ArrayList Venues = new ArrayList<>();
    ArrayList Status = new ArrayList<>();
    ArrayList Keys = new ArrayList<>();

    ArrayList MilliSeconds = new ArrayList<>();

    RVAdapter adapter;
    BlockButton btn_Book;
    List<String> VenueFilters =  new ArrayList<String>();
    DatabaseReference BookingDetailsRef;
    TextView txtMonth;
    static TextView txtNoData;
    Integer backpress=0;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    final Intent i = new Intent(MainMenu.this, DefaultIntro.class);

                    runOnUiThread(new Runnable() {
                        @Override public void run() {
                            startActivity(i);
                        }
                    });

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();


        BookingDetailsRef = FirebaseDatabase.getInstance().getReference();
        BookingDetailsRef=BookingDetailsRef.child("BookingDetails");

        final CompactCalendarView compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);

        txtMonth= findViewById(R.id.txtMonth);
        btn_Book=findViewById(R.id.btnContact);
        txtNoData= findViewById(R.id.txtNoData);

        btn_Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(MainMenu.this,Book.class);
                startActivity(intent);
            }
        });

        String month=compactCalendarView.getFirstDayOfCurrentMonth().toString().split(" ")[1];
        String year=compactCalendarView.getFirstDayOfCurrentMonth().toString().split(" ")[5];
        txtMonth.setText(month + " " + year);


        final RecyclerView rv = (RecyclerView)findViewById(R.id.rv);
        //rv.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(llm);
        adapter = new RVAdapter(Timings,Venues,Status,Keys);
        rv.setAdapter(adapter);

        BookingDetailsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                tempVenues.add( dataSnapshot.child("Venue").getValue());
                tempTimings.add( dataSnapshot.child("Starting Time").getValue() + " - " +
                        dataSnapshot.child("Ending Time").getValue());
                tempStatus.add( dataSnapshot.child("Status").getValue());
                tempKeys.add( dataSnapshot.getKey());

                MilliSeconds.add( dataSnapshot.child("MilliSeconds").getValue());

                Event ev1 = new Event(Color.BLACK,Long.parseLong( dataSnapshot.child("MilliSeconds").getValue().toString()));
                compactCalendarView.addEvent(ev1);
                compactCalendarView.showCalendar();

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {

                Venues.clear();
                Timings.clear();
                Status.clear();
                Keys.clear();
                adapter.notifyDataSetChanged();


                List<Event> events = compactCalendarView.getEvents(dateClicked);

                for (int i=0;i<events.size();i++)
                {

                    Log.e("Events: ","Database: " + MilliSeconds.get(i) + " , Event " + Long.toString( events.get(i).getTimeInMillis()));
                        int position= MilliSeconds.indexOf(Long.toString( events.get(i).getTimeInMillis()));
                        Log.e("Events: ",Integer.toString( position));

                        Venues.add(tempVenues.get(position));
                        Timings.add(tempTimings.get(position));
                        Status.add(tempStatus.get(position));
                        Keys.add(tempKeys.get(position));

                    Log.e("Filtered Timings: ",tempTimings.get(position).toString());

                    adapter.notifyDataSetChanged();

                }

                rv.setAdapter(adapter);

                for(int c=0;c<Timings.size();c++)
                    Log.e("TempTimings: ",Timings.get(c).toString());




            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                String month=firstDayOfNewMonth.toString().split(" ")[1];
                String year=firstDayOfNewMonth.toString().split(" ")[5];
                txtMonth.setText(month + " " + year);            }
        });
    }

    public static void showEmptyView(boolean empty)
    {
        if(empty)
            txtNoData.setVisibility(View.VISIBLE);
        else
            txtNoData.setVisibility(View.GONE);

    }

    public void onBackPressed(){
        backpress = (backpress + 1);
        Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();

        if (backpress>1) {
            this.finishAffinity();
        }
    }

}



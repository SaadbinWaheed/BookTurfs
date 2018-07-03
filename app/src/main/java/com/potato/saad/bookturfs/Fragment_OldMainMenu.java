package com.potato.saad.bookturfs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Fragment_OldMainMenu.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Fragment_OldMainMenu#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_OldMainMenu extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_old__main_menu, container, false);
    }
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            //  Declare a new thread to do a preference check
            super.onActivityCreated(savedInstanceState);

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    //  Initialize SharedPreferences
                    SharedPreferences getPrefs = PreferenceManager
                            .getDefaultSharedPreferences(getActivity().getBaseContext());

                    //  Create a new boolean and preference and set it to true
                    boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                    //  If the activity has never started before...
                    if (isFirstStart) {

                        //  Launch app intro
                        final Intent i = new Intent(getActivity(), DefaultIntro.class);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
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
            BookingDetailsRef = BookingDetailsRef.child("BookingDetails");

            final CompactCalendarView compactCalendarView = (CompactCalendarView) getView().findViewById(R.id.compactcalendar_view);

            txtMonth = getView().findViewById(R.id.txtMonth);
            btn_Book = getView().findViewById(R.id.btnContact);
            txtNoData = getView().findViewById(R.id.txtNoData);

            btn_Book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //  Intent intent= new Intent(com.potato.saad.bookturfs.Fragment_OldMainMenu.this,Book.class);
                    ///startActivity(intent);
                }
            });

            String month = compactCalendarView.getFirstDayOfCurrentMonth().toString().split(" ")[1];
            String year = compactCalendarView.getFirstDayOfCurrentMonth().toString().split(" ")[5];
            txtMonth.setText(month + " " + year);


            final RecyclerView rv = (RecyclerView) getView().findViewById(R.id.rv);
            //rv.setHasFixedSize(true);

            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            rv.setLayoutManager(llm);
            adapter = new RVAdapter(Timings, Venues, Status, Keys);
            rv.setAdapter(adapter);

            BookingDetailsRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    tempVenues.add(dataSnapshot.child("Venue").getValue());
                    tempTimings.add(dataSnapshot.child("Starting Time").getValue() + " - " +
                            dataSnapshot.child("Ending Time").getValue());
                    tempStatus.add(dataSnapshot.child("Status").getValue());
                    tempKeys.add(dataSnapshot.getKey());

                    MilliSeconds.add(dataSnapshot.child("MilliSeconds").getValue());

                    Event ev1 = new Event(Color.BLACK, Long.parseLong(dataSnapshot.child("MilliSeconds").getValue().toString()));
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

                    for (int i = 0; i < events.size(); i++) {

                        Log.e("Events: ", "Database: " + MilliSeconds.get(i) + " , Event " + Long.toString(events.get(i).getTimeInMillis()));
                        int position = MilliSeconds.indexOf(Long.toString(events.get(i).getTimeInMillis()));
                        Log.e("Events: ", Integer.toString(position));

                        Venues.add(tempVenues.get(position));
                        Timings.add(tempTimings.get(position));
                        Status.add(tempStatus.get(position));
                        Keys.add(tempKeys.get(position));

                        Log.e("Filtered Timings: ", tempTimings.get(position).toString());

                        adapter.notifyDataSetChanged();

                    }

                    rv.setAdapter(adapter);

                    for (int c = 0; c < Timings.size(); c++)
                        Log.e("TempTimings: ", Timings.get(c).toString());


                }

                @Override
                public void onMonthScroll(Date firstDayOfNewMonth) {
                    String month = firstDayOfNewMonth.toString().split(" ")[1];
                    String year = firstDayOfNewMonth.toString().split(" ")[5];
                    txtMonth.setText(month + " " + year);
                }
            });
        }

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Fragment_OldMainMenu() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_OldMainMenu.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_OldMainMenu newInstance(String param1, String param2) {
        Fragment_OldMainMenu fragment = new Fragment_OldMainMenu();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


        @SuppressLint("ResourceAsColor")


        public static void showEmptyView(boolean empty)
        {
            if(empty)
                txtNoData.setVisibility(View.VISIBLE);
            else
                txtNoData.setVisibility(View.GONE);

        }







}

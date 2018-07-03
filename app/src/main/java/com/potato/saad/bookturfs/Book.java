package com.potato.saad.bookturfs;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.brouding.blockbutton.BlockButton;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class Book extends AppCompatActivity {

    DatabaseReference BookingDetailsRef;
    EditText txtName,txtTeamName,txtNumber;
    Spinner txtVenue;
    TextView txtStartingTime,txtEndingTime,txtDate;
    BlockButton btnBook,btnRequestOpponent;
    ArrayList VenueFilters=new ArrayList();
    Calendar myCalendar;
    String strVenue,strName,strTeamName,strContactNumber,strStartingTime,strEndingTime,strDate;
    String ActualStartingTime;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        BookingDetailsRef = FirebaseDatabase.getInstance().getReference();
        BookingDetailsRef=BookingDetailsRef.child("BookingDetails");

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        txtName=findViewById(R.id.edName);
        txtTeamName=findViewById(R.id.edTeamName);
        txtNumber=findViewById(R.id.edNumber);
        txtStartingTime=findViewById(R.id.txtStartingTime);
        txtEndingTime=findViewById(R.id.txtEndingTime);
        txtDate=findViewById(R.id.txtDate);
        btnBook=findViewById(R.id.btnContact);
        btnRequestOpponent=findViewById(R.id.btn_RequestOpponent);

        VenueFilters.add("F8 Multipurpose");
        VenueFilters.add("High Velocity");
        VenueFilters.add("H-8 Roots");
        VenueFilters.add("ICAS Ground");
        VenueFilters.add("Kick Off");
        VenueFilters.add("The Stadium E-11");
        VenueFilters.add("Total");
        VenueFilters.add("Total Chak Shezad");
        VenueFilters.add("Midfield");

        ArrayAdapter<String> FilterAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, VenueFilters);

        FilterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner sItems = (Spinner) findViewById(R.id.spinner_venue);
        sItems.setAdapter(FilterAdapter);

        sItems.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                strVenue = parent.getItemAtPosition(position).toString();
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        txtStartingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Book.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String AMPM = "AM";
                        String strMinute;
                        strMinute=Integer.toString( selectedMinute);

                        ActualStartingTime=Integer.toString(selectedHour) +":"+ Integer.toString(selectedMinute);
                        if(selectedHour > 12)
                        {
                            selectedHour=selectedHour-12;
                            AMPM="PM";
                        }
                        else if(selectedHour==12)
                        {
                            AMPM="PM";
                        }

                        if(selectedMinute<10)
                        {
                            strMinute="0"+selectedMinute;
                        }

                        txtStartingTime.setText( selectedHour + ":" + strMinute + " " + AMPM);
                    }
                }, 12, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        txtEndingTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Book.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String AMPM = "AM";
                        String strMinute;
                        strMinute=Integer.toString( selectedMinute);

                        if(selectedHour > 12)
                        {
                            selectedHour=selectedHour-12;
                            AMPM="PM";
                        }
                        else if(selectedHour==12)
                        {
                            AMPM="PM";
                        }
                        else if(selectedHour==0)
                        {
                            selectedHour=12;
                            AMPM="AM";
                        }

                        if(selectedMinute<10)
                        {
                            strMinute="0"+selectedMinute;
                        }

                        txtEndingTime.setText( selectedHour + ":" + strMinute + " " + AMPM);
                    }
                }, 12, 00, false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });
        myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Book.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(Book.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(Book.this);
                }
                builder.setTitle("Are you sure?")
                        .setMessage("This app currently has no connection with the Turf's Management. Place a booking only" +
                                " if you have taken one.")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new LongOperation().execute("Booked");
                                progressBar.setVisibility(View.VISIBLE);
                                progressBar.setProgress(0);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        btnRequestOpponent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new LongOperation().execute("Requesting Opponent");
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(0);
            }
        });


    }

    private void updateLabel() {
        String myFormat = "dd-MM-yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        txtDate.setText(sdf.format(myCalendar.getTime()));
    }

    private Boolean uploadData(String strStatus)
    {

        strName=txtName.getText().toString();
        strTeamName=txtTeamName.getText().toString();
        strContactNumber= txtNumber.getText().toString();
        strStartingTime=txtStartingTime.getText().toString();
        strEndingTime=txtEndingTime.getText().toString();
        strDate=txtDate.getText().toString();

        if (TextUtils.isEmpty(strTeamName))
            strTeamName="Unspecified";

        if(TextUtils.isEmpty(strName) || TextUtils.isEmpty(strContactNumber) || TextUtils.isEmpty(strStartingTime) ||
                TextUtils.isEmpty(strEndingTime) || TextUtils.isEmpty(strDate) )
        {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(),"Kindly Fill all Fields",Toast.LENGTH_SHORT).show();
                }
            });

            return false;

        }
        else
        {
            HashMap<String, String> Map = new HashMap<>();
            Map.put("Venue", strVenue);
            Map.put("Name", strName);
            Map.put("Team Name", strTeamName);
            Map.put("Contact Number", strContactNumber);
            Map.put("Starting Time", strStartingTime);
            Map.put("Ending Time", strEndingTime);
            Map.put("Date", strDate);
            Map.put("Status", strStatus);


            String toParse = strDate + " " + ActualStartingTime.split(" ")[0];
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy hh:mm"); // I assume d-M, you may refer to M-d for month-day instead.
            Date date = null; // You will need try/catch around this
            try {
                date = formatter.parse(toParse);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long millis = date.getTime();
            Map.put("MilliSeconds", Long.toString(millis));


            //Writing to Firebase
            BookingDetailsRef.child(toParse+":"+strTeamName).setValue(Map);

            runOnUiThread(new Runnable() {
                public void run() {
                    clearTextFields();
                }
            });

            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getApplicationContext(),"Uploaded Succesfully",Toast.LENGTH_SHORT).show();
                }
            });

            return true;
        }

    }

    public void clearTextFields()
    {
        txtName.setText("");
        txtNumber.setText("");
        txtStartingTime.setText("");
        txtEndingTime.setText("");
        txtDate.setText("");
    }

    private class LongOperation extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            if(uploadData(params[0]))
                return "Executed";
            else
                return "Failed";


        }

        @Override
        protected void onPostExecute(String result) {
            progressBar.setVisibility(View.GONE);
            if(result.equals("Executed"))
                finish();


            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
        }
    }
}





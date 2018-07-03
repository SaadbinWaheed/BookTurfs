package com.potato.saad.bookturfs;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.brouding.blockbutton.BlockButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BookingDetails extends AppCompatActivity {

    DatabaseReference BookingDetailsRef;
    TextView txtVenue,txtName,txtTeamName,txtContact,txtTiming,txtDate,txtStatus;
    BlockButton btnContact;
    String Venue,Contact,Name,TeamName,Date,Status,Timing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);

        btnContact=findViewById(R.id.btnContact);

        BookingDetailsRef = FirebaseDatabase.getInstance().getReference();
        BookingDetailsRef=BookingDetailsRef.child("BookingDetails");

        txtVenue=findViewById(R.id.txtVenue);
        txtName=findViewById(R.id.txtName);
        txtTeamName=findViewById(R.id.txtTeamName);
        txtContact=findViewById(R.id.txtContact);
        txtTiming=findViewById(R.id.txtTimings);
        txtDate=findViewById(R.id.txtDate);
        txtStatus=findViewById(R.id.txtStatus);

        Bundle b=getIntent().getExtras();
        String key = b.getString("Key");

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(BookingDetails.this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(BookingDetails.this);
                }
                builder.setTitle("")
                        .setMessage("Contact " + Name)
                        .setPositiveButton("Call", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:"+Contact));
                                startActivity(intent);                             }
                        })
                        .setNegativeButton("Message", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                composeSmsMessage("",Contact);
                            }
                        })
                        .setIcon(android.R.drawable.ic_menu_call)
                        .show();
            }
        });
        BookingDetailsRef.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Venue=dataSnapshot.child("Venue").getValue().toString();
                Name=dataSnapshot.child("Name").getValue().toString();
                TeamName=dataSnapshot.child("Team Name").getValue().toString();
                Contact=dataSnapshot.child("Contact Number").getValue().toString();
                Timing=dataSnapshot.child("Starting Time").getValue().toString()
                        + "-" + dataSnapshot.child("Ending Time").getValue().toString() ;
                Date=dataSnapshot.child("Date").getValue().toString();
                Status=dataSnapshot.child("Status").getValue().toString();


                txtVenue.setText(Venue);
                txtName.setText(Name);
                txtTeamName.setText(TeamName);
                txtContact.setText(Contact);
                txtTiming.setText(Timing);
                txtDate.setText(Date);
                txtStatus.setText(Status);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void composeSmsMessage(String message, String phoneNumber) {
        try {

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", phoneNumber, null)));

        } catch (android.content.ActivityNotFoundException anfe) {
            Log.d("Error" , "Error");
        }
    }
}

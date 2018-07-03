package com.potato.saad.bookturfs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Saad on 17/02/2018.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CardViewHolder>{


    private List<String> timings;
    private List<String> venues;
    private List<String> status;
    private static List<String> keys;

    RVAdapter(List<String> timings, List<String> venues, List<String> status, ArrayList keys){
        this.timings = timings;
        this.venues = venues;
        this.status=status;
        this.keys=keys;
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_bookings, parent, false);
        CardViewHolder pvh = new CardViewHolder(v);

        return pvh;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        CardViewHolder.txt_venue_name.setText(venues.get(position));
        CardViewHolder.txt_timing.setText(timings.get(position));
        CardViewHolder.txt_status.setText(status.get(position));

        if(venues.get(position).equals("Total Rwp") || venues.get(position).equals("Total")) {
            CardViewHolder.img_venue_image.setImageResource(R.drawable.total_rwp);
        }
        else if (venues.get(position).equals("Midfield"))
        {
            CardViewHolder.img_venue_image.setImageResource(R.drawable.midfield);

        }
        else if (venues.get(position).equals("Kick Off"))
        {
            CardViewHolder.img_venue_image.setImageResource(R.drawable.kickoff);

        }
        else if (venues.get(position).equals("High Velocity"))
        {
            CardViewHolder.img_venue_image.setImageResource(R.drawable.high_velocity);

        }
        else if (venues.get(position).equals("The Stadium E-11"))
        {
            CardViewHolder.img_venue_image.setImageResource(R.drawable.the_stadium);

        }
        else if (venues.get(position).equals("ICAS Ground"))
        {
            CardViewHolder.img_venue_image.setImageResource(R.drawable.icas);

        }
        else if (venues.get(position).equals("H-8 Roots"))
        {
            CardViewHolder.img_venue_image.setImageResource(R.drawable.roots);

        }
        else
        {
            CardViewHolder.img_venue_image.setImageResource(R.drawable.total_rwp);

        }

    }

    @Override
    public int getItemCount() {
        if (timings.size()==0)
            Fragment_OldMainMenu.showEmptyView(true);
        else
            Fragment_OldMainMenu.showEmptyView(false);


        return timings.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        static TextView txt_venue_name;
        static TextView txt_timing;
        static TextView txt_status;
        static ImageView img_venue_image;

        CardViewHolder(final View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            txt_timing = (TextView)itemView.findViewById(R.id.timing);
            txt_venue_name = (TextView)itemView.findViewById(R.id.venue_name);
            txt_status = (TextView)itemView.findViewById(R.id.status);
            img_venue_image = (ImageView)itemView.findViewById(R.id.venue_image);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    int position=getAdapterPosition();
                    Intent intent=new Intent(img_venue_image.getContext(),BookingDetails.class);
                    Bundle b=new Bundle();
                    b.putString("Key",keys.get(position));
                    intent.putExtras(b);
                    img_venue_image.getContext().startActivity(intent);
                }
            });
        }


    }




}
package com.potato.saad.bookturfs;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class BottomNavigationMenu extends Activity{


    SpaceNavigationView spaceNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_menu);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final Fragment_OldMainMenu f1 = new Fragment_OldMainMenu();
        final Fragment_CaptainList f2 = new Fragment_CaptainList();

        fragmentTransaction.add(R.id.fragmentContainer, f1);
        fragmentTransaction.commit();


        spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        //spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.setCentreButtonIcon(R.drawable.enjoy_icon);
        spaceNavigationView.addSpaceItem(new SpaceItem("Home", R.drawable.icon_home));
        spaceNavigationView.addSpaceItem(new SpaceItem("Captains", R.drawable.app_icon));


        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Toast.makeText(BottomNavigationMenu.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Toast.makeText(BottomNavigationMenu.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                if(itemName.equals("Home")) {
                    final Fragment_OldMainMenu f1 = new Fragment_OldMainMenu();
                    fragmentTransaction.replace(R.id.fragmentContainer, f1);
                }
                else if (itemName.equals("Captains"))
                {

                    fragmentTransaction.replace(R.id.fragmentContainer,f2);

                }
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                //Toast.makeText(BottomNavigationMenu.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        spaceNavigationView.onSaveInstanceState(outState);
    }
}

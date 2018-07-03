package com.potato.saad.bookturfs;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

//public class DefaultIntro extends FancyWalkthroughActivity {
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        FancyWalkthroughCard fancywalkthroughCard1 = new FancyWalkthroughCard("Find Restaurant", "Find the best restaurant in your neighborhood.",R.drawable.icon_home);
//        FancyWalkthroughCard fancywalkthroughCard2 = new FancyWalkthroughCard("Pick the best", "Pick the right place using trusted ratings and reviews.",R.drawable.icon2);
//        FancyWalkthroughCard fancywalkthroughCard4 = new FancyWalkthroughCard("Meal is on the way", "Get ready and comfortable while our biker bring your meal at your door.",R.drawable.app_icon);
//
//        fancywalkthroughCard1.setBackgroundColor(R.color.white);
//        fancywalkthroughCard1.setIconLayoutParams(300,300,0,0,0,0);
//        fancywalkthroughCard2.setBackgroundColor(R.color.white);
//        fancywalkthroughCard2.setIconLayoutParams(300,300,0,0,0,0);
//        fancywalkthroughCard4.setBackgroundColor(R.color.white);
//        fancywalkthroughCard4.setIconLayoutParams(300,300,0,0,0,0);
//        List<FancyWalkthroughCard> pages = new ArrayList<>();
//
//        pages.add(fancywalkthroughCard1);
//        pages.add(fancywalkthroughCard2);
//        pages.add(fancywalkthroughCard4);
//
//        for (FancyWalkthroughCard page : pages) {
//            page.setTitleColor(R.color.black);
//            page.setDescriptionColor(R.color.black);
//        }
//        setFinishButtonTitle("Get Started");
//        showNavigationControls(true);
//        setColorBackground(R.color.colorAccent);
//        //setImageBackground(R.drawable.restaurant);
//        setInactiveIndicatorColor(R.color.grey_600);
//        setActiveIndicatorColor(R.color.black);
//        setOnboardPages(pages);
//
//    }
//
//    @Override
//    public void onFinishButtonPressed() {
//        Toast.makeText(this, "Finish Pressed", Toast.LENGTH_SHORT).show();
//
//    }
//}

public class DefaultIntro extends AppIntro {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);

        // Note here that we DO NOT use setContentView();

        // Add your slide fragments here.
        // AppIntro will automatically generate the dots indicator and buttons.
//        addSlide(firstFragment);
//        addSlide(secondFragment);
//        addSlide(thirdFragment);
//        addSlide(fourthFragment);

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest.
        addSlide(AppIntroFragment.newInstance("Book", "Broadcast your booking details to Everyone.",
                R.drawable.broadcast_icon, Color.parseColor("#3c3c40")));
        addSlide(AppIntroFragment.newInstance("Find", "Find an opponent before Booking.",
                R.drawable.find_icon, Color.parseColor("#3c3c40")));
        addSlide(AppIntroFragment.newInstance("Enjoy", "No more Khuwari!",
                R.drawable.enjoy_icon, Color.parseColor("#3c3c40")));

        // OPTIONAL METHODS
        // Override bar/separator color.
        setBarColor(Color.parseColor("#3c3c40"));
        setSeparatorColor(Color.parseColor("#3c3c40"));
        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);


        // Turn vibration on and set intensity.
        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(false);
        setVibrateIntensity(30);
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        Intent intent= new Intent(DefaultIntro.this, MainMenu.class);
        startActivity(intent);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        // Do something when users tap on Done button.
        Intent intent= new Intent(DefaultIntro.this, MainMenu.class);
        startActivity(intent);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        // Do something when the slide changes.
    }
}
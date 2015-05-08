package design.troph.com.audiocast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;


public class MainActivity extends ActionBarActivity {

    //shared pref
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    // primary focus of this simple main activity is choose where to start!!!
    // Main Activity only chooses which one to start, and send the corresponding information
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = pref.edit();

        //If root set... either start audio, if audio is present, or open to main gallery page...
        if (pref.getBoolean("ROOT_DIRECTORY_SET", false)) {
            // ROOT SET

            //if should be continuing, go directly into the player activity
            if (pref.getBoolean("CONTINUE", false)) {

                // CONTINUING
                Intent i = new Intent(MainActivity.this, PlayerActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                //todo pass it the needed keys
                startActivity(i);
            } else {
                // ROOT SET
                // NO BOOK CURRENTLY PLAYING/BOOK FINISHED RECENTLY
                // (sends to gallery view)
                Intent i = new Intent(MainActivity.this, GalleryActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                //todo pass it the needed keys
                startActivity(i);
            }

        } else {
            // NO ROOT
            // START FRESH
            // CONTINUING
            Intent i = new Intent(MainActivity.this, AddRootActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

            //todo pass it the needed keys
            startActivity(i);

        }
        super.onCreate(savedInstanceState);

    }
}

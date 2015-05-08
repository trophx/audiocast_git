package design.troph.com.audiocast;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class AddRootActivity extends ActionBarActivity {

    //below
    private final String LOGTAG = "AFBExActivity";
    private final int REQUEST_CODE_PICK_DIR = 1;
    //shared pref
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = pref.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_root);

        final Activity activityForButton = this;


        ImageView addButton = (ImageView)findViewById(R.id.addRoot);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(LOGTAG, "pressed");
                Intent fileExploreIntent = new Intent(
                        FileBrowserActivity.INTENT_ACTION_SELECT_DIR,
                        null,
                        activityForButton,
                        FileBrowserActivity.class
                );
                //If the parameter below is not provided the Activity will try to start from sdcard(external storage),
                // if fails, then will start from roor "/"
                // Do not use "/sdcard" instead as base address for sdcard use Environment.getExternalStorageDirectory()
//        		fileExploreIntent.putExtra(
//        				ua.com.vassiliev.androidfilebrowser.FileBrowserActivity.startDirectoryParameter,
//        				"/sdcard"
//        				);
                startActivityForResult(
                        fileExploreIntent,
                        REQUEST_CODE_PICK_DIR
                );

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_root, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PICK_DIR) {
            if(resultCode == RESULT_OK) {
                String newDir = data.getStringExtra(FileBrowserActivity.returnDirectoryParameter);
                Toast.makeText(this, "Received DIRECTORY path from file browser:\n" + newDir, Toast.LENGTH_LONG).show();

                // Write root directory to sharedPref
                editor.putBoolean("ROOT_DIRECTORY_SET", true);
                editor.putString("ROOT_DIRECTORY", newDir);
                editor.apply();

                // Go to PlayerActivity
                Intent i = new Intent(AddRootActivity.this, GalleryActivity.class);
                startActivity(i);


            } else {//if(resultCode == this.RESULT_OK) {
                Toast.makeText(this, "Received NO result from file browser", Toast.LENGTH_LONG).show();
            }//END } else {//if(resultCode == this.RESULT_OK) {
        }//if (requestCode == REQUEST_CODE_PICK_DIR) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}

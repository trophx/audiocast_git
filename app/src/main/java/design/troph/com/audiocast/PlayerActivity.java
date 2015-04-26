package design.troph.com.audiocast;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


public class PlayerActivity extends ActionBarActivity implements View.OnClickListener{

    //below
    private final String LOGTAG = "AFBExActivity";
    private final int REQUEST_CODE_PICK_DIR = 1;
    private final int REQUEST_CODE_PICK_FILE = 2;
    //Arbitrary constant to discriminate against values returned to onActivityResult
    // as requestCode

    //init
    private ProgressBar seek;


    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;

    //shared pref
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = pref.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_v2);

        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(""); //rid of title

        // init
        seek = (ProgressBar) findViewById(R.id.seekBar);
        seek.setProgress(10);
        seek.getProgressDrawable().setColorFilter(getResources().getColor(R.color.white),
                android.graphics.PorterDuff.Mode.SRC_IN);


        ImageView cover = (ImageView) findViewById(R.id.croppedBookCover);
        cover.setImageBitmap(getCroppedBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.samplecover)));
    }

    private void addDrawerItems() {

        final Activity activityForButton = this;

        String[] osArray = {"","Android", "iOS", "Windows", "OS X", "Linux"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PlayerActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();

                if (pref.contains("ROOT_DIRECTORY")){
                    FileHelper fileHelper = new FileHelper(pref.getString("ROOT_DIRECTORY", null));
                    fileHelper.organize();
                }

            }
        });
    }


    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
//                getSupportActionBar().setTitle("Navigation!");
//                getSupportActionBar().setDisplayHomeAsUpEnabled(false);

                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_player, menu);
//        return true;
//    }

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

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public Bitmap getCroppedBitmap(Bitmap bitmap) {

        int sideLength;
        if (bitmap.getWidth() == bitmap.getHeight() || bitmap.getWidth() < bitmap.getHeight()) {

            sideLength = bitmap.getWidth();
        } else {
            sideLength = bitmap.getHeight();

        }
        Bitmap output = Bitmap.createBitmap(sideLength,
                sideLength, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        //deal with rectangle


        final Rect rect = new Rect(0, 0, sideLength, sideLength);


        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        // canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        canvas.drawCircle(sideLength / 2, sideLength / 2,
                sideLength / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        //Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
        //return _bmp;
        return output;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.nextTrack:

                break;

        }
    }
}

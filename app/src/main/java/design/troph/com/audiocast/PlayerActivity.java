package design.troph.com.audiocast;


import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;



public class PlayerActivity extends ActionBarActivity implements View.OnClickListener{

    //below
    private final String LOGTAG = "AFBExActivity";
    private final int REQUEST_CODE_PICK_DIR = 1;
    private final int REQUEST_CODE_PICK_FILE = 2;

    //Arbitrary constant to discriminate against values returned to onActivityResult
    // as requestCode

    //init
    private ProgressBar seek;



    //shared pref
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;


    // Needs to set up Musicplayer service
    // needs to count durations for first time
    // write it to the database?

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = pref.edit();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_v2);



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

package design.troph.com.audiocast;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;


public class GalleryActivity extends ActionBarActivity {


    //song list variables
    private ArrayList<Track> songList;
    private ListView songView;



    private final int REQUEST_CODE_PICK_FILE = 2;
    //Arbitrary constant to discriminate against values returned to onActivityResult
    // as requestCode

    //shared pref
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    //hamburger
    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;


    //books for list
    private String[] folders, authors, albumArt, mAudioPath;

    // traverse path
    private File path;
    private Item[] fileList;
    private static final String TAG = "F_PATH";
    private Folder[] folderList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);

        path = new File(pref.getString("ROOT_DIRECTORY", null) + "");

        Log.d("sharedPref root", pref.getString("ROOT_DIRECTORY", null));
        Log.d("path (file)", path.toString());

        editor = pref.edit();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        // Setup Nav Menu
        mDrawerList = (ListView)findViewById(R.id.navList);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();


        ListView mListView = (ListView) findViewById(R.id.listView);

//        mMusicList = getAudioList();

        // todo implement options
        // 1 traverse file path (could be slower?)
        // 2 get from database (faster? easier to manage?)
        traverseFilePath();


        //test
        for (int i = 0; i < folderList.length; i++){
            Log.d("Audiobook List #" +i, folderList[i].toString());
        }

//        mListView.setAdapter(new bookListAdapter(folders, authors, albumArt, mAudioPath));
        mListView.setAdapter(new bookListAdapter(folderList));







    }

    private void traverseFilePath(){
        //browse through folders || read file?

        if (path.exists()) {
            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    File sel = new File(dir, filename);
                    // Filters based on whether the file is hidden or not
                    return (sel.isFile() || sel.isDirectory())
                            && !sel.isHidden();

                }
            };

            String[] fList = path.list(filter);
            // Now we have filtered result of only files and directories...
            // First layer folder names will be used to keep track of books.


            fileList = new Item[fList.length];
            folderList = new Folder[fList.length];



            for (int i = 0; i < fList.length; i++) {
                fileList[i] = new Item(fList[i], R.drawable.file_icon);
                // Convert into file path
                File sel = new File(path, fList[i]);

                ////mycode


                // Set drawables
                if (sel.isDirectory()) {
//                    fileList[i].icon = R.drawable.directory_icon;
                    Log.d("DIRECTORY", fileList[i].file);



                    //mycode
                    folderList[i] = new Folder(sel.getAbsolutePath(), sel.getName());

                    //create audiobook database
                    Audiobook aBook = new Audiobook(sel.getName(), sel.getAbsolutePath());
                    aBook.save();

//                    sel.getName()

                } else {
                    Log.d("FILE", fileList[i].file);
                }
            }

//            if (!firstLvl) {
//                Item temp[] = new Item[fileList.length + 1];
//                for (int i = 0; i < fileList.length; i++) {
//                    temp[i + 1] = fileList[i];
//                }
//                temp[0] = new Item("Up", R.drawable.directory_up);
//                fileList = temp;
//            }
        } else {
            Log.e(TAG, "path does not exist");
        }

    }




    private void addDrawerItems() {

        final Activity activityForButton = this;

        String[] osArray = {"ME","Android", "iOS", "Windows", "OS X", "Linux"};
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(GalleryActivity.this, "Time for an upgrade!", Toast.LENGTH_SHORT).show();

                if (pref.contains("ROOT_DIRECTORY")) {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    //havent fixed yet bottom, directly copied.

    /**
     *
     * @param match
     */
    public void getSongList(String match) {
        //query external audio
        ContentResolver musicResolver = getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);
        //iterate over results if valid
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);

                //get meta album art
                MediaMetadataRetriever meta = new MediaMetadataRetriever();
                Uri trackUri = ContentUris.withAppendedId(
                        android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        thisId);

                //Only add The Beatles
                if (thisArtist.contains(match)) {
                    songList.add(new Track(thisId, thisTitle, thisArtist));
                }
//do i need? maybe not, but could try something new next time with below
//                songList.add(new Song(thisId, thisTitle, thisArtist, trackUri, getApplicationContext()));

            }
            while (musicCursor.moveToNext());
        }
    }

    class bookListAdapter extends BaseAdapter {
        String[] tempSongs, tempArtists, tempPaths, tempAlbumArt;
        Folder[] folderList;

        bookListAdapter(String[] songList, String[] artistList, String[] albumArtList, String[] pathList) {
            tempSongs = songList;
            tempArtists = artistList;
            tempPaths = pathList;
            tempAlbumArt = albumArtList;
        }

        bookListAdapter(Folder[] folderList){
            this.folderList = folderList;

        }

        @Override
        public int getCount() {
//            return tempSongs.length;
            return folderList.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {


            //Metadata getter
//            MediaMetadataRetriever meta = new MediaMetadataRetriever();
//            meta.setDataSource(tempPaths[position]);
//            meta.setDataSource(folderList[position].toString());


//            Log.d("tempSongs", tempSongs[position]);





            //mycode





            LayoutInflater inflater = getLayoutInflater();
            View row;
            row = inflater.inflate(R.layout.audiobook, parent, false);
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(GalleryActivity.this, PlayerActivity.class);
                    startActivity(i);
                    /*///////////////

                    Toast.makeText(getApplicationContext(), "pressed: ", Toast.LENGTH_SHORT).show();
                    try {
//                    Toast.makeText(getApplicationContext(), arg2 + "::" + mAudioPath[arg2], Toast.LENGTH_SHORT).show();
                        playSong(position);

//                        lastSongPath = mAudioPath[position];
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                        Log.d("exception", e.toString());
                        Log.d("SEE THIS", mAudioPath[position]);
                    }


                    *////////////////////

                }
            });

            //mycode init
            ImageView ivAlbumArt = (ImageView) row.findViewById(R.id.iv_song_albumArt);
            TextView tvSong = (TextView) row.findViewById(R.id.tv_song_title);
            TextView tvArtist = (TextView) row.findViewById(R.id.tv_song_artist);

            tvSong.setText(folderList[position].title);

            //////
            /*
//            Drawable art = Drawable.createFromPath(tempAlbumArt[position]);
            ImageView ivAlbumArt = (ImageView) row.findViewById(R.id.iv_song_albumArt);
//            ivAlbumArt.setImageDrawable(art);

            TextView tvSong = (TextView) row.findViewById(R.id.tv_song_title);
            TextView tvArtist = (TextView) row.findViewById(R.id.tv_song_artist);
            tvSong.setText(tempSongs[position]);
            tvArtist.setText(tempArtists[position]);


//            tvArtist.setText(meta.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));



            byte[] art = meta.getEmbeddedPicture();
            try {
                songImage = BitmapFactory.decodeByteArray(art, 0, art.length);
                ivAlbumArt.setImageBitmap(songImage);
            } catch (NullPointerException e) {

//                songImage = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.ic_sample_album_art);
            }

            *///


            return (row);



        }


    }

    private class Item {
        public String file;
        public int icon;

        public Item(String file, Integer icon) {
            this.file = file;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return file;
        }
    }

    private class Folder {
        public String file;
        public String title;


        public Folder(String file, String title) {
            this.file = file;
            this.title = title;
        }
        @Override
        public String toString() {
            return file;
        }
    }


}

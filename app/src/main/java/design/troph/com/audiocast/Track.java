package design.troph.com.audiocast;

/**
 * Created by Chris on 4/25/15.
 */

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

/*
 * This is demo code to accompany the Mobiletuts+ series:
 * Android SDK: Creating a Music Player
 *
 */


public class Track {

    private long id;
    private String title;
    private String artist;
    private byte[] albumArt;
    private Uri uri;
    private Context context;
    private boolean initialized=false;


    public Track(long songID, String songTitle, String songArtist) {
        id = songID;
        title = songTitle;
        artist = songArtist;
    }

    public Track(long songID, String songTitle, String songArtist, Uri songURI, Context songContext) {
        id = songID;
        title = songTitle;
        artist = songArtist;
        uri = songURI;
        context = songContext;


    }

    public void init(){
        if(initialized){

        } else{
            try{
                MediaMetadataRetriever meta = new MediaMetadataRetriever();
                meta.setDataSource(context.getApplicationContext(), uri);
                albumArt = meta.getEmbeddedPicture();
            }catch (IllegalArgumentException e) {

            }
            initialized=true;
        }

    }
    public long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public byte[] getAlbumArt() {
        return albumArt;
    }


}

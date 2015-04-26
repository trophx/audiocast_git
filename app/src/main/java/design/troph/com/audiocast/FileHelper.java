package design.troph.com.audiocast;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 4/19/15.
 */
public class FileHelper {
    private String root;

    public FileHelper(String input) {
        root = input;

    }

    public void organize(){
        // From the root folder, check every folder
        // file structure is as follows
        /*
            /ROOT_DIRECTORY/book1/tracks...
            (create database)

            get # of books
            create library using book names:

            in each folder, get track playlist



         */

        // List files first
        List<File> files = getFolders(new File(root));

        /*  how to get track list from book
            book has:
                - current playback position in ms
                - root folder/title (folder name)
                - tracks and titles (filenames) and durations
                - album art (full)
                - album art (cropped)
                - playlist

            calculate each track and add to get millisec. FUNCTION



         */



    }
    // get all folder names from root.
    private List<File> getFolders(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                //first loop
                inFiles.add(file);
                Log.d("filename", file.getName());

                //TODO: I can handle case of one folder with multiple "albums" and organize if alphabetical
                //Later

                inFiles.addAll(getListFiles(file));

            } else {
                if(file.getName().endsWith(".csv")){
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }
    private List<File> getListFiles(File parentDir) {
        ArrayList<File> inFiles = new ArrayList<File>();
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file));
            } else {
                if(file.getName().endsWith(".csv")){
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }

}

package design.troph.com.audiocast;

import com.orm.SugarRecord;

/**
 * Created by Chris on 4/27/15.
 */
public class Audiobook extends SugarRecord<Audiobook> {
    String folderTitle;
    String bookTitle;
    String bookAuthor;
    int duration;
    int saved;
    String path;


    public Audiobook(String folderTitle, String bookTitle, String bookAuthor, String path){
        this.folderTitle = folderTitle;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.path = path;

    }

    public Audiobook(String folderTitle, String path){
        this.folderTitle = folderTitle;
        this.path = path;

    }


}

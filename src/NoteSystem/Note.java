package NoteSystem;

import java.util.Calendar;

public class Note {
    Calendar date;
    String title;
    String description;
    String[] tags;
    int numOfFiles;
    long size;
    
    public Note(String title, String description, String[] tags, Calendar date, int numOfFiles, long size){
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.date = date;
        this.numOfFiles = numOfFiles;
        this.size = size;
    }
    
}

package NoteSystem;

import java.util.Calendar;

public class Note {
    Calendar date;
    String title;
    String description;
    String[] tags;
    
    public Note(String title, String description, String[] tags, Calendar date){
        this.title = title;
        this.description = description;
        this.tags = tags;
        this.date = date;
    }
    
}

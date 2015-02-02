package NoteSystem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Scanner;

public class NoteList {
    ArrayList<Note> list;
    String defaultPath;
    
    public NoteList(String defaultPath){
        list = new ArrayList<>();
        this.defaultPath = defaultPath;
    }
    
    //This method will refresh the noteList variable
    //by re-reading the data from our default save path.
    public void refreshNoteList() throws Exception {
       File saveFolder = new File(defaultPath);
       File[] filesInFolder = saveFolder.listFiles();
       ArrayList<File> directoriesInFolder = new ArrayList<>();
       for(int i = 0; i<filesInFolder.length; i++){
           if(filesInFolder[i].isDirectory()){
               directoriesInFolder.add(filesInFolder[i]);
           }
       }
       
       int numOfNotes = directoriesInFolder.size();
       
       for(int i = 0; i<numOfNotes; i++){
           File currentFolder = directoriesInFolder.get(i);
           File infoFile = new File(currentFolder.getPath() + "/data.info");
           Scanner infoReader = new Scanner(infoFile);
           ArrayList<String> info = new ArrayList<>();
           while(infoReader.hasNext()){
               info.add(infoReader.nextLine());
           }
           infoReader.close();
           
           String title = info.get(2).substring(6, info.get(2).length());
           String description = info.get(3).substring(12, info.get(3).length());
           String[] tags = formatTags(info.get(4).substring(5, info.get(4).length()));
           String dateString = info.get(5).substring(5, info.get(5).length());
           Calendar date = Calendar.getInstance();
           SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
           date.setTime(dateFormat.parse(dateString));
           list.add(new Note(title, description, tags, date));
       }
    }
    
    //Takes in the raw input from the tags textfield
    //Outputs the tags in an array with all empty tags removed
    //and all valid tags trimmed for leading/trailing spaces.
    public String[] formatTags(String tags) {
        String[] seperatedTags = tags.split(",");
        ArrayList<String> tagsList = new ArrayList<String>(Arrays.asList(seperatedTags));

        //Trim for leading/trailing spaces
        for (int i = 0; i < tagsList.size(); i++) {
            tagsList.set(i, tagsList.get(i).toString().trim());
        }

        //Remove all empty elements
        for (int i = tagsList.size() - 1; i >= 0; i--) {
            if (tagsList.get(i).length() == 0) {
                tagsList.remove(i);
            }
        }
        return (tagsList.toArray(new String[tagsList.size()]));
    }
}

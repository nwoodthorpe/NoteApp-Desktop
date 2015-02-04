package NoteSystem;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class NoteList {

    ArrayList<Note> list;
    String defaultPath;
    final int SORT_DATE_NEWESTFIRST = 0;
    final int SORT_DATE_OLDESTFIRST  = 1;
    final int SORT_FILESIZE_BIGGESTFIRST = 2;
    final int SORT_FILESIZE_SMALLESTFIRST = 3;
    final int SORT_VIEWS_MOST = 4;

    public NoteList(String defaultPath) {
        list = new ArrayList<>();
        this.defaultPath = defaultPath;
    }

    public ArrayList<Note> getList() {
        return list;
    }

    public void setList(ArrayList<Note> list) {
        this.list = list;
    }

    //This method will refresh the noteList variable
    //by re-reading the data from our default save path.
    public void refreshNoteList() {
        list.clear();
        File saveFolder = new File(defaultPath);
        File[] filesInFolder = saveFolder.listFiles();
        System.out.println(filesInFolder.length);
        ArrayList<File> directoriesInFolder = new ArrayList<>();
        for (int i = 0; i < filesInFolder.length; i++) {
            if (filesInFolder[i].isDirectory()) {
                directoriesInFolder.add(filesInFolder[i]);
            }
        }

        int numOfNotes = directoriesInFolder.size();

        for (int i = 0; i < numOfNotes; i++) {
            File currentFolder = directoriesInFolder.get(i);
            File infoFile = new File(currentFolder.getPath() + "/data.info");
            Scanner infoReader;
            try {
                infoReader = new Scanner(infoFile);

                ArrayList<String> info = new ArrayList<>();
                while (infoReader.hasNext()) {
                    info.add(infoReader.nextLine());
                }
                infoReader.close();

                String title = info.get(2).substring(6, info.get(2).length());
                String description = info.get(3).substring(12, info.get(3).length());
                String[] tags = formatTags(info.get(4).substring(5, info.get(4).length()));
                int numOfFiles = currentFolder.listFiles().length;
                String dateString = info.get(5).substring(5, info.get(5).length());
                Calendar date = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd h:mm a");
                date.setTime(dateFormat.parse(dateString));
                list.add(new Note(title, description, tags, date, numOfFiles - 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
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
    
    public void sortList(int sortMethod){
        switch(sortMethod){
            case(SORT_DATE_NEWESTFIRST):
                System.out.println("before: " + list.get(0).title);
                Collections.sort(list, new SortDateNewestFirst());
                System.out.println("after: " + list.get(0).title);
                break;
                
            case(SORT_DATE_OLDESTFIRST):
                
                break;
                
            case(SORT_FILESIZE_BIGGESTFIRST):
                
                break;
                
            case(SORT_FILESIZE_SMALLESTFIRST):
                
                break;
                
            case(SORT_VIEWS_MOST):
                
                break;
                
            default:
                System.err.println("INVALID SORT METHOD PASSED");
        }
    }
}

class SortDateNewestFirst implements Comparator<Note> {
    @Override
    public int compare(Note a, Note b) {
        if(a.date.before(b.date)) return 1;
        return -1;
    }
}
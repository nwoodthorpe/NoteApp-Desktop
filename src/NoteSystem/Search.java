package NoteSystem;

import java.util.ArrayList;

public class Search {

    //In order to search through the database and find the most relevant item
    //We'll compare each word of the search term to the title, description, and each tag.
    //Each match will add "Points" to the overall note revelance score
    public static ArrayList<Note> sortListBy(ArrayList<Note> noteList, String searchTerm) {
        int[] relevanceScoreList = new int[noteList.size()];
        String[] searchList = searchTerm.toLowerCase().split(" ");
        for (int s = 0; s < searchList.length; s++) {
            for (int i = 0; i < noteList.size(); i++) {
                //Start with title:
                String[] noteTitle = noteList.get(i).title.toLowerCase().split(" ");
                for(int q = 0; q<noteTitle.length; q++){
                    if(noteTitle[q].equals(searchList[s])){
                        relevanceScoreList[i] += 20; //20 points per match for title
                    }else if(noteTitle[q].contains(searchList[s])){
                                relevanceScoreList[i] += 5; //5 points if the title word  contains the search term but doesn't equal
                   }
                }
                
                //Then the description
                String[] noteDescription = noteList.get(i).description.toLowerCase().split(" ");
                for(int q = 0; q<noteDescription.length; q++){
                    if(noteDescription[q].equals(searchList[s])){
                        relevanceScoreList[i] += 3; //3 points for a description match
                    }else if(noteDescription[q].contains(searchList[s])){
                                relevanceScoreList[i] += 2; //2 points if the description word contains the search term but doesn't equal
                   }
                }
                
                //Then the tags
                String[] noteTags = noteList.get(i).tags;
                for(int a = 0; a<noteTags.length; a++){
                    noteTags[a] = noteTags[a].toLowerCase();
                }
                for(int q = 0; q<noteTags.length; q++){
                    if(noteTags[q].equals(searchList[s])){
                        relevanceScoreList[i] += 8; //8 points for a tag match
                    }else if(noteTags[q].contains(searchList[s])){
                                relevanceScoreList[i] += 3; //3 points if the tag word contains the search term but doesn't equal
                   }
                }
            }
        }
        for(int i=0; i < noteList.size()-1; i++){
            for(int j=1; j < noteList.size()-i; j++){
                if(relevanceScoreList[j-1] < relevanceScoreList[j]){
                    Note temp=noteList.get(j-1);
                    noteList.set(j-1, noteList.get(j));
                    noteList.set(j, temp);
                    
                    int temp2 = relevanceScoreList[j-1];
                    relevanceScoreList[j-1] = relevanceScoreList[j];
                    relevanceScoreList[j] = temp2;
                }
            }
        }
        
        return noteList;
    }
}

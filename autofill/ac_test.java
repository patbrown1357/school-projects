import java.io.*;
import java.util.*;

public class ac_test {


  public static void main( String args[] ) throws Exception {


    String currSearch = "";

    //avg time components
    float totalTime = 0;
    int timeCount = 0;
    boolean firstLoop = true;

    //inputs and data structures
    File user_history = new File( "user_history.txt");
    Scanner dictionary = new Scanner( new File("dictionary.txt"));
    Scanner userIn = new Scanner( System.in );
    DLBTrie autoFill = new DLBTrie();
    DLBTrie hisTrie = new DLBTrie();
    LinkedList<String> history = new LinkedList<String>();

    //check for user history
    //if it exists read it in
    //if not create the file
    if( user_history.exists() )   {

      Scanner histScanner = new Scanner( user_history);
      while( histScanner.hasNext() )  {
        String newString = histScanner.nextLine();
        hisTrie.add( newString );


      }
      histScanner.close();

    } else {

      File newDoc = new File( "user_history.txt");
       newDoc.createNewFile();

    }


    //fill up dictionary DLB
    while( dictionary.hasNext() ) {
      String dicWord = dictionary.nextLine();
      autoFill.add( dicWord );
    }

    dictionary.close();

    //Start of the main loop;
    while( true ) {

      //different greetings
      if( firstLoop ) {
        System.out.print( "Please enter your first character: " );
      } else {
      System.out.print( "Please enter your next character: " );
      }

      //read input
      //check for specific chars !, $, 1-5
      char currChar = userIn.next().charAt(0);

      if( currChar == '$') {

        if( !firstLoop ) {

          if( hisTrie.get(currSearch) == null )
            writeToHist( currSearch);
          history.add( currSearch );
          currSearch = "";
          System.out.print( "\n");
          firstLoop = true;
          continue;
        }
        System.out.println();
        continue;

      }

      // now we set to false
      firstLoop = false;

      if( currChar == '!' ) {

        //stop NaN calc time and exit
        if( timeCount == 0 ) timeCount = 1;
        System.out.println( "Average Time: " + String.valueOf( totalTime / timeCount ) + " s");
        System.exit(0);
      }


      //if number make sure its in range, then decide to write to file or not
      if( Character.isDigit( currChar ) ) {

        int index = Character.getNumericValue(currChar);
        if( index > history.size() ) {
          System.out.println( "You have entered an invalid index." );
          continue;
        } else {

        System.out.print( "WORD COMPLETED: " );
        System.out.print( history.get(index-1) + "\n\n" );

        //stop duplicates in history file
        if( hisTrie.get( history.get(index-1)) == null )
          writeToHist( history.get(index-1));
        hisTrie.add( history.get(index-1));
        firstLoop = true;
        //reset search for new input
        currSearch = "";
        continue;

      }
    }

      //append for next loop
      currSearch += currChar;

      //clear history of any old terms
      history.clear();

      float startTime = System.nanoTime();
      System.out.println( startTime );

      //check history for words, if < 5 pull from dic
      if( hisTrie.getRoot() != null ) {
         history = hisTrie.keysWithPrefix( currSearch );
       }

      if( history.size() < 5 )  {

        LinkedList<String> dictRecs = autoFill.keysWithPrefix( currSearch );

        for( int i = 0; i < dictRecs.size(); i++) {

          // check for dupes then add
          // break once history full
          if( history.size() == 5 ) break;
          if( !history.contains( dictRecs.get(i) ) ) {
            history.add( dictRecs.get(i));

          }
        }
      }
    
      float newTime = System.nanoTime();
      System.out.println( newTime );
      float diff = ( newTime  - startTime) / (float) Math.pow( 10, 9 );
      timeCount++;
      totalTime += diff;
      System.out.println( "(" + diff + " s)\n");

      //tell if no prediction, else print them to user
      if( history.size() == 0 ) {

        System.out.println( "No predictions were found.\n\n");

      } else {



      System.out.print( "Predictions: \n");
      for( int i = 0; i < history.size(); i++ ) {

        int j = i+1;
        System.out.print( "(" + j + ")" + " " + history.get(i) + " \t");
      }

        System.out.print("\n\n");

    }
  }
}

  //function to write to user_history
  public static void writeToHist( String pred ) throws Exception {
    if( pred.equals( null) ) return;
    File file = new File("user_history.txt");
    PrintWriter pw = new PrintWriter(new FileWriter(file, true));
    pw.println( pred );
    pw.close();

  }
  //gets time and converts into seconds


}

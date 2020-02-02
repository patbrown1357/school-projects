import java.io.*;
import java.util.*;

public class ac_test {
  public static void main( String args[] ) throws Exception {

    DLBTrie autoFill = new DLBTrie();
    DLBTrie savedTrie = new DLBTrie();
    File saved = new File( "savedSearch.txt");

    String firstChar = new String("Please enter your first character: ");
    String nextChar = new String("Please enter your next character: ");
    String predict = new String("Predictions: \n");

    Scanner dictionary = new Scanner( new File("dictionary.txt"));


    if( saved.exists() ) {
      Scanner savedScanner = new Scanner( saved );
      while( savedScanner.hasNext() )
      {
        String newString = savedScanner.nextLine();
        savedTrie.add( newString );
      }

    } else {

      File newDoc = new File( "savedSearch.txt");
      if( newDoc.createNewFile() )
      System.out.println( "File creation done");
    }

        savedTrie.traverse( savedTrie.getRoot() );

    /*while( dictionary.hasNext() ) {
      autoFill.add( dictionary.readLine() );
    }*/




    char testChar = 'x';
    DLBTrie newTree = new DLBTrie(testChar);

    if( newTree.getRoot() != null )
    {
      System.out.println("default constructor works!");
    }

    System.out.println( newTree.getRoot().getVal() );

    newTree.deleteRoot();

    if( newTree.getRoot() == null )
      System.out.println( "rootDeleted" );

    newTree.add( "test string" );
    newTree.add( "second test");
    newTree.add( "test branch");

    if( newTree.getRoot() != null )
      System.out.println( "Added String");


    System.out.println( newTree.getRoot().getVal() );

  //  System.out.println( newTree.searchTree("test string").getVal() );


    newTree.printTest();
    System.out.println();
    newTree.traverse( newTree.getRoot() );




  }
}

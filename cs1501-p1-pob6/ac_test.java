import java.io.*;
import java.util.*;

public class ac_test {
  public static void main( String args[] ) {

    String firstChar = new String("Please enter your first character: ");
    String nextChar = new String("Please enter your next character: ");
    String predict = new String("Predictions: \n");

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

    newTree.addString( "test string" );

    if( newTree.getRoot() != null )
      System.out.println( "Added String");

    newTree.printTest();



  }
}

import java.util.*;
import java.io.*;

/* copper connected and two-point removal methods are
   written into the AdjList class

   Shortest path methods are stored in the MinPath class

*/
public class NetworkAnalysis {

  public static void main( String args[] ) throws Exception {
    boolean update = true;
    int sz = 0;
    if( args.length == 0 ) return;

    Scanner file = new Scanner( new File( args[0]));
      if( file.hasNextLine() ) sz = Integer.parseInt(file.nextLine());

      AdjList network = new AdjList(sz);

      while( file.hasNextLine()) {
        String line = file.nextLine();
        String[] cableIn = line.split(" ");
        network.addDst(cableIn);
        //make sure to add the back edges
        String swap = cableIn[0];
        cableIn[0] = cableIn[1];
        cableIn[1] = swap;
        network.addDst(cableIn);
      }

      while( update ) {
        System.out.print( "\nNetwork Options:\n" +
                          " 1) Find the Lowest Latency Path \n"   +
                          " 2) Determine if network is Copper-Only Connected \n" +
                          " 3) Determine if Graph Would Remain Connected if Two Nodes Fail \n"   +
                          " 4) Quit \n" +
                          " Please enter a number: ");

                          int choice = Integer.parseInt(checkInt());

                          switch( choice ) {
                            case 1:
                            System.out.print("Please enter the first vertex: ");
                            int v1 = Integer.parseInt(checkInt());
                            if( !inBounds(network.V, v1) ) break;

                            System.out.print("Please enter the second vertex: ");
                            int v2 = Integer.parseInt(checkInt());
                            if( !inBounds(network.V, v2) ) break;

                            MinPath min = new MinPath(network);
                            System.out.print("\nThe minimum path is: " );
                            min.getMin(v1, v2);
                            System.out.println("The maximum bandwidth for this path is: " + min.getBndwt() + " gigabits per second");

                            //lowest latency path
                             break;
                           case 2:
                           //determine if network is copper only connected
                            if(network.copperConnected()) {
                              System.out.println("Graph is copper connected!");
                            } else {
                              System.out.println("Graph is not copper connected!");
                            }
                             break;
                           case 3:
                           network.twoPoints();
                           //determine if connected on two node failure
                             break;
                           case 4:
                               update = false;
                             break;
                           default:
                             System.out.println( "Invalid choice please select another option: ");
                             break;
                           }
  }
}
  //check the vertices given are in the graphs bounds 0 <= num < V
  public static boolean inBounds(int sz, int num) {
    if( num >= sz || num < 0) {
      System.out.println("Invalid Vertex Selected");
      return false;
    }
    return true;
  }

//check that when asked for a number a number is provided
  public static String checkInt() {
    Scanner userIn = new Scanner(System.in);
    boolean invalid = true;
    String input = "";
    while(invalid) {
      input = userIn.next();
      try {
        Integer.parseInt(input);
        invalid = false;
      } catch( Exception NumberFormatException ) {
        System.out.print("Invalid Input\nPlease enter a number:");
      } finally {

      }
    }
    return input;
  }
}

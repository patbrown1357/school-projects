import java.util.*;
import java.io.*;
import java.math.*;

public class DivTest {

  public static void main(String[] args) {

    Scanner userIn = new Scanner( System.in);

    System.out.print("First Number: ");
    String f = userIn.nextLine();
    System.out.print("Second Number: ");
    String s = userIn.nextLine();

    HeftyInteger qtnt = new HeftyInteger(new BigInteger(f).toByteArray());
    HeftyInteger dvsr = new HeftyInteger(new BigInteger(s).toByteArray());

    HeftyInteger[] res = qtnt.divide(dvsr);
    HeftyInteger ans = res[0];
    HeftyInteger rem = res[1];

    System.out.println("HUH");
    printHeftyInteger(ans);
    System.out.println("WHAT");
    printHeftyInteger(rem);

  }

  public static void printHeftyInteger(HeftyInteger hi) {
      System.out.println(new BigInteger(hi.getVal()).toString());
  }


}

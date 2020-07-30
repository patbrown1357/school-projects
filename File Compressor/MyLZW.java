/*************************************************************************
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *
 *  Compress or expand binary input from standard input using LZW.
 *
 *  WARNING: STARTING WITH ORACLE JAVA 6, UPDATE 7 the SUBSTRING
 *  METHOD TAKES TIME AND SPACE LINEAR IN THE SIZE OF THE EXTRACTED
 *  SUBSTRING (INSTEAD OF CONSTANT SPACE AND TIME AS IN EARLIER
 *  IMPLEMENTATIONS).
 *
 *  See <a href = "http://java-performance.info/changes-to-string-java-1-7-0_06/">this article</a>
 *  for more details.
 *
 *************************************************************************/

 /* How to handle
 *  read in mode and store in char variable
 *  when changing size of codebook read char and work from there
 *
 *
 *
 */

public class MyLZW {

    private static final int R = 256;        // number of input chars
    private static int L = 512;              // starting number of codewords = 2^W, 2^12
    private static int W = 9;                // codeword starting width
    private static String mode = "";
    private static int count = 0;            //number of chars in
    private static int compCount = 0;        //size of compressed chars
    private static float cRatio = 0;         //ratio of compressed to not compressed
    private static float oldcRatio = 1;

    //comp ratio = compCount/compCount
    //if compration == 2.25 (ex) reset the codebook

    public static void compress() {


        String input = BinaryStdIn.readString();  //read the input string
        TST<Integer> st = new TST<Integer>();     //create a new ternary search tree Integer var
        for (int i = 0; i < R; i++)   {           //for all numbers from 0 -> size of char set
          st.put("" + (char) i, i);               //put the char at number position, all single chars
        }
        int code = R+1;  // R is codeword for EOF

        //write the mode at the start of the file at 9 bits
        BinaryStdOut.write( mode, W );

        while (input.length() > 0) {

          String s = "";

          if( code >= L && W < 16 ) {

            W++;       //up the bit output by one
            L = 2 * L; //double the size of possible codewords

          }

          if( code >= L && W == 16 ) {

            //reset the codebook and relevant parameters
            cRatio = (float) count/compCount;

            if( mode.equals("r") || ( mode.equals("m") && oldcRatio/cRatio > 1.1 )) {

              //reset bit length && size of array
              W = 9;
              L = 512;
              code = R+1;
              //reset code length
              TST newSt = new TST<Integer>();
              for( int i = 0; i < R; i++ ) {
                newSt.put("" + (char) i, i);
              }

              st = newSt;
              count = 0;
              compCount = 0;

            }

            oldcRatio = cRatio;


          }

            s = st.longestPrefixOf(input);  // Find max prefix match s.
            BinaryStdOut.write(st.get(s), W);             // Print s's encoding.
            int t = s.length();
            if (t < input.length() && code < L) {         // Add s to symbol table. as long as the book isnt full
              st.put(input.substring(0, t + 1), code++);  //put the substring + 1 char in the book
            }
            if( W == 16 && code >= L ) {

              compCount +=  W; //  #of encoded bits
              count += t * 8; //number of chars * size of chars

            }
            input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    }


    public static void expand() {


        String[] st = new String[L];
        int i; // next available codeword value

        // initialize symbol table with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF

        //read encoded mode and cast to char

        int codeword = BinaryStdIn.readInt(W);
        if (codeword == R) return;           // expanded message is empty string
        String val = st[codeword];

        //get the format character
        mode = val;
        codeword = BinaryStdIn.readInt(W);
        val = st[codeword];


        while (true) {

          if( i == L-1 && W < 16 ) {

            String[] stNew = new String[2*L];

            for( int j = 0; j < L; j++ ){
              stNew[j] = st[j];
            }

            st = stNew;

            W++;       //up the bit output by one
            L = 2 * L; //double the size of possible codewords

          }


          BinaryStdOut.write(val);

          if( W == 16 && i >= L-1 ) {
            compCount += W;
            count += ( val.length() * 8 );
            //System.err.println( count/compCount );
          }
          // reset the codebook
          if( i >= L-1 && W == 16 ) {

            cRatio = (float) count/compCount;

            if( mode.equals("r") || ( mode.equals("m") && oldcRatio/cRatio > 1.1 )) {

              W = 9;
              L = 512;
              i = R+1;
              String[] newSt = new String[L];

              for( i = 0; i < R; i++ ) {
                newSt[i] = "" + (char) i;
              }
              newSt[i++] = "";
              st = newSt;

              codeword = BinaryStdIn.readInt(W);
              val = st[codeword];
              BinaryStdOut.write(val);

              compCount = 0;
              count = 0;



              }

              oldcRatio = cRatio;
            }

            codeword = BinaryStdIn.readInt(W);
            if (codeword == R) break;
            String s = st[codeword];
            if (i == codeword) s = val + val.charAt(0);   // special case hack
            if (i < L) st[i++] = val + s.charAt(0);
            val = s;
        }
        BinaryStdOut.close();
    }


    public static void main(String[] args) {


        if (args[0].equals("-")) {

          if( args.length < 2 ) {
            mode = "n";
          } else {
            mode = args[1];
          }

          compress();

        } else if (args[0].equals("+")) {
          expand();
        }  else {
          throw new IllegalArgumentException("Illegal command line argument");
        }
    }

}

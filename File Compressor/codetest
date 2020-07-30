while (input.length() > 0) {

  int count = 0; //number of chars in
  int compCount = 0; //size of compressed chars


  //if the codebook is full and reset is enabled reset codebook
    if( code == L  && mode.equals("r")) {
      TST<Integer> st = new TST<Integer>();    //create a new ternary search tree Integer var
      for (int i = 0; i < R; i++)              //for all numbers from 0 -> size of char set
          st.put("" + (char) i, i);            //put the char at number position, all single chars
    } else if ( code == L && mode.equals("m")) {
      if( compCount/count >= 1.1 ) {
        TST<Integer> st = new TST<Integer>();    //create a new ternary search tree Integer var
        for (int i = 0; i < R; i++)              //for all numbers from 0 -> size of char set
            st.put("" + (char) i, i);
      }
    }

    if( code)


    String s = st.longestPrefixOf(input);  // Find max prefix match s.
    BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
    int t = s.length();
    if (t < input.length() && code < L)    // Add s to symbol table. as long as the book isnt full
        st.put(input.substring(0, t + 1), code++); //put the substring + 1 char in the book
    compCount += t *  W; // number of chars * #of encoded bits
    count+= t * 8; //number of chars * size of chars
    input = input.substring(t);            // Scan past s in input.
    
}




public static void expand() {

    int count = 0;
    int compCount = 0;

    String[] st = new String[L];
    int i; // next available codeword value

    // initialize symbol table with all 1-character strings
    for (i = 0; i < R; i++)
        st[i] = "" + (char) i;
    st[i++] = "";                        // (unused) lookahead for EOF

    //read encoded mode and cast to char
    mode = (char) BinaryStdIn.readInt( W );

    int codeword = BinaryStdIn.readInt(W);
    if (codeword == R) return;           // expanded message is empty string
    String val = st[codeword];

    while (true) {

       //if codebook is full
       if( i >= L && mode.equals("r")) {
         for (i = 0; i < R; i++)
             st[i] = "" + (char) i;
         st[i++] = "";
       }

       if( i >= L && mode.equals("m")){

         if( true /*compression bad*/) {
           for (i = 0; i < R; i++)
               st[i] = "" + (char) i;
           st[i++] = "";
         }
       }


        BinaryStdOut.write(val);
        codeword = BinaryStdIn.readInt(W);
        if (codeword == R) break;
        String s = st[codeword];

        if (i == codeword) {
          s = val + val.charAt(0);   // special case hack
        }
        if (i < L) {
           st[i++] = val + s.charAt(0);
        }
        val = s;
    }
    BinaryStdOut.close();
}

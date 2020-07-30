public class HeftyInteger {

	private final byte[] ONE = {(byte) 1};

	private byte[] val;

	public String toString() {
		String print = "";
		for( int i = 0; i < this.length(); i++) {
			print += ( "[" + (int) this.getVal()[i] + "]");
		}
		print += "\n";
		return print;
	}

	/**
	 * Construct the HeftyInteger from a given byte array
	 * @param b the byte array that this HeftyInteger should represent
	 */
	public HeftyInteger(byte[] b) {
		val = b;
	}

	/**
	 * Return this HeftyInteger's val
	 * @return val
	 */
	public byte[] getVal() {
		return val;
	}

	/**
	 * Return the number of bytes in val
	 * @return length of the val byte array
	 */
	public int length() {
		return val.length;
	}

	/**
	 * Add a new byte as the most significant in this
	 * @param extension the byte to place as most significant
	 */
	public void extend(byte extension) {
		byte[] newv = new byte[val.length + 1];
		newv[0] = extension;
		for (int i = 0; i < val.length; i++) {
			newv[i + 1] = val[i];
		}
		val = newv;
	}

	/**
	 * If this is negative, most significant bit will be 1 meaning most
	 * significant byte will be a negative signed number
	 * @return true if this is negative, false if positive
	 */
	public boolean isNegative() {
		return (val[0] < 0);
	}

	/**
	 * Computes the sum of this and other
	 * @param other the other HeftyInteger to sum with this
	 */
	public HeftyInteger add(HeftyInteger other) {
		byte[] a, b;
		// If operands are of different sizes, put larger first ...
		if (val.length < other.length()) {
			a = other.getVal();
			b = val;
		}
		else {
			a = val;
			b = other.getVal();
		}

		// ... and normalize size for convenience
		if (b.length < a.length) {
			int diff = a.length - b.length;

			byte pad = (byte) 0;
			if (b[0] < 0) {
				pad = (byte) 0xFF;
			}

			byte[] newb = new byte[a.length];
			for (int i = 0; i < diff; i++) {
				newb[i] = pad;
			}

			for (int i = 0; i < b.length; i++) {
				newb[i + diff] = b[i];
			}

			b = newb;
		}

		// Actually compute the add
		int carry = 0;
		byte[] res = new byte[a.length];
		for (int i = a.length - 1; i >= 0; i--) {
			// Be sure to bitmask so that cast of negative bytes does not
			//  introduce spurious 1 bits into result of cast
			carry = ((int) a[i] & 0xFF) + ((int) b[i] & 0xFF) + carry;

			// Assign to next byte
			res[i] = (byte) (carry & 0xFF);

			// Carry remainder over to next byte (always want to shift in 0s)
			carry = carry >>> 8;
		}

		HeftyInteger res_li = new HeftyInteger(res);

		// If both operands are positive, magnitude could increase as a result
		//  of addition
		if (!this.isNegative() && !other.isNegative()) {
			// If we have either a leftover carry value or we used the last
			//  bit in the most significant byte, we need to extend the result
			if (res_li.isNegative()) {
				res_li.extend((byte) carry);
			}
		}
		// Magnitude could also increase if both operands are negative
		else if (this.isNegative() && other.isNegative()) {
			if (!res_li.isNegative()) {
				res_li.extend((byte) 0xFF);
			}
		}

		// Note that result will always be the same size as biggest input
		//  (e.g., -127 + 128 will use 2 bytes to store the result value 1)
		return res_li;
	}

	/**
	 * Negate val using two's complement representation
	 * @return negation of this
	 */
	public HeftyInteger negate() {
		byte[] neg = new byte[val.length];
		int offset = 0;

		// Check to ensure we can represent negation in same length
		//  (e.g., -128 can be represented in 8 bits using two's
		//  complement, +128 requires 9)
		if (val[0] == (byte) 0x80) { // 0x80 is 10000000
			boolean needs_ex = true;
			for (int i = 1; i < val.length; i++) {
				if (val[i] != (byte) 0) {
					needs_ex = false;
					break;
				}
			}
			// if first byte is 0x80 and all others are 0, must extend
			if (needs_ex) {
				neg = new byte[val.length + 1];
				neg[0] = (byte) 0;
				offset = 1;
			}
		}

		// flip all bits
		for (int i  = 0; i < val.length; i++) {
			neg[i + offset] = (byte) ~val[i];
		}

		HeftyInteger neg_li = new HeftyInteger(neg);

		// add 1 to complete two's complement negation
		return neg_li.add(new HeftyInteger(ONE));
	}

	/**
	 * Implement subtraction as simply negation and addition
	 * @param other HeftyInteger to subtract from this
	 * @return difference of this and other
	 */
	public HeftyInteger subtract(HeftyInteger other) {
		return this.add(other.negate());
	}

	/**
	 * Compute the product of this and other
	 * @param other HeftyInteger to multiply by this
	 * @return product of this and other
	 */
	public HeftyInteger multiply(HeftyInteger other) {
		this.trim();
		other.trim();

		byte[] a, b;
		HeftyInteger tempA = this;
		HeftyInteger tempB = other;
		boolean negative = false;

		if( this.getVal()[0] < 0) {
			tempA = this.negate();
			negative = !negative;
		}

		if( other.getVal()[0] < 0 ) {
			tempB = other.negate();
			negative = !negative;
		}

		a = tempA.getVal();
		b = tempB.getVal();

		if( a.length < b.length ) {
		 	byte[] temp = a;
			a = b;
			b = temp;
		}

		int lenA = a.length;
		int lenB = b.length;
		int count = 0;
		HeftyInteger ans = new HeftyInteger(new byte[1]);

		for( int i = lenB-1; i >= 0; i--) {
			byte mulpcnd = b[i];

			for( int j = lenA-1; j >= 0; j--) {
				byte multplr = a[j];

				int tempInt = (mulpcnd&0xFF) * (multplr&0xFF);
				byte[] newVal = new byte[4];
				newVal[0] = (byte)((tempInt & 0xFF000000) >> 24);
    		newVal[1] = (byte)((tempInt & 0x00FF0000) >> 16);
    		newVal[2] = (byte)((tempInt & 0x0000FF00) >> 8);
    		newVal[3] = (byte)(tempInt & 0x000000FF);
				HeftyInteger temp = new HeftyInteger( newVal );
				for(int k = (lenB-(i+1))+(lenA-(j+1)); k > 0; k-- ) {
					temp.extend((byte)0x00);
					temp.leftShift();
				}
				ans = ans.add(temp);

			}
			count++;
		}
		if( negative) ans = ans.negate();
		return ans;
	}

	//array of {dividend, remainder}
	// this divided by other
	//a divided by b

	public HeftyInteger[] divide( HeftyInteger other ) {

		this.trim();
		other.trim();

	 byte[] a, b;
	 HeftyInteger tempA = this;
	 HeftyInteger tempB = other;
	 boolean negative = false;

	 if( this.getVal()[0] < 0) {tempA = this.negate();}
	 if( other.getVal()[0] < 0 ) { tempB = other.negate();}

	 a = tempA.getVal();
	 b = tempB.getVal();

	 int lenA = a.length;
	 int lenB = b.length;
	 int count = 0;
	 byte[] copy = new byte[b.length];
	 System.arraycopy(b, 0, copy, 0, b.length);
	 HeftyInteger divsr = new HeftyInteger(new byte[1]);
	 HeftyInteger itrtr = new HeftyInteger(copy);
	 HeftyInteger qotnt = new HeftyInteger( new byte[1]);
	 HeftyInteger temp = new HeftyInteger( new byte[2]);
	 HeftyInteger divnd = new HeftyInteger(new byte[1]);
	 HeftyInteger rem = new HeftyInteger(new byte[1]);
	 HeftyInteger[] ans = new HeftyInteger[2];
	 //a / b
	 int dvPos = 1;
	 Integer tempInt;

	 //for all bytes in the quotient
	 for( int i=0; i<lenA; i++) {
		 int j = 0;

		 //extnd quotient and shift val
		 qotnt.extend((byte)0x00);
		 qotnt.leftShift();
		 divsr = new HeftyInteger( new byte[1]);
		 temp.getVal()[1] = (byte)(0xFF&a[i]);
		 qotnt = qotnt.add(temp);
		 //extend and shift dvdnd
		 divnd.extend((byte)0x00);
		 divnd.leftShift();

		 //divsr < qotnt
		 while(divsr.subtract(qotnt).isNegative()) {
			 divsr = divsr.add(itrtr);
			 j++;
		 }
		 //catches case if a is evenly divided by b
		 if(qotnt.subtract(divsr).isNegative()) {
			 divsr = divsr.subtract(itrtr);
			 j--;
		 }

		 //add number to dvdnd and left shift
		 tempInt = new Integer(j);
		 temp.getVal()[1] = (byte)(tempInt.byteValue());
		 divnd = divnd.add(temp);
		 //subtract amount from quotient
		 qotnt = qotnt.subtract(divsr);

	 }

	 //once done whateve is left in qotnt is rem
	 //divnd is already calculated
	 divnd.extend((byte)0x00);
	 rem = qotnt;
	 ans[0] = divnd;
	 ans[1] = rem;
	 return ans;

	}


		/**
		 * Run the extended Euclidean algorithm on this and other
		 * @param other another HeftyInteger
		 * @return an array structured as follows:
		 *   0:  the GCD of this and other
		 *   1:  a valid x value
		 *   2:  a valid y value
		 * such that this * x + other * y == GCD in index 0
		 */
		 public HeftyInteger[] XGCD(HeftyInteger other) {

			HeftyInteger[] vals = new HeftyInteger[4];
			HeftyInteger[] ans = new HeftyInteger[3];
			HeftyInteger tempA = this;
			HeftyInteger tempB = other;
			//make them positive, ignore negs
			if( this.getVal()[0] < 0) {	tempA = this.negate();}
			if( other.getVal()[0] < 0 ) {	tempB = other.negate();}

			HeftyInteger temp = new HeftyInteger(new byte[1]);
			vals[0] = tempA;
			vals[1] = tempB;
			//set s to 1
			vals[2] = new HeftyInteger(new byte[1]);
			vals[2].getVal()[0] = (byte) 0x01;
			//t is set to 0
			vals[3] = new HeftyInteger(new byte[1]);

		  vals = XGCDhelper(vals);
			ans[0] = vals[0];
			ans[1] = vals[2];
			ans[2] = vals[3];
	 		return ans;
		}

		 private static HeftyInteger[] XGCDhelper(HeftyInteger[] vals) {
			 //when done b = 0, and s = 1
			 //so this works as base case
			 if( vals[1].subtract(vals[2]).isNegative())
			 	return vals;
			 //b --> a
			 HeftyInteger newA = vals[1];
			 HeftyInteger[] divRes = vals[0].divide(vals[1]);
			 // a/b --> b
			 HeftyInteger newB = divRes[1];
			 HeftyInteger dvnd = divRes[0];
			 vals[0] = newA;
			 vals[1] = newB;

			 vals = XGCDhelper(vals);

			 //s = t_prev
			 //t = s_prev-(a/b)*t_prev
			 HeftyInteger temp = vals[2];
			 vals[2] = vals[3];
			 vals[3] = temp.subtract(dvnd.multiply(vals[2]));

			 return vals;
		 }

//shift over, used in mult and div
	public void leftShift() {
		for( int i = 0; i<this.length()-1; i++) {
			this.getVal()[i] = this.getVal()[i+1];
		}
		this.getVal()[this.length()-1] = (byte) 0x00;
	}

//trim off excess zeroes from inputs
	 public void trim() {
		 int i = 0;
		 for(i = 0; i<this.length(); i++) {
			 	if( this.getVal()[i] != (byte)0x00) break;
		 }
		 if(i!=0) i--;
		 byte[] newArr = new byte[this.length()-i];
		 for(int j = 0; j < newArr.length; j++) {
			 newArr[j] = this.getVal()[i+j];
		 }

		 if( newArr.length == 0)
		 	newArr = new byte[1];

		 this.val = newArr;

	 }
}

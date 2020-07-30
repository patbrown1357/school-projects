public class Node {
  private final int C_SPEED = 230000000;
  private final int O_SPEED = 200000000;
  private int val;
  private Node next;
  private String mat;
  private int bndWdt;
  private int length;
  private int src;


  //improper construction
  public Node() {
    val = -1;
    mat = "invalid";
  }

  public Node(int j) {
    val = j;
  }

//for creating deep copies
  public Node( Node n ) {
    val = n.val;
    mat = new String(n.mat);
    bndWdt = n.bndWdt;
    length = n.length;
    src = n.src;
  }

//constructor for file read-in
  public Node( String[] props) {
    src = Integer.parseInt(props[0]);
    val = Integer.parseInt(props[1]);
    next = null;
    mat = props[2];
    bndWdt = Integer.parseInt(props[3]);
    length = Integer.parseInt(props[4]);
  }

//perform weight calculation
  public float getWeight() {
    float speed = (float) this.O_SPEED;
    if(this.mat.equals("copper")) {
      speed = this.C_SPEED;
    }
    return this.length/speed;
  }

//encapsulation methods
    public Node getNext() { if(next == null) {
      return null;
    } else {return this.next;} }
    public void setNext(Node n) {this.next = n; }
    public int getVal() { return val; }
    public void setMat(String s ) { this.mat = s; }
    public String getMat() { return this.mat; }
    public void setBndwt(int n) { this.bndWdt = n; }
    public int getBndwt() { return this.bndWdt;}
    public int getSrc() { return this.src; }

}

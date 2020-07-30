public class MinPQ {

//pq that is very similar to p3
//a little smaller and modified for Node class

  public int end = 0;
  Node[] heap = new Node[32];

  public boolean isEmpty() {
    if(end != 0) return false;
    return true;
  }

//get and remove smallest
  public Node poll() {
    Node least = heap[1];
    remove(1);
    return least;
  }

  public void add( Node node) {
    end++;
    heap[end] = node;
    swim(end);
    if( end+1 == heap.length) {resize();}
  }

  public void remove(int ind) {
    exch(ind, end--);
    heap[end+1] = null;
    sink(ind);

  }

  private void sink( int ind ) {
    while( 2*ind <= end ) {
      int i = 2*ind;
      if(i < end && less( i+1, i ))  i++;
      if(!less(i, ind)) break;
      exch(ind, i);
      ind = i;
    }
  }

  private void swim(int ind) {
    while( ind > 1 && less(ind, ind/2)) {
      exch(ind/2, ind);
      ind = ind/2;
    }

  }

  private boolean less( int i, int j ) {
    if( i == 0 || j == 0) return false;

    float secVal = 0;
    float firstVal = 0;

    firstVal = heap[i].getWeight();
    secVal = heap[j].getWeight();

    return ( firstVal - secVal ) < 0;
  }

  private void exch( int i, int j ) {

    Node temp = this.heap[i];
    this.heap[i] = this.heap[j];
    this.heap[j] = temp;

  }
  private void resize() {

    Node[] newHeap = new Node[2*this.heap.length];
    for( int i = 0; i < this.heap.length; i++ ) {
      newHeap[i] = this.heap[i];
    }
    this.heap = newHeap;
  }



}

public class AdjList {

  Node[] graph;
  int V;

  public AdjList(int sz) {
    graph = new Node[sz];
    V = sz;
  }

  public void addDst(String[] props) {
    int src = Integer.parseInt(props[0]);
    if( graph[src] == null ) {
      graph[src] = new Node(props);
      return;
    }

    Node curr = graph[src];

    while( curr.getNext() != null ) {
      curr = curr.getNext();
    }
    curr.setNext(new Node(props));
  }

//deep copy method
//this is needed for two points to work
  public AdjList copy() {
    AdjList copy = new AdjList(this.V);
    copy.graph = this.copyGraph();

    return copy;
  }

//deep copies graph for two points mods
  public Node[] copyGraph() {
    Node[] newGraph = new Node[this.V];
    Node curr;
    Node copy;
    for( int i = 0; i < V; i++ ) {
      if( this.graph[i] != null) {
        curr = this.graph[i];
        copy = new Node(curr);
        newGraph[i] = copy;
        while(curr.getNext() != null ) {
          copy.setNext(new Node(curr.getNext()));
          copy = copy.getNext();
          curr = curr.getNext();
        }
      }
    }
    return newGraph;
  }

  public String toString() {
    String print = "";
    for( int i = 0; i < V; i++ ) {
      print += "["+i+"]";
      Node curr = graph[i];
      while( curr != null ) {
          print += " " + curr.getVal() + " ";
          curr = curr.getNext();
      }
          print += "\n";
    }
      return print;
  }

  public void remove(int i) {
    Node curr = graph[i];
    while( curr!= null) {
      remove(curr.getVal(), i);
      curr = curr.getNext();
    }
    graph[i] = null;

  }

  private void remove(int i, int j) {
    Node curr = graph[i];
    if( curr == null) return;
    if(curr.getVal() == j) {
      graph[i] = curr.getNext();
      return;
    }

    while(curr.getNext() != null ) {
      if(curr.getNext().getVal() == j) {
        curr.setNext( curr.getNext().getNext() );
        return;
      }
      curr = curr.getNext();
    }
  }

  //remove one point
  //remove another
  //call dfs
  public void twoPoints() {
    for(int i = 0; i<V; i++) {
      AdjList copyOne = this.copy();
      copyOne.remove(i);
      for(int j = i+1; j<V; j++) {
        AdjList copyTwo = copyOne.copy();
        copyTwo.remove(j);
        if( !copyTwo.dfs(i,j)) {
          System.out.print("The points " +i + " and " + j +" would disconnect the graph if removed.\n");
          return;

        }
      }
    }
  }

//run dfs for two points method
  public boolean dfs(int p, int q) {
    boolean[] visd = new boolean[V];
    int j;
    //to skip over removed nodes
    for( j = 0; j < V; j++) {
      if( graph[j] != null) {
        break;
    }
  }

//call once, if any are unvisited the graph is broken
    visd = dfs(j, visd);
    for( int i = 0; i < V; i++) {
      if(!visd[i] && (i!=p && i!=q )) { return false;}
    }
    return true;
  }

//depth first traversal of graph
//visd array keeps track of nodes
  private boolean[] dfs(int i, boolean[] visd) {
    Node curr = graph[i];
    if( curr == null ) {
      visd[i] = true;
    }
    while( curr != null ) {
      visd[i] = true;
      if(!visd[curr.getVal()]) {
        visd = dfs(curr.getVal(), visd);
      }
      curr = curr.getNext();
    }
    return visd;
  }

//same principal as two points (dfs based)
//if we can't visit all the Nodes
//using only copper wires it's not copper connected
  public boolean copperConnected() {
    boolean[] marked = new boolean[this.V];
    marked = copperConnected(0, marked);
    int i = 0;
    for(boolean bool: marked) {
      if( !bool ) return false;
    }
    return true;
  }

  private boolean[] copperConnected(int i, boolean[] marked) {
    Node curr = graph[i];
    while( curr != null ) {
      if(curr.getMat().equals("copper") && !marked[curr.getVal()]) {
        marked[curr.getVal()] = true;
        marked = copperConnected(curr.getVal(), marked);
      }
      curr = curr.getNext();
    }
    return marked;
  }

}

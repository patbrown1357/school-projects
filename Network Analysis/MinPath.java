public class MinPath {

//for determining shortest path
  int[] nodes;
  float[] distTo;
  boolean[] visd;
  AdjList graph;
  MinPQ smallest;

//for getting path + bandwidth
  int[] parent;
  int bndWdt;

//constructed from an AdjList
  public MinPath(AdjList newG) {
    nodes = new int[newG.V];
    distTo = new float[newG.V];
    visd = new boolean[newG.V];
    smallest = new MinPQ();
    graph = newG;
    parent = new int[newG.V];
  }

  public int getBndwt(){
      return bndWdt;
  }

//main PF function
  public void pathFinder(int i) {
    bndWdt = Integer.MAX_VALUE;
    String s = "";
    s = pathFinder(i, s);
    System.out.println(s);
  }

//PF helper function
//recursively build the path and locate smalles bandwidth
  private String pathFinder(int i, String s) {

    if( parent[i] == -1 ) {s += i + " "; return s;}
    Node curr = graph.graph[parent[i]];
    while( curr != null ) {
      if(bndWdt > curr.getBndwt()) {
        bndWdt = curr.getBndwt();
        break;
      }
      curr = curr.getNext();
    }
    s += pathFinder(parent[i], s) + i + " ";
    return s;
  }

//public shortest path
  public void getMin(int j, int k) {
    djiskstra(j,k);
  }

//helper shortest path
  private void djiskstra(int j, int k) {
    for( int i = 0; i<distTo.length; i++) {
      distTo[i] = Float.MAX_VALUE;
    }
    distTo[j] = 0;
    parent[j] = -1;
    visd[j] = true;
    smallest.add(graph.graph[j]);
    djsHelp(k);
    pathFinder(k);
  }

//helper djikstra function
  private void djsHelp(int dst) {

    while( !smallest.isEmpty()) {
      Node edge = smallest.poll();
      int j = edge.getSrc();
      Node curr = graph.graph[j];

      while( curr != null  ) {
        if( !visd[curr.getVal()]) {
          float dist = curr.getWeight();
          if( distTo[curr.getVal()] > dist + distTo[j])
          //for writing shortest path
            parent[curr.getVal()] = j;
            distTo[curr.getVal()] = dist + distTo[j];

          smallest.add(graph.graph[curr.getVal()]);
        }

        curr = curr.getNext();
      }
      visd[j] = true;
    }
  }



}

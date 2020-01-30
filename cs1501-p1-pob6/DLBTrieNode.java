public class DLBTrieNode<Value>
{
  //first char in tree
  private Value val;
  private boolean key;

  private DLBTrieNode child;
  private DLBTrieNode sibling;

  //default constructor
  public DLBTrieNode()
  {
    this.setVal(null);
    boolean key = false;
    child = null;
    sibling = null;
  }

  //char constructor
  public DLBTrieNode( Value x )
  {
    val = x;
    boolean key = false;
    child = null;
    sibling = null;
  }

  public Value getVal() { return this.val; }
  public boolean isKey() { return key; }
  public DLBTrieNode getChild() { return this.child;  }
  public DLBTrieNode getSib() { return this.sibling; }

  public void setVal(Value v) { this.val = v; }
  public void makeKey() { this.key = true; }
  public void setChild( DLBTrieNode n) { this.child = n; }
  public void setSib( DLBTrieNode n) { this.sibling = n; }

  public void makeChild( Value nodeVal )
  {
    this.child = new DLBTrieNode( nodeVal );
  }

  public void makeSib( DLBTrieNode pNode, Value nodeVal )
  {
    pNode.sibling = new DLBTrieNode( nodeVal );
  }


}

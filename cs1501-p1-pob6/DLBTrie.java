public class DLBTrie<Value>
{

  DLBTrieNode root = null;

  public DLBTrie()
  {
    this.root = null;
  }

  public DLBTrie( Value newVal )
  {
    this.root = new DLBTrieNode( newVal );
  }

  public DLBTrieNode getRoot()
  {
    if( this.root == null )
      return null;

    return this.root;
  }

  public void deleteRoot()
  {
    this.root = null;
  }

  //template for dfs traversal of DLBTrie
  public void traverse( DLBTrieNode root )
  {
    if( root == null ) return;
    //if( root.isKey() )
      System.out.println(root.getVal());
    travChild( root.getChild() );
    travSibling( root.getSib() );

  }

  private DLBTrieNode travChild( DLBTrieNode curr )
  {
    if( curr == null ) return null;
    traverse( curr );
    return root;
  }

  private DLBTrieNode travSibling( DLBTrieNode curr )
  {
    if( curr == null ) return null;
    traverse( curr );
    return root;
  }

  public void add( String key ) {

    if( key == null ) return;
    int i = 0;
    DLBTrieNode curr = this.root;

    root = add( curr, key, i );


  }

  private DLBTrieNode add( DLBTrieNode node, String key, int pos ) {

    if( key.length()-1 == pos ) {
      node = new DLBTrieNode( key.charAt(pos));
      node.makeKey();
      return node;
    }

    if( node == null ) {
      node = new DLBTrieNode( key.charAt(pos) );
      node.setChild( add( node.getChild(), key, pos+1 ));
      return node;
    }

    if( !node.getVal().equals( key.charAt(pos)) )
    {
      node.setSib( add( node.getSib(), key, pos ) );
      return node;
    } else {
      node.setChild( add( node.getChild(), key, pos+1 ) );

    }

    return node;


  }

  public void addString( String key ) {

    char[] keyArr = key.toCharArray();
    DLBTrieNode curr = root;
    if( curr == null ) {
      curr = new DLBTrieNode( key.charAt(0) );
      root = curr;
    }

    for( int i = 1; i < key.length(); i++)
    {
      curr.makeChild( key.charAt(i) );
      curr = curr.getChild();

    }

    curr.makeKey();

  }

  public void printTest() {
    DLBTrieNode curr = this.getRoot();
    if( curr == null )
      return;

    while( !curr.isKey() ) {
      System.out.println( curr.getVal() );
      curr = curr.getChild();

      if( curr == null ) return;
    }

    System.out.println( curr.getVal() );
  }

  public String test() { return " ";}


}

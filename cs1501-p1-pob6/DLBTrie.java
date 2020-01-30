public class DLBTrie<Value>
{

  DLBTrieNode root = null;

  public DLBTrie()
  {
    this.root = new DLBTrieNode();
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
  public DLBTrieNode traverse( DLBTrieNode root )
  {
    if( root == null ) return root;

    travChild( root.getChild() );
    travSibling( root.getSib() );

    return root;

  }

  private DLBTrieNode travChild( DLBTrieNode root )
  {
    if( root.getChild() == null ) return null;
    travChild( root.getChild() );
    return root;
  }

  private DLBTrieNode travSibling( DLBTrieNode root )
  {
    if( root.getSib() == null ) return null;
    travSibling( root.getSib() );
    return root;
  }

  public void add( String key ) {

    if( key == null ) return;

    char[] keyArr = key.toCharArray();

    if( this.root == null ) {
      //if this is null add a new child sib
      this.root = new DLBTrieNode( key.charAt(0) );
      add( root.getChild(), keyArr, key.length(), 1 );
    }

    //check next node
    add( root.getSib(), keyArr, key.length(), 1 );

  }
  private void add( DLBTrieNode root, char[] key, int length, int pos ) {

    if( length == pos ) {
      root.makeKey();
    }

    if( root.getChild() == null ) {
      root.setChild( new DLBTrieNode( key[pos] ) );
      add( root.getChild(), key, length, pos + 1 );
    }
  }

  public void addString( String key ) {

    char[] keyArr = key.toCharArray();
    DLBTrieNode curr = root;
    if( curr == null ) {
      curr = new DLBTrieNode( keyArr[0] );
      root = curr;
    }

    for( int i = 1; i < key.length(); i++)
    {
      curr.makeChild( keyArr[i]);
      curr = curr.getChild();

    }

    curr.makeKey();

  }

  public void printTest() {
    root = this.getRoot();
    if( root == null )
      return;

    while( !root.isKey() ) {
      System.out.println( root.getVal() );
      root = root.getChild();
    }

    System.out.println( root.getVal() );
  }


}

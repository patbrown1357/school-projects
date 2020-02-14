import java.io.*;
import java.util.*;


public class DLBTrie<Value>
{

  private DLBTrieNode root = null;

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
  public void traverse( DLBTrieNode curr )
  {
    if( curr == null ) return;

    traverse( curr.getChild() );
    traverse( curr.getSib() );

  }

  //recursively add strings to trie
  public void add( String key ) {

    if( key == null ) return;
    int i = 0;
    DLBTrieNode curr = this.root;

    root = add( curr, key, i );


  }

  //node = current node, key is whats added, position give the char
  //only step through key if going to a child
  private DLBTrieNode add( DLBTrieNode node, String key, int pos ) {


    if( key.length() == pos )  return node;
    if( node == null ) {

      node = new DLBTrieNode( key.charAt(pos) );
      node.setChild( add( node.getChild(), key, pos+1 ));
      if( key.length() -1 == pos ) {
        node.makeKey();
      }
      return node;
    }

    if( !node.getVal().equals( key.charAt(pos)) )
    {

      node.setSib( add( node.getSib(), key, pos ) );

    } else {

      node.setChild(add( node.getChild(), key, pos+1 ) );

    }

    return node;


  }

  //gets the value of a node and returns as string
  public String get( String key ) {

    DLBTrieNode isFound = get( this.root, key );
    if( isFound == null ) return null;

    String found = isFound.getVal().toString();

    return found;

  }

  //iterate down the tree, recursion unecessary
  private DLBTrieNode get( DLBTrieNode curr, String key ) {

      if( key.equals("")) return root;
      for( int i = 0; i < key.length(); i++ ) {

        if( curr == null ) return null;

        while( !curr.getVal().equals(key.charAt(i))) {
          curr = curr.getSib();
          if( curr == null ) return null;
        }

        if( i == key.length() - 1 ) return curr;
        curr = curr.getChild();

      }

      return null;

  }

  //get a ds containing all strings with a certain prefix
  private void collect( DLBTrieNode node, String pre, LinkedList<String> q) {


      if( node == null ) return;
      if( q.size() == 5 ) return;
      if( node.isKey() ) q.add( pre + node.getVal() );


      collect( node.getChild(), pre + node.getVal(), q );
      collect( node.getSib(), pre, q);




  }

  //public method uses collect to get strings, and returns ds
  public LinkedList<String> keysWithPrefix( String pre ) {
    LinkedList<String> q = new LinkedList<String>();

    DLBTrieNode start = get( root, pre );
    if( start == null ) return q;


    collect( get(root, pre).getChild(), pre, q );


    return q;

  }










}

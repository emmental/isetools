package ca.nines.ise.validator;

import java.util.LinkedList;

import ca.nines.ise.node.Node;

public class ValidatorStack<E extends Node> extends LinkedList<E>{
  
  /**
   * returns the head of the nodeStack
   * null if empty
   */
  public E get_head(){
    if (!this.isEmpty())
      return this.peekFirst();
    return null;
  }
  
  /**
   * returns the  name of the head of nodeStack
   * null if empty
   */
  public String get_head_name(){
    if (!this.isEmpty())
      return this.peekFirst().getName();
    return null;
  }
  
  /**
   * true if the given name is equal to the name at the head of nodeStack
   * false if stack is empty or head is not equal
   */
  public Boolean is_head_equal(String name){
    return get_head_name().toLowerCase().equals(name.toLowerCase());
  }
  
  /**
   * true if node at @i is equal to given name
   * false otherwise
   */
  public Boolean is_equal(int i, String name){
    if (this.get(i) != null)
      return this.get(i).getName().toLowerCase().equals(name.toLowerCase());
    return null;
  }
  
  /**
   * returns the first instance of @n's name in nodeStack
   * null if no occurence
   */
  public E get_first(Node n){
    for (int i=0; i<this.size(); i++){
      if (this.get(i).getName().toLowerCase().equals(n.getName().toLowerCase()))
        return this.get(i);
    }
    return null;
  }
  
}

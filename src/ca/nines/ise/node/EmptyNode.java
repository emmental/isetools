/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.ise.node;

import ca.nines.ise.node.Node;
import ca.nines.ise.node.TagNode;

/**
 *
 * @author Michael Joyce <ubermichael@gmail.com>
 */
public class EmptyNode extends TagNode {

  public EmptyNode() {
    super();
  }

  public EmptyNode(Node n) {
    super(n);
  }

  public EmptyNode(String tagname) {
    super(tagname);
  }

  @Override
  public NodeType type() {
    return NodeType.EMPTY;
  }

}

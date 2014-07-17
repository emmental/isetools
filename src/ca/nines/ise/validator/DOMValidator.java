/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.ise.validator;

import ca.nines.ise.validator.node.EmptyNodeValidator;
import ca.nines.ise.validator.node.CommentNodeValidator;
import ca.nines.ise.validator.node.AbbrNodeValidator;
import ca.nines.ise.validator.node.StartNodeValidator;
import ca.nines.ise.validator.node.CharNodeValidator;
import ca.nines.ise.validator.node.NodeValidator;
import ca.nines.ise.validator.node.EndNodeValidator;
import ca.nines.ise.validator.node.TextNodeValidator;
import ca.nines.ise.dom.DOM;
import ca.nines.ise.node.Node;
import ca.nines.ise.node.Node.NodeType;
import ca.nines.ise.schema.Schema;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * DOMValidator validates all the nodes in a DOM against a schema. All
 * validation messages are saved in Log.
 * <p>
 * Usage example:
 * <p>
 * <
 * pre> null {@code
 * DOM dom = new DOM();
 * Schema schema = new Schema(); // optional
 * DOMValidator validator = new DOMValidator(schema);
 * validator.validate(dom);
 * System.out.println(Log.getInstance());
 * }
 * </pre>
 * <p>
 * @author Michael Joyce <michael@negativespace.net>
 */
public class DOMValidator {

  /**
   * Mapping of node types to node validators.
   */
  private static final Map<NodeType, NodeValidator<? extends Node>> validators;

  static {
    validators = new HashMap<>();
    validators.put(NodeType.ABBR, new AbbrNodeValidator());
    validators.put(NodeType.CHAR, new CharNodeValidator());
    validators.put(NodeType.COMMENT, new CommentNodeValidator());
    validators.put(NodeType.EMPTY, new EmptyNodeValidator());
    validators.put(NodeType.END, new EndNodeValidator());
    validators.put(NodeType.START, new StartNodeValidator());
    validators.put(NodeType.TEXT, new TextNodeValidator());
  }

  /**
   * Perform all the schema and node validations.
   * <p>
   * @param dom <p>
   * @param schema
   * @throws Exception
   */
  @SuppressWarnings({"unchecked", "rawtypes"})
  public void validate(DOM dom, Schema schema) throws Exception {
    NodeValidator validator;
    for(Node n : dom) {
      validator = validators.get(n.type());
      if (validator == null) {
        throw new Exception("Unknown node type: " + n.type());
      }      
      validator.validate(n, schema);
    }
  }

}

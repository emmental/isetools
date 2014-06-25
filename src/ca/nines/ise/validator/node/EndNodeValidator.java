/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.ise.validator.node;

import ca.nines.ise.log.Message;
import ca.nines.ise.annotation.ErrorCode;
import ca.nines.ise.node.EndNode;
import ca.nines.ise.schema.Schema;
import ca.nines.ise.schema.Tag;

/**
 * Validate an end node.
 * 
 * Validations performed:
 * 
 * <ul>
 * <li>The tag is defined in the schema.</li>
 * <li>The tag is not required to be empty.</li>
 * </ul>
 * 
 * WARNING: Depreciated end tags are not reported.
 *
 * @author Michael Joyce <ubermichael@gmail.com>
 */
public class EndNodeValidator extends TagNodeValidator<EndNode> {

  /**
   * Construct an end node validator.
   * 
   * @param schema The schema for validation.
   */
  public EndNodeValidator(Schema schema) {
    super(schema);
  }

  @ErrorCode(code = {
    "validator.tag.unknown",
    "validator.tag.endempty",
  })
  @Override
  public void validate(EndNode n) {
    Tag t = schema.getTag(n.getName());
    if (t == null) {
      Message m = log.error("validator.tag.unknown", n);
      m.addNote("Tag " + n.getName() + " is not defined in the schema.");
      return;
    }
    if (t.isEmpty()) {
      Message m = log.error("validator.tag.endempty", n);
      m.addNote("End tag " + n.getName() + " should not occur.");
    }
  }

}

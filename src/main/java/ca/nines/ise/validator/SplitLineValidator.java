package ca.nines.ise.validator;

import java.util.ArrayDeque;

import ca.nines.ise.annotation.ErrorCode;
import ca.nines.ise.dom.DOM;
import ca.nines.ise.log.Log;
import ca.nines.ise.log.Message;
import ca.nines.ise.node.EmptyNode;
import ca.nines.ise.node.EndNode;
import ca.nines.ise.node.Node;
import ca.nines.ise.node.StartNode;
import ca.nines.ise.schema.Schema;

public class SplitLineValidator {
  public SplitLineValidator() {}
  
EmptyNode current;

@ErrorCode(code = {
    "validator.splitLine.recursive"
})
private void process_start(EmptyNode n) {
    if (current != null) {
        Message m = Message.builder("validator.splitLine.recursive")
               .fromNode(n)
               .addNote(get_name(n) + " cannot start a new split line (part=\"i\") before the current one ("+get_name(current)+" @ TLN="+current.getTLN()+") is finished")
               .build();
        Log.addMessage(m);
    }
    else
      current = n;
}

@ErrorCode(code = {
    "validator.splitLine.notStartedM"
})
private void process_middle(EmptyNode n) {
  if (current == null){
    Message m = Message.builder("validator.splitLine.notStartedM")
        .fromNode(n)
        .addNote(get_name(n) + " has a part=\"m\" but no matching \"i\" L tag")
        .build();
    Log.addMessage(m);
  }
}

@ErrorCode(code = {
  "validator.splitLine.notStartedF"
})
private void process_finish(EmptyNode n) {
  if (current == null){
    Message m = Message.builder("validator.splitLine.notStartedF")
            .fromNode(n)
            .addNote(get_name(n) + " has a part=\"f\" but no matching \"i\" L tag")
            .build();
    Log.addMessage(m);
  }else 
    current = null;
}



private String get_name(EmptyNode n){
  if (n.hasAttribute("n"))
    return "L n="+n.getAttribute("n")+"";
  else
    return "";
}

@ErrorCode(code = {
  "validator.splitLine.unclosed"
})
public void validate(DOM dom) {
  current = null;

  for (Node n : dom) {
    if (n.getName().toLowerCase().equals("l")){
      EmptyNode en = (EmptyNode) n;
      if (en.hasAttribute("part")){
        switch (en.getAttribute("part")) {
          case "i":
            process_start(en);
            break;
          case "m":
            process_middle(en);
            break;
          case "f":
            process_finish(en);
        }
      }
    } 
  }

  if (current != null) {
    Message m = Message.builder("validator.splitLine.unclosed")
            .fromNode(current)
            .addNote(get_name(current) + " has a part=\"i\" but no matching \"f\" L tag")
            .build();
    Log.addMessage(m);
  }

}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.ise.document;

import ca.nines.ise.node.lemma.Coll;
import java.io.File;

/**
 *
 * @author michael
 */
public class Collations extends Apparatus<Coll> {

  Collations(File collationsFile) {
    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
  }

  @Override
  public String nodeXpath() {
    return "collations";
  }

  @Override
  public String rootXPath() {
    return "coll";
  }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.ise;

import ca.nines.ise.dom.DOM;
import ca.nines.ise.dom.DOMBuilder;
import ca.nines.ise.log.Log;
import ca.nines.ise.output.XMLOutput;        

/**
 *
 * @author michael
 */
public class Tester {

  public static void main(String[] args) {
    Log log = Log.getInstance();
    try {
      DOM dom = new DOMBuilder("<a a='3'>hello<b>yes</b><ok></ok> world.</a>").build();
      XMLOutput out = new XMLOutput();
      out.render(dom);
    } catch (Exception ex) {
      System.err.println(ex.getMessage());
      ex.printStackTrace(System.err);
    }
    System.out.println(log);
  }
}

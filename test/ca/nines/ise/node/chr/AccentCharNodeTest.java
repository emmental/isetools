/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.ise.node.chr;

import ca.nines.ise.dom.Fragment;
import ca.nines.ise.node.CharNode;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
public class AccentCharNodeTest {

  public AccentCharNodeTest() {
  }

  @BeforeClass
  public static void setUpClass() {
  }

  @AfterClass
  public static void tearDownClass() {
  }

  @Before
  public void setUp() {
  }

  @After
  public void tearDown() {
  }

  /**
   * Test of getCharType method, of class AccentCharNode.
   */
  @Test
  public void testGetCharType() {
    System.out.println("getCharType");
    AccentCharNode instance = new AccentCharNode();
    CharNode.CharType expResult = null;
    CharNode.CharType result = instance.getCharType();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

  /**
   * Test of expanded method, of class AccentCharNode.
   */
  @Test
  public void testExpanded() {
    System.out.println("expanded");
    AccentCharNode instance = new AccentCharNode();
    Fragment expResult = null;
    Fragment result = instance.expanded();
    assertEquals(expResult, result);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
  }

}
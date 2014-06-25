/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.ise.util;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author michael
 */
public class XMLResourceReaderTest {

  /**
   * Test constructor with a file.
   * <p>
   * @throws ParserConfigurationException
   * @throws SAXException
   * @throws IOException
   * @throws XPathExpressionException
   */
  @Test
  public void testConstructorFile() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
    File f = new File("/resources/data/test-schema.xml");
    XMLResourceReader xmlIn = new XMLResourceReader(f);

    Node n = xmlIn.xpathNode("/schema");
    assertEquals("schema", n.getNodeName());
    assertEquals("test", xmlIn.xpathString("/schema/@edition"));
  }

  /**
   * Test of xpathList method, of class XMLResourceReader.
   * <p>
   * @throws javax.xml.parsers.ParserConfigurationException
   * @throws org.xml.sax.SAXException
   * @throws java.io.IOException
   * @throws javax.xml.xpath.XPathExpressionException
   */
  @Test
  public void testXpathList_String() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
    XMLResourceReader xmlIn;
    Node[] nl;

    xmlIn = new XMLResourceReader("<foo><bar n='1'/><bar n='2'/></foo>");
    nl = xmlIn.xpathList("/foo/bar");
    assertEquals(2, nl.length);
  }

  /**
   * Test of xpathList method, of class XMLResourceReader.
   * <p>
   * @throws javax.xml.parsers.ParserConfigurationException
   * @throws org.xml.sax.SAXException
   * @throws java.io.IOException
   * @throws javax.xml.xpath.XPathExpressionException
   */
  @Test
  public void testXpathNode_String() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
    XMLResourceReader xmlIn;
    Node n;

    xmlIn = new XMLResourceReader("<foo><bar n='1'/><qux n='2'/></foo>");
    n = xmlIn.xpathNode("/foo/node()[position() = 1]");

    assertEquals("bar", n.getNodeName());

    n = xmlIn.xpathNode("/foo/barley");
    assertEquals(null, n);
  }

  /**
   * Test of xpathList method, of class XMLResourceReader.
   * <p>
   * @throws javax.xml.parsers.ParserConfigurationException
   * @throws org.xml.sax.SAXException
   * @throws java.io.IOException
   * @throws javax.xml.xpath.XPathExpressionException
   */
  @Test
  public void testXpathNode_String_Node() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
    XMLResourceReader xmlIn;
    Node a, n;

    xmlIn = new XMLResourceReader("<foo><a><bar n='1'/><qux n='2'/></a></foo>");
    a = xmlIn.xpathNode("/foo/a");
    assertNotNull(a);
    n = xmlIn.xpathNode("*[@n=1]", a);
    assertNotNull(n);
    assertEquals("bar", n.getNodeName());
    n = xmlIn.xpathNode("*[@n=5]", a);
    assertNull(n);
  }

  /**
   * Test of xpathString method, of class XMLResourceReader.
   * <p>
   * @throws javax.xml.parsers.ParserConfigurationException
   * @throws org.xml.sax.SAXException
   * @throws java.io.IOException
   * @throws javax.xml.xpath.XPathExpressionException
   */
  @Test
  public void testXpathString_String() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
    XMLResourceReader xmlIn;

    xmlIn = new XMLResourceReader("<foo bar='3'/>");
    assertEquals("3", xmlIn.xpathString("/foo/@bar"));
    assertEquals("", xmlIn.xpathString("/foo/@blorp"));
  }

  /**
   * Test of xpathString method, of class XMLResourceReader.
   * <p>
   * @throws javax.xml.parsers.ParserConfigurationException
   * @throws org.xml.sax.SAXException
   * @throws java.io.IOException
   * @throws javax.xml.xpath.XPathExpressionException
   */
  @Test
  public void testXpathString_String_Node() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException {
    XMLResourceReader xmlIn;

    xmlIn = new XMLResourceReader("<foo><bar a='3'/></foo>");
    Node n = xmlIn.xpathNode("/foo/bar");

    assertEquals("3", xmlIn.xpathString("@a", n));
    assertEquals("", xmlIn.xpathString("@blorp", n));
  }
}
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.ise.log;

import ca.nines.ise.util.XMLDriver;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author michael
 */
public class ErrorCodesTest {

  private final String data = ""
          + "<messages>\n"
          + "  <severities>\n"
          + "    <severity id=\"fatal\">An error occured in the application, processing cannot continue.</severity>\n"
          + "    <severity id=\"error\">An unrecoverable error in the document. Output cannot be processed.</severity>\n"
          + "    <severity id=\"warning\">A recoverable error was discovered in the document. Processing can continue.</severity>\n"
          + "    <severity id=\"unknown\">An unknown or unanticipated error occured in the application. Processing cannot continue.</severity>\n"
          + "  </severities>\n"
          + "  <errorCodes>\n"
          + "    <message code=\"test.error\" severity=\"error\">\n"
          + "      error code\n"
          + "    </message>\n"
          + "    <message code=\"test.warning\" severity=\"warning\">\n"
          + "      warning code\n"
          + "    </message>\n"
          + "    <message code=\"test.fatal\" severity=\"fatal\">\n"
          + "      fatal code\n"
          + "    </message>\n"
          + "    <message code=\"test.unknown\" severity=\"unknown\">\n"
          + "      unknown code\n"
          + "    </message>\n"
          + "  </errorCodes>\n"
          + "</messages>\n";

  @Test
  public void testBuilderDefaults() {
    ErrorCodes c = ErrorCodes.builder().build();
    assertEquals(c.size(), 1);
    ErrorCode e = c.getErrorCode("unknown");
    assertNotNull(e);
    assertEquals(e.getCode(), "unknown");
  }

  public void testBuilderSetters() {
    ErrorCode a;
    a = ErrorCode.builder().setCode("hello").build();
    ErrorCodes c = ErrorCodes.builder().addErrorCode(a).build();

    assertEquals(c.size(), 2);
    a = c.getErrorCode("unknown");
    assertNotNull(a);
    assertEquals(a.getCode(), "unknown");

    a = c.getErrorCode("hello");
    assertNotNull(a);
    assertEquals(a.getCode(), "unknown");
  }

  @Test
  public void testBuilderString() throws SAXException, TransformerException, ParserConfigurationException {
    ErrorCodes ec = ErrorCodes.builder().from(data).build();
    assertEquals(ec.size(), 5);
  }

  @Test
  public void testBuilderFile() throws IOException, TransformerException, SAXException, ParserConfigurationException {
    File file = new File("test/resources/data/test-errors.xml");
    ErrorCodes ec = ErrorCodes.builder().from(file).build();
    assertEquals(ec.size(), 5);
  }

  @Test
  public void testBuilderNode() throws ParserConfigurationException, TransformerConfigurationException, SAXException, TransformerException {
    Document doc = new XMLDriver().drive(data);
    Node n = doc.getElementsByTagName("errorCodes").item(0);
    ErrorCodes ec = ErrorCodes.builder().from(n).build();
    assertEquals(ec.size(), 5);
  }

  @Test
  public void testBuilderStream() throws ParserConfigurationException, TransformerConfigurationException, SAXException, TransformerException, IOException {
    String loc = "/resources/data/test-errors.xml";
    InputStream stream = ErrorCodesTest.class.getResourceAsStream(loc);
    ErrorCodes ec = ErrorCodes.builder().from(loc, stream).build();
    assertEquals(ec.size(), 5);
  }

  /**
   * Test of defaultErrorCodes method, of class ErrorCodes.
   */
  @Test
  public void testDefaultErrorCodes() throws ParserConfigurationException, TransformerConfigurationException, SAXException, TransformerException, IOException {
    ErrorCodes def = ErrorCodes.defaultErrorCodes();
    assertTrue(def.size() > 1);
  }

}

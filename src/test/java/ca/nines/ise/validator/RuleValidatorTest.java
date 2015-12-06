/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.ise.validator;

import ca.nines.ise.dom.DOM;
import ca.nines.ise.dom.DOMBuilder;
import ca.nines.ise.log.Log;
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
public class RuleValidatorTest {
	
	Log log;
	
	public RuleValidatorTest() {
	}
	
	@BeforeClass
	public static void setUpClass() {
	}
	
	@AfterClass
	public static void tearDownClass() {
	}
	
	@Before
	public void setUp() {
		log = Log.getInstance();
		log.clear();
	}
	
	@After
	public void tearDown() {
	}

	/**
	 * Test of validate method, of class RuleValidator.
	 * @throws java.lang.Exception
	 */
	@Test
	public void testValidate() throws Exception {
		System.out.println("validate");
		DOM dom = new DOMBuilder("<RULE/>").build();
		RuleValidator instance = new RuleValidator();
		instance.validate(dom);
		assertEquals(0, log.count());
	}
	
	/**
	 * Test of validate method, of class RuleValidator.
	 * @throws java.lang.Exception
	 */
	@Test
	public void testValidateBad() throws Exception {
		System.out.println("validate");
		DOM dom = new DOMBuilder("<RULE/> content ").build();
		RuleValidator instance = new RuleValidator();
		instance.validate(dom);
		assertEquals(1, log.count());
		assertEquals("validator.rule.linenotempty", log.get(0).getCode());
	}
	
	/**
	 * Test of validate method, of class RuleValidator.
	 * @throws java.lang.Exception
	 */
	@Test
	public void testValidateTags() throws Exception {
		System.out.println("validate");
		DOM dom = new DOMBuilder("<tln/><RULE/><foo><bar>").build();
		RuleValidator instance = new RuleValidator();
		instance.validate(dom);
		assertEquals(0, log.count());
	}
	
	/**
	 * Test of validate method, of class RuleValidator.
	 * @throws java.lang.Exception
	 */
	@Test
	public void testValidateComment() throws Exception {
		System.out.println("validate");
		DOM dom = new DOMBuilder("<RULE/><!-- comment -->").build();
		RuleValidator instance = new RuleValidator();
		instance.validate(dom);
		assertEquals(0, log.count());
	}
	
	/**
	 * Test of validate method, of class RuleValidator.
	 * @throws java.lang.Exception
	 */
	@Test
	public void testValidateSpaceChars() throws Exception {
		System.out.println("validate");
		DOM dom = new DOMBuilder("<RULE/>{ }{#}").build();
		RuleValidator instance = new RuleValidator();
		instance.validate(dom);
		assertEquals(0, log.count());
	}
	
	/**
	 * Test of validate method, of class RuleValidator.
	 * @throws java.lang.Exception
	 */
	@Test
	public void testValidateContentChars() throws Exception {
		System.out.println("validate");
		DOM dom = new DOMBuilder("<RULE/>{s}{ffl}").build();
		RuleValidator instance = new RuleValidator();
		instance.validate(dom);
		assertEquals(1, log.count());
	}

}

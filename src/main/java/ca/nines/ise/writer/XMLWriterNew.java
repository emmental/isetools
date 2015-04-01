package ca.nines.ise.writer;


import ca.nines.ise.document.Annotation;
import ca.nines.ise.dom.DOM;
import ca.nines.ise.node.EmptyNode;
import ca.nines.ise.node.EndNode;
import ca.nines.ise.node.Node;
import ca.nines.ise.node.StartNode;
import ca.nines.ise.constants.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Stack;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Text;

public class XMLWriterNew extends Writer{
	
	  public class  Lines extends Stack<ImmutablePair<String,Integer>>{
		  
		  public String peek_type(){
			  return (String)super.peek().getLeft();
		  }
		  
		  public int peek_count(){
			  return (int)super.peek().getRight();
		  }
		  
		  private void dec(){
			  change_line_head(-1);
		  }
		  
		  private void inc(){
			  change_line_head(1);
		  }
		  private void change_line_head(int n){
			  String left = (String)super.peek().getLeft();
			  int right = (int)super.pop().getRight();
			  super.push(new ImmutablePair<String, Integer>(left,right+n));
		  }
		  
		  private void new_element(){
			  if (!super.empty())
				  inc();
		  }
		  
		  private void rm_element(){
			  if (!super.empty())
				  dec();
		  }
	  }
	  
	  public class XMLStack extends ArrayDeque<Element>{
		  	private Boolean endSplitLine;
		  	private String align;
		  	Document xml;
		  	Lines line;
		  	
		  	public XMLStack() throws ParserConfigurationException{
		  		endSplitLine = false;
		  		align = null;
		  		xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		  		line = new Lines();
		  	}
		  
			public void new_line(EmptyNode node){
				new_line(IML.LINE);
				endSplitLine = false;
		    	for (String name : node.getAttributeNames()) {
		    		//only attribute we care about
		    		if (name.equals("part")){
		    			if (node.getAttribute(name).equals("i")){
		    				//new splitline element
		    				Element e = xml.createElement("splitline");
		        			super.peekFirst().appendChild(e);
		        			super.push(e);
		    			}else if (node.getAttribute(name).equals("f")){
		    				//close splitline element
		    				//flag that splitline is closing on next line close
		    				endSplitLine = true;
		    			}
		    		}
		    	}
		    	Element e = xml.createElement("l");
		    	if (align != null)
		    		e.setAttribute("align", align);
		    	super.peekFirst().appendChild(e);
		    	super.push(e);
			}
			
			public void end_line(){
				line.pop();
				super.pop();
				//if ending splitline, pop that as well
				if (endSplitLine)
					super.pop();		    
			}
			
			public void end_page(){
				line.pop();
				super.pop();	    
			}
			
			private void new_ms_element(String ln, String n){
		    	Element e = xml.createElement("ms");
		    	e.setAttribute("t", ln);
		    	e.setAttribute("n", n);
		    	super.peekFirst().appendChild(e);
			}
	   		
			//if not in a line, but should be, push new line
	    	private void new_element(StartNode node){
				if (Arrays.asList(IML.LINE_CHILDREN).contains(node.getName()) && !in_line())
		   			new_line(new EmptyNode());
				line.new_element();
	    	}    	
	    	//if closing node that's not a child of current line, close line
	    	private void rm_element(EndNode node){    		
		    	if (peekFirst().getNodeName().equals(IML.LINE))
		    		end_line();
		    	line.rm_element();
	    	}
	    	public boolean in_line(){
	    		if (!line.empty())
	    			return line.peek_type().equals(IML.LINE);
				return false;
	    	}
			  
	    	public boolean in_line_element(){
	    		return !line.empty();
	    	}
			  
	    	public boolean in_page(){
	    		if (!line.empty())
	    			return line.peek_type().equals(IML.PAGE);
	    		return false;
	    	}
				
	    	public boolean in_line_child(){
	    		if (!line.empty())
	    			return (int)line.peek().getRight() != 0;	
	    		return false;
	    	}
	    	public void new_line(String type){
	    		line.push(new ImmutablePair<String, Integer>(type, 0));
	    	}
	  }
	  
	  /**
	   * Construct an XMLWriter and send output to System.out.
	   *
	   * @throws ParserConfigurationException
	   * @throws UnsupportedEncodingException
	   */
	  public XMLWriterNew() throws ParserConfigurationException, UnsupportedEncodingException {
	    this(new PrintStream(System.out, true, "UTF-8"));
	  }

	  /**
	   * Construct an XMLWriter and send the output to the print stream.
	   *
	   * @param out the output destination.
	   *
	   * @throws ParserConfigurationException
	   * @throws UnsupportedEncodingException
	   */
	  public XMLWriterNew(PrintStream out) throws ParserConfigurationException, UnsupportedEncodingException {
	    super(out);
	  }
	  
	@Override
	public void render(DOM dom) throws TransformerConfigurationException, TransformerException, IOException, Exception {
	    // @TODO check if the DOM is expanded, and expand if necessary.
		XMLStack xmlStack = new XMLStack();
		
	    Element e = xmlStack.xml.createElement("root");
	    xmlStack.xml.appendChild(e);
	    xmlStack.push(e);
	
	    for (Node n : dom.expanded()) {
	    	switch (n.type()) {
	        case ABBR:
	        	throw new UnsupportedOperationException("Cannot serialize depreciated abbreviation markup.");
	        case COMMENT:
	        	Comment c = xmlStack.xml.createComment(n.getText());
	        	xmlStack.peekFirst().appendChild(c);
	        	break;
	        case EMPTY:
	        	EmptyNode emptyNode = (EmptyNode) n;
	        	Element emptyElement = xmlStack.xml.createElement(emptyNode.getName().toLowerCase());
	        	if (parse_empty(emptyNode, emptyElement, xmlStack))
	        		break;
	        	for (String name : emptyNode.getAttributeNames()) {
	        		emptyElement.setAttribute(name, emptyNode.getAttribute(name));
	        	}
	        	xmlStack.peekFirst().appendChild(emptyElement);
	        	break;
	        case END:
	        	EndNode endNode = (EndNode) n;
	        	if (parse_end(endNode, xmlStack))
	        		break;
	        	xmlStack.pop();
	       		break;
	      	case START:
	       		StartNode startNode = (StartNode) n;
	       		Element startElement = xmlStack.xml.createElement(startNode.getName().toLowerCase());	       		
	       		if (parse_start(startNode, startElement, xmlStack))
	       			break;
	       		for (String name : startNode.getAttributeNames()) {
		       		startElement.setAttribute(name, startNode.getAttribute(name));
		       	}
	       		xmlStack.peekFirst().appendChild(startElement);
	       		xmlStack.push(startElement);
	       		break;
	       	case TEXT:
	       		if (parse_text(n.getText(), xmlStack))
	       			break;
	       		Text t = xmlStack.xml.createTextNode(n.getText());
	       		xmlStack.peekFirst().appendChild(t);
	       		break;
	       	default:
	       		throw new Exception("Cannot convert " + n.getName() + " to XML");
	    	}
	    }    
	    output(xmlStack.xml);
	}
	
	private Boolean parse_text(String text, XMLStack xmlStack){
		if (text.trim().isEmpty() || xmlStack.in_page())
			return false;
		if (!xmlStack.in_line())
   			xmlStack.new_line(new EmptyNode());
		String[] lines = null;
		//if there is no newline character, return false and handle normally
		if ((lines = text.split("\n")).length == text.length())
			return false;
		for(int i=0; i<lines.length; i++){
			if (lines[i].isEmpty())
				continue;
			//if not in a line, start one
			if (!xmlStack.in_line_element())
				xmlStack.new_line(new EmptyNode());
			xmlStack.peekFirst().appendChild(xmlStack.xml.createTextNode(lines[i]));
			//if in a line and it is not the last line of this text node, end line
			if (xmlStack.in_line_element() && i < (lines.length -1))
				xmlStack.end_line();
		}
		return true;
	}
	
	private String get_alignment(String str){
		return XML.ALIGNMENT[Arrays.asList(IML.ALIGNMENT).indexOf(str)];
	}
	
	private Boolean parse_start(StartNode node, Element e, XMLStack xmlStack){
    	xmlStack.new_element(node);
  		
   		switch (node.getName().toUpperCase()){
   		case IML.WORK:
   			e.setAttribute("xmlns", "http://internetshakespeare.uvic.ca/exist/rest/db/apps/iseapp/content/schema/text/documentClass");
   			return false;
   		//alignments
   		case IML.RA:
   		case IML.C:
   		case IML.J:
			xmlStack.align = get_alignment(node.getName());
			if (xmlStack.in_line())
				xmlStack.end_line();
			xmlStack.new_line(new EmptyNode());	        			
			return true;
   		case IML.COL: //column
   	    	Element e_col = xmlStack.xml.createElement("col");
   	    	xmlStack.peekFirst().appendChild(e_col);
   	    	return true;
   		case IML.ISEHEADER:
   			return true;
   		case IML.SP:
   			if (!xmlStack.peekFirst().getTagName().equals("l"))
   				xmlStack.new_line(new EmptyNode());
   			if (!xmlStack.peekFirst().getTagName().equals("s")){
	   	   		Element e_s = xmlStack.xml.createElement("s");
	   	       	xmlStack.peekFirst().appendChild(e_s);
	   	       	xmlStack.push(e_s);
   			}
   			return false;
   		case IML.TITLEHEAD:
   			if (!xmlStack.peekFirst().getTagName().equals("l"))
   				xmlStack.new_line(new EmptyNode());
   	    	Element e_title = xmlStack.xml.createElement("title");
   	    	xmlStack.peekFirst().appendChild(e_title);
   	    	xmlStack.push(e_title);
   			return true;
   		case IML.PAGE:
   			xmlStack.new_line(IML.PAGE);
   			return false;
   		case IML.LINEGROUP:
       		for (String name : node.getAttributeNames()) {
       			if (name.equals("form"))
       				e.setAttribute("metric", node.getAttribute("form"));
       			else
       				e.setAttribute(name, node.getAttribute(name));
	       	}
   			xmlStack.peekFirst().appendChild(e);
   			xmlStack.push(e);
        	return true;
   		case IML.QUOTE:
   			//if not currently in a line element, assume quoting a line element
   			Element e_quote;
   			if (!xmlStack.in_line_element())
   				e_quote = xmlStack.xml.createElement("quote");
   			else
   				e_quote = xmlStack.xml.createElement("q");
       		for (String name : node.getAttributeNames())
       			e_quote.setAttribute(name, node.getAttribute(name));
   			xmlStack.peekFirst().appendChild(e_quote);
   			xmlStack.push(e_quote); 
   			return true;
   		}
   		return false;
	}
	
	private Boolean parse_end(EndNode node, XMLStack xmlStack){
    	xmlStack.rm_element(node);
    	
   		switch (node.getName().toUpperCase()){
   		//alignments
   		case IML.RA:
   		case IML.C:
   		case IML.J:
   			if (xmlStack.in_line())
   				xmlStack.end_line();	
    		return true;
   		case IML.COL: //column
   	    	return true;
   		case IML.ISEHEADER:
   			return true;
   		case IML.SP:
   			return false;
   		case IML.PAGE:
   			if (xmlStack.in_page())
   				xmlStack.end_page();
   			return true;
   		case IML.LINEGROUP:
   			//linegroup can't be empty; default insert an empty line if so
   			if (!xmlStack.peekFirst().equals(IML.LINEGROUP) && !xmlStack.peekFirst().hasChildNodes()){
   				xmlStack.new_line(new EmptyNode());
   				xmlStack.end_line();
   			}
   			return false;
   		case IML.MARG:
   			//marg can't be empty; default insert an empty line if so
   			if (!xmlStack.peekFirst().equals(IML.MARG) && !xmlStack.peekFirst().hasChildNodes()){
   				xmlStack.new_line(new EmptyNode());
   				xmlStack.end_line();
   			}
   			return false;
   		}
    	return false;
	}
	
	private boolean parse_empty(EmptyNode node, Element e, XMLStack xmlStack){
   		switch (node.getName().toUpperCase()){
   		case IML.LINE:
        	//line can't be a child of a line, so close if not in a child of line
        	if (xmlStack.in_line() && !xmlStack.in_line_child())
        		xmlStack.end_line();
        	xmlStack.new_line(node);
        	return true;
   		case IML.QLN:
   			if (!xmlStack.in_line())
   				xmlStack.new_line(new EmptyNode());
   			xmlStack.new_ms_element("qln", node.getAttribute("n"));
   			return true;
   		case IML.WLN:
   			if (!xmlStack.in_line())
   				xmlStack.new_line(new EmptyNode());
   			xmlStack.new_ms_element("wln", node.getAttribute("n"));
   			return true;
   		case IML.TLN:
   			if (!xmlStack.in_line())
   				xmlStack.new_line(new EmptyNode());
   			xmlStack.new_ms_element("tln", node.getAttribute("n"));
   			return true;
   		case IML.LINK:
   			return true;
   		case IML.META:
   			return true;
   		case IML.RULE:
   			if (node.hasAttribute("n"))
   				e.setAttribute("l", node.getAttribute("n"));
   			xmlStack.peekFirst().appendChild(e);
        	return true;
		case IML.SPACE:
   			if (!xmlStack.in_line())
   				xmlStack.new_line(new EmptyNode());
   			if (node.hasAttribute("n"))
   				e.setAttribute("l", node.getAttribute("n"));
   			xmlStack.peekFirst().appendChild(e);
        	return true;
		case IML.BR:
   			if (xmlStack.in_line())
   				xmlStack.end_line();
   			xmlStack.new_line(new EmptyNode());
   			return true;	
   		}
   		return false;
	}
	
	private void output(Document xml){
	    try {    	
	        Transformer transformer = TransformerFactory.newInstance().newTransformer();
	        transformer.setOutputProperty(OutputKeys.INDENT, "no");
	        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        DOMSource source = new DOMSource(xml);
	        
	        //temporary stream
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	        PrintStream ps = new PrintStream(baos);
	        StreamResult stream = new StreamResult(ps);
	        transformer.transform(source, stream);
	        
	        //remove root from ps
	        String str = remove_root(baos.toString());
	        //System.out.println(str);
	        //add version & encoding to out stream
	        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");      
	        //print rest without root node
	        out.print(str);
	      } catch (Exception ex) {
	        ex.printStackTrace();
	      }		
	}
	
	private String remove_root(String str){
		return str.replaceAll("\\<root>|</root>", "");
	}

	@Override
	public void render(DOM dom, Annotation ann)
			throws TransformerConfigurationException, TransformerException,
			IOException, Exception {
		// TODO Auto-generated method stub
		
	}

}

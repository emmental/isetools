/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.ise.dom;

import ca.nines.ise.node.chr.AccentCharNode;
import ca.nines.ise.node.chr.UnicodeCharNode;
import ca.nines.ise.node.chr.LigatureCharNode;
import ca.nines.ise.node.chr.TypographicCharNode;
import ca.nines.ise.node.chr.DigraphCharNode;
import ca.nines.ise.node.chr.NestedCharNode;
import ca.nines.ise.node.chr.SpaceCharNode;
import ca.nines.ise.node.StartNode;
import ca.nines.ise.node.EndNode;
import ca.nines.ise.node.EmptyNode;
import ca.nines.ise.grammar.ISELexer;
import ca.nines.ise.grammar.ISEParser;
import ca.nines.ise.grammar.ISEParser.*;

import ca.nines.ise.grammar.ISEParserBaseListener;
import ca.nines.ise.log.Log;
import ca.nines.ise.node.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 * Builder creates a DOM from from either a File or a String. The class itself
 * extends ISEParserBaseListener, which is automatically generated by Antlr.
 * Errors are handled by ParserErrorListener.
 * <p>
 * Sample usage:
 * <p>
 * <blockquote><pre><code>
 * File file = new File("/path/to/file");
 * Builder builder = new Builder(file);
 * DOM dom = builder.getDOM();
 * </code></pre></blockquote></p>
 * <p>
 * Or
 * <p>
 * <blockquote><pre><code>
 * String input = "<WORK>Hello world.</WORK>";
 * Builder builder = new Builder(input);
 * DOM dom = builder.getDOM();
 * </code></pre></blockquote></p>
 * <p>
 * <p>
 * @see ParserErrorListener
 * <p>
 * @author michael
 */
public class Builder extends ISEParserBaseListener {

  private final ANTLRInputStream ais;
  private String currentAttrName;

  private TagNode currentTag;
  private final DOM dom = new DOM();
  private TokenStream tokens;

  /**
   *
   * @param in
   * @param source <p>
   * @throws java.io.IOException
   */
  public Builder(InputStream in, String source) throws IOException {
    DOMStream domStream = new DOMStream(in, source);
    dom.setSource(source);
    dom.setLines(domStream.getLines());
    ais = new ANTLRInputStream(domStream.getContent());
  }

  /**
   * Constructs a Builder from a string. The resulting DOM source will be
   * "#STRING".
   * <p>
   * @param in The string to parse.
   * <p>
   * @throws java.io.IOException
   */
  public Builder(String in) throws IOException {
    DOMStream domStream = new DOMStream(in);
    dom.setSource("#STRING");
    dom.setLines(domStream.getLines());
    ais = new ANTLRInputStream(domStream.getContent());
  }

  /**
   * Constructs a Builder from a File. The resulting DOM source will return the
   * absolute path to the file.
   * <p>
   * @param in The file to read and parse.
   * <p>
   * @throws FileNotFoundException if the file cannot be found.
   * @throws IOException           if the file cannot be read.
   */
  public Builder(File in) throws FileNotFoundException, IOException {
    DOMStream domStream = new DOMStream(in);
    dom.setSource(in.getCanonicalPath());
    dom.setLines(domStream.getLines());
    ais = new ANTLRInputStream(domStream.getContent());
  }

  @Override
  public void enterAbbr(AbbrContext ctx) {
    AbbrNode n = (AbbrNode) setupNode(new AbbrNode(), ctx);
    dom.add(n);
  }

  @Override
  public void enterAttributeName(AttributeNameContext ctx) {
    currentAttrName = ctx.TAG_NAME().getText();
  }

  @Override
  public void enterAttributeValue(AttributeValueContext ctx) {
    String value = ctx.ATTRIBUTE_VALUE().getText();
    value = value.replaceAll("^['\"]|['\"]$", "");
    currentTag.setAttribute(currentAttrName, value);
    currentAttrName = null;
  }

  @Override
  public void enterCharAccent(CharAccentContext ctx) {
    AccentCharNode n = (AccentCharNode) setupNode(new AccentCharNode(), ctx);
    dom.add(n);
  }

  @Override
  public void enterCharNested(CharNestedContext ctx) {
    NestedCharNode n = (NestedCharNode) setupNode(new NestedCharNode(), ctx);
    dom.add(n);
  }

  @Override
  public void enterCharDigraph(CharDigraphContext ctx) {
    DigraphCharNode n = (DigraphCharNode) setupNode(new DigraphCharNode(), ctx);
    dom.add(n);
  }

  @Override
  public void enterCharLigature(CharLigatureContext ctx) {
    LigatureCharNode n = (LigatureCharNode) setupNode(new LigatureCharNode(), ctx);
    dom.add(n);
  }

  @Override
  public void enterCharSpace(CharSpaceContext ctx) {
    SpaceCharNode n = (SpaceCharNode) setupNode(new SpaceCharNode(), ctx);
    dom.add(n);
  }

  @Override
  public void enterCharTypographic(CharTypographicContext ctx) {
    TypographicCharNode n = (TypographicCharNode) setupNode(new TypographicCharNode(), ctx);
    dom.add(n);
  }

  @Override
  public void enterCharUnicode(CharUnicodeContext ctx) {
    UnicodeCharNode n = (UnicodeCharNode) setupNode(new UnicodeCharNode(), ctx);
    dom.add(n);
  }
  
  @Override
  public void enterCharCodePoint(CharCodePointContext ctx) {
    UnicodeCharNode n = (UnicodeCharNode) setupNode(new UnicodeCharNode(), ctx);
    dom.add(n);
  }  

  @Override
  public void enterComment(CommentContext ctx) {
    CommentNode n = (CommentNode) setupNode(new CommentNode(), ctx);
    dom.add(n);
  }

  @Override
  public void enterContent(ContentContext ctx) {
    TextNode n = (TextNode) setupNode(new TextNode(), ctx);
    dom.add(n);
  }

  @Override
  public void enterEmptyTag(EmptyTagContext ctx) {
    EmptyNode n = (EmptyNode) setupNode(new EmptyNode(), ctx);
    currentTag = n;
  }

  @Override
  public void enterEndTag(EndTagContext ctx) {
    EndNode n = (EndNode) setupNode(new EndNode(), ctx);
    currentTag = n;
  }

  @Override
  public void enterStartTag(StartTagContext ctx) {
    StartNode n = (StartNode) setupNode(new StartNode(), ctx);
    currentTag = n;
  }

  @Override
  public void enterTagName(TagNameContext ctx) {
    String name = ctx.TAG_NAME().getText();
    currentTag.setName(name);
  }

  @Override
  public void exitTag(TagContext ctx) {
    dom.add(currentTag);
    currentTag = null;
    currentAttrName = null;
  }

  /**
   * Fire off the full parse of the input and return the resulting DOM object.
   * Any errors encountered during the parse will be logged in the Log class.
   * <p>
   * @see Log
   * <p>
   * @return the DOM built by parsing the string or file.
   */
  public DOM getDOM() {
    ParserErrorListener parserListener = new ParserErrorListener(dom.getLines());
    parserListener.setSource(dom.getSource());
    ISELexer lexer = new ISELexer(ais);
    lexer.removeErrorListeners();
    lexer.addErrorListener(parserListener);

    CommonTokenStream tokenStream = new CommonTokenStream(lexer);

    parserListener.setSource(dom.getSource());
    ISEParser parser = new ISEParser(tokenStream);
    parser.removeErrorListeners();
    parser.addErrorListener(parserListener);

    ParseTreeWalker ptw = new ParseTreeWalker();
    tokens = parser.getTokenStream();
    ParseTree pt = parser.document();
    ptw.walk(this, pt);
    dom.index();
    return dom;
  }

  private Node setupNode(Node n, ParserRuleContext ctx) {
    Token t = ctx.getStart();
    n.setSource(dom.getSource());
    n.setLine(t.getLine());
    n.setColumn(t.getCharPositionInLine());
    n.setText(tokens.getText(ctx.getSourceInterval()));
    return n;
  }
}

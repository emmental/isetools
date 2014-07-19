/*
 * Copyright (C) 2014 Michael Joyce <michael@negativespace.net>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation version 2.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package ca.nines.ise.cmd;

import ca.nines.ise.dom.DOM;
import ca.nines.ise.dom.DOM.DOMStatus;
import ca.nines.ise.dom.DOMBuilder;
import ca.nines.ise.output.Output;
import ca.nines.ise.output.RTFOutput;
import ca.nines.ise.output.TextOutput;
import ca.nines.ise.output.XMLOutput;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Locale;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;

/**
 *
 * @author michael
 */
public class Transform extends Command {

  @Override
  public String description() {
    return "Transform an ISE SGML document another format.";
  }

  @Override
  public void execute(CommandLine cmd) throws Exception {
    PrintStream out;
    Output renderer = null;
    Locale.setDefault(Locale.ENGLISH);
    out = new PrintStream(System.out, true, "UTF-8");
    if (cmd.hasOption("o")) {
      out = new PrintStream(new FileOutputStream(cmd.getOptionValue("o")), true, "UTF-8");
    }

    if (cmd.hasOption("text")) {
      renderer = new TextOutput(out);
    }
    if (cmd.hasOption("xml")) {
      renderer = new XMLOutput(out);
    }
    if (cmd.hasOption("rtf")) {
      renderer = new RTFOutput(out);
    }

    String[] files = getArgList(cmd);
    if (files.length > 1) {
      System.err.println("Can only transform one file at a time.");
      help();
      System.exit(1);
    }
    DOM dom = new DOMBuilder(new File(files[0])).build();
    if (dom.getStatus() != DOMStatus.ERROR) {
      renderer.render(dom);
    }
  }

  @Override
  public Options getOptions() {
    Options opts = new Options();
    opts.addOption("o", true, "Send output to file.");
    opts.addOption("xml", false, "Transform output to XML.");
    opts.addOption("text", false, "Transform output to UTF-8 (unicode) text.");
    opts.addOption("rtf", false, "Transform output to an RTF document.");
    return opts;
  }

  @Override
  public String getUsage() {
    return "[options] file";
  }

}

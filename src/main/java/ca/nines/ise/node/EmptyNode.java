/*
 * Copyright (C) 2014 Michael Joyce <ubermichael@gmail.com>
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
package ca.nines.ise.node;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Empty nodes.
 *
 * @author Michael Joyce <ubermichael@gmail.com>
 */
public class EmptyNode extends TagNode {

    private static final Logger logger = Logger.getLogger(EmptyNode.class.getName());

    /**
   * Construct an empty node.
   */
  public EmptyNode() {
    super();
  }

  /**
   * Copy constructor.
   */
  public EmptyNode(Node n) {
        super(n);
  }

  /**
   * Construct an empty node with a name.
   */
  public EmptyNode(String tagname) {
    super(tagname);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public NodeType type() {
    return NodeType.EMPTY;
  }

}

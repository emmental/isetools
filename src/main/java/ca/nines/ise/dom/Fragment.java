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
package ca.nines.ise.dom;

import ca.nines.ise.log.Log;

/**
 * A Fragment is a piece of a DOM. They are identical to DOMs in almost every
 * way: validations may be different for Fragments.
 * <p>
 */
public class Fragment extends DOM {
  
  public Fragment(Log log){
    super(log);
  }
  
  public Fragment(){
    super();
  }
}

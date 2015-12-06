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
package ca.nines.ise;

import ca.nines.ise.dom.DOM;
import ca.nines.ise.dom.DOMBuilder;
import ca.nines.ise.log.Log;
import ca.nines.ise.validator.RuleValidator;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;
import javax.xml.validation.Validator;

/**
 * Dumb main class to test out experimental stuff.
 * <p>
 * @author Michael Joyce <ubermichael@gmail.com>
 */
public class Tester {

    private static final Logger logger = Logger.getLogger(Tester.class.getName());

    /**
     * Experiment runner.
     * <p>
     * @param args command line arguments.
     */
    public static void main(String[] args) throws IOException {
        Log log = Log.getInstance();
        String s = "<b/>text \n<RULE> d\n... \n-- {#} \n<a></a>";
        System.out.println(s);
        DOM d = new DOMBuilder(s).build();
        System.out.println(Arrays.toString(d.getLines()));
        System.out.println(d);
        RuleValidator v = new RuleValidator();
        v.validate(d);
        
        if (log.count() > 0) {
            System.err.println("");
            System.err.println(log);
        }
    }
}

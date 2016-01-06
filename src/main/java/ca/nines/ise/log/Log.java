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
package ca.nines.ise.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

/**
 * Log collects error messages generated during parsing and validation.
 * <p>
 * @author Michael Joyce <ubermichael@gmail.com>
 */
public final class Log {

  /**
   * List of messages.
   */
  private List<Message> messages;

  /**
   * Convenience method to add a message to the log.
   *
   * @param m
   */
  public void addMessage(Message m) {
    add(m);
  }

  /**
   * Log is a singleton. Use #getInstance().
   */
  public Log() {
    messages = new ArrayList<>();
  }

  /**
   * Add a message to the log.
   * <p>
   * @param m the message to error.
   */
  public void add(Message m) {
    messages.add(m);
  }

  /**
   * Empty the message log.
   */
  public void clear() {
    messages.clear();
  }

  /**
   * Count the messages.
   *
   * @return int
   */
  public int count() {
    return messages.size();
  }

  /**
   * Get the i-th message in the log.
   *
   * @param i
   * @return Message
   */
  public Message get(int i) {
    return messages.get(i);
  }

  /**
   * Return am array of error messages, sorted by source and line number.
   * <p>
   * @return sorted message array.
   */
  public Message[] messages() {
    Message[] m = messages.toArray(new Message[messages.size()]);
    Arrays.sort(m);
    return m;
  }

  /**
   * Serialize the messages into a string.
   * <p>
   * @return string of messages.
   */
  @Override
  public String toString() {
    Formatter formatter = new Formatter();

    for (Message message : messages()) {
      formatter.format("%s%n", message);
    }
    String fm = formatter.toString();
    formatter.close();
    
    return fm;
  }
}

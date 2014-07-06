/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.ise.log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;

/**
 * Log collects error messages generated during parsing and validation.
 * <p>
 * Log is a singleton: use getInstance() to get an object, rather than new().
 * <p>
 * @author michael
 */
public class Log {

  private static final Log instance = new Log();

  private final ArrayList<Message> messages = new ArrayList<>();

  /**
   * Get an instance of the log.
   * <p>
   * @return log instance.
   */
  public static Log getInstance() {
    return instance;
  }

  private Log() {
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

  public int count() {
    return messages.size();
  }

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
    StringBuilder sb = new StringBuilder();
    Formatter formatter = new Formatter(sb);

    for (Message message : messages) {
      formatter.format("%s%n", message);
    }
    return sb.toString();
  }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.nines.ise.document;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public class Work extends Document implements Comparable<Work> {

  private final Map<String, Edition> editions;
  private final String playCode;
  private final File root;

  public Work(File file) throws IOException {
    this.editions = new HashMap<>();
    this.root = file;
    this.playCode = root.getName();
  }

  public void addEdition(File file) throws IOException {
    String filename = file.getName();
    if(validName(filename)) {
      String edition = extractEdition(filename);
      editions.put(edition, new Edition(file));
    }
  }

  @Override
  public int compareTo(Work o) {
    return playCode.toLowerCase().compareTo(o.playCode.toLowerCase());
  }

  public Edition getEdition(String code) throws IOException {
    if (editions.containsKey(code)) {
      return editions.get(code);
    }
    String editionPath = playCode + "_" + code + ".txt";
    String path = root.getCanonicalPath() + "/" + editionPath;
    File file = new File(path);
    if (file.exists()) {
      return new Edition(file);
    }
    throw new FileNotFoundException("Cannot find " + editionPath + " in " + root.getCanonicalPath());
  }

  public Edition[] getEditions() throws IOException {
    File files[] = root.listFiles(new FilenameFilter() {
      @Override
      public boolean accept(File dir, String name) {
        return Document.validName(name);
      }
    });

    if (files.length == 0) {
      return new Edition[0];
    }

    for (File f : files) {
      addEdition(f);
    }
    Edition e[] = editions.values().toArray(new Edition[editions.size()]);
    Arrays.sort(e);
    return e;
  }

  /**
   * @return the root
   */
  public String getPath() throws IOException {
    return root.getCanonicalPath();
  }

  /**
   * @return the playCode
   */
  public String getPlayCode() {
    return playCode;
  }

  public boolean hasTitlePage() throws IOException {
    return root.getCanonicalPath().contains("withTitlePage");
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(playCode).append("\n");
    try {
      for (Edition edition : getEditions()) {
        sb.append(edition);
      }
    } catch (IOException ex) {
      Logger.getLogger(Work.class.getName()).log(Level.SEVERE, null, ex);
    }
    return sb.toString();
  }

}

package com.bol.api.entity;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Users")
public class User {
  private String id;
  private String name;

  public String getId() {
    return this.id;
  }

  /**
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }
}
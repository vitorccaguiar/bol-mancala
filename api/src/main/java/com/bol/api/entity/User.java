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

  @Override
  public boolean equals(Object obj) {
    if (obj == this) {
      return true;
    }

    if (obj == null || obj.getClass() != this.getClass()) {
      return false;
    }

    User guest = (User) obj;
    return id.equals(guest.getId()) &&
        name.equals(guest.getName());
  }
}
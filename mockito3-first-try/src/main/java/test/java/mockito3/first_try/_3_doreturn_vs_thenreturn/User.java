package test.java.mockito3.first_try._3_doreturn_vs_thenreturn;

import lombok.Data;

@Data
public class User {
  private String name;

  public String getName() {
    System.out.println("side effect!");
    return name;
  }
}

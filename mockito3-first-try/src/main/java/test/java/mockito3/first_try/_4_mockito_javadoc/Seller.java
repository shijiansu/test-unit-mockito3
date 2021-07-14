package test.java.mockito3.first_try._4_mockito_javadoc;

import lombok.Data;

@Data
public class Seller {

  public Bread askForBread() {
    return new Bread();
  }

  public void hi(){
    System.out.println("Hi");
  }
}

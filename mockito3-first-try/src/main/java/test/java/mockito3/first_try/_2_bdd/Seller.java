package test.java.mockito3.first_try._2_bdd;

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

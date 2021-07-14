package test.java.mockito3.first_try._2_bdd;

import lombok.Data;

@Data
public class Goods {
  Bread bread;

  public Goods(Bread bread) {
    this.bread = bread;
  }
}

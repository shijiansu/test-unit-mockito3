package test.java.mockito3.first_try._4_mockito_javadoc;

import lombok.Data;

@Data
public class Goods {
  Bread bread;

  public Goods(Bread bread) {
    this.bread = bread;
  }
}

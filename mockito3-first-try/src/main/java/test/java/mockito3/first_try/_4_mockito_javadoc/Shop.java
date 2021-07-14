package test.java.mockito3.first_try._4_mockito_javadoc;

import lombok.Data;

@Data
public class Shop {
  Seller seller;

  public Shop(Seller seller) {
    this.seller = seller;
  }

  public Goods buyBread() {
    return new Goods(seller.askForBread());
  }

  public String hi() {
    try {
      seller.hi();
      return "SUCC";
    } catch (Exception e) {
      return e.getMessage();
    }
  }
}

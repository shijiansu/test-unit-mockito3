package test.java.mockito3.first_try._2_bdd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShopBddTest {

  @Mock Seller seller;
  @Mock Bread bread;
  @InjectMocks Shop shop;

  @Test
  public void should_buy_bread() {
    Bread b = new Bread();
    // given
    // "When" and "Given" families.
    // Simply treat them as aliases of each other.
    // "Given" family is added in Mockito 1.8.x as to make it looks more aligned to BDD practices.
    given(seller.askForBread()).willReturn(b);
    // when
    Goods goods = shop.buyBread();
    // then
    assertEquals(goods.getBread(), b);
  }

  @Test
  public void should_throw_exception_in_void_method() {
    // given
    willThrow(new RuntimeException("boo")).given(seller).hi();
    // when
    String result = shop.hi();
    // then
    assertEquals("boo", result);
  }

  @Test
  public void should_buy_bread_then() {
    Bread b = new Bread();
    // given
    given(seller.askForBread()).willReturn(b);
    // when
    Goods goods = shop.buyBread();
    // then
    then(seller).should(times(1)).askForBread();
    assertEquals(goods.getBread(), b);
    then(bread).shouldHaveNoInteractions();
    then(seller).shouldHaveNoMoreInteractions();
  }

  @Test
  public void should_buy_bread_then_inorder() {
    // InOrder - use for verify in order process
    Bread b = new Bread();
    InOrder inOrder = Mockito.inOrder(seller);
    // given
    given(seller.askForBread()).willReturn(b);
    // when
    seller.hi(); // call first
    Goods goods = shop.buyBread(); // call next
    // then
    then(seller).should(inOrder).hi();
    then(seller).should(inOrder, times(1)).askForBread();
    assertEquals(goods.getBread(), b);
    then(seller).shouldHaveNoMoreInteractions();
  }
}

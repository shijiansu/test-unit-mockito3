package test.java.mockito3.first_try._3_doreturn_vs_thenreturn;

import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class UserTest {

  @Test
  public void thenreturn_pros() {
    User user = Mockito.mock(User.class);
    // better readability + type safety
    when(user.getName()).thenReturn("John");
    try {
      // no compile error, but runtime error
      doReturn(true).when(user).getName();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  // In Mockito, a side effect happens to the real method invocation
  public void doreturn_no_side_effect() {
    // if use mock then no side effort always, because only invoke the mock object
    User user = Mockito.mock(User.class);
    when(user.getName()).thenReturn("John");
    doReturn("John").when(user).getName();

    System.out.println("spy will trigger side effect...");
    // if use spy, then it could cause side effect at "user2.getName()"
    User user2 = spy(new User());
    System.out.println("[then return]");
    when(user2.getName()).thenReturn("John");
    System.out.println("[do return] no side effect");
    doReturn("John").when(user2).getName();

    // A side effect doesn’t happen always but here are the usual cases:
    // A method throws an exception with precondition checking.
    // A method does what you don’t want while unit testing. For example, network or disk
    // access.
  }

  @Test
  public void side_effect_example1() {
    List<String> list = new LinkedList<>();
    List<String> spiedList = spy(list);

    // Impossible: real method is called so spy.get(0)
    // throws IndexOutOfBoundsException (the list is yet empty)
    Assertions.assertThrows(
        IndexOutOfBoundsException.class, () -> when(spiedList.get(0)).thenReturn("foo"));
    // You have to use doReturn() for stubbing
    doReturn("foo").when(spiedList).get(0);
  }

  @Test
  public void side_effect_example2() {
    AccountService accountService = spy(new AccountService("Account"));
    // created stubber, had hit the stubber, so it is no side effect (invoke actual obejct method)
    doNothing().when(accountService).sendDeleteRequest();
    // doCallRealMethod()
    // cannot use when/thenThrow and when/then for a void method
    accountService.onHandleIntent(new Intent("DELETE"));
    verify(accountService).sendDeleteRequest();
  }
}

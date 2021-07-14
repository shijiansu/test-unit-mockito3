package test.java.mockito3.first_try._4_mockito_javadoc;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class YourOwnAnswer implements Answer<String> {

  @Override
  public String answer(InvocationOnMock invocation) {
    return null;
  }
}

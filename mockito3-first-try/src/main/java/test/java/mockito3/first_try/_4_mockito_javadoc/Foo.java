package test.java.mockito3.first_try._4_mockito_javadoc;

import java.util.List;

@MyAnnotation
public class Foo {
  public static String method() {
    System.out.println();
    return "foo";
  }

  public String foo() {
    return "foo";
  }

  public String someMethod() {
    return null;
  }

  public String someMethod(String value) {
    return value;
  }

  public String someMethod(List<String> values) {
    return values.get(0);
  }

  public Person doSomething(Person person) {
    person.setName("foo");
    return person;
  }

  public void bar() {}

  List<String> bar2() {
    return null;
  }

  public void doSomething() {
  }

  public void doSomething(String s1, String s2, String s3) {
  }
}

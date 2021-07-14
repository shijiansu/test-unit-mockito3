package test.java.mockito3.first_try._4_mockito_javadoc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.atMostOnce;
import static org.mockito.Mockito.description;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.ignoreStubs;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.mockingDetails;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;
import static org.mockito.mock.SerializableMode.ACROSS_CLASSLOADERS;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatcher;
import org.mockito.InOrder;
import org.mockito.MockedStatic;
import org.mockito.MockingDetails;
import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.stubbing.Answer;
import test.java.mockito3.first_try._4_mockito_javadoc.OuterAbstract.InnerAbstract;

public class MockitoTest {

  @Test
  public void _1_verify_some_behaviour() {
    // mock creation
    List<String> mockedList = mock(List.class);

    // using mock object
    mockedList.add("one");
    mockedList.clear();

    // verification
    verify(mockedList).add("one");
    verify(mockedList).clear();
  }

  @Test
  public void _2_how_about_some_stubbing() {
    // You can mock concrete classes, not just interfaces
    LinkedList<String> mockedList = mock(LinkedList.class);

    // stubbing
    when(mockedList.get(0)).thenReturn("first");
    when(mockedList.get(1)).thenThrow(new RuntimeException());

    // following prints "first"
    System.out.println(mockedList.get(0));

    try {
      // following throws runtime exception
      System.out.println(mockedList.get(1));
    } catch (Exception e) {
      System.out.println(e.getClass());
    }

    // following prints "null" because get(999) was not stubbed
    System.out.println(mockedList.get(999));

    // Although it is possible to verify a stubbed invocation, usually <b>it's just redundant</b>
    // If your code cares what get(0) returns, then something else breaks (often even before
    // verify() gets executed).
    // If your code doesn't care what get(0) returns, then it should not be stubbed.
    verify(mockedList).get(0);
  }

  @Test
  public void _3_argument_matchers() {
    List<String> mockedList = mock(LinkedList.class);
    // stubbing using built-in anyInt() argument matcher
    when(mockedList.get(anyInt())).thenReturn("element");

    // stubbing using custom matcher (let's say isValid() returns your own matcher implementation):
    // when(mockedList.contains(argThat(isValid()))).thenReturn(true);
    when(mockedList.contains(argThat("element"::equalsIgnoreCase))).thenReturn(true);
    System.out.println(mockedList.contains("element"));

    // following prints "element"
    System.out.println(mockedList.get(999));
    // <b>you can also verify using an argument matcher</b>
    verify(mockedList).get(anyInt());

    // <b>argument matchers can also be written as Java 8 Lambdas</b>
    when(mockedList.add("10000000")).thenReturn(true);
    mockedList.add("10000000");
    verify(mockedList).add(argThat(someString -> someString.length() > 5));
  }

  @Test
  public void _4_verifying_exact_number_of_invocations() {
    List<String> mockedList = mock(LinkedList.class);
    // using mock
    mockedList.add("once");

    mockedList.add("twice");
    mockedList.add("twice");

    mockedList.add("three times");
    mockedList.add("three times");
    mockedList.add("three times");

    // following two verifications work exactly the same - times(1) is used by default
    verify(mockedList).add("once");
    verify(mockedList, times(1)).add("once");

    // exact number of invocations verification
    verify(mockedList, times(2)).add("twice");
    verify(mockedList, times(3)).add("three times");

    // verification using never(). never() is an alias to times(0)
    verify(mockedList, never()).add("never happened");

    // verification using atLeast()/atMost()
    verify(mockedList, atMostOnce()).add("once");
    verify(mockedList, atLeastOnce()).add("three times");
    verify(mockedList, atLeast(2)).add("three times");
    verify(mockedList, atMost(5)).add("three times");
  }

  @Test
  public void _5_stubbing_void_methods_with_exceptions() {
    List<String> mockedList = mock(LinkedList.class);
    doThrow(new RuntimeException()).when(mockedList).clear();

    // following throws RuntimeException:
    assertThrows(RuntimeException.class, mockedList::clear);
  }

  @Test
  public void _6_in_order_verification() {
    // A. Single mock whose methods must be invoked in a particular order
    List<String> singleMock = mock(List.class);

    // using a single mock
    singleMock.add("was added first");
    singleMock.add("was added second");

    // create an inOrder verifier for a single mock
    InOrder inOrder = inOrder(singleMock);

    // following will make sure that add is first called with "was added first", then with "was
    // added second"
    inOrder.verify(singleMock).add("was added first");
    inOrder.verify(singleMock).add("was added second");

    // B. Multiple mocks that must be used in a particular order
    List<String> firstMock = mock(List.class);
    List<String> secondMock = mock(List.class);

    // using mocks
    firstMock.add("was called first");
    secondMock.add("was called second");

    // create inOrder object passing any mocks that need to be verified in order
    InOrder inOrder2 = inOrder(firstMock, secondMock);

    // following will make sure that firstMock was called before secondMock
    inOrder2.verify(firstMock).add("was called first");
    inOrder2.verify(secondMock).add("was called second");

    // Oh, and A + B can be mixed together at will
  }

  @Test
  public void _7_never_interaction_verification() {
    List<String> mockOne = mock(LinkedList.class);
    List<String> mockTwo = mock(LinkedList.class);
    List<String> mockThree = mock(LinkedList.class);

    // using mocks - only mockOne is interacted
    mockOne.add("one");
    // ordinary verification
    verify(mockOne).add("one");
    // verify that method was never called on a mock
    verify(mockOne, never()).add("two");

    // verify that other mocks were not interacted
    verifyNoInteractions(mockTwo, mockThree);
  }

  @Test
  public void _8_finding_redundant_invocations() {
    List<String> mockedList = mock(LinkedList.class);
    // using mocks
    mockedList.add("one");
    mockedList.add("two");
    verify(mockedList).add("one");

    // following verification will fail
    // verifyNoInteractions(mockedList);
  }

  // @Mock ArticleDatabase database;
  @Test
  public void _9_mock_annotation() {}

  @Test
  public void _10_stubbing_consecutive_calls() {
    Foo mock = mock(Foo.class);
    when(mock.someMethod("some arg")).thenThrow(new RuntimeException()).thenReturn("foo");
    // First call: throws runtime exception:
    assertThrows(RuntimeException.class, () -> mock.someMethod("some arg"));
    // Second call: prints "foo"
    assertEquals("foo", mock.someMethod("some arg"));
    // Any consecutive call: prints "foo" as well (last stubbing wins).
    assertEquals("foo", mock.someMethod("some arg"));

    // Alternative 2
    when(mock.someMethod("some arg")).thenReturn("one", "two", "three");

    // multiple stubbing with the same matchers or arguments is used, then each stubbing will
    // override the previous one:
    // All mock.someMethod("some arg") calls will return "two"
    when(mock.someMethod("some arg")).thenReturn("one");
    when(mock.someMethod("some arg")).thenReturn("two");
    assertEquals("two", mock.someMethod("some arg"));
  }

  @Test
  public void _11_Stubbing_with_callbacks() {
    Foo mock = mock(Foo.class);
    when(mock.someMethod(anyString()))
        .thenAnswer(
            (Answer<String>)
                invocation -> {
                  Object[] args = invocation.getArguments();
                  Object mock1 = invocation.getMock(); // get the mocked object
                  assertEquals(mock, mock1);
                  return "called with arguments: " + Arrays.toString(args);
                });

    // Following prints "called with arguments: [foo]"
    assertEquals("called with arguments: [foo]", mock.someMethod("foo"));
  }

  @Test
  public void _12_do_family_methods_stubs() {
    List<String> mockedList = mock(LinkedList.class);
    doThrow(new RuntimeException()).when(mockedList).clear();
    // following throws RuntimeException
    assertThrows(RuntimeException.class, mockedList::clear);
  }

  @Test
  public void _13_spy() {
    // Spying on real objects can be associated with "partial mocking" concept.
    List<String> spy = spy(new LinkedList<>());

    // optionally, you can stub out some methods:
    when(spy.size()).thenReturn(100);

    // using the spy calls <b>*real*</b> methods
    spy.add("one");
    spy.add("two");

    // prints "one" - the first element of a list
    System.out.println(spy.get(0));

    // size() method was stubbed - 100 is printed
    System.out.println(spy.size());

    // optionally, you can verify
    verify(spy).add("one");
    verify(spy).add("two");

    // Sometimes it's impossible or impractical to use {@link Mockito#when(Object)} for stubbing
    // spies.
    // Therefore when using spies please consider doReturn|Answer|Throw() family of methods for
    // stubbing.
    // Impossible: real method is called so spy.get(0) throws IndexOutOfBoundsException (the list is
    // yet empty)
    // side effect
    List<String> spy2 = spy(new LinkedList<>());
    assertThrows(IndexOutOfBoundsException.class, () -> when(spy2.get(0)).thenReturn("foo"));

    // You have to use doReturn() for stubbing
    doReturn("foo").when(spy2).get(0);
  }

  @Test
  public void _14_default_return_values_of_unstubbed_invocations() {
    // It is the default answer so it will be used <b>only when you don't</b> stub the method call.
    Foo mock = mock(Foo.class, Mockito.RETURNS_SMART_NULLS);
    Foo mockTwo = mock(Foo.class, new YourOwnAnswer());
    // all methods return same answer
    System.out.println(mock.foo());
    assertNull(mockTwo.foo());
  }

  @Test
  public void _15_captors_arguments() {
    // Mockito verifies argument values in natural java style: by using an <code>equals()</code>
    // method.
    // This is also the recommended way of matching arguments because it makes tests clean & simple.
    // In some situations though, it is helpful to assert on certain arguments after the actual
    // verification.
    Foo mock = mock(Foo.class);
    ArgumentCaptor<Person> argument = ArgumentCaptor.forClass(Person.class);
    mock.doSomething(new Person());
    verify(mock).doSomething(argument.capture());
  }

  @Test
  public void _16_real_partial_mocks() {
    // use spy for partial mock
    Foo mock = spy(Foo.class);
    Assertions.assertNull(mock.someMethod());
    // or, you can enable partial mock capabilities selectively on mocks,
    Foo mock2 = mock(Foo.class);
    // Be sure the real implementation is 'safe'.
    // If real implementation throws exceptions or depends on specific state of the object then
    // you're in trouble.
    when(mock2.someMethod()).thenCallRealMethod();
    Assertions.assertNull(mock2.someMethod());
  }

  @Test
  public void _17_resetting_mocks() {
    // The only reason we added <code>reset()</code> method is to
    // make it possible to work with container-injected mocks.
    List<Integer> mock = mock(List.class);
    when(mock.size()).thenReturn(10);
    mock.add(1);
    reset(mock); // at this point the mock forgot any interactions & stubbing
  }

  @Test
  public void _18_framework_validation() {
    //  However, there's a gotcha so please read the javadoc for {@link
    // Mockito#validateMockitoUsage()}
  }

  @Test
  public void _19_aliases_for_behavior_driven_development() {
    Seller seller = mock(Seller.class);
    Shop shop = new Shop(seller);
    Bread b = new Bread();
    // given
    given(seller.askForBread()).willReturn(b);
    // when
    Goods goods = shop.buyBread();
    // then
    assertEquals(goods.getBread(), b);
  }

  @Test
  public void _20_serializable_mocks() {
    List serializableMock = mock(List.class, withSettings().serializable());
    // king a real object spy serializable is a bit more effort as the spy(...) method does not have
    // an overloaded version
    List<Object> list = new ArrayList<>();
    List<Object> spy =
        mock(
            ArrayList.class,
            withSettings().spiedInstance(list).defaultAnswer(CALLS_REAL_METHODS).serializable());
  }

  @Test
  public void _40_stricter_mockito() {
    // Strict stubbing with JUnit Rules - {@link MockitoRule#strictness(Strictness)} with {@link
    // Strictness#STRICT_STUBS}
    // Strict stubbing with JUnit Runner - {@link MockitoJUnitRunner.StrictStubs}
    // Strict stubbing if you cannot use runner/rule (like TestNG) - {@link MockitoSession}
    // Unnecessary stubbing detection with {@link MockitoJUnitRunner}
    // Stubbing argument mismatch warnings, documented in {@link MockitoHint}
  }

  @Test
  public void _21_captor_annotation() {
    // {@link MockitoAnnotations#openMocks(Object)}.
  }

  @Test
  public void _22_verification_timeout() {
    Foo mock = mock(Foo.class);
    // passes when someMethod() is called no later than within 100 ms
    // exits immediately when verification is satisfied (e.g. may not wait full 100 ms)
    mock.someMethod();
    verify(mock, timeout(100)).someMethod();
    // above is an alias to:
    verify(mock, timeout(100).times(1)).someMethod();

    mock.someMethod();
    // passes as soon as someMethod() has been called 2 times under 100 ms
    verify(mock, timeout(100).times(2)).someMethod();
    // equivalent: this also passes as soon as someMethod() has been called 2 times under 100 ms
    verify(mock, timeout(100).atLeast(2)).someMethod();
  }

  @Test
  public void _23_automatic_instantiation_of_spies() {
    // instead:
    // @Spy BeerDrinker drinker = new BeerDrinker();
    // you can write:
    // @Spy BeerDrinker drinker;
    // same applies to@InjectMocks annotation:
    // @InjectMocks LocalPub;
  }

  @Test
  public void _24_one_liner_stub() {
    Car boringStubbedCar =
        when(mock(Car.class).shiftGear()).thenThrow(EngineNotStarted.class).getMock();
  }

  @Test
  public void _25_ignore_stubs_verification() {
    // Sometimes useful when coupled with <code>verifyNoMoreInteractions()</code> or verification
    // <code>inOrder()</code>.
    // Helps avoiding redundant verification of stubbed calls - typically we're not interested in
    // verifying stubs.
    Foo mock = mock(Foo.class);
    Foo mockTwo = mock(Foo.class);

    mock.foo();
    mockTwo.bar();
    verify(mock).foo();
    verify(mockTwo).bar();

    // ignores all stubbed methods:
    verifyNoMoreInteractions(ignoreStubs(mock, mockTwo));

    // creates InOrder that will ignore stubbed
    InOrder inOrder = inOrder(ignoreStubs(mock, mockTwo));
    inOrder.verify(mock).foo();
    inOrder.verify(mockTwo).bar();
    inOrder.verifyNoMoreInteractions();
  }

  @Test
  public void _26_inspect_the_details_of_a_mock_object() {
    Foo someObject = spy(Foo.class);
    // To identify whether a particular object is a mock or a spy:
    mockingDetails(someObject).isMock();
    mockingDetails(someObject).isSpy();

    Foo mock = mock(Foo.class);
    // Getting details like type to mock or default answer:
    MockingDetails details = mockingDetails(mock);
    details.getMockCreationSettings().getTypeToMock();
    details.getMockCreationSettings().getDefaultAnswer();
    details.getInvocations();
    details.getStubbings();
    // Printing all interactions (including stubbing, unused stubs)
    System.out.println(mockingDetails(mock).printInvocations());
  }

  @Test
  public void _27_delegating_call_to_real_instance() {
    //  See more information in docs for {@link AdditionalAnswers#delegatesTo(Object)}.
    // Possible use cases for this feature:
    // Final classes but with an interface
    // Already custom proxied object
    // Special objects with a finalize method, i.e. to avoid executing it 2 times.
    // It is useful when spy() cannot be used
  }

  @Test
  public void _28_mock_maker_plugin() {
    // It is forn Android. E.g. Android can use "dexmaker".
    // an extension point that allows replacing the proxy generation engine. By default,
    // Mockito uses <a href="https://github.com/raphw/byte-buddy">Byte Buddy</a>
    // to create dynamic proxies.
    // the docs for {@link org.mockito.plugins.MockMaker}.
  }

  @Test
  public void _29_bdd_behavior_verification() {
    // given(dog.bark()).willReturn(2);
    // when
    // then(person).should(times(2)).ride(bike);
  }

  @Test
  public void _30_spying_abstract_classes() {
    // spy on abstract class by using constructor.
    // convenience API, new overloaded spy() method:
    SomeAbstract spy = spy(SomeAbstract.class);

    // Mocking abstract methods, spying default methods of an interface (only available since
    // 2.7.13)
    Function<Foo, Bar> function = spy(Function.class);

    // Robust API, via settings builder:
    OtherAbstract spy2 =
        mock(
            OtherAbstract.class, withSettings().useConstructor().defaultAnswer(CALLS_REAL_METHODS));

    // Mocking an abstract class with constructor arguments (only available since 2.7.14)
    SomeAbstract spy3 =
        mock(
            SomeAbstract.class,
            withSettings().useConstructor("arg1", 123).defaultAnswer(CALLS_REAL_METHODS));

    // Mocking a non-static inner abstract class:
    OuterAbstract outerInstance =
        new OuterAbstract() {
          @Override
          public void test() {}
        };
    InnerAbstract spy4 =
        mock(
            InnerAbstract.class,
            withSettings()
                .useConstructor()
                .outerInstance(outerInstance)
                .defaultAnswer(CALLS_REAL_METHODS));
  }

  @Test
  public void _31_serilization_across_classloader() {
    //  For more details see {@link MockSettings#serializable(SerializableMode)}.
    // use regular serialization
    mock(Book.class, withSettings().serializable());
    // use serialization across classloaders
    mock(Book.class, withSettings().serializable(ACROSS_CLASSLOADERS));
  }

  @Test
  public void _32_better_generic_support_with_deep_stubs() {
    Lines lines = mock(Lines.class, RETURNS_DEEP_STUBS);
    // Now Mockito understand this is not an Object but a Line
    Line line = lines.iterator().next();
  }

  @Test
  public void _33_mockito_junit_rule() {
    // Rule public MockitoRule mockito = MockitoJUnit.rule();
    // For more information see {@link MockitoJUnit#rule()}.
  }

  @Test
  public void _34_switch_on_or_off_plugins() {
    // More information here {@link org.mockito.plugins.PluginSwitch}.
  }

  @Test
  public void _35_custom_verification_failure_message() {
    Foo mock = mock(Foo.class);
    assertThrows(
        MockitoAssertionError.class,
        () ->
            // will print a custom message on verification failure
            verify(mock, description("This will print on failure")).someMethod());

    mock.someMethod();
    mock.someMethod();
    // will work with any verification mode
    verify(mock, times(2).description("someMethod should be called twice")).someMethod();
  }

  @Test
  public void _36_java_8_lambda_matcher_support() {
    List<String> list = mock(List.class);
    list.add("123");
    list.add("123");
    // verify a list only had strings of a certain length added to it
    // note - this will only compile under Java 8
    verify(list, times(2)).add(argThat(string -> string.length() < 5));

    // Java 7 equivalent - not as neat
    verify(list, times(2)).add(argThat(arg -> arg.length() < 5));

    // more complex Java 8 example - where you can specify complex verification behaviour
    // functionally
    // verify(target, times(1)).receiveComplexObject(argThat(obj ->
    // obj.getSubObject().get(0).equals("expected"));

    // this can also be used when defining the behaviour of a mock under different inputs
    // in this case if the input list was fewer than 3 items the mock returns null
    Foo mock = mock(Foo.class);
    when(mock.someMethod(argThat((ArgumentMatcher<List<String>>) l -> l.size() < 3)))
        .thenReturn(null);
  }

  @Test
  public void _37_java_8_custom_answer_support() {
    Foo mock = mock(Foo.class);
    // answer by returning 12 every time
    doAnswer(invocation -> 12).when(mock).doSomething();

    // answer by using one of the parameters - converting into the right
    // type as your go - in this case, returning the length of the second string parameter
    // as the answer. This gets long-winded quickly, with casting of parameters.
    doAnswer(invocation -> ((String) invocation.getArgument(1)).length())
        .when(mock)
        .doSomething(anyString(), anyString(), anyString());
  }

  @Test
  public void _38_meta_data_and_generic_type_retention() throws NoSuchMethodException {
    Class<?> mockType = mock(Foo.class).getClass();
    assertTrue(mockType.isAnnotationPresent(MyAnnotation.class));
    assertTrue(
        mockType.getDeclaredMethod("bar2").getGenericReturnType() instanceof ParameterizedType);
  }

  @Test
  public void _39_mocking_final_types_enums_and_final_methods() {}

  @Test
  public void _40_strict_mockito() {
    // Improved productivity and cleaner tests with "stricter" Mockito</a> (Since 2.+)</h3>
  }

  @Test
  public void _41_advanced_framework_integrations() {
    // https://www.linkedin.com/pulse/mockito-vs-powermock-opinionated-dogmatic-static-mocking-faber
    // https://github.com/mockito/mockito/issues/1110
  }

  @Test
  public void _42_VerificationStartedListener() {}

  @Test
  public void _43_MockitoSession() {}

  @Test
  public void _44_InstantiatorProvider2() {
    // replace InstantiatorProvider as it was leaking internal API
  }

  @Test
  public void _45_junit5_extension() {
    // org.mockito:mockito-junit-jupiter
  }

  @Test
  public void _46_lenient() {
    Foo mock = mock(Foo.class);
    //  Strict stubbing feature is available since early Mockito 2.
    lenient().when(mock.foo()).thenReturn("ok");
    // for the given mock
    Foo mock2 = mock(Foo.class, withSettings().lenient());
    mock2.foo();
  }

  @Test
  public void _47_clearing_mock_state_in_inline_mocking() {
    // MockitoFramework.clearInlineMocks()
    // 只针对inline mocking, 有极少情况会有memory leaks.
    // 该API是显式清除mock state (only make sense in inline mocking!)
    // @After // normally put to here
    // public void clearMocks() {
    Mockito.framework().clearInlineMocks();
    // }
  }

  @Test
  public void _48_mocking_static_methods() {
    assertEquals("foo", Foo.method());
    // To make sure a static mock remains temporary,
    // it is recommended to define the scope within a try-with-resources construct.
    try (MockedStatic<Foo> mocked = mockStatic(Foo.class)) {
      // [IMPORTANT] cannot run with "@ExtendWith(MockitoExtension.class)"
      mocked.when(Foo::method).thenReturn("bar");
      assertEquals("bar", Foo.method());
      mocked.verify(Foo::method);
    }
    assertEquals("foo", Foo.method());
    Mockito.framework().clearInlineMocks(); // for example 47
  }
}

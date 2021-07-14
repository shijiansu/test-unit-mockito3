package test.java.mockito3.first_try._1_book;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

@ExtendWith(MockitoExtension.class) // repeatable
public class _1_BookReaderExtensionWithTest {
  @Mock Book mockedBook;

  BookReader reader;

  @InjectMocks // automatically inject mocks/spies fields annotated with @Spy or @Mock
  BookReader reader2;

  @BeforeEach
  public void setUp() {
    reader = new BookReader(mockedBook);
  }

  @Test
  public void get_content() {
    Mockito.when(mockedBook.getContent()).thenReturn("Mockito");
    // using "@ExtendWith(MockitoExtension.class)", error if no assertion
    assertEquals("Mockito", reader.getContent());
  }

  @Test
  public void get_content_with_lenient() {
    // putting lenient() can ignore assertion; with using "@ExtendWith"
    Mockito.lenient().when(mockedBook.getContent()).thenReturn("Mockito");
    reader.getContent();
    Mockito.verify(mockedBook).getContent();
  }

  @Test
  public void get_content_inject_mock_method_parameters(@Mock Book mockedBook2) {
    Mockito.when(mockedBook2.getContent()).thenReturn("Mockito");
    assertEquals("Mockito", new BookReader(mockedBook2).getContent());
  }

  @Test
  public void get_content_with_answer(@Mock Book mockedBook, @Mock BookReader reader) {
    Mockito.when(mockedBook.getContent()).thenReturn("Mockito");

    Mockito.when(reader.getContent(any(String.class)))
        .thenAnswer((Answer<String>) invocation -> mockedBook.getContent());

    assertEquals("Mockito", reader.getContent("Hello World!"));
    Mockito.verify(mockedBook).getContent();
    Mockito.verify(reader).getContent(eq("Hello World!"));
  }

  @Test
  public void get_content_inject_mock() {
    Mockito.when(mockedBook.getContent()).thenReturn("Mockito");
    assertEquals("Mockito", reader2.getContent());
  }

  @Test
  public void get_content_given() {
    Mockito.when(mockedBook.getContent()).thenReturn("Mockito");
    assertEquals("Mockito", reader2.getContent());
  }

  @Test
  public void get_content_multiple_calls() {
    Mockito.when(mockedBook.getContent()).thenReturn("Mockito", "Hello");
    assertEquals("Mockito", reader2.getContent());
    assertEquals("Hello", reader2.getContent());
  }
}

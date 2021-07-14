package test.java.mockito3.first_try._1_book;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

// Benefit is no need to import the "mockito-junit-jupiter";
// It can import separately of mockito and junit5
public class _2_BookReaderTest {
  @Mock Book mockedBook;

  BookReader reader;

  @BeforeEach
  public void setUp() {
    // for @Mock when without "MockitoExtension"
    MockitoAnnotations.openMocks(this);
    reader = new BookReader(mockedBook);
  }

  @Test
  public void print_content() {
    mockedBook.printContent();
    Mockito.verify(mockedBook).printContent();
    // do not require assertion
  }

  @Test
  public void get_content() {
    Mockito.when(mockedBook.getContent()).thenReturn("Mockito");
    assertEquals("Mockito", reader.getContent());
  }

  @Test
  @Disabled(
      "org.junit.jupiter.api.extension.ParameterResolutionException: No ParameterResolver registered for parameter")
  public void failed_get_content_with_answer(@Mock Book mockedBook) {}
}

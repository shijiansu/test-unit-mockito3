package test.java.mockito3.first_try._1_book;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class _3_BookReaderNoMockAnnotationTest {
  BookReader reader;
  Book mockedBook;

  @BeforeEach
  public void setUp() {
    mockedBook = Mockito.mock(Book.class);
    reader = new BookReader(mockedBook);
  }

  @Test
  public void print_content() {
    mockedBook.printContent();
    Mockito.verify(mockedBook).printContent();
  }

  @Test
  public void get_content() {
    Mockito.when(mockedBook.getContent()).thenReturn("Mockito");
    assertEquals("Mockito", reader.getContent());
  }
}

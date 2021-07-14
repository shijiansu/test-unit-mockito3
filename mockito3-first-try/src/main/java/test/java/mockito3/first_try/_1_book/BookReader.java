package test.java.mockito3.first_try._1_book;

import lombok.Data;

@Data
public class BookReader {
  private Book book;

  public BookReader(Book book) {
    this.book = book;
  }

  public String getContent() {
    return this.book.getContent();
  }

  public String getContent(String title) {
    return title + " : " + this.book.getContent();
  }
}

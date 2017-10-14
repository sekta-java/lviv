package edu.cherkassy.test.lviv.testlviv.repository;

import edu.cherkassy.test.lviv.testlviv.entity.Book;
import edu.cherkassy.test.lviv.testlviv.entity.Isbn;

import java.util.List;

public interface BookRepository {
    List<Book> getAllBooks();
    Book getBookById(Integer bookId);
    int addNewIsbn(Isbn isbn);
    int addNewBook(Book book);
}

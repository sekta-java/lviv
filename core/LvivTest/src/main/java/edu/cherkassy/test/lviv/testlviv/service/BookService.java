package edu.cherkassy.test.lviv.testlviv.service;

import edu.cherkassy.test.lviv.testlviv.entity.Book;
import edu.cherkassy.test.lviv.testlviv.entity.Isbn;

import java.util.List;

public interface BookService {
    List<Book> getAllBooks();
    Book getBookById(Integer bookId);
    int addNewIsbn(Isbn isbn);
    int addNewBook(Book book);
}

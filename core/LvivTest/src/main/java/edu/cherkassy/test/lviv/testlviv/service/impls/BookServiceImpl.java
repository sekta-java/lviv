package edu.cherkassy.test.lviv.testlviv.service.impls;

import edu.cherkassy.test.lviv.testlviv.entity.Book;
import edu.cherkassy.test.lviv.testlviv.entity.Isbn;
import edu.cherkassy.test.lviv.testlviv.repository.BookRepository;
import edu.cherkassy.test.lviv.testlviv.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @Override
    public List<Book> getAllBooks() {
        return bookRepository.getAllBooks();
    }

    @Override
    public Book getBookById(Integer bookId) {
        return bookRepository.getBookById(bookId);
    }

    @Override
    public int addNewIsbn(Isbn isbn) {
        return bookRepository.addNewIsbn(isbn);
    }

    @Override
    public int addNewBook(Book book) {
        return bookRepository.addNewBook(book);
    }
}

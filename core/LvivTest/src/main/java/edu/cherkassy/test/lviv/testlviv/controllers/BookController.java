package edu.cherkassy.test.lviv.testlviv.controllers;

import edu.cherkassy.test.lviv.testlviv.entity.Book;
import edu.cherkassy.test.lviv.testlviv.entity.Isbn;
import edu.cherkassy.test.lviv.testlviv.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lviv/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping()
    public List<Book> getAllBooks() {
        return this.bookService.getAllBooks();
    }

    @GetMapping("/{bookId}")
    public Book getBookById(@PathVariable Integer bookId) {
        return this.bookService.getBookById(bookId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewBook(@RequestBody Book book) {
        this.bookService.addNewBook(book);
    }

    @PostMapping("/isbn")
    @ResponseStatus(HttpStatus.CREATED)
    public void addNewIsbn(@RequestBody Isbn isbn) {
        this.bookService.addNewIsbn(isbn);
    }
}
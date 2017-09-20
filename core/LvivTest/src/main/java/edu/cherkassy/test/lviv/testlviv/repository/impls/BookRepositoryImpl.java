package edu.cherkassy.test.lviv.testlviv.repository.impls;

import edu.cherkassy.test.lviv.testlviv.entity.Book;
import edu.cherkassy.test.lviv.testlviv.entity.Isbn;
import edu.cherkassy.test.lviv.testlviv.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
public class BookRepositoryImpl implements BookRepository {

    private static final String SQL_SELECT_BOOKS = "SELECT " +
            "LVIV.BOOKS.BOOKID, " +
            "LVIV.BOOKS.TITLE, " +
            "LVIV.BOOKS.PUBLISHING, " +
            "LVIV.BOOKS.YEAROFPUBLISHING, " +
            "LVIV.BOOKS.NUMBEROFPAGE, " +
            "LVIV.BOOKS.PRICE, " +
            "LVIV.BOOKS.DESCRIPTION " +
            " FROM LVIV.BOOKS";

    private static final String SQL_SELECT_ISBN = "SELECT" +
            "  LVIV.ISBN.ISBNID," +
            "  LVIV.ISBN.BOOKID," +
            "  LVIV.ISBN.NUMBER," +
            "  LVIV.ISBN.TYPE," +
            "  LVIV.ISBN.TRANSLATION," +
            "  LVIV.ISBN.LANGUAGE" +
            " FROM LVIV.ISBN WHERE BOOKID = ?";

    private static final String SQL_SELECT_CATEGORIES = "SELECT LVIV.CATEGORY.CATEGORY " +
            "FROM LVIV.CATEGORY" +
            " INNER JOIN LVIV.BOOK_CATEGORY ON LVIV.BOOK_CATEGORY.CATEGORYID = LVIV.CATEGORY.CATEGORYID" +
            " INNER JOIN LVIV.BOOKS ON LVIV.BOOK_CATEGORY.BOOKID = LVIV.BOOKS.BOOKID " +
            "WHERE LVIV.BOOKS.BOOKID = ?";

    private static final String SQL_SELECT_AUTHORS = "SELECT LVIV.AUTHORS.NAME " +
            "FROM LVIV.AUTHORS" +
            " INNER JOIN LVIV.AUTHOR_BOOK ON LVIV.AUTHOR_BOOK.AUTHORID = LVIV.AUTHORS.AUTHORID" +
            " INNER JOIN LVIV.BOOKS ON LVIV.AUTHOR_BOOK.BOOKID = LVIV.BOOKS.BOOKID " +
            "WHERE LVIV.BOOKS.BOOKID = ?";

    private static final String SQL_SELECT_BOOK_BY_ID = SQL_SELECT_BOOKS + " WHERE  LVIV.BOOKS.BOOKID = ?";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = this.getBooks();
        books.forEach(book -> book.setIsbnList(this.getListIsbnsOfBook(book.getId())));
        books.forEach(book -> book.setCategories(this.getBookStringAttributes(book.getId(), SQL_SELECT_CATEGORIES, "CATEGORY")));
        books.forEach(book -> book.setAuthors(this.getBookStringAttributes(book.getId(), SQL_SELECT_AUTHORS, "NAME")));
        return books;
    }

    @Override
    public Book getBookById(Integer bookId) {
        Book book = this.getBook(bookId);
        book.setIsbnList(this.getListIsbnsOfBook(bookId));
        book.setCategories(this.getBookStringAttributes(bookId, SQL_SELECT_CATEGORIES, "CATEGORY"));
        book.setAuthors(this.getBookStringAttributes(bookId, SQL_SELECT_AUTHORS, "NAME"));
        return book;
    }

    @Override
    public boolean addNewIsbn(Isbn isbn) {
        return false;
    }

    private Book getBook(Integer bookId) {
        return this.jdbcTemplate.queryForObject(SQL_SELECT_BOOK_BY_ID,
                new Object[]{bookId},
                (rs, rowNum) -> {
                    Book book = new Book();
                    book.setId(rs.getInt("BOOKID"));
                    book.setTitle(rs.getString("TITLE"));
                    book.setPublishing(rs.getString("PUBLISHING"));
                    book.setYearOfPublishing(rs.getString("YEAROFPUBLISHING"));
                    book.setNumberOfPages(rs.getShort("NUMBEROFPAGE"));
                    book.setPrice(rs.getDouble("PRICE"));
                    book.setDescription(rs.getString("DESCRIPTION"));
                    return book;
                });
    }

    private List<Book> getBooks() {
        List<Book> result = new ArrayList<>();
        for (Map<String, Object> row : this.jdbcTemplate.queryForList(SQL_SELECT_BOOKS)) {
            Book book = new Book();
            book.setId((Integer) row.get("BOOKID"));
            book.setTitle((String) row.get("TITLE"));
            book.setPublishing((String) row.get("PUBLISHING"));
            book.setYearOfPublishing((String) row.get("YEAROFPUBLISHING"));
            book.setNumberOfPages((Short) row.get("NUMBEROFPAGE"));
            book.setPrice((Double) row.get("PRICE"));
            book.setDescription((String) row.get("DESCRIPTION"));
            result.add(book);
        }
        return result;
    }

    private List<Isbn> getListIsbnsOfBook(Integer bookId) {
        List<Isbn> isbns = new ArrayList<>();
        return this.jdbcTemplate.execute(SQL_SELECT_ISBN, (PreparedStatementCallback<List<Isbn>>) ps -> {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Isbn isbn = new Isbn();
                isbn.setIsbnId(rs.getInt("ISBNID"));
                isbn.setBookId(rs.getInt("BOOKID"));
                isbn.setNumber(rs.getString("NUMBER"));
                isbn.setType(rs.getString("TYPE"));
                isbn.setTranslation(rs.getBoolean("TRANSLATION"));
                isbn.setLanguage(rs.getString("LANGUAGE"));
                isbns.add(isbn);
            }
            return isbns;
        });
    }

    private List<String> getBookStringAttributes(Integer bookId, String query, String titleOfColumn) {
        List<String> resultOfQuery = new ArrayList<>();
        return this.jdbcTemplate.execute(query, (PreparedStatementCallback<List<String>>) ps -> {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultOfQuery.add(rs.getString(titleOfColumn));
            }
            return resultOfQuery;
        });
    }
}
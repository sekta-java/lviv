package edu.cherkassy.test.lviv.testlviv.repository.impls;

import edu.cherkassy.test.lviv.testlviv.entity.Book;
import edu.cherkassy.test.lviv.testlviv.entity.Isbn;
import edu.cherkassy.test.lviv.testlviv.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

@Repository
public class BookRepositoryH2DbImpl implements BookRepository {

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

    private static final String SQL_INSERT_BOOK = "INSERT INTO LVIV.BOOKS " +
            "(BOOKID ,TITLE, PUBLISHING, YEAROFPUBLISHING, NUMBEROFPAGE, PRICE, DESCRIPTION) " +
            "VALUES (DEFAULT ,?, ?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_ISBN = "INSERT INTO LVIV.ISBN " +
            "(ISBNID, BOOKID, LANGUAGE, NUMBER, TYPE, TRANSLATION) " +
            "VALUES (DEFAULT, ?, ?, ?, ?, ?)";

    private static final String SQL_INSERT_CATEGORY = "INSERT INTO LVIV.CATEGORY " +
            " (CATEGORYID, CATEGORY) VALUES (DEFAULT , ?)";

    private static final String SQL_INSERT_AUTHORS = "INSERT INTO LVIV.AUTHORS" +
            " (AUTHORID, NAME) VALUES (DEFAULT , ?)";

    private static final String SQL_INSERT_BOOK_CATEGORY = "INSERT INTO LVIV.BOOK_CATEGORY " +
            "(BOOKID, CATEGORYID) VALUES (?, ?)";

    private static final String SQL_INSERT_AUTHOR_BOOK = "INSERT INTO LVIV.AUTHOR_BOOK " +
            "(BOOKID, AUTHORID) VALUES (?, ?)";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepositoryH2DbImpl(JdbcTemplate jdbcTemplate) {
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
    public int addNewIsbn(Isbn isbn) {
        return this.addIsbns(Collections.singletonList(isbn));
    }

    @Override
    public int addNewBook(Book book) {
        int rowAddCount = 0;

        KeyHolder holder = new GeneratedKeyHolder();
        rowAddCount += this.jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(SQL_INSERT_BOOK, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getPublishing());
            ps.setString(3, book.getYearOfPublishing());
            ps.setShort(4, book.getNumberOfPages());
            ps.setDouble(5, book.getPrice());
            ps.setString(6, book.getDescription());
            return ps;
        }, holder);

        Integer generatedId = holder.getKey().intValue();
        book.setId(generatedId);
        book.getIsbnList().forEach(isbn -> isbn.setBookId(generatedId));

        rowAddCount += this.addIsbns(book.getIsbnList());
        HashMap<String, List<String>> attrs = new HashMap<>();
        attrs.put("CATEGORIES", book.getCategories());
        attrs.put("NAME", book.getAuthors());

        rowAddCount += this.addStringsAttributesToBook(attrs, book.getId());

        return rowAddCount;
    }

    private int addIsbns(List<Isbn> isbns) {
        int addedRows = 0;
        for (Isbn isbn : isbns) {
            addedRows += this.jdbcTemplate.update(SQL_INSERT_ISBN,
                    isbn.getBookId(),
                    isbn.getLanguage(),
                    isbn.getNumber(),
                    isbn.getType(),
                    isbn.getTranslation());
        }
        return addedRows;
    }

    private int addStringsAttributesToBook(Map<String, List<String>> attributes, Integer bookId) {
        int rowAddCount = 0;
        for (Map.Entry<String, List<String>> entry : attributes.entrySet()) {
            if (entry.getKey().equals("CATEGORIES")) {
                for (String attribute : entry.getValue()) {
                    rowAddCount += fillIntermediateTable(attribute, SQL_INSERT_CATEGORY, SQL_INSERT_BOOK_CATEGORY, bookId);
                }
            } else if (entry.getKey().equals("NAME")) {
                for (String attribute : entry.getValue()) {
                    rowAddCount += fillIntermediateTable(attribute, SQL_INSERT_AUTHORS, SQL_INSERT_AUTHOR_BOOK, bookId);
                }
            }
        }
        return rowAddCount;
    }

    private int fillIntermediateTable(String attr, String insertAttribute, String insertIntermediateTable, Integer bookId) {
        int rowAddCount = 0;
        KeyHolder holder = new GeneratedKeyHolder();
        rowAddCount += this.jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(insertAttribute, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, attr);
            return ps;
        }, holder);
        Long attrId = holder.getKey().longValue();
        rowAddCount += this.jdbcTemplate.update(insertIntermediateTable, bookId, attrId);
        return rowAddCount;
    }

    private Book getBook(Integer bookId) {
        return this.jdbcTemplate.queryForObject(SQL_SELECT_BOOK_BY_ID,
                new Object[]{bookId},
                (rs, rowNum) -> Book.resultSetExtractor(rs, new Book()));
    }

    private List<Book> getBooks() {
        return jdbcTemplate.query(SQL_SELECT_BOOKS, rs -> {
            List<Book> result = new ArrayList<>();
            while (rs.next()) {
                result.add(Book.resultSetExtractor(rs, new Book()));
            }
            return result;
        });
    }

    private List<Isbn> getListIsbnsOfBook(Integer bookId) {
        List<Isbn> isbns = new ArrayList<>();
        return this.jdbcTemplate.execute(SQL_SELECT_ISBN, (PreparedStatementCallback<List<Isbn>>) ps -> {
            ps.setInt(1, bookId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                isbns.add(Isbn.resultSetExtractor(rs, new Isbn()));
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
package edu.cherkassy.test.lviv.testlviv.entity;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private Integer id;
    private String title;
    private List<String> authors = new ArrayList<>();
    private String publishing;
    private String yearOfPublishing;
    private Short numberOfPages;
    private List<Isbn> isbnList = new ArrayList<>();
    private List<String> categories = new ArrayList<>();
    private String description;
    private Double price;

    public static Book resultSetExtractor(ResultSet rs, Book book) throws SQLException {
        book.setId(rs.getInt("BOOKID"));
        book.setTitle(rs.getString("TITLE"));
        book.setPublishing(rs.getString("PUBLISHING"));
        book.setYearOfPublishing(rs.getString("YEAROFPUBLISHING"));
        book.setNumberOfPages(rs.getShort("NUMBEROFPAGE"));
        book.setPrice(rs.getDouble("PRICE"));
        book.setDescription(rs.getString("DESCRIPTION"));
        return book;
    }
}
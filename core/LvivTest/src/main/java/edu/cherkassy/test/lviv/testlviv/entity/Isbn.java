package edu.cherkassy.test.lviv.testlviv.entity;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Isbn {
    private Integer isbnId;
    private Integer bookId;
    private String language;
    private String number;
    private String type;
    private Boolean translation;

    public static Isbn resultSetExtractor(ResultSet rs, Isbn isbn) throws SQLException {
        isbn.setIsbnId(rs.getInt("ISBNID"));
        isbn.setBookId(rs.getInt("BOOKID"));
        isbn.setNumber(rs.getString("NUMBER"));
        isbn.setType(rs.getString("TYPE"));
        isbn.setTranslation(rs.getBoolean("TRANSLATION"));
        isbn.setLanguage(rs.getString("LANGUAGE"));
        return isbn;
    }
}
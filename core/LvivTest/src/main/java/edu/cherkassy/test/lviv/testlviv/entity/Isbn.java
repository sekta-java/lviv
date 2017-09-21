package edu.cherkassy.test.lviv.testlviv.entity;

import lombok.*;

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
}
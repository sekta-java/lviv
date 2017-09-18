package edu.cherkassy.test.lviv.testlviv.entity;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Isbn {
    @Getter @Setter private Integer isbnId;
    @Getter @Setter private Integer bookId;
    @Getter @Setter private String language;
    @Getter @Setter private String number;
    @Getter @Setter private String type;
    @Getter @Setter private Boolean translation;
}
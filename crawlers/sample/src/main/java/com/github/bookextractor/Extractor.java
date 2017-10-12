package com.github.bookextractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Наталия on 22.09.2017.
 */
public class Extractor {

    public static void main(String[] args) throws IOException, ParseException {
        URL resource = Extractor.class.getClassLoader().getResource("book-page-example.html");
        String s = IOUtils.toString(resource, "UTF-8");
        Document document = Jsoup.parse(s);
        Elements characteristics = document.getElementsByClass("characteristics");

        Book book = new Book();
        book.setTitle(characteristics.select("#title").text());
        book.setAuthors(stringItemsToList(characteristics.select("#authors").text()));

        Isbn isbnUa = new Isbn();
        isbnUa.setLanguage("UA");
        isbnUa.setNumber(characteristics.select("#isbn-ua").text());
        isbnUa.setType(isbnUa.getNumber().length());
        isbnUa.setTranslation(true);

        Isbn isbnEn10 = new Isbn();
        isbnEn10.setLanguage("EN");
        isbnEn10.setNumber(characteristics.select("#isbn-10").text());
        isbnEn10.setType(isbnUa.getNumber().length());
        isbnEn10.setTranslation(false);

        Isbn isbnEn13 = new Isbn();
        isbnEn13.setLanguage("EN");
        isbnEn13.setNumber(characteristics.select("#isbn-13").text());
        isbnEn13.setType(isbnUa.getNumber().length());
        isbnEn13.setTranslation(false);

        book.setIsbns(new ArrayList<>(Arrays.asList(isbnUa, isbnEn10, isbnEn13)));
        String price = characteristics.select("#price").text().replace(",", ".");
        book.setPrice(new BigDecimal(price));
        book.setCategories((characteristics.select("#categories").text()));
        book.setDescription(characteristics.select("#annotation").text());

        System.out.println(new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(book));
    }

    private static List<String> stringItemsToList(String itemsText) {
        return Stream.of(itemsText.split(",")).map(String::trim).collect(Collectors.toList());
    }
}

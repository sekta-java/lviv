package com.github.bookextractor;

import java.math.BigDecimal;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Наталия on 22.09.2017.
 */

public class Book {

    private String title;

    private List<String> authors;

    private String publishing;

    private Year yearOfPublishing;

    private int numberOfPages;

    private ArrayList<Isbn> isbns;

    private BigDecimal price;

    private List<String> categories;

    private String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public String getPublishing() {
        return publishing;
    }

    public void setPublishing(String publishing) {
        this.publishing = publishing;
    }

    public Year getYearOfPublishing() {
        return yearOfPublishing;
    }

    public void setYearOfPublishing(Year yearOfPublishing) {
        this.yearOfPublishing = yearOfPublishing;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public ArrayList<Isbn> getIsbns() {
        return isbns;
    }

    public void setIsbns(ArrayList<Isbn> isbns) {
        this.isbns = isbns;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

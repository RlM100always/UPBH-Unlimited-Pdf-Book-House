package com.techtravelcoder.educationalbooks.model;

public class BookModel {
    String bookName;
    String bookImageLink;
    String bookKey;
    String bookDriveurl;
    String bookCategoryKey;
    String bookCategoryName;
    String bookPostDate;

    public BookModel(){

    }

    public String getBookPostDate() {
        return bookPostDate;
    }

    public void setBookPostDate(String bookPostDate) {
        this.bookPostDate = bookPostDate;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getBookImageLink() {
        return bookImageLink;
    }

    public void setBookImageLink(String bookImageLink) {
        this.bookImageLink = bookImageLink;
    }

    public String getBookKey() {
        return bookKey;
    }

    public void setBookKey(String bookKey) {
        this.bookKey = bookKey;
    }

    public String getBookDriveurl() {
        return bookDriveurl;
    }

    public void setBookDriveurl(String bookDriveurl) {
        this.bookDriveurl = bookDriveurl;
    }

    public String getBookCategoryKey() {
        return bookCategoryKey;
    }

    public void setBookCategoryKey(String bookCategoryKey) {
        this.bookCategoryKey = bookCategoryKey;
    }

    public String getBookCategoryName() {
        return bookCategoryName;
    }

    public void setBookCategoryName(String bookCategoryName) {
        this.bookCategoryName = bookCategoryName;
    }
}

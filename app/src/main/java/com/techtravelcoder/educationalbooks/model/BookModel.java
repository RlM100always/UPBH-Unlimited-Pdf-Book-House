package com.techtravelcoder.educationalbooks.model;

public class BookModel {
    String bookName;
    String bookImageLink;
    String bookKey;
    String bookDriveurl;
    String bookCategoryKey;
    String bookCategoryName;
    String bookPostDate;
    String bookMbSize,bookPageNo,bookCategoryType,bookPriceType,bookLanguageType;


    public BookModel(){

    }

    public String getBookMbSize() {
        return bookMbSize;
    }

    public void setBookMbSize(String bookMbSize) {
        this.bookMbSize = bookMbSize;
    }

    public String getBookPageNo() {
        return bookPageNo;
    }

    public void setBookPageNo(String bookPageNo) {
        this.bookPageNo = bookPageNo;
    }

    public String getBookCategoryType() {
        return bookCategoryType;
    }

    public void setBookCategoryType(String bookCategoryType) {
        this.bookCategoryType = bookCategoryType;
    }

    public String getBookPriceType() {
        return bookPriceType;
    }

    public void setBookPriceType(String bookPriceType) {
        this.bookPriceType = bookPriceType;
    }

    public String getBookLanguageType() {
        return bookLanguageType;
    }

    public void setBookLanguageType(String bookLanguageType) {
        this.bookLanguageType = bookLanguageType;
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

    @Override
    public String toString() {
        return "BookModel{" +
                "bookName='" + bookName + '\'' +
                ", bookCategoryName='" + bookCategoryName + '\'' +
                '}';
    }
}

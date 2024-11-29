package com.techtravelcoder.educationalbooks.model;

public class BookCategoryModel {
    String bCategoryName,bCategoryImageLink,bCategoryKey,bCategoryKeyword;

    public BookCategoryModel(){

    }

    public String getbCategoryKeyword() {
        return bCategoryKeyword;
    }

    public void setbCategoryKeyword(String bCategoryKeyword) {
        this.bCategoryKeyword = bCategoryKeyword;
    }

    public String getbCategoryName() {
        return bCategoryName;
    }

    public void setbCategoryName(String bCategoryName) {
        this.bCategoryName = bCategoryName;
    }

    public String getbCategoryImageLink() {
        return bCategoryImageLink;
    }

    public void setbCategoryImageLink(String bCategoryImageLink) {
        this.bCategoryImageLink = bCategoryImageLink;
    }

    public String getbCategoryKey() {
        return bCategoryKey;
    }

    public void setbCategoryKey(String bCategoryKey) {
        this.bCategoryKey = bCategoryKey;
    }

    @Override
    public String toString() {
        return "BookCategoryModel{" +
                "bCategoryName='" + bCategoryName + '\'' +
                ", bCategoryKeyword='" + bCategoryKeyword + '\'' +
                '}';
    }
}

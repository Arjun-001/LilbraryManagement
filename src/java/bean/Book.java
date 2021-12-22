package bean;



/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *Name: Arjun Chowdhary
 * Student ID: 991454434
 */
public class Book {
//    class contains getter-setter method

    private String code, title, price,genre,availablity;
//    private string objects

    public String getCode() {
//        returns the value of code
        return code;
    }

    public String getTitle() {
//        returns the value of title
        return title;
    }

    public String getPrice() {
//        returns the value of price
        return price;
    }
    public String getGenre(){
        return genre;
    }
    public String getAvailablitiy(){
        return availablity;
    }
    public void setCode(String cc) {
//        sets the value of code
        this.code = cc;
    }
    public void setGenre(String gg) {
//        sets the value of code
        this.genre = gg;
    }
    public void setAvailablity(String aa) {
//        sets the value of code
        this.availablity = aa;
    }

    public void setTitle(String tt) {
//        sets the value of title
        this.title = tt;
    }

    public void setPrice(String pp) {
//      sets the value of price  
        this.price = pp;
    }

    @Override
    public String toString() {
//        toString method returning code,title,price
        return code + "\t" + title + "\t" + price;
    }

}

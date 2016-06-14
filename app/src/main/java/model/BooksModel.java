package model;

/**
 * Created by shituocheng on 16/6/10.
 */

public class BooksModel {
    private int book_id;
    private int book_avg;
    private int book_max;
    private String author;
    private String price;
    private String publisher;
    private String book_name;
    private String book_image;

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public int getBook_avg() {
        return book_avg;
    }

    public void setBook_avg(int book_avg) {
        this.book_avg = book_avg;
    }

    public int getBook_max() {
        return book_max;
    }

    public void setBook_max(int book_max) {
        this.book_max = book_max;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_image() {
        return book_image;
    }

    public void setBook_image(String book_image) {
        this.book_image = book_image;
    }
}

package model;
/**
 * Created by shituocheng on 16/6/6.
 */

public class CastTopModel {

    private int max;
    private int avg;
    private int year;
    private String title;
    private int top_cast_id;
    private String cast_top_image;

    public int getMax() {
        return max;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getAvg() {
        return avg;
    }

    public void setAvg(int avg) {
        this.avg = avg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTop_cast_id() {
        return top_cast_id;
    }

    public void setTop_cast_id(int top_cast_id) {
        this.top_cast_id = top_cast_id;
    }

    public String getCast_top_image() {
        return cast_top_image;
    }

    public void setCast_top_image(String cast_top_image) {
        this.cast_top_image = cast_top_image;
    }
}

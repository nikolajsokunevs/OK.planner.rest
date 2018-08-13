package lv.ok.models;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Event {

    private static final DateFormat FORMATTER_LONG= new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:SS");
    private static final DateFormat FORMATTER_SHORT= new SimpleDateFormat("yyyy-MM-dd");

    private String id;
    private String title;
    private Date start;
    private Date end;
    private Boolean allDay;
    private String color;
    private String tbackgroundColoritle;
    private String borderColor;
    private String textColor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart() {
        return dateToString(this.start);
    }

    public void setStart(String start){
        this.start=stringToDate(start);
    }

    public String getEnd() {
        return dateToString(this.end);
    }

    public void setEnd(String end){
        this.end=stringToDate(end);
    }

    private String dateToString(Date date){
        if(date.getTime() % 100000 == 0)
            return  FORMATTER_SHORT.format(date);
        return FORMATTER_LONG.format(date);
    }

    private Date stringToDate(String date){
        try {
            if (date.length()>10)
                return FORMATTER_LONG.parse(date);
            return FORMATTER_SHORT.parse(date);
        }catch (ParseException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public Boolean isAllDay() {
        return allDay;
    }

    public void setAllDay(Boolean allDay) {
        this.allDay = allDay;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTbackgroundColoritle() {
        return tbackgroundColoritle;
    }

    public void setTbackgroundColoritle(String tbackgroundColoritle) {
        this.tbackgroundColoritle = tbackgroundColoritle;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(String borderColor) {
        this.borderColor = borderColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }
}

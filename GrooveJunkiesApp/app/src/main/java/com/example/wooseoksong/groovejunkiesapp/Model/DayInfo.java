package com.example.wooseoksong.groovejunkiesapp.Model;

/**
 * Created by wooseoksong on 2017. 4. 1..
 */

public class DayInfo {
    private String day;
    private boolean inMonth;
    private boolean today;

    public String getDay() {return day;}
    public void setDay(String day) {this.day = day;}

    public boolean isInMonth() {return inMonth;}
    public void setInMonth(boolean inMonth) {this.inMonth = inMonth;}

    public boolean isToday() {return today;}
    public void setToday(boolean today) {this.today = today;}

    public DayInfo(){}

    public DayInfo(String day, boolean inMonth) {
        this.day = day;
        this.inMonth = inMonth;
        this.today = false;
    }


}

package com.example.today_menu_app.crawling;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class MyThread extends Thread {
    private String year;
    private String month;
    private String week;
    private String realstart;
    private String realend;
    public Document getDoc() {
        return doc;
    }

    private Document doc;

    @Override
    public void run() {
        super.run();
        String URL = String.format("https://dormi.kongju.ac.kr/HOME/sub.php?code=041303&currentYear=%s&currentMonth=%s&currentWeekNo=%s&currentWeekStart=%s&currentWeekEnd=%s", year, month, week, realstart, realend);
        try {

            doc = Jsoup.connect(URL).get();

        } catch(Exception e){e.printStackTrace();}
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public void setRealstart(String realstart) {
        this.realstart = realstart;
    }

    public void setRealend(String realend) {
        this.realend = realend;
    }
}

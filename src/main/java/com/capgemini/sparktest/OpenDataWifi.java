package com.capgemini.sparktest;


import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class OpenDataWifi implements Serializable {

    private String id, browser, device, language, site, os;
    private Date start_time;
    private double duration, input_octets, output_octets;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    public OpenDataWifi() {
    }

    public OpenDataWifi(String id, String start_time, String browser, String device, String language, String site, String os, double duration, double input_octets, double output_octets) {
        this.id = id;
        try {
            this.start_time = toNearestWholeHour(dateFormat.parse(start_time));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.browser = browser;
        this.device = device;
        this.language = language;
        this.site = site;
        this.os = os;
        this.duration = duration;
        this.input_octets = input_octets;
        this.output_octets = output_octets;
    }

    static Date toNearestWholeHour(Date d) {
        Calendar c = new GregorianCalendar();
        c.setTime(d);

        if (c.get(Calendar.MINUTE) >= 30)
            c.add(Calendar.HOUR, 1);

        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);

        return c.getTime();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStart_time() {
        return start_time;
    }

    public void setStart_time(Date start_time) {
        this.start_time = start_time;
    }

    public String getBrowser() {
        return browser;
    }

    public void setBrowser(String browser) {
        this.browser = browser;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getInput_octets() {
        return input_octets;
    }

    public void setInput_octets(double input_octets) {
        this.input_octets = input_octets;
    }

    public double getOutput_octets() {
        return output_octets;
    }

    public void setOutput_octets(double output_octets) {
        this.output_octets = output_octets;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    @Override
    public String toString() {
        return "OpenDataWifi{" +
                "id='" + id + '\'' +
                ", start_time='" + start_time + '\'' +
                ", browser='" + browser + '\'' +
                ", device='" + device + '\'' +
                ", language='" + language + '\'' +
                ", site='" + site + '\'' +
                ", duration=" + duration +
                ", input_octets=" + input_octets +
                ", output_octets=" + output_octets +
                '}';
    }
}

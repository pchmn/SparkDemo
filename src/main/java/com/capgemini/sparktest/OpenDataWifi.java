package com.capgemini.sparktest;


public class OpenDataWifi {

    private String id, start_time, browser, device, language, site;
    private int duration;
    private double input_octets, output_octets;

    public OpenDataWifi() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
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
}

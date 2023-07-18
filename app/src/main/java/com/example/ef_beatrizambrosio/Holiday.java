package com.example.ef_beatrizambrosio;

public class Holiday {
    private String date;
    private String localName;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public Holiday(String date, String localName) {
        this.date = date;
        this.localName = localName;
    }

    @Override
    public String toString() {
        return date + " - " + localName;
    }
}

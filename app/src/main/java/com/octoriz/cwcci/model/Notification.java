package com.octoriz.cwcci.model;

public class Notification {
    private long id;
    private String data;
    private String name;
    private String received_at;
    private String seen_at;

    public Notification(int id, String data, String name, String received_at, String seen_at) {
        this.id = id;
        this.data = data;
        this.name = name;
        this.received_at = received_at;
        this.seen_at = seen_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReceivedAt() {
        return received_at;
    }

    public void setReceived_at(String arrived_at) {
        this.received_at = arrived_at;
    }

    public String getSeenAt() {
        return seen_at;
    }

    public void setSeenAt(String seen_at) { this.seen_at = seen_at; }
}

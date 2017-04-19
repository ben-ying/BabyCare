package com.ben.yjh.babycare.model;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class EventsResult {
    @SerializedName("count") int count;
    @SerializedName("next") String next;
    @SerializedName("previous") String previous;
    @SerializedName("results") List<Event> events;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}

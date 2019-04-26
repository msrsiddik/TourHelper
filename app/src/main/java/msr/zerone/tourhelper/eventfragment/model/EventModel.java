package msr.zerone.tourhelper.eventfragment.model;

public class EventModel {
    private String eventId, uid, name, startloc, destin, createdDate, deparDate, returnDate, budget;

    public EventModel() {
    }

    public EventModel(String eventId, String uid, String name, String startloc, String destin, String createdDate, String deparDate, String returnDate, String budget) {
        this.eventId = eventId;
        this.uid = uid;
        this.name = name;
        this.startloc = startloc;
        this.destin = destin;
        this.createdDate = createdDate;
        this.deparDate = deparDate;
        this.returnDate = returnDate;
        this.budget = budget;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartloc() {
        return startloc;
    }

    public void setStartloc(String startloc) {
        this.startloc = startloc;
    }

    public String getDestin() {
        return destin;
    }

    public void setDestin(String destin) {
        this.destin = destin;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDeparDate() {
        return deparDate;
    }

    public void setDeparDate(String deparDate) {
        this.deparDate = deparDate;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }
}

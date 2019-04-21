package msr.zerone.tourhelper.eventfragment.model;

public class EventModel {
    private String uid, name, startloc, destin, createdDate, deparDate, budget;

    public EventModel() {
    }

    public EventModel(String uid, String name, String startloc, String destin, String createdDate, String deparDate, String budget) {
        this.uid = uid;
        this.name = name;
        this.startloc = startloc;
        this.destin = destin;
        this.createdDate = createdDate;
        this.deparDate = deparDate;
        this.budget = budget;
    }

    public EventModel(String name, String startloc, String destin, String createdDate, String deparDate, String budget) {
        this.name = name;
        this.startloc = startloc;
        this.destin = destin;
        this.createdDate = createdDate;
        this.deparDate = deparDate;
        this.budget = budget;
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

    public String getBudget() {
        return budget;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }
}

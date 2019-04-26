package msr.zerone.tourhelper.eventfragment.model;

public class Budget {
    private String id, descrip, date, Budget;

    public Budget() {
    }

    public Budget(String id, String descrip, String date, String budget) {
        this.id = id;
        this.descrip = descrip;
        this.date = date;
        Budget = budget;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescrip() {
        return descrip;
    }

    public void setDescrip(String descrip) {
        this.descrip = descrip;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBudget() {
        return Budget;
    }

    public void setBudget(String budget) {
        Budget = budget;
    }
}

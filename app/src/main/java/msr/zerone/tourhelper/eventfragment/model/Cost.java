package msr.zerone.tourhelper.eventfragment.model;

public class Cost {
    private String id, descrip, foss, date, amount;

    public Cost() {
    }

    public Cost(String id, String descrip, String foss, String date, String amount) {
        this.id = id;
        this.descrip = descrip;
        this.foss = foss;
        this.date = date;
        this.amount = amount;
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

    public String getFoss() {
        return foss;
    }

    public void setFoss(String foss) {
        this.foss = foss;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

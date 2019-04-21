package msr.zerone.tourhelper.userfragment.model;

public class RegistrationModel {
    private String key;
    private String name, email, phone, pass;

    public RegistrationModel() {
    }

    public RegistrationModel(String name, String email, String phone, String pass) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
    }

    public RegistrationModel(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public RegistrationModel(String key, String name, String email, String phone, String pass) {
        this.key = key;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.pass = pass;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }
}

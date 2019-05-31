package lv.ok.models;

import lv.ok.utils.Utilities;

import java.util.Date;

public class User {

    private String username;
    private String password;
    private String id;
    private String company;
    private Date dateCreated;
    private String emailVerificationHash;

    public String getUsername() {return username;}
    public void  setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void  setPassword(String password) {this.password = password;}

    public String getId() {return id;}
    public void  setId(String id) {this.id = id;}

    public String getCompany() {return company;}

    public void  setCompany(String company) {
        if(company == null) {
            this.company = "";
        }
        else {
            this.company = company;
        }
    }

    public Date getDateCreated() {
        if (dateCreated == null){
            return null;
        }
        return dateCreated;}

    public void setDateCreated() {this.dateCreated = new Date();}

    public String getEmailVerificationHash() {return emailVerificationHash;}

    public void setEmailVerificationHash() {
        this.emailVerificationHash = new Utilities().generateRandomString();
    }
}

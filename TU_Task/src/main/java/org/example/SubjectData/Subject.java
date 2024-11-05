package org.example.SubjectData;

import java.io.Serializable;
/*
class which represents Subject
it is a DTO for input file
*/
public class Subject implements Serializable {

    private String userId;
    private String userName;

    // fields as per input actual_data csv file
    private String creditUtilisation;
    private String debtToIncome;
    private String accountAge;
    private String paymentHistory;
    private String recentInquiry;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCreditUtilisation() {
        return creditUtilisation;
    }

    public void setCreditUtilisation(String creditUtilisation) {
        this.creditUtilisation = creditUtilisation;
    }

    public String getDebtToIncome() {
        return debtToIncome;
    }

    public void setDebtToIncome(String debtToIncome) {
        this.debtToIncome = debtToIncome;
    }

    public String getAccountAge() {
        return accountAge;
    }

    public void setAccountAge(String accountAge) {
        this.accountAge = accountAge;
    }

    public String getPaymentHistory() {
        return paymentHistory;
    }

    public void setPaymentHistory(String paymentHistory) {
        this.paymentHistory = paymentHistory;
    }

    public String getRecentInquiry() {
        return recentInquiry;
    }

    public void setRecentInquiry(String recentInquiry) {
        this.recentInquiry = recentInquiry;
    }
}

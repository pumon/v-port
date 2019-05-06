package com.project.esh_an.portal1.model;

import android.content.Context;
import android.widget.Toast;

public class FeePayments {

    private String email;
    private String orderId;
    private String feeType;
    private String amount;

    private String sem;
    private String roomNo;
    private String usn;
    private String gender;

    private String month;
    private String cardNumber;
    private String expiryDate;

    public FeePayments() {}

    public FeePayments(String email, String orderId, String feeType, String amount, String sem,
                       String roomNo, String usn, String gender, String month, String cardNumber,
                       String expiryDate) {
        this.email = email;
        this.orderId = orderId;
        this.feeType = feeType;
        this.amount = amount;
        this.sem = sem;
        this.roomNo = roomNo;
        this.usn = usn;
        this.gender = gender;
        this.month = month;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSem() {
        return sem;
    }

    public void setSem(String sem) {
        this.sem = sem;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}


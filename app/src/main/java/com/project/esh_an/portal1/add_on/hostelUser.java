package com.project.esh_an.portal1.add_on;

/**
 * Created by Chandra on 4/24/2018.
 */

public class hostelUser {
    String roomnum,spinner,usn,amt;

    public hostelUser(){}

    public hostelUser(String roomnum, String spinner, String usn, String amt) {
        this.roomnum = roomnum;
        this.spinner = spinner;
        this.usn = usn;
        this.amt = amt;
    }

    public String getRoomnum() {
        return roomnum;
    }

    public void setRoomnum(String roomnum) {
        this.roomnum = roomnum;
    }

    public String getSpinner() {
        return spinner;
    }

    public void setSpinner(String spinner) {
        this.spinner = spinner;
    }

    public String getUsn() {
        return usn;
    }

    public void setUsn(String usn) {
        this.usn = usn;
    }

    public String getAmt() {
        return amt;
    }

    public void setAmt(String amt) {
        this.amt = amt;
    }
}

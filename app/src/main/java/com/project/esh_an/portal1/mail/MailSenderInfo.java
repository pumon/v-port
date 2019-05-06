package com.project.esh_an.portal1.mail;

public class MailSenderInfo {

     String senderEmail = "noreply.vemanait@gmail.com";
     String senderPassword = "feeportal";


     String mailSubject = "Payment Info";

    public MailSenderInfo() {
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public String getSenderPassword() {
        return senderPassword;
    }

    public String getMailSubject() {
        return mailSubject;
    }
}

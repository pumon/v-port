package com.project.esh_an.portal1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.project.esh_an.portal1.add_on.netConnection;
import com.project.esh_an.portal1.mail.GMailSender;
import com.project.esh_an.portal1.mail.MailSenderInfo;
import com.project.esh_an.portal1.model.FeePayments;
import com.project.esh_an.portal1.utility.Constants;

import com.project.esh_an.portal1.utility.SharedPreferenceUtils;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.project.esh_an.portal1.activity.InitialScreenActivity;
import com.project.esh_an.portal1.model.FeePayments;
import com.project.esh_an.portal1.utility.Constants;
import com.project.esh_an.portal1.utility.SharedPreferenceUtils;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class checksum extends AppCompatActivity implements PaytmPaymentTransactionCallback {
    String custid="", orderId="", mid="",amt="";
    private String mailBody = "";
    private String userName = "";
    private String amountValue = "";
    private String feeType = "";
    private String gender = "";
    private String roomNo = "";
    private String usn = "";
    private String semester = "";
    private String month = "";
    private FirebaseFirestore mFirestore;
    String collegeType = "";

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        //initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Intent intent = getIntent();
        if (getIntent().getExtras() != null) {
            amountValue = getIntent().getExtras().getString("amount");
            feeType = getIntent().getExtras().getString("feeType");
            userName = getIntent().getExtras().getString("userName");

            gender = getIntent().getExtras().getString("gender");
            roomNo = getIntent().getExtras().getString("roomNo");
            usn = getIntent().getExtras().getString("usn");
            semester = getIntent().getExtras().getString("sem");

            month = getIntent().getExtras().getString("month");
        }
        orderId = intent.getExtras().getString("orderid");
        custid = intent.getExtras().getString("custid");
        mid = "VxFseK90514008062136";
        amt=intent.getExtras().getString("amount");
        sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
        dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
// vollye , retrofit, asynch
    }
    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {
        private ProgressDialog dialog = new ProgressDialog(checksum.this);
        //private String orderId , mid, custid, amt;
        String url = "https://testiugale.000webhostapp.com/feeportal/paytm/generateChecksum.php";
        String varifyurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
        // "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID"+orderId;
        String CHECKSUMHASH = "";

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {
            JSONParser jsonParser = new JSONParser(checksum.this);
            String param =
                    "MID=" + mid +
                            "&ORDER_ID=" + orderId +
                            "&CUST_ID=" + custid +
                            "&CHANNEL_ID=WAP&TXN_AMOUNT=" + amt + "&WEBSITE=DEFAULT" +
                            "&CALLBACK_URL=" + varifyurl + "&INDUSTRY_TYPE_ID=Retail";
            JSONObject jsonObject = jsonParser.makeHttpRequest(url, "POST", param);
            // yaha per checksum ke saht order id or status receive hoga..
            Log.e("CheckSum result >>", jsonObject.toString());
            if (jsonObject != null) {
                Log.e("CheckSum result >>", jsonObject.toString());
                try {
                    CHECKSUMHASH = jsonObject.has("CHECKSUMHASH") ? jsonObject.getString("CHECKSUMHASH") : "";
                    Log.e("CheckSum result >>", CHECKSUMHASH);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return CHECKSUMHASH;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(" setup acc ", "  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            PaytmPGService Service = PaytmPGService.getProductionService();
            // when app is ready to publish use production service
            // PaytmPGService  Service = PaytmPGService.getProductionService();
            // now call paytm service here
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            HashMap<String, String> paramMap = new HashMap<String, String>();
            //these are mandatory parameters
            paramMap.put("MID", mid); //MID provided by paytm
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", custid);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", amt);
            paramMap.put("WEBSITE", "DEFAULT");
            paramMap.put("CALLBACK_URL", varifyurl);
            //paramMap.put( "EMAIL" , "abc@gmail.com");   // no need
            // paramMap.put( "MOBILE_NO" , "9144040888");  // no need
            paramMap.put("CHECKSUMHASH", CHECKSUMHASH);
            //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");
            PaytmOrder Order = new PaytmOrder(paramMap);
            Log.e("checksum ", "param " + paramMap.toString());
            Service.initialize(Order, null);
            // start payment service call here
            Service.startPaymentTransaction(checksum.this, true, true,
                    checksum.this);


        }
    }
    @Override
    public void onTransactionResponse(Bundle bundle) {
        Toast.makeText(this,"Please Wait",Toast.LENGTH_LONG).show();
        sendUser send=new sendUser();
        send.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    @Override
    public void networkNotAvailable() {
    }
    @Override
    public void clientAuthenticationFailed(String s) {
    }
    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " ui fail respon  "+ s );
        Toast.makeText(this,"UI errror",Toast.LENGTH_LONG).show();
        finish();

    }
    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading pagerespon true "+ s + "  s1 " + s1);
        finish();
    }


    @Override
    public void onBackPressedCancelTransaction() {
        Log.e("checksum ", " cancel call back respon  " );
        finish();
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Log.e("checksum ", "  transaction cancel " );
        finish();
    }

    private class SenMailTask extends AsyncTask<String, Void, Void> {
        final ProgressDialog progressDialog = new ProgressDialog(checksum.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please wait.. Sending Email");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(String... strings) {
            try {
                MailSenderInfo senderInfo = new MailSenderInfo();

                GMailSender sender = new GMailSender(senderInfo.getSenderEmail(), senderInfo.getSenderPassword());
                sender.sendMail(senderInfo.getMailSubject(),
                        strings[1],
                        senderInfo.getSenderEmail(),
                        strings[0]);
            } catch (Exception e) {
                Log.e("SendMail", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            finish();

        }
    }




    private class sendUser extends AsyncTask<String, Void, Void> {
        final ProgressDialog progressDialog = new ProgressDialog(checksum.this);

        @Override
        protected Void doInBackground(String... strings) {
            final String studentEmail = (String) SharedPreferenceUtils.get(
                    checksum.this, Constants.STUDENT_EMAIL, "aa");

            mailBody = "Hi, \n" +
                    "Your Payment details\n\n" +
                    "Fee Type: " + feeType +
                    "\nName: " + userName +
                    "\nOrderId: " + orderId +
                    "\nUSN: " + usn +
                    "\nAmount Paid : " + amountValue +
                    "\n\nRegards,\nPaymentTeam";
            initFirestore();
            FeePayments feePayments = new FeePayments(studentEmail, orderId, feeType,
                    amountValue, semester, roomNo, usn, gender, month, "XXXXXXXXXXXX",
                    "XX" + "/" + "XXXX");
            mFirestore.collection(collegeType)
                    .document(studentEmail).collection(Constants.FEE_PAYMENTS).document(feeType).
                    set(feePayments).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    new checksum.SenMailTask().execute(studentEmail, mailBody);
                }
            });

            return null;
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage("Please wait.. Processing your Payments..");
            progressDialog.show();

        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Toast.makeText(checksum.this, "Your payment is successfully completed", Toast.LENGTH_LONG).show();
            Log.d("PORTAL", "DocumentSnapshot successfully written!");
        }


    }
}



package com.project.esh_an.portal1.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.esh_an.portal1.R;
import com.project.esh_an.portal1.add_on.netConnection;
import com.project.esh_an.portal1.mail.GMailSender;
import com.project.esh_an.portal1.mail.MailSenderInfo;
import com.project.esh_an.portal1.model.FeePayments;
import com.project.esh_an.portal1.utility.Constants;
import com.project.esh_an.portal1.utility.MyTextWatcher;
import com.project.esh_an.portal1.utility.ServiceUtility;
import com.project.esh_an.portal1.utility.SharedPreferenceUtils;
//import com.sendgrid.SendGrid;
//import com.sendgrid.SendGridException;


public class InitialScreenActivity extends AppCompatActivity {
    private static final String SENDGRID_USERNAME = "developer629";

    private static final String SENDGRID_API_KEY = "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";
    private static final String SENDGRID_PASSWORD = "developer01";
//	private TextView taccessCode, tmerchantId, trsaKeyUrl, tredirectUrl, tcancelUrl;
//	private EditText accessCode, merchantId, currency, amount, orderId, rsaKeyUrl, redirectUrl, cancelUrl;
//	Button nextButton;

    private TextView orderIdView;
    private TextView amountView;
    private TextInputLayout cardNoLayout;
    private TextInputLayout monthLayout;
    private TextInputLayout yearLayout;
    private TextInputLayout cvvLayout;
    private EditText cardNoText;
    private EditText monthText;
    private EditText yearText;
    private EditText cvvText;

    private Button payButton;
    private String mailBody = "";
    private String userName = "";
    private String amountValue = "";
    private String feeType = "";
    private String gender = "";
    private String roomNo = "";
    private String usn = "";
    private String semester = "";
    private String orderId = "";
    private String month = "";
    String cardNo = "";
    String expiryMonth = "";
    String expiryYear = "";
    private FirebaseFirestore mFirestore;
    String collegeType = "";

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    private void init() {
//		accessCode = (EditText) findViewById(R.id.accessCode);
//		accessCode.setVisibility(View.INVISIBLE);
//		merchantId = (EditText) findViewById(R.id.merchantId);
//		merchantId.setVisibility(View.INVISIBLE);
//		orderId  = (EditText) findViewById(R.id.orderId);
//		currency = (EditText) findViewById(R.id.currency);
//		amount = (EditText) findViewById(R.id.amount);
//		rsaKeyUrl = (EditText) findViewById(R.id.rsaUrl);
//		rsaKeyUrl.setVisibility(View.INVISIBLE);
//		redirectUrl = (EditText) findViewById(R.id.redirectUrl);
//		redirectUrl.setVisibility(View.INVISIBLE);
//		cancelUrl = (EditText) findViewById(R.id.cancelUrl);
//		cancelUrl.setVisibility(View.INVISIBLE);
//		taccessCode = (TextView) findViewById(R.id.taccessCode);
//		taccessCode.setVisibility(View.INVISIBLE);
//		tmerchantId = (TextView) findViewById(R.id.tmerchantId);
//		tmerchantId.setVisibility(View.INVISIBLE);
//		currency = (EditText) findViewById(R.id.currency);
//		amount = (EditText) findViewById(R.id.amount);
//		trsaKeyUrl = (TextView) findViewById(R.id.trsaUrl);
//		trsaKeyUrl.setVisibility(View.INVISIBLE);
//		tredirectUrl = (TextView) findViewById(R.id.tredirectUrl);
//		tredirectUrl.setVisibility(View.INVISIBLE);
//		tcancelUrl = (TextView) findViewById(R.id.tcancelUrl);
//		tcancelUrl.setVisibility(View.INVISIBLE);

        initFirestore();

        orderIdView = findViewById(R.id.order_id_view);
        amountView = findViewById(R.id.amount_view);
        payButton = findViewById(R.id.pay_button);
        cardNoLayout = findViewById(R.id.card_no_layout);
        monthLayout = findViewById(R.id.month_layout);
        yearLayout = findViewById(R.id.year_layout);
        cvvLayout = findViewById(R.id.cvv_layout);
        cardNoText = findViewById(R.id.card_no_text);
        monthText = findViewById(R.id.month_text);
        yearText = findViewById(R.id.year_text);
        cvvText = findViewById(R.id.cvv_text);

        if (getIntent().getExtras() != null) {
            amountValue = getIntent().getExtras().getString("amount");
            amountView.setText(amountValue);
            feeType = getIntent().getExtras().getString("feeType");
            userName = getIntent().getExtras().getString("userName");

            gender = getIntent().getExtras().getString("gender");
            roomNo = getIntent().getExtras().getString("roomNo");
            usn = getIntent().getExtras().getString("usn");
            semester = getIntent().getExtras().getString("sem");

            month = getIntent().getExtras().getString("month");
        }
        userName = (String) SharedPreferenceUtils.get(InitialScreenActivity.this, Constants.STUDENT_NAME, "N/A");

        cardNoText.addTextChangedListener(new MyTextWatcher(cardNoLayout));
        monthText.addTextChangedListener(new MyTextWatcher(monthLayout));
        yearText.addTextChangedListener(new MyTextWatcher(yearLayout));
        cvvText.addTextChangedListener(new MyTextWatcher(cvvLayout));

        String type = (String) SharedPreferenceUtils.get(InitialScreenActivity.this, Constants.COLLEGE_TYPE, "aa");
        switch (type) {
            case Constants.BE:
                collegeType = Constants.BE_STUDENTS_INFO;
                break;
            case Constants.MBA:
                collegeType = Constants.MBA_STUDENTS_INFO;
                break;
            case Constants.PUC:
                collegeType = Constants.PUC_STUDENTS_INFO;
                break;
        }

        payButton.setText("Proceed to Pay(" + "Rs." + amountValue + ")");
        payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isValiDated()) {

                    final ProgressDialog progressDialog = new ProgressDialog(InitialScreenActivity.this);
                    progressDialog.setMessage("Please wait.. Processing your Payments..");
                    progressDialog.show();

                    final String studentEmail = (String) SharedPreferenceUtils.get(
                            InitialScreenActivity.this, Constants.STUDENT_EMAIL, "aa");

                    mailBody = "Hi, \n" +
                            "Your Payment details\n\n" +
                            "Fee Type: " + feeType +
                            "\nName: " + userName +
                            "\nOrderId: " + orderId +
                            "\nUSN: " + usn +
                            "\nAmount Paid : " + amountValue +
                            "\n\nRegards,\nPaymentTeam";
                    FeePayments feePayments = new FeePayments(studentEmail, orderId, feeType,
                            amountValue, semester, roomNo, usn, gender, month, cardNo,
                            expiryMonth + "/" + expiryYear);
                    mFirestore.collection(collegeType)
                            .document(studentEmail).collection(Constants.FEE_PAYMENTS).document(feeType).
                            set(feePayments).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
//                            sendMail2(studentEmail);


                            new SenMailTask().execute(studentEmail, mailBody);
                            Toast.makeText(InitialScreenActivity.this, "Your payment is successfully completed", Toast.LENGTH_LONG).show();
//                            finish();
                            Log.d("PORTAL", "DocumentSnapshot successfully written!");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Log.w("PORTAL", "Error writing document", e);
                        }
                    });

                }
            }
        });

    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initial_screen);
        init();

        //net connection
        netConnection checkCon = new netConnection(this);
        if (checkCon.isConnected() == false) {
            new AlertDialog.Builder(this)
                    .setMessage("Internet Connection not Available..!!!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(Settings.ACTION_SETTINGS);
                            startActivity(intent);
                        }
                    })
                    .show();
        }
//		nextButton = (Button) findViewById(R.id.nextButton);
//		nextButton.setOnClickListener(this);
    }

//	public void onClick(View view) {
//		switch (view.getId()) {
//			case R.id.nextButton:
    //Mandatory parameters. Other parameters can be added if required.
//				String vAccessCode = ServiceUtility.chkNull(accessCode.getText()).toString().trim();
//				String vMerchantId = ServiceUtility.chkNull(merchantId.getText()).toString().trim();
//				String vCurrency = ServiceUtility.chkNull(currency.getText()).toString().trim();
//				String vAmount = ServiceUtility.chkNull(amount.getText()).toString().trim();
//				if (!vAccessCode.equals("") && !vMerchantId.equals("") && !vCurrency.equals("") && !vAmount.equals("")) {
//					Intent intent = new Intent(this, WebViewActivity.class);
//					intent.putExtra(AvenuesParams.ACCESS_CODE, ServiceUtility.chkNull(accessCode.getText()).toString().trim());
//					intent.putExtra(AvenuesParams.MERCHANT_ID, ServiceUtility.chkNull(merchantId.getText()).toString().trim());
//					intent.putExtra(AvenuesParams.ORDER_ID, ServiceUtility.chkNull(orderId.getText()).toString().trim());
//					intent.putExtra(AvenuesParams.CURRENCY, ServiceUtility.chkNull(currency.getText()).toString().trim());
//					intent.putExtra(AvenuesParams.AMOUNT, ServiceUtility.chkNull(amount.getText()).toString().trim());
//
//					intent.putExtra(AvenuesParams.REDIRECT_URL, ServiceUtility.chkNull(redirectUrl.getText()).toString().trim());
//					intent.putExtra(AvenuesParams.CANCEL_URL, ServiceUtility.chkNull(cancelUrl.getText()).toString().trim());
//					intent.putExtra(AvenuesParams.RSA_KEY_URL, ServiceUtility.chkNull(rsaKeyUrl.getText()).toString().trim());
//
//
//					startActivity(intent);
//				} else {
//					showToast("All parameters are mandatory.");
//				}
//		}
//	}

    public void showToast(String msg) {
        Toast.makeText(this, "Caution: " + msg, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        //generating new order number for every transaction
        Integer randomNum = ServiceUtility.randInt(0, 9999999);
        orderId = randomNum.toString();
        orderIdView.setText(randomNum.toString());

    }

    private boolean isValiDated() {
        boolean isValid = false;
        cardNo = cardNoText.getText().toString();
        expiryMonth = monthText.getText().toString();
        expiryYear = yearText.getText().toString();
        String cvv = cvvText.getText().toString();

        if (TextUtils.isEmpty(cardNo) && TextUtils.isEmpty(expiryMonth) && TextUtils.isEmpty(expiryYear) &&
                TextUtils.isEmpty(cvv)) {
            Toast.makeText(InitialScreenActivity.this, "Enter all Fields", Toast.LENGTH_SHORT).show();
        } else if (cardNo.length() < 15) {
            cardNoLayout.setError("Enter valid Card No.");
        } else if (TextUtils.isEmpty(expiryMonth)) {
            monthLayout.setError("Enter Expiry Month(01-12)");
        } else if (Integer.parseInt(expiryMonth) < 1 || Integer.parseInt(expiryMonth) > 12) {
            monthLayout.setError("Enter valid Month(1-12)");
        } else if (TextUtils.isEmpty(expiryYear)) {
            yearLayout.setError("Enter Expiry Year");
        } else if (Integer.parseInt(expiryYear) < 18 || Integer.parseInt(expiryYear) > 35) {
            yearLayout.setError("Enter valid Year");
        } else if (TextUtils.isEmpty(cvv)) {
            cvvLayout.setError("Enter CVV");
        } else if (Integer.parseInt(cvv) < 100) {
            cvvLayout.setError("Enter valid CVV");
        } else {
            isValid = true;
        }
        return isValid;
    }

    private class SenMailTask extends AsyncTask<String, Void, Void> {
        final ProgressDialog progressDialog = new ProgressDialog(InitialScreenActivity.this);

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
}
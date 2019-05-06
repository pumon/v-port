package com.project.esh_an.portal1;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.project.esh_an.portal1.add_on.netConnection;
import com.project.esh_an.portal1.model.Student;
import com.project.esh_an.portal1.utility.Constants;
import com.project.esh_an.portal1.utility.SharedPreferenceUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static android.widget.Toast.LENGTH_LONG;

public class Register extends AppCompatActivity {
    Button ok;
    EditText name, usn, email, pswd, cnfpswd;
    Spinner spbranch, spyear;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
//    DatabaseReference databaseStudents;
    TextView result;
    private CheckBox checkBox2;
    MobileClass mobileClass;
    String YouEditTextValue, resultt,status,YourOTP,statuss;
    ArrayAdapter<CharSequence> adapter, adapter1;
    OTPclass otPclass;

    private FirebaseFirestore mFirestore;
    private Query mQuery;
    String studentEmail = "";
    String collegeType = "";

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        studentEmail = (String) SharedPreferenceUtils.get(Register.this, Constants.COLLEGE_TYPE, "aa");

        //net connection
        netConnection checkCon=new netConnection(this);
        if(checkCon.isConnected()==false){
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

        firebaseAuth = FirebaseAuth.getInstance();
        initFirestore();

//        databaseStudents = FirebaseDatabase.getInstance().getReference("students");


        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        name = (EditText) findViewById(R.id.name);
        usn = (EditText) findViewById(R.id.usn);
        email = (EditText) findViewById(R.id.email);
        pswd = (EditText) findViewById(R.id.pswd);
        cnfpswd = (EditText) findViewById(R.id.cnfpswd);
        progressDialog =new ProgressDialog(this);

        spbranch = (Spinner) findViewById(R.id.spbranch);
        spyear = (Spinner) findViewById(R.id.spyear);

        adapter = ArrayAdapter.createFromResource(this, R.array.branch, android.R.layout.simple_spinner_item);

        switch (studentEmail) {
            case Constants.BE:
                usn.setHint("USN");
                adapter1 = ArrayAdapter.createFromResource(this, R.array.spyear, android.R.layout.simple_spinner_item);
                spbranch.setVisibility(View.VISIBLE);
                collegeType = (Constants.BE_STUDENTS_INFO);
                break;
            case Constants.MBA:
                usn.setHint("Roll No.");
                adapter1 = ArrayAdapter.createFromResource(this, R.array.MBA_Year, android.R.layout.simple_spinner_item);
                spbranch.setVisibility(View.GONE);
                collegeType = (Constants.MBA_STUDENTS_INFO);
                break;
            case Constants.PUC:
                usn.setHint("Roll No.");
                adapter1 = ArrayAdapter.createFromResource(this, R.array.PUC_Year, android.R.layout.simple_spinner_item);
                spbranch.setVisibility(View.GONE);
                collegeType = (Constants.PUC_STUDENTS_INFO);
                break;
        }

        spbranch.setAdapter(adapter);
        spyear.setAdapter(adapter1);

        spbranch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {

                    Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) + " selected", LENGTH_LONG).show();
                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spyear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    Toast.makeText(getBaseContext(), adapterView.getItemAtPosition(i) + " selected", LENGTH_LONG).show();
                } else {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        checkBox2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b){
                    pswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    cnfpswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    pswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    cnfpswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });

        ok = (Button) findViewById(R.id.ok);
        result = (TextView) findViewById(R.id.md5);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                EditText et = findViewById(R.id.pswd);
                String pass1 = et.getText().toString();
                EditText et2 = findViewById(R.id.cnfpswd);
                String pass2 = et2.getText().toString();
                et.getEditableText().toString();

                if (!email.getEditableText().toString().isEmpty() ){
               //     if (!email.getText().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())){
                    if (pass1.length() > 6) {
                        if (pass1.equals(pass2)) {
                            //      startActivity(new Intent("com.project.esh_an.vemanafeeportal"));
                            if (validate()) {

                                progressDialog.setMessage("Registering User..!!");
                                progressDialog.show();


                                //Upload data to the database
                                String user_email = email.getText().toString().trim();
                                String user_password = pswd.getText().toString().trim();

                                firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                            if (task.isSuccessful()) {
                                                progressDialog.dismiss();
                                                computeMD5Hash(pswd.toString());
                                                addStudent();

                                                sendEmailVerification();


                                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);

                                                final EditText edittext = new EditText(Register.this);
                                               // builder.setMessage("Enter Your Message");
                                                //builder.setTitle("Enter Your Title");

                                                builder.setView(edittext);
                                                builder.setMessage("ThankYou for, Please CONFIRM your Mobile Number");
                                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {
                                                        Log.d("AlertDialog","positive");
                                                       // startActivity(new Intent(Register.this, Signin.class));
                                                        YouEditTextValue = edittext.getText().toString();
                                                        Log.v("TAG","msg is"+YouEditTextValue);
                                                        new MobileAsyncTask().execute("http://18.222.155.100:4005/user/register");
//
                                                    }
                                                });
                                                AlertDialog dialog = builder.create();
                                                dialog.show();

                                                //startActivity(new Intent(Register.this, Signin.class));
                                            } else {
                                                    progressDialog.dismiss();
                                                    Snackbar.make(view, "Error Occurred", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                                                }
                                    }
                                });
                            }
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                            builder.setTitle("Attention !");
                            builder.setMessage("Passwords Should be Same");
                            AlertDialog dialog = builder.show();
                        }
                    } else {
                        Snackbar.make(view, "password must be greater than 6 digits", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    }
                }
                else{
                    Snackbar.make(view,"Enter All Details",Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    private void sendEmailVerification() {
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        if(user != null){
            if(user.isEmailVerified()){
                Log.d("EmailVerification ", "Email Verified");
            }else{
                Log.d("EmailVerification ", "Sending verification link");
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d("EmailVerification ", "Email Sent");
                        }else{
                            Log.d("EmailVerification ", "Task Failed");
                        }
                    }
                });
            }
        }
    }


    private void addStudent() {

        String dname = name.getText().toString().trim();
        SharedPreferenceUtils.set(Register.this, Constants.STUDENT_NAME, dname);
        String dusn = usn.getText().toString();
        String rbranch = "";
        if(spbranch.getVisibility() == View.VISIBLE){
            rbranch = spbranch.getSelectedItem().toString();
        }
        String rspyear = spyear.getSelectedItem().toString();
        String remail = email.getText().toString().trim();
        String rpswd = pswd.getText().toString();
        //String rcnfpswd = cnfpswd.getText().toString();
        String empass = result.getText().toString().trim();

        if (!TextUtils.isEmpty(dname)) {

//            String id = databaseStudents.push().getKey();
//            user student = new user(dname, dusn, rbranch, rspyear, remail, empass);
//            databaseStudents.child(id).setValue(student);
            Student student = new Student(dname, dusn, rbranch, rspyear, remail, empass);
            mFirestore.collection(collegeType)
                    .document(remail).set(student, SetOptions.merge()).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("PORTAL", "DocumentSnapshot successfully written!");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w("PORTAL", "Error writing document", e);
                }
            });

        }
    }

    private Boolean validate(){
        Boolean result = false;

        String vname = name.getText().toString();
        String password = pswd.getText().toString();
        String vemail = email.getText().toString();

        if(vname.isEmpty() || password.isEmpty() || vemail.isEmpty()){
            Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
        }else if (!vemail.contains("@")) {
            Toast.makeText(this, "Enter the Correct Email", Toast.LENGTH_SHORT).show();
        }//else if (!vemail.equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
            //Toast.makeText(this, "Email Already Exists", Toast.LENGTH_SHORT).show();
        else   {
            result = true;
        }

        return result;
    }

    public void computeMD5Hash(String password){
        try{
            //create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer MD5Hash = new StringBuffer();
            for(int i=0;i<messageDigest.length;i++){
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length()<2)
                    h = "0"+h;
                MD5Hash.append(h);
            }
            result.setText(MD5Hash);
        }
        catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        netConnection checkCon=new netConnection(this);
        if(checkCon.isConnected()==false){
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

    }

    private class MobileAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            mobileClass = new MobileClass();
            mobileClass.setMobilenumber(YouEditTextValue);
          //  updateMarketclass.setRate(rate.getEditableText().toString());


            return POST(urls[0],mobileClass);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String resultt) {
            //  Toast.makeText(getBaseContext(), "hello "+result, Toast.LENGTH_LONG).show();

            String trr = "true";
            if(status==trr){
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);

                final EditText edittext = new EditText(Register.this);
                // builder.setMessage("Enter Your Message");
                //builder.setTitle("Enter Your Title");

                builder.setView(edittext);
                builder.setMessage("PLEASE ENTER OTP");
                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("AlertDialog","positive");
                        // startActivity(new Intent(Register.this, Signin.class));
                        YourOTP = edittext.getText().toString();
                        Log.v("TAG","msg is"+YouEditTextValue);
                        new MobileVerifyAsyncTask().execute("http://18.222.155.100:4005/user/verify");
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

                //startActivity(new Intent(Register.this, Signin.class));

            }else {
//                Toast.makeText(UpdateMarketValue.this, "Successfully uploaded", Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(UpdateMarketValue.this,MainActivity.class);
//                // intent.putExtra("nameofuser",name);
//                // Log.v("tag","name login"+name);
//                startActivity(intent);
                return;
            }
        }
    }

//    private boolean validate(){
//        if(login_MobileNum.getText().toString().trim().equals(""))
//            return false;
//        else if(login_pass_word.getText().toString().trim().equals(""))
//            return false;
//        else
//            return true;
//    }
//
    public String POST(String url, MobileClass mobileClass){
        InputStream inputStream = null;
        try {

            // 1. create HttpClient

            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();

            jsonObject.accumulate("mobile", MobileClass.getMobilenumber());
//            jsonObject.accumulate("date", UpdateMarketclass.getDate());
//            jsonObject.accumulate("district", "MANDYA");
//            jsonObject.accumulate("rate", UpdateMarketclass.getRate());
//            jsonObject.accumulate("commodity", UpdateMarketclass.getCommodity());
//            // jsonObject.accumulate("iduser",pref.setUserId(iduser));

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
            Log.v("tag","json"+json);

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
            Log.v("tag","se"+se);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            Log.v("tag","httpResponse"+httpResponse);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            Log.v("tag","inputStream"+inputStream);

            // 10. convert inputstream to string
            if(inputStream != null) {
                resultt = convertInputStreamToString(inputStream);
                Log.v("result",resultt);

                JSONObject finalresult = new JSONObject(resultt);
                status = finalresult.getString("success");
                Log.v("status",""+status);
            }
            else
                resultt = "Did not work!";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }
        // 11. return result
        return resultt;

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";

        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    private class MobileVerifyAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            otPclass = new OTPclass();
            otPclass.setOtp(YourOTP);
            //  updateMarketclass.setRate(rate.getEditableText().toString());
            return POST(urls[0],otPclass);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String resultt) {
            //  Toast.makeText(getBaseContext(), "hello "+result, Toast.LENGTH_LONG).show();
            String trr = "true";
            if(statuss==trr){
                Toast.makeText(Register.this,"OTP VERIFIED SUCCESSFULLY",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Register.this, Signin.class));
                finish();
            }else {
                return;
            }
        }
    }

    public String POST(String url, OTPclass otPclass){
        InputStream inputStream = null;
        try {

            // 1. create HttpClient

            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();

            jsonObject.accumulate("mobile", MobileClass.getMobilenumber());
            jsonObject.accumulate("otp", OTPclass.getOtp());
//            jsonObject.accumulate("date", UpdateMarketclass.getDate());
//            jsonObject.accumulate("district", "MANDYA");
//            jsonObject.accumulate("rate", UpdateMarketclass.getRate());
//            jsonObject.accumulate("commodity", UpdateMarketclass.getCommodity());
//            // jsonObject.accumulate("iduser",pref.setUserId(iduser));

            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();
            Log.v("tag","json"+json);

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json);
            Log.v("tag","se"+se);

            // 6. set httpPost Entity
            httpPost.setEntity(se);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);
            Log.v("tag","httpResponse"+httpResponse);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();
            Log.v("tag","inputStream"+inputStream);

            // 10. convert inputstream to string
            if(inputStream != null) {
                resultt = convertInputStreamToString(inputStream);
                Log.v("result",resultt);

                JSONObject finalresult = new JSONObject(resultt);
                statuss = finalresult.getString("success");
                Log.v("status",""+statuss);
            }
            else
                resultt = "Did not work!";
        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return resultt;
    }
}





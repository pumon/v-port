package com.project.esh_an.portal1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.esh_an.portal1.add_on.Menu;
import com.project.esh_an.portal1.add_on.netConnection;
import com.project.esh_an.portal1.add_on.resetPass;
import com.project.esh_an.portal1.utility.Constants;
import com.project.esh_an.portal1.utility.SharedPreferenceUtils;


public class Signin extends Register implements View.OnClickListener {

    Button blogin;
    private EditText usrname, pswd;
    private TextView fgtpswd, signup;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_signin);


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

        usrname = (EditText) findViewById(R.id.usrname);
        pswd = (EditText) findViewById(R.id.pswd);
        blogin = (Button) findViewById(R.id.login);
        fgtpswd = (TextView) findViewById(R.id.fgtpswd);
        signup = (TextView) findViewById(R.id.signup);

        checkBox = (CheckBox) findViewById(R.id.checkBox);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        final FirebaseUser user = firebaseAuth.getCurrentUser();

        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(usrname.getText()) && !TextUtils.isEmpty(pswd.getText())) {
                    validate(usrname.getText().toString(), pswd.getText().toString());
                } else {
                    Toast.makeText(Signin.this, "fill the details correctly and try again....", Toast.LENGTH_SHORT).show();
                }
                /*if (user != null) {
                    if (user.isEmailVerified()) {
                        Toast.makeText(Signin.this, "Email Verified", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(Signin.this, "Please verify the email", Toast.LENGTH_LONG).show();

                    }
                }*//*else{
                    Toast.makeText(Signin.this, "Please Signin", Toast.LENGTH_SHORT).show();
                }*/

            }
        });


        fgtpswd.setOnClickListener(this);
        signup.setOnClickListener(this);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    pswd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    pswd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.fgtpswd:
                startActivity(new Intent(Signin.this, resetPass.class));
                // Toast.makeText(Signin.this, "Nothing is Linked", Toast.LENGTH_LONG).show();
                break;
            case R.id.signup:
                startActivity(new Intent(this, Register.class));
                break;
        }

    }

    private void validate(final String userName, final String userPassword) {

        progressDialog.setMessage("Wait...!! Sign In Progress");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(userName, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    SharedPreferenceUtils.set(Signin.this, Constants.STUDENT_EMAIL, userName);
                    Toast.makeText(Signin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Signin.this, Menu.class));
                    finish();
                } else {
                    progressDialog.dismiss();
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signin.this);
                    builder.setMessage("Invalid E-mail/Password");
                    builder.setPositiveButton("ok", null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.RED));
                }
            }
        });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    }
}
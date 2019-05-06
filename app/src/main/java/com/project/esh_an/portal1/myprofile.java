package com.project.esh_an.portal1;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.esh_an.portal1.add_on.netConnection;
import com.project.esh_an.portal1.model.Student;
import com.project.esh_an.portal1.utility.Constants;
import com.project.esh_an.portal1.utility.SharedPreferenceUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class myprofile extends Signin implements View.OnClickListener {


    TextView u_name, u_usn, u_year, u_branch, u_email, textView9, textView5;
    ImageView imageView;
//    private FirebaseAuth firebaseAuth;
    private static final int REQ_CODE = 9001;
    //    DatabaseReference databaseStudents;
    ArrayList<String> userInfoList = new ArrayList<>();
    private static final String TAG = "PORTAL";
    // ...
    private FirebaseFirestore mFirestore;

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getOverflowMenu();
        setContentView(R.layout.activity_myprofile);

        initFirestore();
        u_name = (TextView) findViewById(R.id.name_myprofile);
        u_usn = (TextView) findViewById(R.id.usn_myprofile);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView9 = (TextView) findViewById(R.id.textView9);
        u_year = (TextView) findViewById(R.id.year_myprofile);
        u_branch = (TextView) findViewById(R.id.branch_myprofile);
        u_email = (TextView) findViewById(R.id.email_myprofile);
        imageView = (ImageView) findViewById(R.id.imageView);

//        firebaseAuth = FirebaseAuth.getInstance();
//        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();


        /*databaseStudents = FirebaseDatabase.getInstance().getReference("students");
        databaseStudents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (currentUser.getEmail().equalsIgnoreCase(ds.getValue(user.class).getEmail())) {
                        user userInfo = ds.getValue(user.class);
                        u_name.setText(userInfo.name);
                        u_usn.setText(userInfo.usn);
                        u_year.setText(userInfo.year);
                        u_email.setText(userInfo.email);
                        u_branch.setText(userInfo.branch);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
        String type = (String) SharedPreferenceUtils.get(myprofile.this, Constants.COLLEGE_TYPE, "aa");
        switch (type) {
            case Constants.BE:
                collegeType = Constants.BE_STUDENTS_INFO;
                textView9.setText("USN");
                u_usn.setHint("USN");
                u_branch.setVisibility(View.VISIBLE);
                textView5.setVisibility(View.VISIBLE);
                break;
            case Constants.MBA:
                collegeType = Constants.MBA_STUDENTS_INFO;
                textView9.setText("Roll No.");
                u_usn.setHint("Roll No.");
                u_branch.setVisibility(View.GONE);
                textView5.setVisibility(View.GONE);
                break;
            case Constants.PUC:
                collegeType = Constants.PUC_STUDENTS_INFO;
                textView9.setText("Roll No.");
                u_usn.setHint("Roll No.");
                u_branch.setVisibility(View.GONE);
                textView5.setVisibility(View.GONE);
                break;
        }
        final String studentEmail = (String) SharedPreferenceUtils.get(myprofile.this, Constants.STUDENT_EMAIL, "aa");
        DocumentReference docRef = mFirestore.collection(collegeType).document(studentEmail);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Student student = documentSnapshot.toObject(Student.class);
                    u_name.setText(student.getName());
                    u_usn.setText(student.getUsn());
                    u_year.setText(student.getYear());
                    u_email.setText(student.getEmail());
                    if(u_branch.getVisibility() == View.VISIBLE) {
                        u_branch.setText(student.getBranch());
                    }
                }
            }
        });


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
    }

    private void getOverflowMenu() {

        try {
            ViewConfiguration config = ViewConfiguration.get(this);
            Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if (menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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




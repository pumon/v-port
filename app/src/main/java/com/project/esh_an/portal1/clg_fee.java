package com.project.esh_an.portal1;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
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
import com.project.esh_an.portal1.activity.InitialScreenActivity;
import com.project.esh_an.portal1.add_on.netConnection;
import com.project.esh_an.portal1.model.Student;
import com.project.esh_an.portal1.utility.Constants;
import com.project.esh_an.portal1.utility.SharedPreferenceUtils;

import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class clg_fee extends AppCompatActivity {

    Button ok;
    TextView c_name,c_usn,c_year,c_branch,c_email, textView9, textView5;
//    private FirebaseAuth firebaseAuth;
//    DatabaseReference databaseStudents;
    ArrayList<String> userInfoList = new ArrayList<>();
    String userName = "";
    String userUSN = "";
private String collegeType = "";
    private FirebaseFirestore mFirestore;

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_clg_fee);
        initFirestore();
        c_name = (TextView) findViewById(R.id.name_clgfee);
        c_usn = (TextView) findViewById(R.id.usn_clgfee);
        textView5 = (TextView) findViewById(R.id.textView5);
        textView9 = (TextView) findViewById(R.id.textView9);
        c_year = (TextView) findViewById(R.id.year_clgfee);
        c_branch = (TextView) findViewById(R.id.branch_clgfee);
        c_email  = (TextView) findViewById(R.id.email_clgfee);

        final EditText c_amount = (EditText) findViewById(R.id.amnt);

        ok = findViewById(R.id.ok_clgfee);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(clg_fee.this, checksum.class);
                i.putExtra("amount",c_amount.getText().toString());
                i.putExtra("feeType","College Fee");
                i.putExtra("userName",userName);
                i.putExtra("usn",userUSN);
                i.putExtra("custid",userUSN);
                i.putExtra("orderid",new randnum().getNum());
                startActivity(i);
                finish();

            }
        });

//        firebaseAuth = FirebaseAuth.getInstance();
//        final FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//        databaseStudents = FirebaseDatabase.getInstance().getReference("students");
//        databaseStudents.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    if (currentUser.getEmail().equalsIgnoreCase(ds.getValue(user.class).getEmail())) {
//                        user userInfo = ds.getValue(user.class);
//                        c_name.setText(userInfo.name);
//                        c_usn.setText(userInfo.usn);
//                        c_year.setText(userInfo.year);
//                        c_email.setText(userInfo.email);
//                        c_branch.setText(userInfo.branch);
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

        String type = (String) SharedPreferenceUtils.get(clg_fee.this, Constants.COLLEGE_TYPE, "aa");
        switch (type) {
            case Constants.BE:
                collegeType = Constants.BE_STUDENTS_INFO;
                textView9.setText("USN");
                c_usn.setHint("USN");
                c_branch.setVisibility(View.VISIBLE);
                textView5.setVisibility(View.VISIBLE);
                break;
            case Constants.MBA:
                collegeType = Constants.MBA_STUDENTS_INFO;
                textView9.setText("Roll No.");
                c_usn.setHint("Roll No.");
                c_branch.setVisibility(View.GONE);
                textView5.setVisibility(View.GONE);
                break;
            case Constants.PUC:
                collegeType = Constants.PUC_STUDENTS_INFO;
                textView9.setText("Roll No.");
                c_usn.setHint("Roll No.");
                c_branch.setVisibility(View.GONE);
                textView5.setVisibility(View.GONE);
                break;
        }
        final String studentEmail = (String) SharedPreferenceUtils.get(clg_fee.this, Constants.STUDENT_EMAIL, "aa");
        DocumentReference docRef = mFirestore.collection(collegeType).document(studentEmail);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Student student = documentSnapshot.toObject(Student.class);
                    c_name.setText(student.getName());
                    userName = student.getName();
                    userUSN = student.getUsn();
                    c_usn.setText(student.getUsn());
                    c_year.setText(student.getYear());
                    c_email.setText(student.getEmail());
                    if(c_branch.getVisibility() == View.VISIBLE) {
                        c_branch.setText(student.getBranch());
                    }
                }
            }
        });

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
 }

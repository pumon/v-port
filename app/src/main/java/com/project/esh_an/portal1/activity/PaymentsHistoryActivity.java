package com.project.esh_an.portal1.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.esh_an.portal1.PaymentsHistoryAdapter;
import com.project.esh_an.portal1.R;
import com.project.esh_an.portal1.Register;
import com.project.esh_an.portal1.model.FeePayments;
import com.project.esh_an.portal1.myprofile;
import com.project.esh_an.portal1.utility.Constants;
import com.project.esh_an.portal1.utility.SharedPreferenceUtils;

import java.util.List;

public class PaymentsHistoryActivity extends AppCompatActivity {

    private static final String TAG = "PORTAL";
    String type = "";
    String collegeType = "";

    private FirebaseFirestore mFirestore;

    private void initFirestore() {
        mFirestore = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_history);

        initFirestore();
        type = (String) SharedPreferenceUtils.get(PaymentsHistoryActivity.this, Constants.COLLEGE_TYPE, "aa");
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
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        //    private RecyclerView.Adapter mAdapter;
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        final String studentEmail = (String) SharedPreferenceUtils.get(
                PaymentsHistoryActivity.this, Constants.STUDENT_EMAIL, "aa");
        final DocumentReference docRef = mFirestore.collection(collegeType).
                document(studentEmail).collection(Constants.FEE_PAYMENTS).document();

        CollectionReference collectionReference = mFirestore.collection(collegeType).
                document(studentEmail).collection(Constants.FEE_PAYMENTS);
        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult() != null) {
                        List<DocumentSnapshot> documents = task.getResult().getDocuments();
                        if(documents.size() != 0) {
                            PaymentsHistoryAdapter myAdapter = new PaymentsHistoryAdapter(PaymentsHistoryActivity.this, documents);
                            recyclerView.setAdapter(myAdapter);
                        }else {
                            Toast.makeText(PaymentsHistoryActivity.this, "No Payments Done So far!!", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }
            }
        });

//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
////                    for (QueryDocumentSnapshot document : task.getResult().getData()) {
//                        Log.d(TAG, document.getId() + " => " + document.getData());
////                    }
//                } else {
//                    Log.d(TAG, "Error getting documents: ", task.getException());
//                }
//
//            }
//        });

        // specify an adapter (see also next example)
//        PaymentsHistoryAdapter myAdapter = new PaymentsHistoryAdapter();
//        recyclerView.setAdapter(myAdapter);
    }
}

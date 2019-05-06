package com.project.esh_an.portal1;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.project.esh_an.portal1.model.FeePayments;

import java.util.List;

public class PaymentsHistoryAdapter extends RecyclerView.Adapter<PaymentsHistoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<DocumentSnapshot> documentSnapshots;

    public PaymentsHistoryAdapter(Context context, List<DocumentSnapshot> snapshots) {
        this.mContext = context;
        this.documentSnapshots = snapshots;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_payments_history, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DocumentSnapshot snapshot = documentSnapshots.get(position);
        if(snapshot.exists()) {
            FeePayments payments = snapshot.toObject(FeePayments.class);
            holder.feeTypeView.setText(payments.getFeeType());
            holder.orderIdView.setText("Order Id : " +payments.getOrderId());
            holder.amountView.setText("Amount Paid : "+ payments.getAmount());
        }
    }

    @Override
    public int getItemCount() {
        return documentSnapshots.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView feeTypeView;
        TextView orderIdView;
        TextView amountView;

        public MyViewHolder(View itemView) {
            super(itemView);

            feeTypeView = itemView.findViewById(R.id.fee_type_view);
            orderIdView = itemView.findViewById(R.id.order_id_view);
            amountView = itemView.findViewById(R.id.amount_view);
        }
    }
}

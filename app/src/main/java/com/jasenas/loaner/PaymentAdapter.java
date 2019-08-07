package com.jasenas.loaner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.PaymentViewHolder> {
    private List<Payment> dateset;
    private NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.GERMANY);
    private OnPaymentListener mOnPaymentListener;

    public PaymentAdapter(Context context, List<Payment> dataset) {
        this.dateset = dataset;
        this.mOnPaymentListener = (OnPaymentListener) context;
    }

    public class PaymentViewHolder extends RecyclerView.ViewHolder {
        OnPaymentListener onPaymentListener;
        TextView name;
        TextView amount;
        ImageButton status;


        public PaymentViewHolder(View view, final OnPaymentListener onPaymentListener) {
            super(view);

            this.onPaymentListener = onPaymentListener;

            this.name = view.findViewById(R.id.name);
            this.amount = view.findViewById(R.id.amount);
            this.status = view.findViewById(R.id.status);

            this.status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onPaymentListener.onStatusClick(getAdapterPosition());
                }
            });
        }
    }

    @Override
    public PaymentAdapter.PaymentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.payment_list_row, parent, false);
        return new PaymentAdapter.PaymentViewHolder(itemView, mOnPaymentListener);
    }

    @Override
    public void onBindViewHolder(PaymentViewHolder holder, int position) {
        Payment thisPayment = this.dateset.get(position);

        holder.name.setText(thisPayment.name);
        holder.amount.setText(this.formatter.format((double) thisPayment.amount / 100));

        if (thisPayment.isPaid) holder.status.setImageResource(R.drawable.ic_check_round);
        else holder.status.setImageResource(R.drawable.ic_cross_round);
    }

    @Override
    public int getItemCount() {
        return dateset.size();
    }

    public interface OnPaymentListener {
        void onStatusClick(int position);
    }
}

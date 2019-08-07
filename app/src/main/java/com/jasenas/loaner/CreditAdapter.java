package com.jasenas.loaner;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.CreditViewHolder> {
    private NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.GERMANY);

    private List<Credit> creditList;
    private Context mContext;
    private OnCreditListener mOnCreditListener;
    private char type;

    public CreditAdapter(Context context, List<Credit> dataset, char type) {
        this.creditList = dataset;
        this.mContext = context;
        this.mOnCreditListener = (OnCreditListener) context;
        this.type = type;
    }

    public class CreditViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private char type;
        private TextView name;
        private TextView count;
        private TextView amount;
        private ImageView bar;

        OnCreditListener onCreditListener;

        public CreditViewHolder(View view, OnCreditListener onCreditListener, char type) {
            super(view);
            this.name = view.findViewById(R.id.name);
            this.count = view.findViewById(R.id.count);
            this.amount = view.findViewById(R.id.amount);
            this.bar = view.findViewById(R.id.bar);
            this.type = type;

            view.setOnClickListener(this);
            this.onCreditListener = onCreditListener;
        }

        @Override
        public void onClick(View view) {
            this.onCreditListener.onCreditClick(getAdapterPosition(), this.type);
        }
    }

    @Override
    public CreditAdapter.CreditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.credit_list_row, parent, false);

        return new CreditViewHolder(itemView, mOnCreditListener, this.type);
    }

    @Override
    public void onBindViewHolder(CreditViewHolder holder, int position) {
        Credit thisCredit = creditList.get(position);

        holder.name.setText(thisCredit.name);
        holder.amount.setText(formatter.format((double) thisCredit.amount / 100));

        if (holder.type == 'p') {
            holder.count.setText(this.mContext.getString(R.string.paid_count, Integer.toString(thisCredit.payments.size())));

            final ContextThemeWrapper wrapper = new ContextThemeWrapper(this.mContext, R.style.paid_bar);
            final Drawable statusBar = ResourcesCompat.getDrawable(this.mContext.getResources(), R.drawable.paid_unpaid_bar, wrapper.getTheme());
            holder.bar.setImageDrawable(statusBar);
        } else {
            holder.count.setText(this.mContext.getString(R.string.unpaid_count, Integer.toString(thisCredit.countUnpaid())));

            final ContextThemeWrapper wrapper = new ContextThemeWrapper(this.mContext, R.style.unpaid_bar);
            final Drawable statusBar = ResourcesCompat.getDrawable(this.mContext.getResources(), R.drawable.paid_unpaid_bar, wrapper.getTheme());
            holder.bar.setImageDrawable(statusBar);
        }

    }

    @Override
    public int getItemCount() {
        return creditList.size();
    }

    public interface OnCreditListener {
        void onCreditClick(int id, char type);
    }

}

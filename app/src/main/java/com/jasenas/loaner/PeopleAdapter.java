package com.jasenas.loaner;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder> {
    private List<Payment> dataset;
    private OnPaymentListener listener;

    public PeopleAdapter(List<Payment> dataset, OnPaymentListener listener) {
        this.dataset = dataset;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PeopleAdapter.PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.people_list_row, parent, false);
        return new PeopleAdapter.PeopleViewHolder(itemView, this.listener);
    }


    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull PeopleAdapter.PeopleViewHolder holder, int position) {
        Payment thisPayment = this.dataset.get(position);
        holder.name.setText(thisPayment.name);
        if(thisPayment.name.equals("")) holder.name.requestFocus();
        double amount = (double) thisPayment.amount / 100;
        holder.amount.setText(String.format("%.2f", amount));
    }

    @Override
    public int getItemCount() {
        return this.dataset.size();
    }

    public class PeopleViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView amount;
        ImageButton remove;
        OnPaymentListener listener;

        public PeopleViewHolder(@NonNull View itemView, final OnPaymentListener listener) {
            super(itemView);

            this.name = itemView.findViewById(R.id.name);
            this.amount = itemView.findViewById(R.id.amount);
            this.remove = itemView.findViewById(R.id.remove);

            this.listener = listener;

            this.remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onRemoveClick(getAdapterPosition());
                }
            });


            this.name.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    dataset.get(getAdapterPosition()).name = charSequence.toString();
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            this.amount.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    String val = charSequence.toString();
                    if (val.isEmpty()) val = "0";
                    dataset.get(getAdapterPosition()).amount = (int) (Double.parseDouble(val) * 100);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
        }
    }

    public interface OnPaymentListener {
        void onRemoveClick(int position);
    }
}

package com.jasenas.loaner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.text.ParseException;

public class CreditActivity extends AppCompatActivity implements PaymentAdapter.OnPaymentListener{
    private RecyclerView.Adapter paymentsAdapter;
    private RecyclerView paymentsView;
    private ActionBar actionBar;

    private Credit credit;
    private CreditManager creditManager;

    private boolean changed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.actionBar = getSupportActionBar();
        this.actionBar.setDisplayHomeAsUpEnabled(true);

        final Intent intent = getIntent();

        this.paymentsView = findViewById(R.id.payments);

        this.creditManager = CreditManager.getInstance();
        try {
            this.credit = this.creditManager.getCredit(Integer.parseInt(intent.getStringExtra("id")));
            this.actionBar.setTitle(this.credit.name);
            this.renderCredit();
        } catch (ParseException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            this.credit = this.creditManager.getCredit(this.credit.id);
            this.actionBar.setTitle(this.credit.name);
            this.renderCredit();
        } catch (ParseException | JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        try {
            this.creditManager.saveCredit(this.credit, false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void renderCredit() {
        updateStatusBar();

        TextView moneyBig = findViewById(R.id.moneyBig);
        moneyBig.setText(Integer.toString(this.credit.amount / 100));

        TextView moneySmall = findViewById(R.id.moneySmall);
        int amountSmall = this.credit.amount % 100;
        moneySmall.setText(getApplicationContext().getString(R.string.comma, String.format("%02d", amountSmall)));

        this.paymentsAdapter = new PaymentAdapter(this, this.credit.payments);
        RecyclerView.LayoutManager paymentsLayoutManager = new LinearLayoutManager(getApplicationContext());
        this.paymentsView.setLayoutManager(paymentsLayoutManager);
        this.paymentsView.setItemAnimator(new DefaultItemAnimator());
        this.paymentsView.setAdapter(this.paymentsAdapter);
        this.paymentsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStatusClick(int position) {
        this.credit.payments.get(position).toggleStatus();
        this.paymentsAdapter.notifyDataSetChanged();
        this.changed = true;
        updateStatusBar();
    }

    private void updateStatusBar() {
        ImageView bar;
        bar = findViewById(R.id.statusBar);

        if (this.credit.isPaid()) {
            final ContextThemeWrapper wrapper = new ContextThemeWrapper(getApplication(), R.style.paid_bar);
            final Drawable statusBar = ResourcesCompat.getDrawable(getResources(), R.drawable.paid_unpaid_bar, wrapper.getTheme());
            bar.setImageDrawable(statusBar);
        } else {
            final ContextThemeWrapper wrapper = new ContextThemeWrapper(getApplication(), R.style.unpaid_bar);
            final Drawable statusBar = ResourcesCompat.getDrawable(getResources(), R.drawable.paid_unpaid_bar, wrapper.getTheme());
            bar.setImageDrawable(statusBar);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.credit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent(getApplicationContext(), CreditEditActivity.class);
        switch (item.getItemId()) {
            case R.id.action_edit:
                intent.putExtra("op", "edit");
                intent.putExtra("id", Integer.toString(this.credit.id));
                startActivity(intent);
                return true;

            case R.id.action_delete:
                try {
                    this.creditManager.deleteCredit(this.credit);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return true;

            case R.id.action_duplicate:
                intent.putExtra("op", "duplicate");
                intent.putExtra("id", Integer.toString(this.credit.id));
                startActivity(intent);
                return true;

            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

}

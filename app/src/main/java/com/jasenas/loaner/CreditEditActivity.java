package com.jasenas.loaner;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class CreditEditActivity extends AppCompatActivity implements PeopleAdapter.OnPaymentListener {
    private RecyclerView peopleList;
    private RecyclerView.Adapter peopleAdapter;

    private EditText nameInput;
    private EditText amountInput;

    private CreditManager creditManager;

    private Credit credit;
    private boolean newCredit = false;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_edit);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        actionBar.setTitle(R.string.create_credit);

        this.creditManager = CreditManager.getInstance();

        this.nameInput = findViewById(R.id.name);
        this.amountInput = findViewById(R.id.amount);
        this.peopleList = findViewById(R.id.peopleList);

        final Intent intent = getIntent();
        System.out.println(intent.getStringExtra("op"));
        if (intent.getStringExtra("op").equals("edit")) {
            try {
                this.credit = this.creditManager.getCredit(Integer.parseInt(intent.getStringExtra("id")));
                this.nameInput.setText(this.credit.name);
                this.amountInput.setText(String.format("%.2f", (double) this.credit.amount / 100));
            } catch (ParseException | JSONException e) {
                e.printStackTrace();
            }
        } else if (intent.getStringExtra("op").equals("new")) {
            this.credit = new Credit(this.creditManager.getNextId());
            this.newCredit = true;
        } else if (intent.getStringExtra("op").equals("duplicate")) {
            try {
                this.newCredit = true;
                this.credit = this.creditManager.getCredit(Integer.parseInt(intent.getStringExtra("id")));
                this.credit.id = this.creditManager.getNextId();
                this.nameInput.setText(this.credit.name);
                this.amountInput.setText(String.format("%.2f", (double) this.credit.amount / 100));
            } catch (ParseException | JSONException e) {
                e.printStackTrace();
            }
        }

        this.peopleAdapter = new PeopleAdapter(this.credit.payments, this);
        RecyclerView.LayoutManager peopleLayoutMan = new LinearLayoutManager(getApplicationContext());
        this.peopleList.setLayoutManager(peopleLayoutMan);
        this.peopleList.setItemAnimator(new DefaultItemAnimator());
        this.peopleList.setAdapter(this.peopleAdapter);

        MaterialButton addPerson = findViewById(R.id.add_person);
        addPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                credit.payments.add(new Payment());
                peopleAdapter.notifyDataSetChanged();
            }
        });

        MaterialButton auto = findViewById(R.id.auto);
        auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCredit();
                credit.auto();
                peopleAdapter.notifyDataSetChanged();
            }
        });

        FloatingActionButton saveFab = findViewById(R.id.floating_action_button);
        saveFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCredit();
                try {
                    creditManager.saveCredit(credit, newCredit);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onRemoveClick(int position) {
        this.credit.payments.remove(position);
        this.peopleAdapter.notifyDataSetChanged();
    }

    private void updateCredit() {
        this.credit.name = nameInput.getText().toString();
        this.credit.amount = (int) (Double.parseDouble(amountInput.getText().toString()) * 100);
    }
}

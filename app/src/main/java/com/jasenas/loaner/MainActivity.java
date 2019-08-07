package com.jasenas.loaner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CreditAdapter.OnCreditListener {
    private RecyclerView.Adapter unpaidAdapter;
    private RecyclerView.Adapter paidAdapter;

    private CreditManager creditManager;

    private List<Credit> unpaidCredits = new ArrayList<>();
    private List<Credit> paidCredits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CreditEditActivity.class);
                intent.putExtra("op", "new");
                startActivity(intent);
            }
        });

        if(!CreditManager.isSetup()) {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "credit-db")
                    .allowMainThreadQueries()
                    .build();

            CreditManager.construct(db);
        }

        this.creditManager = CreditManager.getInstance();

        RecyclerView unpaidList = findViewById(R.id.unpaidList);
        RecyclerView paidList = findViewById(R.id.paidList);

        this.unpaidAdapter = new CreditAdapter(this, this.unpaidCredits, 'u');
        RecyclerView.LayoutManager unpaidLayoutManager = new LinearLayoutManager(getApplicationContext());
        unpaidList.setLayoutManager(unpaidLayoutManager);
        unpaidList.setItemAnimator(new DefaultItemAnimator());
        unpaidList.setAdapter(this.unpaidAdapter);

        this.paidAdapter = new CreditAdapter(this, this.paidCredits,  'p');
        RecyclerView.LayoutManager paidLayoutManager = new LinearLayoutManager(getApplicationContext());
        paidList.setLayoutManager(paidLayoutManager);
        paidList.setItemAnimator(new DefaultItemAnimator());
        paidList.setAdapter(this.paidAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        fetchCredits();
    }

    @Override
    public void onCreditClick(int position, char type) {
        int id;
        if (type == 'u') {
            id = this.unpaidCredits.get(position).id;
        } else if (type == 'p') {
            id = this.paidCredits.get(position).id;
        } else return;

        Intent intent = new Intent(this, CreditActivity.class);
        intent.putExtra("id", Integer.toString(id));
        startActivity(intent);

    }

    private void fetchCredits() {
        System.out.println("Fetching");
        try {
            List<Credit> newCredits = this.creditManager.getCredits();

            this.paidCredits.clear();
            this.unpaidCredits.clear();

            for (int i = 0; i < newCredits.size(); i++) {
                if (newCredits.get(i).isPaid()) this.paidCredits.add(newCredits.get(i));
                else this.unpaidCredits.add(newCredits.get(i));
            }

            this.unpaidAdapter.notifyDataSetChanged();
            this.paidAdapter.notifyDataSetChanged();
        } catch (ParseException | JSONException e) {
            e.printStackTrace();
        }
    }

}

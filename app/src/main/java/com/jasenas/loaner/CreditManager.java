package com.jasenas.loaner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

public final class CreditManager {
    private List<Payment> payments1 = Arrays.asList(
        new Payment("Payer1", 400, false),
        new Payment("Payer2", 400, true)
    );
    private List<Payment> payments2 = Arrays.asList(
            new Payment("Payer1", 400, true),
            new Payment("Payer2", 400, true)
    );
    private Credit mockCredit1 = new Credit(0, "Spotify", new GregorianCalendar(2019, 8, 2), 811, payments1, true);
    private Credit mockCredit2 = new Credit(1, "Spotify", new GregorianCalendar(2019, 8, 2), 911, payments2);
    private CreditDAO doa;
    private boolean setup = false;

    private final static CreditManager instance = new CreditManager();

    public static void construct(AppDatabase db) {
        instance.doa = db.getCreditDAO();
        instance.setup = true;
    }

    public static CreditManager getInstance() {
        return instance;
    }

    public static boolean isSetup() {
        return instance.setup;
    }

    private CreditManager() {
    }

    public List<Credit> getCredits() throws ParseException, JSONException {
        List<CreditDB> creditsDB = doa.getItems();
        List<Credit> credits = new ArrayList<>();

        for(int x = 0; x < creditsDB.size(); x++) {
            credits.add(new Credit(creditsDB.get(x)));
        }

        return credits;
    }

    public Credit getCredit(int id) throws ParseException, JSONException {
        CreditDB creditDB = this.doa.getItemByID(id);
        return new Credit(creditDB);
    }

    public void saveCredit(Credit credit, boolean newCredit) throws JSONException {
        if (newCredit) {
            this.doa.insert(new CreditDB(credit));
        } else this.doa.update(new CreditDB(credit));
    }

    public void deleteCredit(Credit credit) throws JSONException {
        this.doa.delete(new CreditDB(credit));
    }

    public static List<Payment> parsePayments(String jsonString) throws JSONException {
        List<Payment> payments = new ArrayList<>();

        JSONObject json = new JSONObject(jsonString);
        JSONArray jsonPayments = json.getJSONArray("payments");

        for (int i=0; i < jsonPayments.length(); i++) {
            try {
                JSONObject payment = jsonPayments.getJSONObject(i);
                // Pulling items from the array
                String name = payment.getString("name");
                int amount = payment.getInt("amount");
                boolean isPaid = payment.getBoolean("isPaid");

                Payment newPayment = new Payment(name, amount, isPaid);
                payments.add(newPayment);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return payments;
    }

    public static String convertPayments(List<Payment> payments) throws JSONException {
        JSONArray paymentArray = new JSONArray();
        for(int x = 0; x < payments.size(); x++) {
            JSONObject payment = new JSONObject();

            payment.put("name", payments.get(x).name);
            payment.put("amount", payments.get(x).amount);
            payment.put("isPaid", payments.get(x).isPaid);

            paymentArray.put(payment);
        }

        JSONObject json = new JSONObject();
        json.put("payments", paymentArray);

        return json.toString();
    }

    public int getNextId() {
        return this.doa.getMaxId() + 1;
    }
}

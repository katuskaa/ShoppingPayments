package katka.shoppingpayments.screens.payments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import katka.shoppingpayments.R;
import katka.shoppingpayments.database.Database;
import katka.shoppingpayments.database.FirebaseConstants;
import katka.shoppingpayments.helpers.DatePickerHelper;
import katka.shoppingpayments.helpers.shared_preferences.SharedPreferencesHelper;
import katka.shoppingpayments.screens.add_payment.AddPaymentActivity;
import katka.shoppingpayments.screens.create_group.CreateGroupActivity;
import katka.shoppingpayments.screens.payments.adapters.PaymentsAdapter;
import katka.shoppingpayments.screens.show_groups.ShowGroupsActivity;
import katka.shoppingpayments.structures.FriendUser;
import katka.shoppingpayments.structures.Payment;

public class PaymentsActivity extends AppCompatActivity {
    private EditText editTextMonth;
    private Spinner spinnerUser;
    private ListView listViewPayments;
    private TextView textViewSum;
    private HashMap<String, Payment> paymentsMap;
    private ArrayList<Payment> payments;
    private PaymentsAdapter paymentsAdapter;
    private DatabaseReference databaseReference;


    public static void startActivity(Context context) {
        Intent intent = new Intent(context, PaymentsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initiateParameters();
        setDatePicker();
        setSpinnerUser();
        setFloatingActionButton();
        showPayments();
        setListViewPaymentsUpdate();
    }

    private void startAddPaymentActivity() {
        AddPaymentActivity.startActivity(this, getMonth(), getYear());
    }

    private void setFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ak je selected user owner
                startAddPaymentActivity();
            }
        });
    }

    private void initiateParameters() {
        editTextMonth = (EditText) findViewById(R.id.payments_activity__month_editText);
        spinnerUser = (Spinner) findViewById(R.id.payments_activity__user_spinner);
        listViewPayments = (ListView) findViewById(R.id.payments_activity__listView);
        textViewSum = (TextView) findViewById(R.id.payments_activity__sum_textView);
    }

    private void setDatePicker() {
        DatePickerHelper datePickerHelper = new DatePickerHelper(this, editTextMonth);
        final DatePickerDialog datePickerDialog = datePickerHelper.getCurrentDate();
        editTextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        datePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                showPayments();
            }
        });
    }

    private void setSpinnerUser() {
        ArrayList<FriendUser> friendUsers = new ArrayList<>();
        friendUsers.add(new FriendUser("katka"));
        friendUsers.add(new FriendUser("petko"));
        ArrayAdapter<FriendUser> userArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        userArrayAdapter.addAll(friendUsers);
        userArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUser.setAdapter(userArrayAdapter);
    }

    private void showPayments() {
        databaseReference = Database.getFirebaseDatabase().getReference(SharedPreferencesHelper.getUserUid(this)).child(FirebaseConstants.PAYMENTS);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                payments = new ArrayList<>();
                paymentsMap = new HashMap<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Payment payment = snapshot.getValue(Payment.class);
                    if (payment.getMonth().equals(getMonth()) && payment.getYear().equals(getYear())) {
                        payments.add(payment);
                        paymentsMap.put(snapshot.getKey(), payment);
                    }
                }
                updateListView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String getMonth() {
        return editTextMonth.getText().toString().substring(0, 3);
    }

    private String getYear() {
        return editTextMonth.getText().toString().substring(4, editTextMonth.getText().toString().length());
    }

    private void updateListView() {
        textViewSum.setText(getPaymentSum());
        paymentsAdapter = new PaymentsAdapter(this, payments);
        listViewPayments.setAdapter(paymentsAdapter);
    }

    private String getPaymentSum() {
        float sum = 0;
        for (Payment payment : payments) {
            sum += Float.valueOf(payment.getPrice());
        }
        DecimalFormat decimalFormat = new DecimalFormat("##,00");
        decimalFormat.setMaximumFractionDigits(2);
        return decimalFormat.format(sum);
    }

    private void setListViewPaymentsUpdate() {
        listViewPayments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addDialog(payments.get(position));
            }
        });
    }

    private void addDialog(final Payment payment) {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.activity_payments__add_dialog);
        final EditText editTextPrice = (EditText) dialog.findViewById(R.id.activity_payments__add_dialog__price);
        final EditText editTextShop = (EditText) dialog.findViewById(R.id.activity_payments__add_dialog__shop);
        editTextPrice.setText(payment.getPrice());
        editTextShop.setText(payment.getShop());

        Button positiveButton = (Button) dialog.findViewById(R.id.activity_payments__add_dialog__positiveButton);
        Button negativeButton = (Button) dialog.findViewById(R.id.activity_payments__add_dialog__negativeButton);

        positiveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String price = editTextPrice.getText().toString();
                String shop = editTextShop.getText().toString();
                if (price.isEmpty() || shop.isEmpty()) {
                    return;
                }
                for (String key : paymentsMap.keySet()) {
                    if (paymentsMap.get(key).equals(payment)) {
                        payment.setPrice(price);
                        payment.setShop(shop);
                        databaseReference.child(key).setValue(payment);
                        updateListView();
                        break;
                    }
                }
                dialog.dismiss();
            }
        });

        negativeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                for (String key : paymentsMap.keySet()) {
                    if (paymentsMap.get(key).equals(payment)) {
                        databaseReference.child(key).removeValue();
                        updateListView();
                        break;
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_payments, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_payments__create_group:
                CreateGroupActivity.startActivity(this);
                return true;
            case R.id.menu_payments__show_groups:
                ShowGroupsActivity.startActivity(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

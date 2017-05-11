package katka.shoppingpayments.screens.payments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import katka.shoppingpayments.structures.Group;
import katka.shoppingpayments.structures.Member;
import katka.shoppingpayments.structures.Payment;

public class PaymentsActivity extends AppCompatActivity {
    private EditText editTextMonth;
    private Spinner spinnerUser;
    private Spinner spinnerGroup;
    private ListView listViewPayments;
    private TextView textViewSum;
    private HashMap<String, Payment> paymentsMap;
    private ArrayList<Payment> payments;
    private DatabaseReference databaseReference;
    private Group selectedGroup;
    private Member selectedMember;
    private ArrayList<Group> groups;
    private ArrayList<Member> members;


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
        setSpinnerGroup();
        setFloatingActionButton();
        showPayments();
        setListViewPaymentsUpdate();
    }

    private void startAddPaymentActivity() {
        AddPaymentActivity.startActivity(this, getMonth(), getYear());
    }

    private void setFloatingActionButton() {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOwner()) {
                    startAddPaymentActivity();
                } else {
                    CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.activity_payments__coordinatorLayout);
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, R.string.payments_activity__not_owner, Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }

    private boolean isOwner() {
        return selectedMember.getUid().equals(SharedPreferencesHelper.getUserUid(this));
    }

    private void initiateParameters() {
        editTextMonth = (EditText) findViewById(R.id.payments_activity__month_editText);
        spinnerUser = (Spinner) findViewById(R.id.payments_activity__user_spinner);
        spinnerGroup = (Spinner) findViewById(R.id.payments_activity__group_spinner);
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

    private void setSpinnerGroup() {
        DatabaseReference databaseReference = Database.getFirebaseDatabase().getReference(FirebaseConstants.GROUPS);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groups = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Group group = snapshot.getValue(Group.class);
                    ArrayList<Member> members = group.getMembers();
                    for (Member member : members) {
                        if (member.getUid().equals(SharedPreferencesHelper.getUserUid(getApplicationContext()))) {
                            groups.add(group);
                            break;
                        }
                    }
                }
                updateSpinnerGroup();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateSpinnerGroup() {
        ArrayAdapter<Group> groupArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        groupArrayAdapter.addAll(groups);
        groupArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(groupArrayAdapter);

        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGroup = groups.get(position);
                updateSpinnerMember();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void updateSpinnerMember() {
        members = selectedGroup.getMembers();
        ArrayAdapter<Member> userArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        userArrayAdapter.addAll(members);
        userArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUser.setAdapter(userArrayAdapter);
        spinnerUser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMember = members.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
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
        PaymentsAdapter paymentsAdapter = new PaymentsAdapter(this, payments);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }


}

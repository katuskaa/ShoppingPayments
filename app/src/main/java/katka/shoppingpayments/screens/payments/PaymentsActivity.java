package katka.shoppingpayments.screens.payments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import katka.shoppingpayments.R;
import katka.shoppingpayments.helpers.MonthDatePickerHelper;
import katka.shoppingpayments.screens.add_payment.AddPaymentActivity;
import katka.shoppingpayments.screens.create_group.CreateGroupActivity;
import katka.shoppingpayments.screens.payments.adapters.PaymentsAdapter;
import katka.shoppingpayments.screens.show_groups.ShowGroupsActivity;
import katka.shoppingpayments.structures.FriendUser;
import katka.shoppingpayments.structures.Payment;

public class PaymentsActivity extends AppCompatActivity {
    private EditText editTextMonth;
    private Spinner spinnerUser;
    private  ListView listViewPayments;

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

        setFloatingActionButton();
        initiateParameters();
        setMonthDatePicker();
        setSpinnerUser();
        showPayments();

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppInvite.API)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .build();

//        // Check for App Invite invitations and launch deep-link activity if possible.
//        // Requires that an Activity is registered in AndroidManifest.xml to handle
//        // deep-link URLs.
//        boolean autoLaunchDeepLink = true;
//        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
//                .setResultCallback(
//                        new ResultCallback<AppInviteInvitationResult>() {
//                            @Override
//                            public void onResult(AppInviteInvitationResult result) {
//                                Log.i("TAG", "getInvitation:onResult:" + result.getStatus());
//                                if (result.getStatus().isSuccess()) {
//                                    // Extract information from the intent
//                                    Intent intent = result.getInvitationIntent();
//                                    String deepLink = AppInviteReferral.getDeepLink(intent);
//                                    String invitationId = AppInviteReferral.getInvitationId(intent);
//
//                                    // Because autoLaunchDeepLink = true we don't have to do anything
//                                    // here, but we could set that to false and manually choose
//                                    // an Activity to launch to handle the deep link here.
//                                    // ...
//                                }
//                            }
//                        });

    }

    private void startAddPaymentActivity() {
        AddPaymentActivity.startActivity(this);
    }

    private void setFloatingActionButton() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAddPaymentActivity();
            }
        });
    }

    private void initiateParameters() {
        editTextMonth = (EditText) findViewById(R.id.payments_activity__month_editText);
        spinnerUser = (Spinner) findViewById(R.id.payments_activity__user_spinner);
        listViewPayments = (ListView) findViewById(R.id.payments_activity__listView);
    }

    private void setMonthDatePicker() {
        final MonthDatePickerHelper monthDatePickerHelper = new MonthDatePickerHelper(this, editTextMonth);
        final DatePickerDialog datePickerDialog = monthDatePickerHelper.getCurrentDate();
        editTextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
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
        ArrayList<Payment> payments = new ArrayList<>();
        Payment payment = new Payment("12", "samoska");
        payments.add(payment);
        payments.add(new Payment("123", "billa"));
        PaymentsAdapter paymentsAdapter = new PaymentsAdapter(this, payments);
        listViewPayments.setAdapter(paymentsAdapter);
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

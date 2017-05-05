package katka.shoppingpayments.screens.add_payment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import katka.shoppingpayments.R;
import katka.shoppingpayments.database.Database;
import katka.shoppingpayments.database.FirebaseConstants;
import katka.shoppingpayments.helpers.shared_preferences.SharedPreferencesHelper;
import katka.shoppingpayments.structures.Payment;

public class AddPaymentActivity extends AppCompatActivity {
    private static final String MONTH_BUNDLE_KEY = "MONTH_BUNDLE_KEY";
    private static final String YEAR_BUNDLE_KEY = "YEAR_BUNDLE_KEY";

    private EditText editTextPrice;
    private EditText editTextShop;
    private Button buttonAdd;

    public static void startActivity(Context context, String month, String year) {
        Intent intent = new Intent(context, AddPaymentActivity.class);
        intent.putExtra(MONTH_BUNDLE_KEY, month);
        intent.putExtra(YEAR_BUNDLE_KEY, year);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);

        initiateParameters();
        setButtonAdd();
    }

    private void initiateParameters() {
        editTextPrice = (EditText) findViewById(R.id.add_payment_activity__price_editText);
        editTextShop = (EditText) findViewById(R.id.add_payment_activity__shop_editText);
        buttonAdd = (Button) findViewById(R.id.add_payment_activity__add_button);
    }

    private void setButtonAdd() {
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPayment();
            }
        });
    }

    private void addPayment() {
        if (editTextPrice.getText().toString().length() == 0) {
            editTextPrice.setHintTextColor(ContextCompat.getColor(this, R.color.colorError));
        } else if (editTextShop.getText().toString().length() == 0) {
            editTextShop.setHintTextColor(ContextCompat.getColor(this, R.color.colorError));
        } else {
            String price = editTextPrice.getText().toString();
            String shop = editTextShop.getText().toString();
            Payment payment = new Payment();
            payment.setPrice(price);
            payment.setShop(shop);
            payment.setMonth(getIntent().getStringExtra(MONTH_BUNDLE_KEY));
            payment.setYear(getIntent().getStringExtra(YEAR_BUNDLE_KEY));
            DatabaseReference databaseReference = Database.getFirebaseDatabase().getReference(SharedPreferencesHelper.getUserUid(this)).child(FirebaseConstants.PAYMENTS).push();
            databaseReference.setValue(payment);
            this.finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        finish();
        return true;
    }

}

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
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import katka.shoppingpayments.R;
import katka.shoppingpayments.helpers.shared_preferences.SharedPreferencesHelper;

public class AddPaymentActivity extends AppCompatActivity {
    private EditText editTextPrice;
    private EditText editTextShop;
    private Button buttonAdd;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, AddPaymentActivity.class);
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
        } else {
            String price = editTextPrice.getText().toString();
            String shop = editTextShop.getText().toString();
            HashMap<String, String> json = new HashMap<>();
            json.put("price", price);
            json.put("shop", shop);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(SharedPreferencesHelper.getUserUid(this)).child("payments");
            databaseReference.setValue(json);
            this.finish();
        }

    }
}

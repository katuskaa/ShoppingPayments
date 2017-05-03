package katka.shoppingpayments.screens.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import katka.shoppingpayments.R;
import katka.shoppingpayments.helpers.shared_preferences.SharedPreferencesHelper;
import katka.shoppingpayments.screens.payments.PaymentsActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogin;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initiateParameters();
        setButtonLogin();
    }

    private void startPaymentActivity() {
        PaymentsActivity.startActivity(this);
        this.finish();
    }

    private void initiateParameters() {
        editTextEmail = (EditText) findViewById(R.id.login_activity__email_editText);
        editTextPassword = (EditText) findViewById(R.id.login_activity__password_editText);
        buttonLogin = (Button) findViewById(R.id.login_activity__login_button);
    }

    private void setButtonLogin() {
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextEmail.setTextColor(Color.BLACK);
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();
                if (isValidEmail(email) && isValidPassword(password)) {
                    firebaseAuthentication(email, password);
                }
            }
        });
    }

    private boolean isValidEmail(String email) {
        if (email.isEmpty()) {
            editTextEmail.setHintTextColor(ContextCompat.getColor(this, R.color.colorError));
        }
        else if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        } else {
            editTextEmail.setTextColor(ContextCompat.getColor(this, R.color.colorError));
        }
        return false;
    }

    private boolean isValidPassword(String password) {
        if (password.isEmpty()) {
            editTextPassword.setHintTextColor(ContextCompat.getColor(this, R.color.colorError));
            return false;
        }
        return true;
    }

    private void firebaseAuthentication(String email, String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Task<AuthResult> authResultTask = firebaseAuth.createUserWithEmailAndPassword(email, password);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                    saveUserUid();
                    startPaymentActivity();
                }
            }
        });
    }

    private void saveUserUid() {
        SharedPreferencesHelper.saveUserUid(this, FirebaseAuth.getInstance().getCurrentUser().getUid());
    }


}

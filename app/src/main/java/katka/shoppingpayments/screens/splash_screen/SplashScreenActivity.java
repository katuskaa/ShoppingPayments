package katka.shoppingpayments.screens.splash_screen;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import katka.shoppingpayments.R;
import katka.shoppingpayments.screens.login.LoginActivity;
import katka.shoppingpayments.screens.payments.PaymentsActivity;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (isUserLoggedIn()) {
            startPaymentsActivity();
        } else {
            startLoginActivity();
        }
    }

    private void startLoginActivity() {
        LoginActivity.startActivity(this);
        this.finish();
    }

    private void startPaymentsActivity() {
        PaymentsActivity.startActivity(this);
        this.finish();
    }

    private boolean isUserLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }


}

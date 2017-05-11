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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import katka.shoppingpayments.R;
import katka.shoppingpayments.database.Database;
import katka.shoppingpayments.database.FirebaseConstants;
import katka.shoppingpayments.helpers.shared_preferences.SharedPreferencesHelper;
import katka.shoppingpayments.screens.nickname.NicknameActivity;
import katka.shoppingpayments.screens.payments.PaymentsActivity;
import katka.shoppingpayments.structures.User;

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

    private void startNicknameActivity() {
        NicknameActivity.startActivity(this);
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
                if (isValidEmail(email) && isNotEmpty(password, editTextPassword)) {
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

    private boolean isNotEmpty(String text, EditText editText) {
        if (text.isEmpty()) {
            editText.setHintTextColor(ContextCompat.getColor(this, R.color.colorError));
            return false;
        }
        return true;
    }

    private void firebaseAuthentication(final String email, final String password) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Task<AuthResult> authResultTask = firebaseAuth.createUserWithEmailAndPassword(email, password);
        authResultTask.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseDatabase.getInstance().setPersistenceEnabled(true);
                    saveUser(email, password, task.getResult().getUser().getUid());
                    saveUserUid(task.getResult().getUser().getUid());
                    startNicknameActivity();
                } else {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password);
                    DatabaseReference databaseReference = Database.getFirebaseDatabase().getReference(FirebaseConstants.USERS);
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                if (user.getEmail().equals(email) && user.getPassword().equals(password)) {
                                    saveUserUid(user.getUid());
                                    startPaymentActivity();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private void saveUserUid(String uid) {
        SharedPreferencesHelper.saveUserUid(this, uid);
    }

    private void saveUser(String email, String password, String uid) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        user.setUid(uid);
        DatabaseReference databaseReference = Database.getFirebaseDatabase().getReference(FirebaseConstants.USERS).push();
        databaseReference.setValue(user);
    }


}

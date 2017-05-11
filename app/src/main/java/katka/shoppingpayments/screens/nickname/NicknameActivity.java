package katka.shoppingpayments.screens.nickname;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import katka.shoppingpayments.R;
import katka.shoppingpayments.database.Database;
import katka.shoppingpayments.database.FirebaseConstants;
import katka.shoppingpayments.helpers.shared_preferences.SharedPreferencesHelper;
import katka.shoppingpayments.screens.payments.PaymentsActivity;
import katka.shoppingpayments.structures.Group;
import katka.shoppingpayments.structures.Member;

public class NicknameActivity extends AppCompatActivity {
    private static final String EMAIL = "EMAIL";
    private EditText editTextNickname;
    private Button buttonSetNickname;

    public static void startActivity(Context context, String email) {
        Intent intent = new Intent(context, NicknameActivity.class);
        intent.putExtra(EMAIL, email);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nickname);

        initiateParameters();
        setButtonSetNickname();
    }

    private void startPaymentsActivity() {
        PaymentsActivity.startActivity(this);
        this.finish();
    }

    private void initiateParameters() {
        editTextNickname = (EditText) findViewById(R.id.nickname_activity__nickname);
        buttonSetNickname = (Button) findViewById(R.id.nickname_activity__set_nickname_button);
    }

    private boolean isNicknameFilled() {
        if (editTextNickname.getText().toString().isEmpty()) {
            editTextNickname.setHintTextColor(ContextCompat.getColor(this, R.color.colorError));
            return false;
        }
        return true;
    }

    private void setButtonSetNickname() {
        buttonSetNickname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isNicknameFilled()) {
                    String nickname = editTextNickname.getText().toString();
                    Member member = setMember(nickname);
                    setGroup(nickname, member);
                    startPaymentsActivity();
                }
            }
        });
    }

    private Member setMember(String nickname) {
        Member member = new Member();
        member.setNickname(nickname);
        member.setUid(SharedPreferencesHelper.getUserUid(this));
        member.setEmail(getIntent().getStringExtra(EMAIL));
        DatabaseReference databaseReference = Database.getFirebaseDatabase().getReference(FirebaseConstants.MEMBERS).push();
        databaseReference.setValue(member);
        SharedPreferencesHelper.saveUserNickname(this, nickname);
        return member;
    }

    private void setGroup(String name, Member member) {
        ArrayList<Member> members = new ArrayList<>();
        members.add(member);
        Group group = new Group();
        group.setName(name);
        group.setMembers(members);
        DatabaseReference databaseReference = Database.getFirebaseDatabase().getReference(FirebaseConstants.GROUPS).push();
        databaseReference.setValue(group);
        this.finish();
    }
}

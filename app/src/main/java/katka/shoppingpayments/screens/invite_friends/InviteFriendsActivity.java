package katka.shoppingpayments.screens.invite_friends;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import katka.shoppingpayments.R;
import katka.shoppingpayments.database.Database;
import katka.shoppingpayments.database.FirebaseConstants;
import katka.shoppingpayments.helpers.shared_preferences.SharedPreferencesHelper;
import katka.shoppingpayments.structures.Group;
import katka.shoppingpayments.structures.Member;

public class InviteFriendsActivity extends AppCompatActivity {

    private static final String GROUP_NAME = "GROUP_NAME";

    private EditText editTextFriendName;
    private Button buttonInviteFriend;

    public static void startActivity(Context context, String groupName) {
        Intent intent = new Intent(context, InviteFriendsActivity.class);
        intent.putExtra(GROUP_NAME, groupName);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inivite_friends);

        initiateParameters();
        setButtonInvite();
    }

    private void initiateParameters() {
        editTextFriendName = (EditText) findViewById(R.id.invite_friends_activity__friend_name_editText);
        buttonInviteFriend = (Button) findViewById(R.id.invite_friends_activity__invite_friend_button);
    }

    private void setButtonInvite() {
        buttonInviteFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friendEmail = editTextFriendName.getText().toString();
                if (isNotEmpty(friendEmail, editTextFriendName)) {
                    addGroupWithMember(friendEmail);
                }
            }
        });
    }

    private boolean isNotEmpty(String text, EditText editText) {
        if (text.isEmpty()) {
            editText.setHintTextColor(ContextCompat.getColor(this, R.color.colorError));
            return false;
        }
        return true;
    }

    private void addGroupWithMember(final String friendEmail) {
        DatabaseReference databaseReference = Database.getFirebaseDatabase().getReference(FirebaseConstants.MEMBERS);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Member member = snapshot.getValue(Member.class);
                    if (member.getEmail().equals(friendEmail)) {
                        addGroup(member);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addGroup(Member friendUser) {
        ArrayList<Member> members = new ArrayList<>();
        Member member = new Member();
        member.setNickname(SharedPreferencesHelper.getUserNickname(this));
        member.setUid(SharedPreferencesHelper.getUserUid(this));
        member.setEmail(SharedPreferencesHelper.getUserEmail(this));
        members.add(member);
        members.add(friendUser);
        Group group = new Group();
        group.setName(getIntent().getStringExtra(GROUP_NAME));
        group.setMembers(members);
        DatabaseReference databaseReference = Database.getFirebaseDatabase().getReference(FirebaseConstants.GROUPS).push();
        databaseReference.setValue(group);
        this.finish();
    }

}

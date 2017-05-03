package katka.shoppingpayments.screens.invite_friends;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.appinvite.AppInviteInvitation;

import katka.shoppingpayments.R;

public class InviteFriendsActivity extends AppCompatActivity {

    private static final int REQUEST_INVITE = 1;

    private EditText editTextFriendName;
    private Button buttonInviteFriend;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, InviteFriendsActivity.class);
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
                startSendAppInvitationActivityForResult();
            }
        });
    }

    private void startSendAppInvitationActivityForResult() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invite_friends_activity__invitation_title))
                .setMessage(getString(R.string.invite_friends_activity__invitation_message))
                .setDeepLink(Uri.parse("https://play.google.com/store/apps/details?id=air.com.sgn.familyguy.gp&hl=en"))
                .setCallToActionText(getString(R.string.invite_friends_activity__invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
}

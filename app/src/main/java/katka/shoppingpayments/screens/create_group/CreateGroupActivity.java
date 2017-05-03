package katka.shoppingpayments.screens.create_group;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import katka.shoppingpayments.R;
import katka.shoppingpayments.screens.invite_friends.InviteFriendsActivity;

public class CreateGroupActivity extends AppCompatActivity {
    private EditText editTextGroupName;
    private Button buttonCreateGroup;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, CreateGroupActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        initiateParameters();
        setButtonCreateGroup();
    }

    private void startInviteFriendsActivity() {
        InviteFriendsActivity.startActivity(this);
        this.finish();
    }

    private void initiateParameters() {
        editTextGroupName = (EditText) findViewById(R.id.create_group_activity__group_name);
        buttonCreateGroup = (Button) findViewById(R.id.create_group_activity__create_new_group_button);
    }

    private boolean isGroupNameFilled() {
        if (editTextGroupName.getText().toString().isEmpty()) {
            editTextGroupName.setHintTextColor(ContextCompat.getColor(this, R.color.colorError));
            return false;
        }
        return true;
    }

    private void setButtonCreateGroup() {
        buttonCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGroupNameFilled()) {
                    startInviteFriendsActivity();
                }
            }
        });
    }
}

package katka.shoppingpayments.screens.show_groups;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import katka.shoppingpayments.R;

public class ShowGroupsActivity extends AppCompatActivity {

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, ShowGroupsActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_groups);
    }

    @Override
    public boolean onSupportNavigateUp() {
        super.onBackPressed();
        finish();
        return true;
    }

}

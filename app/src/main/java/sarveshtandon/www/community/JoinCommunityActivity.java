package sarveshtandon.www.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class JoinCommunityActivity extends AppCompatActivity {

    private final String emailID1 = "EmailID";
    private final String communities = "Communities";
    public static final String USERNAME = "Username";

    private Button createCommunity;
    private Button enterCommunity;
    private Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_community);
        b = getIntent().getExtras();
        createCommunity = (Button) findViewById(R.id.button);
        enterCommunity = (Button) findViewById(R.id.button2);
        createCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), CreateCommunityActivity.class);
                intent1.putExtra(emailID1, b.getString(emailID1));
                intent1.putExtra(USERNAME, b.getString(USERNAME));
                intent1.putExtra(communities, b.getString(communities));
                startActivity(intent1);

            }
        });
        enterCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(), EnterCommunity.class);
                intent2.putExtra(emailID1, b.getString(emailID1));
                intent2.putExtra(USERNAME, b.getString(USERNAME));
                intent2.putExtra(communities, b.getString(communities));
                startActivity(intent2);
            }
        });
    }

}

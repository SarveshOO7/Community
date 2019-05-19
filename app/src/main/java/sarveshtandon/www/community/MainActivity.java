package sarveshtandon.www.community;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button createCommunity , enterCommunity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        createCommunity = (Button) findViewById(R.id.button);
        enterCommunity = (Button) findViewById(R.id.button2);
        createCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), CreateCommunityActivity.class));
            }
        });
        enterCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),EnterCommunity.class));
            }
        });

    }

//TODO: Add notifications feature
//TODO: Admin being added twice to memebers list Fix it

}
package sarveshtandon.www.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CommunityPage extends AppCompatActivity {

    public static final String USERNAME = "Username";
    public final String DOCUMENT_ID = "Document ID";
    public final String DOCUMENT_REFERNCE = "Document Refernce";
    public final String DESCRIPTION = "description";
    public final String COMMUNITY_NAME = "communityName";
    public final String ADMIN_NAME = "adminName";
    public final String ADMIN_PHONE_NUMBER = "adminPhoneNumber";
    public final String IS_UNRESTRICTED = "isUnrestricted";

    DocumentReference documentReference;
    String documentId,username;
    TextView communityName, adminName, adminPhoneNumber, description;
    ListView membersListView;
    List<DocumentSnapshot> membersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_page);

        communityName = findViewById(R.id.communityNameCommunityPage);
        adminName = findViewById(R.id.adminNameCommunityPage);
        adminPhoneNumber = findViewById(R.id.adminPhoneNumberCommunityPage);
        description = findViewById(R.id.descriptionCommunityPage);
        membersListView = (ListView) findViewById(R.id.memebersListView);

        Bundle b = getIntent().getExtras();
        documentId = b.getString(DOCUMENT_REFERNCE);
        username = b.getString(USERNAME);

        documentReference = FirebaseFirestore.getInstance().collection("Communities").document(documentId);


        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                communityName.setText((CharSequence) documentSnapshot.getString(COMMUNITY_NAME));
                adminName.setText((CharSequence) documentSnapshot.getString(ADMIN_NAME));
                adminPhoneNumber.setText((CharSequence) documentSnapshot.getString(ADMIN_PHONE_NUMBER));
                description.setText((CharSequence)("Description \n"+ documentSnapshot.getString(DESCRIPTION)));
            }
        });
        //TODO: Make Members listview




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Add username to members
                Intent intent;
                intent = new Intent(getApplicationContext(),communityQuestions.class);
                intent.putExtra(DOCUMENT_ID, documentId);
                intent.putExtra(USERNAME, username);
                Snackbar.make(view, "You are now a member of this Community.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                startActivity(intent);
            }
        });
    }
}

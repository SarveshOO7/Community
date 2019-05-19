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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    CollectionReference members;
    String documentId,username;
    TextView communityName, adminName, adminPhoneNumber, description;
    ListView membersListView;
    List<DocumentSnapshot> membersList;
    memebrsListAdapter MemebrsListAdapter;
    private Boolean isPublic;

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
        members = FirebaseFirestore.getInstance().collection("Communities").document(documentId).collection("Members");

        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                communityName.setText((CharSequence) documentSnapshot.getString(COMMUNITY_NAME));
                adminName.setText((CharSequence) documentSnapshot.getString(ADMIN_NAME));
                adminPhoneNumber.setText((CharSequence) documentSnapshot.getString(ADMIN_PHONE_NUMBER));
                description.setText((CharSequence)("Description \n"+ documentSnapshot.getString(DESCRIPTION)));
                isPublic = documentSnapshot.getBoolean(IS_UNRESTRICTED);
            }
        });



        Query q = members.orderBy("Rank").whereGreaterThan("Rank", "");
        q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                membersList = queryDocumentSnapshots.getDocuments();
                memebrsListAdapter MembersListAdapter = new memebrsListAdapter(getApplicationContext(), R.layout.memebers_list_item, membersList);
                membersListView.setAdapter(MembersListAdapter);
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isPublic){
                    Intent intent;
                    intent = new Intent(getApplicationContext(), communityQuestions.class);
                    intent.putExtra(DOCUMENT_ID, documentId);
                    intent.putExtra(USERNAME, username);
                    Map<String, Object> newMember = new HashMap<String, Object>() ;
                    newMember.put("Name", username);
                    newMember.put("Rank", "Rookie");
                    members.add(newMember);
                    startActivity(intent);
                }
                else {
                    Query q2 = members.whereEqualTo("Name", username);
                    q2.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                Intent intent;
                                intent = new Intent(getApplicationContext(), communityQuestions.class);
                                intent.putExtra(DOCUMENT_ID, documentId);
                                intent.putExtra(USERNAME, username);

                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });
    }
}

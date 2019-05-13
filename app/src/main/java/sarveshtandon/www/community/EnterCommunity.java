package sarveshtandon.www.community;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;

import java.util.List;

public class EnterCommunity extends AppCompatActivity {
    public static final String USERNAME = "Username";
    public final String DOCUMENT_REFERNCE = "Document Refernce";
    public final String DESCRIPTION = "description";
    public final String COMMUNITY_NAME = "communityName";
    public final String ADMIN_NAME = "adminName";
    public final String ADMIN_PHONE_NUMBER = "adminPhoneNumber";
    public final String IS_UNRESTRICTED = "isUnrestricted";
    EditText descriptionView, communityNameView, adminNameView, adminPhoneNumberView, userNameView;
    String description, communityName , adminName, adminPhoneNumber, userName;
    Boolean isUnrestricted;
    List<DocumentSnapshot> l;
    ListView communitiesFound;
    private CollectionReference communitiesRef = FirebaseFirestore.getInstance().collection("Communities");
    //private DocumentReference docRef = FirebaseFirestore.getInstance().collection("Communities").document();
    Button searchCommunity;
    Switch isUnrestrictedView;

    public Activity getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_community);

        searchCommunity = (Button) findViewById(R.id.searchForCommunityButton);
        userNameView = (EditText) findViewById(R.id.userName);
        communityNameView = (EditText) findViewById(R.id.searchCommunityName);
        communitiesFound = (ListView) findViewById(R.id.foundCommunities);
        searchCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communityName = communityNameView.getText().toString();
                userName = userNameView.getText().toString();
                if((!communityName.isEmpty())&&(!userName.isEmpty())){
                    Query query = communitiesRef.whereEqualTo(COMMUNITY_NAME, communityName);
                    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            l = queryDocumentSnapshots.getDocuments();
                            foundCommunitiesAdapter mfoundCommunitiesAdapter = new foundCommunitiesAdapter(getApplicationContext(), R.layout.found_communities_list_item,l);
                            communitiesFound.setAdapter(mfoundCommunitiesAdapter);



                            // Make List Adapter
                            communitiesFound.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long x) {
                                    Intent intent;
                                    intent = new Intent(EnterCommunity.this, CommunityPage.class);
                                    DocumentSnapshot d = l.get(i);
                                    intent.putExtra(DESCRIPTION, d.getString(DESCRIPTION) );
                                    intent.putExtra(ADMIN_NAME, d.getString(ADMIN_NAME) );
                                    intent.putExtra(ADMIN_PHONE_NUMBER, d.getString(ADMIN_PHONE_NUMBER) );
                                    intent.putExtra(IS_UNRESTRICTED, d.getBoolean(IS_UNRESTRICTED) );
                                    intent.putExtra(COMMUNITY_NAME, d.getString(COMMUNITY_NAME) );
                                    intent.putExtra(DOCUMENT_REFERNCE, d.getId());
                                    intent.putExtra(USERNAME, userName);
                                    startActivity(intent);
                                }
                            });
                            Toast.makeText(EnterCommunity.this, "There you go!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Snackbar.make(view, "Fill in all details!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
}

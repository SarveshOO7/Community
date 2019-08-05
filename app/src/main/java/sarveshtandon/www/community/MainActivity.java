package sarveshtandon.www.community;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private final String emailID1 = "EmailID";
    private final String communities = "Communities";
    public final String DOCUMENT_REFERNCE = "Document Refernce";
    public final String DESCRIPTION = "description";
    public final String COMMUNITY_NAME = "communityName";
    public final String ADMIN_NAME = "adminName";
    public final String ADMIN_PHONE_NUMBER = "adminPhoneNumber";
    public final String IS_UNRESTRICTED = "isUnrestricted";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 1;
    public static final String USERNAME = "Username";
    public String username, emailID;
    private static final String ANONYMOUS = "Guest";
    private Intent intent;
    DocumentReference userDoc;
    CollectionReference users,communitiesRef;
    private List<DocumentSnapshot> joinedCommunities = new ArrayList<DocumentSnapshot>();
    private Query q;
    ListView joinedCommunitiesListView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fab = findViewById(R.id.create);
        joinedCommunitiesListView = findViewById(R.id.joinedCommunities);

        users = FirebaseFirestore.getInstance().collection("Users");
        communitiesRef = FirebaseFirestore.getInstance().collection("Communities");

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(getApplicationContext(), JoinCommunityActivity.class);
                intent.putExtra(USERNAME, username);
                intent.putExtra(emailID1, emailID);
                startActivity(intent);
            }
        });

        mFirebaseAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    username = user.getDisplayName();
                    emailID = user.getEmail();
                    Toast.makeText(MainActivity.this, "You're now signed in. Welcome to Community, "+username, Toast.LENGTH_SHORT).show();



                    userDoc = FirebaseFirestore.getInstance().collection("Users").document(emailID+"");
                    final CollectionReference userCommunities = FirebaseFirestore.getInstance().collection("Users").document(emailID+"").collection("Communities");


                    userDoc.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if(documentSnapshot!=null){
                                Query q = userCommunities.whereGreaterThanOrEqualTo("Rank", "").orderBy("Rank");
                                q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        joinedCommunities = queryDocumentSnapshots.getDocuments();
                                        JoinedCommunitiesAdapter joinedCommunitiesAdapter = new JoinedCommunitiesAdapter(getApplicationContext(), R.layout.communities_list_item, joinedCommunities);
                                        joinedCommunitiesListView.setAdapter(joinedCommunitiesAdapter);
                                        joinedCommunitiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long x) {
                                                DocumentSnapshot pressedCommunity = joinedCommunities.get(i);
                                                Query q = communitiesRef.whereEqualTo("communityName", pressedCommunity.getString("Name")) ;
                                                q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                                        openCommunityPage(queryDocumentSnapshots.getDocuments().get(0).getId());
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });

                            }
                            else{
                                Map<String, Object> newUserData = new HashMap<String, Object>();
                                newUserData.put(USERNAME, username);
                                newUserData.put(emailID1, emailID);
                                users.document(emailID).set(newUserData);
                                joinedCommunities = Collections.emptyList();

                            }
                            JoinedCommunitiesAdapter joinedCommunitiesAdapter = new JoinedCommunitiesAdapter(getApplicationContext(), R.layout.communities_list_item, joinedCommunities);
                            joinedCommunitiesListView.setAdapter(joinedCommunitiesAdapter);
                            //Toast.makeText(MainActivity.this, "There you go!!!"+joinedCommunities.isEmpty(), Toast.LENGTH_SHORT).show();
                        }
                    });




                } else {
                    // User is signed out
                    username = ANONYMOUS;
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setAvailableProviders(Arrays.asList(
                                            //new AuthUI.IdpConfig.EmailBuilder().build(),
                                            new AuthUI.IdpConfig.GoogleBuilder().build()))
                                    .setLogo(R.mipmap.ic_launcher)
                                    .build(),
                            RC_SIGN_IN);
                }

            }
        };

    }

    private void openCommunityPage(String id) {
        DocumentReference communitySelected = FirebaseFirestore.getInstance().collection("Communities").document(id);
        communitySelected.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot d) {
                Intent intent;
                intent = new Intent(MainActivity.this, CommunityPage.class);

                intent.putExtra(DESCRIPTION, d.getString(DESCRIPTION) );
                intent.putExtra(ADMIN_NAME, d.getString(ADMIN_NAME) );
                intent.putExtra(ADMIN_PHONE_NUMBER, d.getString(ADMIN_PHONE_NUMBER) );
                intent.putExtra(IS_UNRESTRICTED, d.getBoolean(IS_UNRESTRICTED) );
                intent.putExtra(COMMUNITY_NAME, d.getString(COMMUNITY_NAME) );
                intent.putExtra(DOCUMENT_REFERNCE, d.getId());
                intent.putExtra(USERNAME, username);
                intent.putExtra(emailID1, emailID);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                username = user.getDisplayName();
                emailID = user.getEmail();
                userDoc = FirebaseFirestore.getInstance().collection("Users").document(emailID+"");
                Map<String, Object> newUserData = new HashMap<String, Object>();
                newUserData.put(USERNAME, username);
                newUserData.put(emailID1, emailID);
                users.document(emailID).set(newUserData);


                Toast.makeText(this, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(this, "Sign in canceled"+data.getDataString(), Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
    @Override
    public void onPause() {

        super.onPause();
        if(mAuthStateListener!=null)
            mFirebaseAuth.removeAuthStateListener(mAuthStateListener);
        detachDatabaseReadListener();
    }

    private void detachDatabaseReadListener() {
        username = ANONYMOUS;
    }

    @Override
    public void onResume() {

        super.onResume();

        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

//TODO: Add notifications feature
}
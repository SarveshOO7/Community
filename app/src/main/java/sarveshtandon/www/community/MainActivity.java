package sarveshtandon.www.community;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;



import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private final String emailID1 = "EmailID";
    private final String communities = "Communities";

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private static final int RC_SIGN_IN = 1;
    public static final String USERNAME = "Username";
    public String username, emailID;
    private static final String ANONYMOUS = "Guest";
    private Intent intent;
    DocumentReference userDoc;
    CollectionReference users;
    private List<DocumentSnapshot> joinedCommunities = new ArrayList<>();
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
                                Query q = userCommunities.orderBy("Rank");
                                q.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        joinedCommunities = queryDocumentSnapshots.getDocuments();
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
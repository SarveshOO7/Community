package sarveshtandon.www.community;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.Comparator;
import java.util.List;

public class EnterCommunity extends AppCompatActivity {
    public static final String USERNAME = "Username";
    public final String DOCUMENT_REFERNCE = "Document Refernce";
    public final String DESCRIPTION = "description";
    public final String COMMUNITY_NAME = "communityName";
    public final String ADMIN_NAME = "adminName";
    public final String ADMIN_PHONE_NUMBER = "adminPhoneNumber";
    public final String IS_UNRESTRICTED = "isUnrestricted";
    public static final String COMMUNITY_NAME_CASE_IGNORE = "communityNameCaseIgnore";


    private final String emailID1 = "EmailID";
    private final String communities = "Communities";

    EditText descriptionView, communityNameView, adminNameView, adminPhoneNumberView, userNameView;
    String description, communityName , adminName, adminPhoneNumber, userName;
    Boolean isUnrestricted;
    List<DocumentSnapshot> l;
    ListView communitiesFound;
    private CollectionReference communitiesRef = FirebaseFirestore.getInstance().collection("Communities");
    //private DocumentReference docRef = FirebaseFirestore.getInstance().collection("Communities").document();
    Button searchCommunity;
    Switch isUnrestrictedView;

    Bundle b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_community);

        b = getIntent().getExtras();

        searchCommunity = (Button) findViewById(R.id.searchForCommunityButton);
        userNameView = (EditText) findViewById(R.id.userName);
        communityNameView = (EditText) findViewById(R.id.searchCommunityName);
        communitiesFound = (ListView) findViewById(R.id.foundCommunities);

        userNameView.setText(b.getString(USERNAME));

        communityNameView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                communityName = communityNameView.getText().toString();
                userName = userNameView.getText().toString();
                if((!communityName.isEmpty())&&(!userName.isEmpty())){
                    Query query = communitiesRef.orderBy(COMMUNITY_NAME_CASE_IGNORE).startAt(communityName.toUpperCase()).endAt(communityName.toUpperCase()+"\uf8ff");
                    query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            l = queryDocumentSnapshots.getDocuments();
                           /*
                            //to order queried documents in custom way
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                l.sort(new Comparator<DocumentSnapshot>() {
                                    @Override
                                    public int compare(DocumentSnapshot o1, DocumentSnapshot o2) {
                                        String commName1 = o1.getString(COMMUNITY_NAME).toLowerCase(), commName2 = o2.getString(COMMUNITY_NAME).toLowerCase() ;
                                        if(compStr(commName1, commName2)<0) // compStr is defined at line 187
                                            return -1;
                                        if(compStr(commName1, commName2)>0)
                                            return 1;
                                        return 0;
                                    }
                                });
                            }
                            */
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
                                    intent.putExtra(emailID1, b.getString(emailID1));
                                    startActivity(intent);
                                }
                            });
                            Toast.makeText(EnterCommunity.this, "There you go!!!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else{
                    Snackbar.make(communityNameView, "Fill in all details!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communityName = communityNameView.getText().toString();
                userName = userNameView.getText().toString();
                if((!communityName.isEmpty())&&(!userName.isEmpty())){
                    Query query = communitiesRef.orderBy(COMMUNITY_NAME_CASE_IGNORE).startAt(communityName).endAt(communityName+"\uf8ff");
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
                                    intent.putExtra(emailID1, b.getString(emailID1));
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
/*
    private int compStr( String a, String b){
        int ch1=0, ch2=0 , minLen = Math.min(a.length(), b.length());
        for(int i=0;i<minLen;i++){
            ch1 += (int) a.charAt(i);
            ch2 += (int) b.charAt(i);
        }
        return ch1-ch2;
    }

 */
}

package sarveshtandon.www.community;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreateCommunityActivity extends AppCompatActivity {
    public static final String DESCRIPTION = "description";
    public static final String COMMUNITY_NAME = "communityName";
    public static final String ADMIN_NAME = "adminName";
    public static final String ADMIN_PHONE_NUMBER = "adminPhoneNumber";
    public static final String IS_UNRESTRICTED = "isUnrestricted";
    public static final String MEMBERS_LIST = "Memeber List";
    public static final String NAME = "Name";
    public static final String RANK = "Rank";
    public static final String CHIEF = "Chief";
    public static final String COMMUNITY_NAME_CASE_IGNORE = "communityNameCaseIgnore";

    private final String emailID1 = "EmailID";
    private final String communities = "Communities";
    public static final String USERNAME = "Username";

    EditText descriptionView, communityNameView, adminNameView, adminPhoneNumberView;
    String description, communityName , adminName, adminPhoneNumber;
    Boolean isUnrestricted , isSucess= false;
    private CollectionReference communitiesRef = FirebaseFirestore.getInstance().collection("Communities"), members;
    Button submitCommunity;
    public final String DOCUMENT_REFERNCE = "Document Refernce";
    Switch isUnrestrictedView;
    String documentid;
    CollectionReference userCommunities;

    Bundle b;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community);

        b= getIntent().getExtras();
        userCommunities = FirebaseFirestore.getInstance().collection("Users").document(b.getString(emailID1, "Tester")).collection(communities);

        descriptionView = (EditText) findViewById(R.id.description);
        communityNameView = (EditText) findViewById(R.id.CommunityName);
        adminNameView = (EditText) findViewById(R.id.adminName);
        adminPhoneNumberView = (EditText) findViewById(R.id.adminPhoneNumber);
        submitCommunity = (Button) findViewById(R.id.submitCommunity);
        isUnrestrictedView = (Switch) findViewById(R.id.isUnrestricted);

        adminNameView.setText(b.getString(USERNAME));

        submitCommunity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                description = descriptionView.getText().toString();
                communityName = communityNameView.getText().toString();
                adminName = adminNameView.getText().toString();
                adminPhoneNumber = adminPhoneNumberView.getText().toString();
                isUnrestricted = isUnrestrictedView.isChecked();
                if(description.isEmpty() || communityName.isEmpty() || adminName.isEmpty() || adminPhoneNumber.isEmpty()){
                    Toast.makeText(CreateCommunityActivity.this, "Please fill the form! Unable to create community!!! :(", Toast.LENGTH_SHORT).show();
                }
                else {
                    Map<String, Object> dataToSave = new HashMap<String, Object>();
                    dataToSave.put(DESCRIPTION, description);
                    dataToSave.put(COMMUNITY_NAME, communityName);
                    dataToSave.put(COMMUNITY_NAME_CASE_IGNORE, communityName.toUpperCase());
                    dataToSave.put(ADMIN_NAME, adminName);
                    dataToSave.put(ADMIN_PHONE_NUMBER, adminPhoneNumber);
                    dataToSave.put(IS_UNRESTRICTED, isUnrestricted);
                    communitiesRef.add(dataToSave).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(final DocumentReference documentReference) {
                            Toast.makeText(CreateCommunityActivity.this, "Community Created!!!", Toast.LENGTH_SHORT).show();
                            isSucess = true;
                            documentid = documentReference.getId();
                            members = FirebaseFirestore.getInstance().collection("Communities").document(documentid).collection("Members");
                            Map<String, Object> k = new HashMap<String, Object>();
                            k.put(NAME, adminName);
                            k.put(RANK, CHIEF);
                            members.add(k);
                            Map<String, Object> communityData = new HashMap<String, Object>();
                            communityData.put(NAME, communityName);
                            communityData.put(RANK, CHIEF);
                            userCommunities.add(communityData).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(CreateCommunityActivity.this, "Unable to create community! :(", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                            Intent intent = new Intent(getApplicationContext(), CommunityPage.class);
                            intent.putExtra(DOCUMENT_REFERNCE, documentid);
                            startActivity(intent);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateCommunityActivity.this, "Unable to create community!!! :(", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}

package sarveshtandon.www.community;

import android.content.Intent;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class CreateCommunityActivity extends AppCompatActivity {
    public static final String DESCRIPTION = "description";
    public static final String COMMUNITY_NAME = "communityName";
    public static final String ADMIN_NAME = "adminName";
    public static final String ADMIN_PHONE_NUMBER = "adminPhoneNumber";
    public static final String IS_UNRESTRICTED = "isUnrestricted";
    EditText descriptionView, communityNameView, adminNameView, adminPhoneNumberView;
    String description, communityName , adminName, adminPhoneNumber;
    Boolean isUnrestricted , isSucess= false;
    private CollectionReference communitiesRef = FirebaseFirestore.getInstance().collection("Communities");
    Button submitCommunity;
    public final String DOCUMENT_REFERNCE = "Document Refernce";
    Switch isUnrestrictedView;
    String documentid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_community);
        descriptionView = (EditText) findViewById(R.id.description);
        communityNameView = (EditText) findViewById(R.id.CommunityName);
        adminNameView = (EditText) findViewById(R.id.adminName);
        adminPhoneNumberView = (EditText) findViewById(R.id.adminPhoneNumber);
        submitCommunity = (Button) findViewById(R.id.submitCommunity);
        isUnrestrictedView = (Switch) findViewById(R.id.isUnrestricted);
        submitCommunity.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                description = descriptionView.getText().toString();
                communityName = communityNameView.getText().toString();
                adminName = adminNameView.getText().toString();
                adminPhoneNumber = adminPhoneNumberView.getText().toString();
                isUnrestricted = isUnrestrictedView.isChecked();

                Map<String, Object> dataToSave = new HashMap<String, Object>();
                dataToSave.put(DESCRIPTION, description);
                dataToSave.put(COMMUNITY_NAME, communityName);
                dataToSave.put(ADMIN_NAME, adminName);
                dataToSave.put(ADMIN_PHONE_NUMBER, adminPhoneNumber);
                dataToSave.put(IS_UNRESTRICTED, isUnrestricted);
                communitiesRef.add(dataToSave).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(CreateCommunityActivity.this, "Community Created!!!", Toast.LENGTH_SHORT).show();
                        isSucess=true;
                        documentid = documentReference.getId();
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
        });

    }
}

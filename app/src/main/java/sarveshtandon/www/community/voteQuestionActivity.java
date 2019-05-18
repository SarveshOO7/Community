package sarveshtandon.www.community;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class voteQuestionActivity extends AppCompatActivity {
    public static final String QUESTION_ID = "Question ID";
    public final String DOCUMENT_ID = "Document ID";
    public static final String USERNAME = "Username";
    public static final String QUESTION_TITLE = "Question Title";
    public static final String QUESTION_DETAIL = "Question Detail";
    public static final String CHOICE_1 = "Choice 1";
    public static final String CHOICE_2 = "Choice 2";
    public static final String CHOICE_3 = "Choice 3";
    public static final String CHOICE_4 = "Choice 4";
    public static final String IS_OPEN = "isOpen";

    Bundle b;
    DocumentReference question;
    String username;
    CheckBox choice1, choice2, choice3, choice4;
    TextView questionTitle, questionDetails;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_question);

        choice1 = findViewById(R.id.option1CheckBox);
        choice2 = findViewById(R.id.option2CheckBox);
        choice3 = findViewById(R.id.option3CheckBox);
        choice4 = findViewById(R.id.option4CheckBox);
        questionTitle = findViewById(R.id.questionTitleVote);
        questionDetails = findViewById(R.id.questionDeatilsVote);
        submit = findViewById(R.id.submitVote);

        b = getIntent().getExtras();
        question = FirebaseFirestore.getInstance().collection("Communities").document(b.getString(DOCUMENT_ID)).collection("Questions").document(b.getString(QUESTION_ID));
        username = b.getString(USERNAME);

        question.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(final DocumentSnapshot documentSnapshot) {
                choice1.setText(documentSnapshot.getString(CHOICE_1));
                choice2.setText(documentSnapshot.getString(CHOICE_2));
                choice3.setText(documentSnapshot.getString(CHOICE_3));
                choice4.setText(documentSnapshot.getString(CHOICE_4));
                questionDetails.setText(documentSnapshot.getString(QUESTION_DETAIL));
                questionTitle.setText(documentSnapshot.getString(QUESTION_TITLE));
                if(documentSnapshot.getString(CHOICE_1).isEmpty())
                    choice1.setVisibility(View.INVISIBLE);
                if(documentSnapshot.getString(CHOICE_2).isEmpty())
                    choice2.setVisibility(View.INVISIBLE);
                if(documentSnapshot.getString(CHOICE_3).isEmpty())
                    choice3.setVisibility(View.INVISIBLE);
                if(documentSnapshot.getString(CHOICE_4).isEmpty())
                    choice4.setVisibility(View.INVISIBLE);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(documentSnapshot.getBoolean(IS_OPEN)){
                            Map<String, Object> vote = new HashMap<String, Object>();
                            vote.put(USERNAME,username);
                            vote.put(CHOICE_1,choice1.isChecked());
                            vote.put(CHOICE_2, choice2.isChecked());
                            vote.put(CHOICE_3, choice3.isChecked());
                            vote.put(CHOICE_4, choice4.isChecked());
                            question.collection("Votes").add(vote);
                        }
                        else
                            Snackbar.make(view, "Sorry! This Question has been closed.", Snackbar.LENGTH_LONG);
                    }
                });
            }
        });


    }
}

package sarveshtandon.www.community;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class addQuestionActivity extends AppCompatActivity {
    public static final String QUESTION_TITLE = "Question Title";
    public static final String QUESTION_DETAIL = "Question Detail";
    public static final String CHOICE_1 = "Choice 1";
    public static final String CHOICE_2 = "Choice 2";
    public static final String CHOICE_3 = "Choice 3";
    public static final String CHOICE_4 = "Choice 4";
    public static final String IS_OPEN = "isOpen";
    public static final String CHOICE_1_VOTES = "Choice 1 Votes";
    public static final String CHOICE_2_VOTES = "Choice 2 Votes";
    public static final String CHOICE_3_VOTES = "Choice 3 Votes";
    public static final String CHOICE_4_VOTES = "Choice 4 Votes";
    TextView questionTitle, questionDetails,choice1,choice2,choice3,choice4;
    Button submitQuestion;
    CollectionReference questions;
    Bundle b;
    Boolean isOpen;
    String communityId, questionTitleValue, questionDetailValue, choice1Value,choice2Value,choice3Value,choice4Value;
    public static final String QUESTIONS_COLLECTION_PATH = "Questions Collection Path";
    public final String DOCUMENT_ID = "Document ID";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_question);
        questionTitle = (TextView) findViewById(R.id.question_title);
        questionDetails = (TextView) findViewById(R.id.question_details);
        choice1 = (TextView) findViewById(R.id.choice1);
        choice2 = (TextView) findViewById(R.id.choice2);
        choice3 = (TextView) findViewById(R.id.choice3);
        choice4 = (TextView) findViewById(R.id.choice4);
        submitQuestion = (Button) findViewById(R.id.submitQuestion);

        b = getIntent().getExtras();
        communityId = b.getString(QUESTIONS_COLLECTION_PATH);
        questions = FirebaseFirestore.getInstance().collection(communityId);

        submitQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                questionTitleValue = (String) questionTitle.getText().toString();
                questionDetailValue = (String) questionDetails.getText().toString();
                choice1Value = (String) choice1.getText().toString();
                choice2Value = (String) choice2.getText().toString();
                choice3Value = (String) choice3.getText().toString();
                choice4Value = (String) choice4.getText().toString();
                isOpen=true;

                Map<String, Object> question = new HashMap<String, Object>();
                question.put(QUESTION_TITLE, questionTitleValue);
                question.put(QUESTION_DETAIL, questionDetailValue);
                question.put(CHOICE_1,choice1Value);
                question.put(CHOICE_2,choice2Value);
                question.put(CHOICE_3,choice3Value);
                question.put(CHOICE_4,choice4Value);
                question.put(IS_OPEN,isOpen);
                question.put(CHOICE_1_VOTES, 0L);
                question.put(CHOICE_2_VOTES, 0L);
                question.put(CHOICE_3_VOTES, 0L);
                question.put(CHOICE_4_VOTES, 0L);


                questions.add(question).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Snackbar.make(view, "Question Added!!!", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                        Intent intent = new Intent(getApplicationContext(),communityQuestions.class);
                        intent.putExtra(DOCUMENT_ID, b.getString(DOCUMENT_ID));
                        intent.putExtra("show toast", true);
                        startActivity(intent);
                    }
                });
            }
        });
    }
}

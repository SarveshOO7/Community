package sarveshtandon.www.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class communityQuestions extends AppCompatActivity {
    public static final String QUESTION_TITLE = "Question Title";
    public static final String QUESTION_DETAIL = "Question Detail";
    public static final String CHOICE_1 = "Choice 1";
    public static final String CHOICE_2 = "Choice 2";
    public static final String CHOICE_3 = "Choice 3";

    public static final String USERNAME = "Username";
    public static final String CHOICE_4 = "Choice 4";
    public static final String IS_OPEN = "isOpen";
    public static final String QUESTION_DATE = "Date";
    public static final String QUESTIONS_COLLECTION_PATH = "Questions Collection Path";
    public static final String QUESTION_ID = "Question ID";
    public final String DOCUMENT_ID = "Document ID";
    public String documentId;
    CollectionReference questions;
    Query query;
    Intent intent;
    ListView questionsList;
    List<DocumentSnapshot> l;
    Bundle b;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_questions);
        b= getIntent().getExtras();
        documentId = b.getString(DOCUMENT_ID);
        questions = FirebaseFirestore.getInstance().collection("Communities").document(documentId).collection("Questions");
        query = questions.whereGreaterThan(QUESTION_TITLE, "");
        questionsList= (ListView) findViewById(R.id.questionList);
        query.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                l = queryDocumentSnapshots.getDocuments();
                questionsAdapter QuestionsAdapter= new questionsAdapter(getApplicationContext(), R.layout.question, l);
                questionsList.setAdapter(QuestionsAdapter);
                questionsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long x) {
                        DocumentSnapshot d = l.get(i);
                        Intent intent = new Intent(communityQuestions.this, voteQuestionActivity.class);
                        intent.putExtra(DOCUMENT_ID, documentId);
                        intent.putExtra(QUESTION_ID, d.getId());
                        intent.putExtra(USERNAME, b.getString(USERNAME));
                        startActivity(intent);

                    }
                });
            }
        });
        //TODO: Make questions list Clickable
        intent = new Intent(getApplicationContext(), addQuestionActivity.class);
        intent.putExtra(QUESTIONS_COLLECTION_PATH, questions.getPath());
        intent.putExtra(DOCUMENT_ID, documentId);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Let's add a question!!!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                startActivity(intent);
            }
        });

    }

}

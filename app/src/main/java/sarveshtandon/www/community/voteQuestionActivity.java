package sarveshtandon.www.community;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class voteQuestionActivity extends AppCompatActivity {
    public static final String QUESTION_ID = "Question ID";
    public static final String CHOICE_1_VOTES = "Choice 1 Votes";
    public static final String CHOICE_2_VOTES = "Choice 2 Votes";
    public static final String CHOICE_3_VOTES = "Choice 3 Votes";
    public static final String CHOICE_4_VOTES = "Choice 4 Votes";
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
    Boolean hasVoted = false;
    Button submit;
    PieChart pieChart;
    ArrayList choiceValues= new ArrayList();
    Float choice1Percentage, choice2Percentage, choice3Percentage, choice4Percentage, sum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_question);

        pieChart = findViewById(R.id.pieChart);

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
            @RequiresApi(api = Build.VERSION_CODES.M)
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
                            if(hasVoted)
                                Snackbar.make(view, "You have already voted!", Snackbar.LENGTH_INDEFINITE);
                            else {
                                Map<String, Object> vote = new HashMap<String, Object>();
                                vote.put(USERNAME, username);
                                vote.put(CHOICE_1, choice1.isChecked());
                                vote.put(CHOICE_2, choice2.isChecked());
                                vote.put(CHOICE_3, choice3.isChecked());
                                vote.put(CHOICE_4, choice4.isChecked());
                                if (choice1.isChecked()) {
                                    question.update(CHOICE_1_VOTES, documentSnapshot.getLong(CHOICE_1_VOTES) + 1L);
                                }
                                if (choice2.isChecked()) {
                                    question.update(CHOICE_2_VOTES, documentSnapshot.getLong(CHOICE_2_VOTES) + 1L);
                                }
                                if (choice3.isChecked()) {
                                    question.update(CHOICE_3_VOTES, documentSnapshot.getLong(CHOICE_3_VOTES) + 1L);
                                }
                                if (choice4.isChecked()) {
                                    question.update(CHOICE_4_VOTES, documentSnapshot.getLong(CHOICE_4_VOTES) + 1L);
                                }

                                choice1Percentage = Float.valueOf(documentSnapshot.getLong(CHOICE_1_VOTES));
                                choice2Percentage = Float.valueOf(documentSnapshot.getLong(CHOICE_2_VOTES));
                                choice3Percentage = Float.valueOf(documentSnapshot.getLong(CHOICE_3_VOTES));
                                choice4Percentage = Float.valueOf(documentSnapshot.getLong(CHOICE_4_VOTES));

                                sum = choice1Percentage + choice2Percentage + choice3Percentage +choice4Percentage;

                                if(documentSnapshot.getBoolean(IS_OPEN))
                                    pieChart.setVisibility(View.INVISIBLE);

                                choice1Percentage  /=sum/100;
                                choice2Percentage  /=sum/100;
                                choice3Percentage  /=sum/100;
                                choice4Percentage  /=sum/100;

                                choiceValues.add(new Entry(choice1Percentage,0 ));
                                choiceValues.add(new Entry(choice2Percentage,1 ));
                                choiceValues.add(new Entry(choice3Percentage,2 ));
                                choiceValues.add(new Entry(choice4Percentage,3 ));


                                PieDataSet pieDataSet = new PieDataSet(choiceValues, "Votes");

                                ArrayList year = new ArrayList();

                                year.add("");
                                year.add("");
                                year.add("");
                                year.add("");

                                PieData pieData = new PieData(year, pieDataSet);
                                pieChart.setData(pieData);
                                int[] colors = {
                                        getColor(R.color.colorAccent),
                                        getColor(R.color.colorPrimary),
                                        getColor(R.color.choice3color),
                                        getColor(R.color.choice4color)
                                };


                                pieDataSet.setColors(colors);
                                pieDataSet.setValueTextSize(20f);
                                pieDataSet.setValueTextColor(R.color.white);


                                question.collection("Votes").add(vote);
                                pieChart.setVisibility(View.VISIBLE);
                                pieChart.animateXY(5000, 5000);
                                hasVoted = true;
                            }

                        }
                        else
                            Snackbar.make(view, "Sorry! This Question has been closed.", Snackbar.LENGTH_LONG);
                    }
                });
            }
        });


    }
}

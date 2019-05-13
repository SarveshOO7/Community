package sarveshtandon.www.community;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.util.List;

public class questionsAdapter extends ArrayAdapter<DocumentSnapshot> {
    public static final String DESCRIPTION = "description";
    public static final String COMMUNITY_NAME = "communityName";
    public static final String ADMIN_NAME = "adminName";
    public static final String ADMIN_PHONE_NUMBER = "adminPhoneNumber";
    public static final String IS_UNRESTRICTED = "isUnrestricted";
    public static final String QUESTION_TITLE = "Question Title";
    public static final String QUESTION_DETAIL = "Question Detail";
    public static final String CHOICE_1 = "Choice 1";
    public static final String CHOICE_2 = "Choice 2";
    public static final String CHOICE_3 = "Choice 3";
    public static final String CHOICE_4 = "Choice 4";
    public static final String IS_OPEN = "isOpen";
    public static final String QUESTION_DATE = "Date";
    Context context;
    public questionsAdapter(Context context, int resource, List<DocumentSnapshot> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater v = LayoutInflater.from(context);
            convertView = v.inflate(R.layout.question, parent, false);
        }

        TextView questionTitle = (TextView) convertView.findViewById(R.id.question);
        TextView isOpen = (TextView) convertView.findViewById(R.id.isOpen);
        DocumentSnapshot d = getItem(position);
        questionTitle.setVisibility(View.VISIBLE);
        questionTitle.setText(d.getString(QUESTION_TITLE));
        Boolean isPublicValue = d.getBoolean(IS_OPEN);
        if(isPublicValue) {
            isOpen.setText("Open");
        }
        else {
            isOpen.setText("Closed");
        }
        return convertView;
    }
}

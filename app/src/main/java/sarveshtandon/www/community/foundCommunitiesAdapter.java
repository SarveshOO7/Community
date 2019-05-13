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

public class foundCommunitiesAdapter extends ArrayAdapter<DocumentSnapshot> {
    public static final String DESCRIPTION = "description";
    public static final String COMMUNITY_NAME = "communityName";
    public static final String ADMIN_NAME = "adminName";
    public static final String ADMIN_PHONE_NUMBER = "adminPhoneNumber";
    public static final String IS_UNRESTRICTED = "isUnrestricted";
    Context context;
    public foundCommunitiesAdapter(Context context, int resource, List<DocumentSnapshot> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater v = LayoutInflater.from(context);
            convertView = v.inflate(R.layout.found_communities_list_item, parent, false);
        }

        TextView foundCommunityName = (TextView) convertView.findViewById(R.id.communityFoundName);
        TextView isPublic = (TextView) convertView.findViewById(R.id.communityFoundIsPublic);
        DocumentSnapshot d = getItem(position);
        foundCommunityName.setVisibility(View.VISIBLE);
        foundCommunityName.setText(d.getString(COMMUNITY_NAME));
        Boolean isPublicValue = d.getBoolean(IS_UNRESTRICTED);
        if(isPublicValue)
            isPublic.setText("Public");
        else
            isPublic.setText("Private");
        return convertView;
    }
}

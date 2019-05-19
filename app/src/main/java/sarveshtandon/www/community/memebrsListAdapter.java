package sarveshtandon.www.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public class memebrsListAdapter extends ArrayAdapter<DocumentSnapshot> {
    public static final String DESCRIPTION = "description";
    public static final String COMMUNITY_NAME = "communityName";
    public static final String ADMIN_NAME = "adminName";
    public static final String ADMIN_PHONE_NUMBER = "adminPhoneNumber";
    public static final String IS_UNRESTRICTED = "isUnrestricted";
    public static final String NAME = "Name";
    public static final String RANK = "Rank";
    Context context;
    public memebrsListAdapter(Context context, int resource, List<DocumentSnapshot> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater v = LayoutInflater.from(context);
            convertView = v.inflate(R.layout.memebers_list_item, parent, false);
        }

        TextView Name = (TextView) convertView.findViewById(R.id.memberName);
        TextView Rank = (TextView) convertView.findViewById(R.id.isAdmin);
        DocumentSnapshot d = getItem(position);
        Name.setVisibility(View.VISIBLE);
        Name.setText(d.getString(NAME));
        Rank.setVisibility(View.VISIBLE);
        Rank.setText(d.getString(RANK));
        return convertView;
    }
}

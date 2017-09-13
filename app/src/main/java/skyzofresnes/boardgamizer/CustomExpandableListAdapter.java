package skyzofresnes.boardgamizer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomExpandableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> expandableListTitle;
    private HashMap<String, List<String>> expandableListDetail;
    private ArrayList<ArrayList<Integer>> check_states = new ArrayList<>();

    public CustomExpandableListAdapter(Context context, List<String> expandableListTitle,
                                       HashMap<String, List<String>> expandableListDetail,
                                       ArrayList<ArrayList<Integer>> check_states) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
        this.check_states = check_states;
    }

    public ArrayList<ArrayList<Integer>> getCheck_states() {
        return check_states;
    }

    public void setCheck_states(ArrayList<ArrayList<Integer>> check_states) {
        this.check_states = check_states;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListChildText = (String) getChild(listPosition, expandedListPosition);
        final int expandedListTextInt = this.context.getResources().getIdentifier(Constantes.VALUE_STRING + expandedListChildText.substring(1), Constantes.VALUE, this.context.getPackageName());
        final String expandedListText = this.context.getString(expandedListTextInt);
        final int grpPos = listPosition;
        final int childPos = expandedListPosition;

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item, null);
        }

        TextView expandedListTextView = (TextView) convertView.findViewById(R.id.expandedListItem);
        ImageView checkBoxItemFilters = (ImageView) convertView.findViewById((R.id.checkBox_itemFilters));

        expandedListTextView.setText(expandedListText);

        if(check_states.get(grpPos).get(childPos) == 1) {
            expandedListTextView.setTextColor(Color.RED);
            checkBoxItemFilters.setBackgroundResource(R.drawable.checked);
        }else {
            expandedListTextView.setTextColor(Color.BLACK);
            checkBoxItemFilters.setBackgroundResource(R.drawable.check);
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition)).size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }
        TextView listTitleTextView = (TextView) convertView.findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);

        ImageView imgViewGroupFilters = (ImageView) convertView.findViewById(R.id.imageView_groupFilters);

        if (isExpanded){
            imgViewGroupFilters.setBackgroundResource(R.drawable.collapse);
        }else{
            imgViewGroupFilters.setBackgroundResource(R.drawable.expand);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public void updateRecords(ArrayList<ArrayList<Integer>> check_states){
        this.check_states = check_states;

        notifyDataSetChanged();
    }
}
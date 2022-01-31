package com.example.kontaktverfolgungapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.example.kontaktverfolgungapp.dbclient.Visit;

import java.util.HashMap;
import java.util.List;

public class MainAdapter extends BaseExpandableListAdapter {

    Context context;
    List<Visit> visits;
    HashMap<String, List<String>> listItemGroups;

    public MainAdapter(Context context, List<Visit> groups, HashMap<String, List <String>> itemsGroups){
        // initialize class variables
        this.context = context;
        visits = groups;
        listItemGroups = itemsGroups;
    }

    @Override
    public int getGroupCount() {
        // returns groups count
        return visits.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // returns items count of a group
        return listItemGroups.get(((Visit)getGroup(groupPosition)).getPlaceName()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        // returns a group
        return visits.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // returns a group item
        return listItemGroups.get(((Visit)getGroup(groupPosition)).getPlaceName()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        // return the group id
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // returns the item id of group
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // returns if the ids are specific ( unique for each group or item)
        // or relatives
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // create main items (groups)
        Visit group = (Visit)  getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView list_name = (TextView) convertView.findViewById(R.id.list_name);
        TextView list_date = (TextView) convertView.findViewById(R.id.list_datum);

        list_name.setText(group.getPlaceName());
        list_date.setText(group.getDateTime());
        //list_datum.setText(String.valueOf(getChildrenDatum(groupPosition)));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // create the subitems (items of groups)
        String child = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_item_group, null);
        }
        TextView list_child_name_item = (TextView) convertView.findViewById(R.id.list_child_name_item);
        //   TextView list_child_datum_item = (TextView) convertView.findViewById(R.id.list_child_datum_item);
        list_child_name_item.setText(child);
        //  list_child_datum_item.setText(child.getStringDatum());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // returns if the subitem (item of group) can be selected
        return true;
    }
}

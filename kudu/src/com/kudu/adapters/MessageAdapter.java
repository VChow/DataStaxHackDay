package com.kudu.adapters;

import java.util.List;

//import android.content.ClipData.Item;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


public class MessageAdapter extends ArrayAdapter<Item> {
    private List<Item> items;
    private LayoutInflater inflater;
 
    public enum RowType {
        // Here we have two items types, you can have as many as you like though
        SENT_ITEM, RECEIVED_ITEM
    }
 
    public MessageAdapter(Context context, LayoutInflater inflater, List<Item> items) {
        super(context, 0, items);
        this.items = items;
        this.inflater = inflater;
    }
 
    @Override
    public int getViewTypeCount() {
        // Get the number of items in the enum
        return RowType.values().length;
 
    }
 
    @Override
    public int getItemViewType(int position) {
        // Use getViewType from the Item interface
        return items.get(position).getViewType();
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Use getView from the Item interface
        return items.get(position).getView(inflater, convertView);
    }
}

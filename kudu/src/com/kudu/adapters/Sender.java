package com.kudu.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.kudu.activities.R;
import com.kudu.adapters.MessageAdapter.RowType;

public class Sender implements Item {
    private final String name;
    private final String timestamp;
 
    public Sender(String name, String timestamp) {
        this.name = name;
        this.timestamp = timestamp;
    }
 
    @Override
    public int getViewType() {
        return RowType.SENT_ITEM.ordinal();
    }
 
    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        if (convertView == null) {
            // No views to reuse, need to inflate a new view
            convertView = (View) inflater.inflate(R.layout.sender_row, null);
        }
 
        TextView text = (TextView) convertView.findViewById(R.id.message);
        TextView time = (TextView) convertView.findViewById(R.id.timestamp);
        text.setText(name);
        time.setText(timestamp);
 
        return convertView;
    }
 
}
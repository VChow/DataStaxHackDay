package com.kudu.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ConversationActivityFragment extends Fragment {

    public ConversationActivityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.conversation_activity_fragment, container, false);
        
        //test to see if the fragment is changing on drawer click.
        //getActivity().setTitle("ConversationActivityFragment");
        return rootView;
    }
}

package com.kudu.activities;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ProfileActivityFragment extends Fragment {

    public ProfileActivityFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profile_activity_fragment, container, false);
        
        //test to see if the fragment is changing on drawer click.
        //getActivity().setTitle("ProfileActivityFragment");
        return rootView;
    }
}

package com.goodleaf.firstapp.goodleafapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goodleaf.firstapp.goodleafapp.R;

public class NoSearchResults extends android.app.Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.no_search_result, container, false);
        return view;
    }
}

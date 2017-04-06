package com.goodleaf.firstapp.goodleafapp.transaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.goodleaf.firstapp.goodleafapp.R;

public class Fragment_Transaction extends android.app.Fragment implements View.OnClickListener {

    Button addTransactionButton;
    Button searchTransactionButton;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_transaction, container, false);
        addTransactionButton = (Button) view.findViewById(R.id.addTransaction);
        addTransactionButton.setOnClickListener(this);
        searchTransactionButton = (Button) view.findViewById(R.id.searchTransaction);
        searchTransactionButton.setOnClickListener(this);

        Fragment fr = new Fragment_Transaction_Recent_Summary();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_transaction, fr);
        fragmentTransaction.commit();
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment fr = new Fragment_Search_Transaction();
        switch (v.getId()) {
            case R.id.addTransaction:
                fr = new Fragment_Create_Transaction();
                break;
            case R.id.searchTransaction:
                fr = new Fragment_Search_Transaction();
                break;
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}

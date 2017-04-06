package com.goodleaf.firstapp.goodleafapp.purchase;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.goodleaf.firstapp.goodleafapp.R;

public class Fragment_Purchase extends Fragment implements View.OnClickListener {

    Button addPurchaseButton;
    Button searchPurchaseButton;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_purchase, container, false);
        addPurchaseButton = (Button) view.findViewById(R.id.addPurchase);
        addPurchaseButton.setOnClickListener(this);
        searchPurchaseButton = (Button) view.findViewById(R.id.searchPurchase);
        searchPurchaseButton.setOnClickListener(this);

        Fragment fr = new Fragment_Purchase_Recent_Summary();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_purchase, fr);
        fragmentTransaction.commit();
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment fr = new Fragment_Search_Purchase();
        switch (v.getId()) {
            case R.id.addPurchase:
                fr = new Fragment_Create_Purchase();
                break;
            case R.id.searchPurchase:
                fr = new Fragment_Search_Purchase();
                break;
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}

package com.goodleaf.firstapp.goodleafapp.order;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.goodleaf.firstapp.goodleafapp.R;

public class Fragment_Order extends Fragment implements View.OnClickListener {

    Button addOrderButton;
    Button searchOrderButton;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_order, container, false);
        addOrderButton = (Button) view.findViewById(R.id.addOrder);
        addOrderButton.setOnClickListener(this);
        searchOrderButton = (Button) view.findViewById(R.id.searchOrder);
        searchOrderButton.setOnClickListener(this);

        Fragment fr = new Fragment_Order_Recent_Summary();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_order, fr);
        fragmentTransaction.commit();
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment fr = new Fragment_Search_Order();
        switch (v.getId()) {
            case R.id.addOrder:
                fr = new Fragment_Create_Order();
                break;
            case R.id.searchOrder:
                fr = new Fragment_Search_Order();
                break;
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}

package com.goodleaf.firstapp.goodleafapp.shipment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.goodleaf.firstapp.goodleafapp.R;

public class Fragment_Shipments extends android.app.Fragment implements View.OnClickListener {
    Button upButton;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_shipment, container, false);
        upButton = (Button) view.findViewById(R.id.addShipment);
        upButton.setOnClickListener(this);
        upButton = (Button) view.findViewById(R.id.searchShipment);
        upButton.setOnClickListener(this);

        Fragment fr = new Fragment_Shipment_Recent_Summary();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_shipment, fr);
        fragmentTransaction.commit();
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment fr = new Fragment_Search_Shipment();
        switch (v.getId()) {
            case R.id.addShipment:
                fr = new Fragment_Create_Shipment();
                break;
            case R.id.searchShipment:
                fr = new Fragment_Search_Shipment();
                break;
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fr);
        fragmentTransaction.commit();
    }
}

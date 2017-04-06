package com.goodleaf.firstapp.goodleafapp.customer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.goodleaf.firstapp.goodleafapp.R;

public class Fragment_Customer extends android.app.Fragment implements View.OnClickListener {

    Button addCustomerButton;
    Button searchCustomerButton;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_customer, container, false);
        addCustomerButton = (Button) view.findViewById(R.id.addCustomer);
        addCustomerButton.setOnClickListener(this);
        searchCustomerButton = (Button) view.findViewById(R.id.searchCustomer);
        searchCustomerButton.setOnClickListener(this);

        Fragment fr = new Fragment_Customer_Recent_Summary();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_customer, fr);
        fragmentTransaction.commit();
        /*setListAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, results));
        getListView().setTextFilterEnabled(true);*/
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment fr = new Fragment_Search_Customer();
        switch (v.getId()) {
            case R.id.addCustomer:
                fr = new Fragment_Create_Customer();
                break;
            case R.id.searchCustomer:
                fr = new Fragment_Search_Customer();
                break;
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}

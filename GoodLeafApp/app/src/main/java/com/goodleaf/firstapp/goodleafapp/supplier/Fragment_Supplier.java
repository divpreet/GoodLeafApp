package com.goodleaf.firstapp.goodleafapp.supplier;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.goodleaf.firstapp.goodleafapp.R;

public class Fragment_Supplier extends android.app.Fragment implements View.OnClickListener {

    Button addSupplierButton;
    Button searchSupplierButton;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_supplier, container, false);
        addSupplierButton = (Button) view.findViewById(R.id.addSupplier);
        addSupplierButton.setOnClickListener(this);
        searchSupplierButton = (Button) view.findViewById(R.id.searchSupplier);
        searchSupplierButton.setOnClickListener(this);

        Fragment fr = new Fragment_Supplier_Recent_Summary();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_supplier, fr);
        fragmentTransaction.commit();

        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment fr = new Fragment_Search_Supplier();
        switch (v.getId()) {
            case R.id.addSupplier:
                fr = new Fragment_Create_Supplier();
                break;
            case R.id.searchSupplier:
                fr = new Fragment_Search_Supplier();
                break;
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

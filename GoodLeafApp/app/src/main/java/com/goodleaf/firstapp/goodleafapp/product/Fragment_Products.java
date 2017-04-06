package com.goodleaf.firstapp.goodleafapp.product;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.goodleaf.firstapp.goodleafapp.R;

public class Fragment_Products extends android.app.Fragment implements View.OnClickListener {

    Button addProductButton;
    Button searchProductButton;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_products, container, false);
        addProductButton = (Button) view.findViewById(R.id.addProduct);
        addProductButton.setOnClickListener(this);
        searchProductButton = (Button) view.findViewById(R.id.searchProduct);
        searchProductButton.setOnClickListener(this);

        Fragment fr = new Fragment_Product_Recent_Summary();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_product, fr);
        fragmentTransaction.commit();
        /*setListAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, results));
        getListView().setTextFilterEnabled(true);*/
        return view;
    }

    @Override
    public void onClick(View v) {
        Fragment fr = new Fragment_Search_Product();
        switch (v.getId()) {
            case R.id.addProduct:
                fr = new Fragment_Create_Product();
                break;
            case R.id.searchProduct:
                fr = new Fragment_Search_Product();
                break;
        }
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}

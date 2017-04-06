package com.goodleaf.firstapp.goodleafapp.purchase;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.goodleaf.firstapp.goodleafapp.NoSearchResults;
import com.goodleaf.firstapp.goodleafapp.R;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Purchase.Purchase;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Purchase.PurchaseDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.Supplier;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.SupplierDataSource;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Search_Purchase extends android.app.Fragment implements View.OnClickListener {
    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    List<Purchase> values;

    Button upButton;
    View view;
    Spinner purchaseSupplierName;
    TextView purchaseNumber;
    TextView purchaseDate;
    TextView purchaseStatus;
    private PurchaseDataSource dataSource;
    private List<Supplier> suppliers;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_search_purchase, container, false);
        upButton = (Button) view.findViewById(R.id.searchPurchaseByFilter);
        upButton.setOnClickListener(this);

        purchaseNumber = (TextView) view.findViewById(R.id.purchaseNumberSearch);
        purchaseDate = (TextView) view.findViewById(R.id.purchaseDateSearch);
        purchaseStatus = (TextView) view.findViewById(R.id.purchaseStatusSearch);
        SupplierDataSource supplierDataSource = new SupplierDataSource(getActivity());
        supplierDataSource.open();
        suppliers = supplierDataSource.getAllSuppliers();

        purchaseSupplierName = (Spinner) view.findViewById(R.id.purchaseSupplierNameSearch);

        ArrayList<String> options=new ArrayList<>();

        for(int i=0;i< suppliers.size();i++){
            options.add(suppliers.get(i).getSupplierName());
        }

// use default spinner item to show options in spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,options);
        purchaseSupplierName.setAdapter(adapter);


        dataSource = new PurchaseDataSource(getActivity());
        dataSource.open();
        //values = dataSource.getAllPurchases();
        return view;
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        Supplier supplier;
        String supplierNo = "";
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        if (v.getId() == R.id.searchPurchaseByFilter) {
            purchaseNumber.setError(null);
            purchaseDate.setError(null);
            purchaseStatus.setError(null);
            if (purchaseSupplierName.getSelectedItem().toString().trim().length() <= 0 &&
                    purchaseNumber.getText().toString().trim().length() <= 0 &&
                    purchaseDate.getText().toString().trim().length() <= 0 &&
                    purchaseStatus.getText().toString().trim().length() <= 0) {
                purchaseNumber.setError(REQUIRED_MSG);
                purchaseDate.setError(REQUIRED_MSG);
                purchaseStatus.setError(REQUIRED_MSG);
                valid = false;
            }
            if (valid) {


                for(int i=0;i< suppliers.size();i++){
                    supplier = suppliers.get(i);
                    if(supplier.getSupplierName().trim().equalsIgnoreCase(purchaseSupplierName.getSelectedItem().toString().trim())){
                        supplierNo = supplier.getSupplierNo();
                    }
                }
                values = dataSource.getPurchases(purchaseNumber.getText().toString().trim(),
                        supplierNo,
                        purchaseDate.getText().toString().trim(),
                        purchaseStatus.getText().toString().trim());
                Fragment fr;
                if (values.size() == 1) {
                    fr = new SinglePurchaseSearchItem();
                    ((SinglePurchaseSearchItem) fr).setPurchase(values.get(0));
                    createFragment(fr);
                } else if (values.size() > 1) {
                    fr = new PurchaseSearchResultsTableLayout();
                    ((PurchaseSearchResultsTableLayout) fr).setListOfPurchases(values);
                    createFragment(fr);
                } else {
                    fr = new NoSearchResults();
                    createFragment(fr);
                }
            }
        }
    }

    private void createFragment(Fragment fr) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_purchase_search_result, fr);
        fragmentTransaction.commit();
    }

    @Override
    public void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    public void onPause() {
        dataSource.close();
        super.onPause();
    }
}

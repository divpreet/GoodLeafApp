package com.goodleaf.firstapp.goodleafapp.supplier;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.goodleaf.firstapp.goodleafapp.NoSearchResults;
import com.goodleaf.firstapp.goodleafapp.R;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.Supplier;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.SupplierDataSource;

import java.util.List;

public class Fragment_Search_Supplier extends android.app.Fragment implements View.OnClickListener {
    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    List<Supplier> values;

    Button upButton;
    View view;
    TextView supplierName;
    TextView supplierNumber;
    private SupplierDataSource dataSource;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_search_supplier, container, false);
        upButton = (Button) view.findViewById(R.id.searchSupplierByFilter);
        upButton.setOnClickListener(this);
        supplierName = (TextView) view.findViewById(R.id.supplierNameSearch);
        supplierNumber = (TextView) view.findViewById(R.id.supplierNumberSearch);
        dataSource = new SupplierDataSource(getActivity());
        dataSource.open();
        //values = dataSource.getAllSuppliers();
        return view;
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        if (v.getId() == R.id.searchSupplierByFilter) {
            supplierName.setError(null);
            supplierNumber.setError(null);
            if (supplierName.getText().toString().trim().length() <= 0 && supplierNumber.getText().toString().trim().length() <= 0) {
                supplierName.setError(REQUIRED_MSG);
                supplierNumber.setError(REQUIRED_MSG);
                valid = false;
            }
            if (valid) {
                values = dataSource.getSuppliers(supplierNumber.getText().toString().trim(),
                        supplierName.getText().toString().trim());
                Fragment fr;
                if (values.size() == 1) {
                    fr = new SingleSupplierSearchItem();
                    ((SingleSupplierSearchItem) fr).setSupplier(values.get(0));
                    createFragment(fr);
                } else if (values.size() > 1) {
                    fr = new SupplierSearchResultsTableLayout();
                    ((SupplierSearchResultsTableLayout) fr).setListOfSuppliers(values);
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
        fragmentTransaction.replace(R.id.content_supplier_search_result, fr);
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

package com.goodleaf.firstapp.goodleafapp.customer;

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
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.Customer;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.CustomerDataSource;

import java.util.List;

public class Fragment_Search_Customer extends android.app.Fragment implements View.OnClickListener {
    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    List<Customer> values;

    Button upButton;
    View view;
    TextView customerName;
    TextView customerNumber;
    private CustomerDataSource dataSource;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_search_customer, container, false);
        upButton = (Button) view.findViewById(R.id.searchCustomerByFilter);
        upButton.setOnClickListener(this);
        customerName = (TextView) view.findViewById(R.id.customerNameSearch);
        customerNumber = (TextView) view.findViewById(R.id.customerNumberSearch);
        dataSource = new CustomerDataSource(getActivity());
        dataSource.open();
        //values = dataSource.getAllCustomers();
        return view;
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        if (v.getId() == R.id.searchCustomerByFilter) {
            customerName.setError(null);
            customerNumber.setError(null);
            if (customerName.getText().toString().trim().length() <= 0 && customerNumber.getText().toString().trim().length() <= 0) {
                customerName.setError(REQUIRED_MSG);
                customerNumber.setError(REQUIRED_MSG);
                valid = false;
            }
            if (valid) {
                values = dataSource.getCustomers(customerNumber.getText().toString().trim(),
                        customerName.getText().toString().trim());
                Fragment fr;
                if (values.size() == 1) {
                    fr = new SingleCustomerSearchItem();
                    ((SingleCustomerSearchItem) fr).setCustomer(values.get(0));
                    createFragment(fr);
                } else if (values.size() > 1) {
                    fr = new CustomerSearchResultsTableLayout();
                    ((CustomerSearchResultsTableLayout) fr).setListOfCustomers(values);
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
        fragmentTransaction.replace(R.id.content_customer_search_result, fr);
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

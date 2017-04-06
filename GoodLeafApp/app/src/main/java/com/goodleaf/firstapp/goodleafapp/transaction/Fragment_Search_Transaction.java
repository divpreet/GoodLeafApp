package com.goodleaf.firstapp.goodleafapp.transaction;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.goodleaf.firstapp.goodleafapp.NoSearchResults;
import com.goodleaf.firstapp.goodleafapp.R;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.Customer;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.CustomerDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.Supplier;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.SupplierDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Transaction.Transaction;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Transaction.TransactionDataSource;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Search_Transaction extends android.app.Fragment implements View.OnClickListener {
    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    List<Transaction> values;

    Button upButton;
    View view;
    Spinner transactionCustomerName;
    Spinner transactionSupplierName;
    Spinner transactionType;
    TextView transactionNumber;
    TextView transactionDate;
    RadioGroup radioCustSupp;
    RadioButton radioNone;
    private TransactionDataSource dataSource;
    private List<Customer> customers;
    private List<Supplier> suppliers;
    String transactionCustSupp = "";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_search_transaction, container, false);
        upButton = (Button) view.findViewById(R.id.searchTransactionByFilter);
        upButton.setOnClickListener(this);

        transactionNumber = (TextView) view.findViewById(R.id.transactionNumberSearch);
        transactionDate = (TextView) view.findViewById(R.id.transactionDateSearch);
        CustomerDataSource customerDataSource = new CustomerDataSource(getActivity());
        customerDataSource.open();
        customers = customerDataSource.getAllCustomers();
        SupplierDataSource supplierDataSource = new SupplierDataSource(getActivity());
        supplierDataSource.open();
        suppliers = supplierDataSource.getAllSuppliers();

        transactionCustomerName = (Spinner) view.findViewById(R.id.transactionCustomerNameSearch);

        ArrayList<String> optionCustomers=new ArrayList<>();

        for(int i=0;i< customers.size();i++){
            optionCustomers.add(customers.get(i).getCustomerName());
        }

// use default spinner item to show options in spinner
        ArrayAdapter<String> adapterCustomer = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,optionCustomers);
        transactionCustomerName.setAdapter(adapterCustomer);

        transactionSupplierName = (Spinner) view.findViewById(R.id.transactionSupplierNameSearch);

        ArrayList<String> optionSuppliers=new ArrayList<>();

        for(int i=0;i< suppliers.size();i++){
            optionSuppliers.add(suppliers.get(i).getSupplierName());
        }

// use default spinner item to show options in spinner
        ArrayAdapter<String> adapterSupplier = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,optionSuppliers);
        transactionSupplierName.setAdapter(adapterSupplier);

        transactionType = (Spinner) view.findViewById(R.id.transactionTypeSearch);

        radioCustSupp = (RadioGroup) view.findViewById(R.id.radioButtonGroupForTransactionSearch);
        radioNone = (RadioButton) view.findViewById(R.id.radioButtonNoneSearch);
        radioCustSupp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioButtonCustomerSearch:
                        transactionCustSupp = "Customer";
                        transactionCustomerName.setEnabled(true);
                        transactionSupplierName.setEnabled(false);
                        break;
                    case R.id.radioButtonSupplierSearch:
                        transactionCustSupp = "Supplier";
                        transactionCustomerName.setEnabled(false);
                        transactionSupplierName.setEnabled(true);
                        break;
                    case R.id.radioButtonNoneSearch:
                        transactionCustSupp = "";
                        transactionCustomerName.setEnabled(false);
                        transactionSupplierName.setEnabled(false);
                }
            }
        });

        transactionCustomerName.setEnabled(false);
        transactionSupplierName.setEnabled(false);
        radioNone.setSelected(true);

        ArrayList<String> optionTransType=new ArrayList<>();

        optionTransType.add("Cash");
        optionTransType.add("Cheque");
        optionTransType.add("Draft");
        optionTransType.add("Online");
        optionTransType.add("Other");


// use default spinner item to show options in spinner
        ArrayAdapter<String> adapterTransType = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,optionTransType);
        transactionType.setAdapter(adapterTransType);

        dataSource = new TransactionDataSource(getActivity());
        dataSource.open();
        //values = dataSource.getAllTransactions();
        return view;
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        Customer customer;
        String customerNo = null;
        Supplier supplier;
        String supplierNo = null;
        String transType;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        if (v.getId() == R.id.searchTransactionByFilter) {
            transactionNumber.setError(null);
            transactionDate.setError(null);
            if (transactionCustomerName.getSelectedItem().toString().trim().length() <= 0 &&
                    transactionSupplierName.getSelectedItem().toString().trim().length() <= 0 &&
                    transactionType.getSelectedItem().toString().trim().length() <= 0 &&
                    transactionNumber.getText().toString().trim().length() <= 0 &&
                    transactionDate.getText().toString().trim().length() <= 0) {
                transactionNumber.setError(REQUIRED_MSG);
                transactionDate.setError(REQUIRED_MSG);
                valid = false;
            }
            if (valid) {

                if(transactionCustSupp.equals("Customer")) {
                    for (int i = 0; i < customers.size(); i++) {
                        customer = customers.get(i);
                        if (customer.getCustomerName().trim().equalsIgnoreCase(transactionCustomerName.getSelectedItem().toString().trim())) {
                            customerNo = customer.getCustomerNo();
                        }
                    }
                }else if(transactionCustSupp.equals("Supplier")) {
                    for (int i = 0; i < suppliers.size(); i++) {
                        supplier = suppliers.get(i);
                        if (supplier.getSupplierName().trim().equalsIgnoreCase(transactionSupplierName.getSelectedItem().toString().trim())) {
                            supplierNo = supplier.getSupplierNo();
                        }
                    }
                }
                transType = transactionType.getSelectedItem().toString().trim();
                values = dataSource.getTransactions(transactionNumber.getText().toString().trim(),
                        customerNo,supplierNo,transType,
                        transactionDate.getText().toString().trim());
                Fragment fr;
                if (values.size() == 1) {
                    fr = new SingleTransactionSearchItem();
                    ((SingleTransactionSearchItem) fr).setTransaction(values.get(0));
                    createFragment(fr);
                } else if (values.size() > 1) {
                    fr = new TransactionSearchResultsTableLayout();
                    ((TransactionSearchResultsTableLayout) fr).setListOfTransactions(values);
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
        fragmentTransaction.replace(R.id.content_transaction_search_result, fr);
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

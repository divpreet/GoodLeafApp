package com.goodleaf.firstapp.goodleafapp.transaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.goodleaf.firstapp.goodleafapp.R;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.Customer;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.CustomerDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.OverallBalances.OverallBalances;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.OverallBalances.OverallBalancesDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.Supplier;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.SupplierDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Transaction.Transaction;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Transaction.TransactionDataSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Fragment_Create_Transaction extends android.app.Fragment implements View.OnClickListener {
    private static final String REQUIRED_MSG = "Required";
    List<Transaction> transactionList;
    Spinner transactionCustomerName;
    Spinner transactionSupplierName;
    private List<Customer> customers;
    private List<Supplier> suppliers;
    private TransactionDataSource transactionDataSource;
    RadioGroup radioCustSupp;
    RadioGroup radioDebitCredit;
    EditText description;
    EditText amount;
    Spinner transactionType;
    private String transactionCustSupp = "Customer";
    private String debitCredit = "Debit";
    Button createTransaction;
    View view;
    OverallBalancesDataSource overallBalancesDataSource;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.fragment_create_transaction, container, false);

        CustomerDataSource customerDataSource = new CustomerDataSource(getActivity());
        SupplierDataSource supplierDataSource = new SupplierDataSource(getActivity());
        transactionDataSource = new TransactionDataSource(getActivity());
        overallBalancesDataSource = new OverallBalancesDataSource(getActivity());
        overallBalancesDataSource.open();
        customerDataSource.open();
        customers = customerDataSource.getAllCustomers();
        supplierDataSource.open();
        transactionDataSource.open();
        suppliers = supplierDataSource.getAllSuppliers();
        transactionList = transactionDataSource.getAllTransactions();
        transactionCustomerName = (Spinner) view.findViewById(R.id.transactionCustomerNameCreate);

        ArrayList<String> optionsCustomers=new ArrayList<>();

        for(int i=0;i< customers.size();i++){
            optionsCustomers.add(customers.get(i).getCustomerName());
        }

// use default spinner item to show options in spinner
        ArrayAdapter<String> adapterCustomer = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,optionsCustomers);
        transactionCustomerName.setAdapter(adapterCustomer);

        transactionSupplierName = (Spinner) view.findViewById(R.id.transactionSupplierNameCreate);

        ArrayList<String> optionSuppliers=new ArrayList<>();

        for(int i=0;i< suppliers.size();i++){
            optionSuppliers.add(suppliers.get(i).getSupplierName());
        }

// use default spinner item to show options in spinner
        ArrayAdapter<String> adapterSupplier = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,optionSuppliers);
        transactionSupplierName.setAdapter(adapterSupplier);

        radioCustSupp = (RadioGroup) view.findViewById(R.id.radioButtonGroupForTransaction);
        radioDebitCredit = (RadioGroup) view.findViewById(R.id.radioButtonGroupForDebitCredit);
        transactionCustomerName.setEnabled(false);
        transactionSupplierName.setEnabled(false);
        radioCustSupp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioButtonCustomer:
                        transactionCustSupp = "Customer";
                        transactionCustomerName.setEnabled(true);
                        transactionSupplierName.setEnabled(false);
                        break;
                    case R.id.radioButtonSupplier:
                        transactionCustSupp = "Supplier";
                        transactionCustomerName.setEnabled(false);
                        transactionSupplierName.setEnabled(true);
                        break;
                }
            }
        });
        radioDebitCredit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.radioButtonDebit:
                        debitCredit = "Debit";
                        break;
                    case R.id.radioButtonCredit:
                        debitCredit = "Credit";
                        break;
                }
            }
        });

        description = (EditText) view.findViewById(R.id.transactionDescriptionCreate);
        amount = (EditText) view.findViewById(R.id.transactionAmountCreate);

        transactionType = (Spinner) view.findViewById(R.id.transactionTypeCreate);

        ArrayList<String> optionTransType=new ArrayList<>();

        optionTransType.add("Cash");
        optionTransType.add("Cheque");
        optionTransType.add("Draft");
        optionTransType.add("Online");
        optionTransType.add("Other");


// use default spinner item to show options in spinner
        ArrayAdapter<String> adapterTransType = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,optionTransType);
        transactionType.setAdapter(adapterTransType);

        createTransaction = (Button) view.findViewById(R.id.createTransaction);
        createTransaction.setOnClickListener(this);
        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onClick(View v) {
        boolean valid = true;
        Customer customer;
        Supplier supplier;
        String customerNo="";
        String supplierNo="";
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        if (v.getId() == R.id.createTransaction) {
            description.setError(null);
            amount.setError(null);
            if (description.getText().toString().trim().length() <= 0) {
                description.setError(REQUIRED_MSG);
                valid = false;
            }
            if (amount.getText().toString().trim().length() <= 0) {
                amount.setError(REQUIRED_MSG);
                valid = false;
            }
            if (valid) {
                String transactionNo;
                if (transactionList.size() > 0) {
                    Transaction cust = transactionList.get(transactionList.size() - 1);
                    transactionNo = String.valueOf(Integer.parseInt(cust.getTransactionNo()) + 1);
                } else {
                    transactionNo = "1";
                }
                // save the new transaction to the database
                // save the new transaction to the database
                if(debitCredit.equals("Debit")) {
                    if(transactionCustSupp.equals("Customer")) {
                        for (int i = 0; i < customers.size(); i++) {
                            customer = customers.get(i);
                            if (customer.getCustomerName().trim().equalsIgnoreCase(transactionCustomerName.getSelectedItem().toString().trim())) {
                                customerNo = customer.getCustomerNo();
                            }
                        }
                        transactionDataSource.createTransaction(transactionNo,
                                formattedDate,
                                description.getText().toString().trim(),
                                customerNo,
                                null,
                                transactionType.getSelectedItem().toString().trim(), amount.getText().toString().trim(), null);
                        OverallBalances balance = overallBalancesDataSource.getAllOverallBalancesForCustomer(customerNo);
                        String balanceValue = String.valueOf(Integer.parseInt(balance.getBalance()) - Integer.parseInt(amount.getText().toString().trim()));
                        overallBalancesDataSource.updateOverallBalance(customerNo,null,balanceValue);
                    }else{
                        for (int i = 0; i < suppliers.size(); i++) {
                            supplier = suppliers.get(i);
                            if (supplier.getSupplierName().trim().equalsIgnoreCase(transactionSupplierName.getSelectedItem().toString().trim())) {
                                supplierNo = supplier.getSupplierNo();
                            }
                        }
                        transactionDataSource.createTransaction(transactionNo,
                                formattedDate,
                                description.getText().toString().trim(),
                                null,
                                supplierNo,
                                transactionType.getSelectedItem().toString().trim(), amount.getText().toString().trim(), null);
                        OverallBalances balance = overallBalancesDataSource.getAllOverallBalancesForSupplier(supplierNo);
                        String balanceValue = String.valueOf(Integer.parseInt(balance.getBalance()) - Integer.parseInt(amount.getText().toString().trim()));
                        overallBalancesDataSource.updateOverallBalance(null,supplierNo,balanceValue);
                    }
                }else {
                    if(transactionCustSupp.equals("Customer")) {
                        for (int i = 0; i < customers.size(); i++) {
                            customer = customers.get(i);
                            if (customer.getCustomerName().trim().equalsIgnoreCase(transactionCustomerName.getSelectedItem().toString().trim())) {
                                customerNo = customer.getCustomerNo();
                            }
                        }
                        transactionDataSource.createTransaction(transactionNo,
                                formattedDate,
                                description.getText().toString().trim(),
                                customerNo,
                                null,
                                transactionType.getSelectedItem().toString().trim(), null, amount.getText().toString().trim());
                        OverallBalances balance = overallBalancesDataSource.getAllOverallBalancesForCustomer(customerNo);
                        String balanceValue = String.valueOf(Integer.parseInt(balance.getBalance()) + Integer.parseInt(amount.getText().toString().trim()));
                        overallBalancesDataSource.updateOverallBalance(customerNo,null,balanceValue);
                    }else{
                        for (int i = 0; i < suppliers.size(); i++) {
                            supplier = suppliers.get(i);
                            if (supplier.getSupplierName().trim().equalsIgnoreCase(transactionSupplierName.getSelectedItem().toString().trim())) {
                                supplierNo = supplier.getSupplierNo();
                            }
                        }
                        transactionDataSource.createTransaction(transactionNo,
                                formattedDate,
                                description.getText().toString().trim(),
                                null,
                                supplierNo,
                                transactionType.getSelectedItem().toString().trim(), null, amount.getText().toString().trim());
                        OverallBalances balance = overallBalancesDataSource.getAllOverallBalancesForSupplier(supplierNo);
                        String balanceValue = String.valueOf(Integer.parseInt(balance.getBalance()) + Integer.parseInt(amount.getText().toString().trim()));
                        overallBalancesDataSource.updateOverallBalance(null,supplierNo,balanceValue);
                    }
                }
                Fragment fr = new Fragment_Transaction();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fr);
                fragmentTransaction.commit();
            }
        }
    }
}

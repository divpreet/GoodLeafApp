package com.goodleaf.firstapp.goodleafapp.customer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.goodleaf.firstapp.goodleafapp.R;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.Customer;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.CustomerDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.OverallBalances.OverallBalancesDataSource;

import java.util.List;

public class Fragment_Create_Customer extends android.app.Fragment implements View.OnClickListener {
    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    List<Customer> values;

    Button upButton;
    View view;
    TextView customerName;
    TextView customerAddress;
    TextView customerContact;
    TextView businessName;
    TextView customerAddress1;
    TextView customerContact1;
    private CustomerDataSource dataSource;
    private OverallBalancesDataSource overallBalancesDataSource;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_create_customer, container, false);
        upButton = (Button) view.findViewById(R.id.createCustomer);
        upButton.setOnClickListener(this);
        customerName = (TextView) view.findViewById(R.id.customerNameCreate);
        customerAddress = (TextView) view.findViewById(R.id.customerAddressCreate);
        customerContact = (TextView) view.findViewById(R.id.customerContactCreate);
        businessName = (TextView) view.findViewById(R.id.businessNameCreate);
        customerAddress1 = (TextView) view.findViewById(R.id.customerAddress1Create);
        customerContact1 = (TextView) view.findViewById(R.id.customerContact1Create);
        dataSource = new CustomerDataSource(getActivity());
        overallBalancesDataSource = new OverallBalancesDataSource(getActivity());
        overallBalancesDataSource.open();
        dataSource.open();
        values = dataSource.getAllCustomers();
        return view;
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        if (v.getId() == R.id.createCustomer) {
            customerName.setError(null);
            customerAddress.setError(null);
            customerContact.setError(null);
            if (customerName.getText().toString().trim().length() <= 0) {
                customerName.setError(REQUIRED_MSG);
                valid = false;
            }
            if (customerAddress.getText().toString().trim().length() <= 0) {
                customerAddress.setError(REQUIRED_MSG);
                valid = false;
            }
            if (customerContact.getText().toString().trim().length() <= 0) {
                customerContact.setError(REQUIRED_MSG);
                valid = false;
            }
            if (valid) {
                String customerNo;
                if (values.size() > 0) {
                    Customer cust = values.get(values.size() - 1);
                    customerNo = String.valueOf(Integer.parseInt(cust.getCustomerNo()) + 1);
                } else {
                    customerNo = "1";
                }
                // save the new customer to the database
                dataSource.createCustomer(customerNo,
                        customerName.getText().toString().trim(),
                        businessName.getText().toString().trim(),
                        customerAddress.getText().toString().trim(),
                        customerContact.getText().toString().trim(),
                        customerAddress1.getText().toString().trim(),
                        customerContact1.getText().toString().trim());
                overallBalancesDataSource.createOverallBalance(customerNo,null,"0");
                Fragment fr = new Fragment_Customer();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fr);
                fragmentTransaction.commit();
            }
        }
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

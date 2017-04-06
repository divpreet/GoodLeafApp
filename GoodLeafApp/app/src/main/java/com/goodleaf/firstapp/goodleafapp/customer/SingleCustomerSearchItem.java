package com.goodleaf.firstapp.goodleafapp.customer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.goodleaf.firstapp.goodleafapp.R;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.Customer;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.CustomerDataSource;


public class SingleCustomerSearchItem extends Fragment implements View.OnClickListener {
    View view;
    private Customer customer;
    Button saveButton;
    EditText customerName;
    EditText businessName;
    EditText customerAddress;
    EditText customerContact;
    EditText customerAddress1;
    EditText customerContact1;
    private static final String REQUIRED_MSG = "Required";
    private CustomerDataSource dataSource;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.single_customer_search_result, container, false);
        saveButton = (Button) view.findViewById(R.id.saveCustomer);
        saveButton.setOnClickListener(this);
        dataSource = new CustomerDataSource(getActivity());
        dataSource.open();
        populateSingleResult(getCustomer());
        return view;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void populateSingleResult(Customer customer) {
        if (customer != null) {
            TableLayout t1 = (TableLayout) view.findViewById(R.id.customerTableResult);
            TextView customerNo = (TextView) t1.findViewById(R.id.customerNumberResult);
            customerNo.setText(customer.getCustomerNo());
            customerName = (EditText) t1.findViewById(R.id.customerNameResult);
            customerName.setText(customer.getCustomerName());
            businessName = (EditText) t1.findViewById(R.id.customerBusinessNameResult);
            businessName.setText(customer.getBusinessName());
            customerAddress = (EditText) t1.findViewById(R.id.customerAddressResult);
            customerAddress.setText(customer.getCustomerAddress());
            customerContact = (EditText) t1.findViewById(R.id.customerContactResult);
            customerContact.setText(customer.getCustomerContact());
            customerAddress1 = (EditText) t1.findViewById(R.id.customerAddress1Result);
            customerAddress1.setText(customer.getCustomerAddress1());
            customerContact1 = (EditText) t1.findViewById(R.id.customerContact1Result);
            customerContact1.setText(customer.getCustomerContact1());
        }
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        if (v.getId() == R.id.saveCustomer) {
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
                String customerNo = getCustomer().getCustomerNo();
                // save the new customer to the database
                dataSource.updateCustomer(customerNo,
                        customerName.getText().toString().trim(),
                        businessName.getText().toString().trim(),
                        customerAddress.getText().toString().trim(),
                        customerContact.getText().toString().trim(),
                        customerAddress1.getText().toString().trim(),
                        customerContact1.getText().toString().trim());
                Fragment fr = new Fragment_Customer();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fr);
                fragmentTransaction.commit();
            }
        }
    }
}

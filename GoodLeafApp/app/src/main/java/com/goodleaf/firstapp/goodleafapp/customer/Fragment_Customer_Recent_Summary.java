package com.goodleaf.firstapp.goodleafapp.customer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.goodleaf.firstapp.goodleafapp.R;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.Customer;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.CustomerDataSource;

import java.util.List;

public class Fragment_Customer_Recent_Summary extends Fragment {
    List<Customer> values;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.recent_customer_summary, container, false);
        CustomerDataSource dataSource = new CustomerDataSource(getActivity());
        dataSource.open();
        values = dataSource.getAllCustomers();

        for (int i = 0; i < values.size(); i++) {
            if (i == 5) {
                break;
            }
            populateTable(values.get(i));
        }
        return view;
    }

    public void populateTable(Customer customer) {
        if (customer != null) {
            TableRow tr1 = (TableRow) View.inflate(getActivity(), R.layout.customer_list_item, null);
            TableLayout tl = (TableLayout) view.findViewById(R.id.customerTable);
            TextView businessName = (TextView) tr1.findViewById(R.id.businessNameListItem);
            if (customer.getBusinessName().length() <= 0) {
                businessName.setText(customer.getCustomerName());
            } else {
                businessName.setText(customer.getBusinessName());
            }
            TextView customerAddress = (TextView) tr1.findViewById(R.id.customerAddressListItem);
            customerAddress.setText(customer.getCustomerAddress());
            TextView customerContact = (TextView) tr1.findViewById(R.id.customerContactListItem);
            customerContact.setText(customer.getCustomerContact());
            tr1.setTag(customer);
            tr1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fr = new SingleCustomerSearchItem();
                    ((SingleCustomerSearchItem) fr).setCustomer((Customer) v.getTag());
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.content_customer, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            tl.addView(tr1);
        }
    }
}

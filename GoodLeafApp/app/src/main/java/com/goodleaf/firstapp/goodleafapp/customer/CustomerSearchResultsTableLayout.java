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

import java.util.List;

public class CustomerSearchResultsTableLayout extends android.app.Fragment {
    View view;
    List<Customer> listOfCustomers;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.customer_searchresults_tablelayout, container, false);
        if (getListOfCustomers().size() > 1) {
            for (int i = 0; i < getListOfCustomers().size(); i++) {
                populateTable(getListOfCustomers().get(i));
            }
        }
        return view;
    }

    public List<Customer> getListOfCustomers() {
        return listOfCustomers;
    }

    public void setListOfCustomers(List<Customer> listOfCustomers) {
        this.listOfCustomers = listOfCustomers;
    }

    public void populateTable(Customer customer) {
        if (customer != null) {
            TableRow tr1 = (TableRow) View.inflate(getActivity(), R.layout.customer_list_item, null);
            TableLayout tl = (TableLayout) view.findViewById(R.id.customerTableLayout);
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
                    fragmentTransaction.replace(R.id.content_customer_search_result, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            tl.addView(tr1);
        }
    }
}

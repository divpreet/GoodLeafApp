package com.goodleaf.firstapp.goodleafapp;

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

import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.Customer;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.CustomerDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.OverallBalances.OverallBalances;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.OverallBalances.OverallBalancesDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.Supplier;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.SupplierDataSource;

import java.util.List;


public class Fragment_Home extends Fragment {
    View view;
    List<OverallBalances> values;
    private List<Customer> customers;
    private List<Supplier> suppliers;
    CustomerDataSource customerDataSource;
    SupplierDataSource supplierDataSource;
    
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_home, container, false);
        OverallBalancesDataSource dataSource = new OverallBalancesDataSource(getActivity());
        customerDataSource = new CustomerDataSource(getActivity());
        supplierDataSource = new SupplierDataSource(getActivity());
        supplierDataSource.open();
        customerDataSource.open();
        customers = customerDataSource.getAllCustomers();
        suppliers = supplierDataSource.getAllSuppliers();
        dataSource.open();
        values = dataSource.getAllOverallBalances();

        for (int i = 0; i < values.size(); i++) {
            populateTable(values.get(i));
        }
        return view;
    }

    public void populateTable(OverallBalances overallBalances) {
        if (overallBalances != null) {
            TableRow tr1 = (TableRow) View.inflate(getActivity(), R.layout.balance_list_item, null);
            TableLayout tl = (TableLayout) view.findViewById(R.id.overallBalancesTable);
            TextView overallBalancesName = (TextView) tr1.findViewById(R.id.balanceNameListItem);
            String name = "";
            if(overallBalances.getCustomerNo() == null){
                for (int i = 0; i < suppliers.size(); i++) {
                    if (suppliers.get(i).getSupplierNo().equals(overallBalances.getSupplierNo())) {
                        name = suppliers.get(i).getSupplierName() + " (Supplier)";
                        break;
                    }
                }
            }else{
                for (int i = 0; i < customers.size(); i++) {
                    if (customers.get(i).getCustomerNo().equals(overallBalances.getCustomerNo())) {
                        name = customers.get(i).getCustomerName() + " (Customer)";
                        break;
                    }
                }
            }
            overallBalancesName.setText(name);
            TextView overallBalancesValue = (TextView) tr1.findViewById(R.id.balanceValueListItem);
            overallBalancesValue.setText(overallBalances.getBalance());
            tl.addView(tr1);
        }
    }

}

package com.goodleaf.firstapp.goodleafapp.supplier;

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
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.Supplier;

import java.util.List;

public class SupplierSearchResultsTableLayout extends android.app.Fragment {
    View view;
    List<Supplier> listOfSuppliers;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.supplier_searchresults_tablelayout, container, false);
        if (getListOfSuppliers().size() > 1) {
            for (int i = 0; i < getListOfSuppliers().size(); i++) {
                populateTable(getListOfSuppliers().get(i));
            }
        }
        return view;
    }

    public List<Supplier> getListOfSuppliers() {
        return listOfSuppliers;
    }

    public void setListOfSuppliers(List<Supplier> listOfSuppliers) {
        this.listOfSuppliers = listOfSuppliers;
    }

    public void populateTable(Supplier supplier) {
        if (supplier != null) {
            TableRow tr1 = (TableRow) View.inflate(getActivity(), R.layout.supplier_list_item, null);
            TableLayout tl = (TableLayout) view.findViewById(R.id.supplierTableLayout);
            TextView businessName = (TextView) tr1.findViewById(R.id.businessNameSupplierListItem);
            if (supplier.getBusinessName().length() <= 0) {
                businessName.setText(supplier.getSupplierName());
            } else {
                businessName.setText(supplier.getBusinessName());
            }
            TextView supplierAddress = (TextView) tr1.findViewById(R.id.supplierAddressListItem);
            supplierAddress.setText(supplier.getSupplierAddress());
            TextView supplierContact = (TextView) tr1.findViewById(R.id.supplierContactListItem);
            supplierContact.setText(supplier.getSupplierContact());
            tr1.setTag(supplier);
            tr1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fr = new SingleSupplierSearchItem();
                    ((SingleSupplierSearchItem) fr).setSupplier((Supplier) v.getTag());
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

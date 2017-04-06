package com.goodleaf.firstapp.goodleafapp.transaction;


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
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.Supplier;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.SupplierDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Transaction.Transaction;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionSearchResultsTableLayout  extends android.app.Fragment {
    View view;
    List<Transaction> listOfTransactions;
    List<Customer> valueCustomers;
    List<Supplier> valueSuppliers;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.transaction_searchresults_tablelayout, container, false);
        CustomerDataSource customerDataSource = new CustomerDataSource(getActivity());
        SupplierDataSource supplierDataSource = new SupplierDataSource(getActivity());
        customerDataSource.open();
        supplierDataSource.open();
        valueCustomers = customerDataSource.getAllCustomers();
        valueSuppliers = supplierDataSource.getAllSuppliers();
        if (getListOfTransactions().size() > 1) {
            for (int i = 0; i < getListOfTransactions().size(); i++) {
                populateTable(getListOfTransactions().get(i));
            }
        }
        return view;
    }

    public List<Transaction> getListOfTransactions() {
        return listOfTransactions;
    }

    public void setListOfTransactions(List<Transaction> listOfTransactions) {
        this.listOfTransactions = listOfTransactions;
    }

    public void populateTable(Transaction transaction) {
        if (transaction != null) {
            TableRow tr1 = (TableRow) View.inflate(getActivity(), R.layout.transaction_list_item, null);
            TableLayout tl = (TableLayout) view.findViewById(R.id.transactionTableLayout);
            TextView transactionNo = (TextView) tr1.findViewById(R.id.transactionNumberListItem);
            transactionNo.setText(transaction.getTransactionNo());
            TextView transactionDate = (TextView) tr1.findViewById(R.id.transactionDateListItem);
            String date = transaction.getTransactionDate();
            try {
                DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                fromFormat.setLenient(false);
                DateFormat toFormat = new SimpleDateFormat("dd-MM-yyyy");
                toFormat.setLenient(false);
                date = toFormat.format(fromFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            transactionDate.setText(date);
            TextView transactionPrice = (TextView) tr1.findViewById(R.id.transactionValueListItem);
            if(!transaction.getCredit().equals("")) {
                transactionPrice.setText(transaction.getCredit());
            }else if(!transaction.getDebit().equals("")){
                transactionPrice.setText(transaction.getDebit());
            }
            TextView transactionStatus = (TextView) tr1.findViewById(R.id.transactionWithListItem);
            if(!transaction.getCustomerNo().equals("")) {
                for (int i = 0; i < valueCustomers.size(); i++) {
                    if(transaction.getCustomerNo().equals(valueCustomers.get(i).getCustomerNo())){
                        transactionStatus.setText(valueCustomers.get(i).getCustomerName());
                        break;
                    }
                }
            }else if(!transaction.getSupplierNo().equals("")){
                for (int i = 0; i < valueSuppliers.size(); i++) {
                    if(transaction.getSupplierNo().equals(valueSuppliers.get(i).getSupplierNo())){
                        transactionStatus.setText(valueSuppliers.get(i).getSupplierName());
                        break;
                    }
                }
            }
            tr1.setTag(transaction);
            tr1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fr = new SingleTransactionSearchItem();
                    ((SingleTransactionSearchItem) fr).setTransaction((Transaction) v.getTag());
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.content_transaction, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            tl.addView(tr1);
        }
    }
}

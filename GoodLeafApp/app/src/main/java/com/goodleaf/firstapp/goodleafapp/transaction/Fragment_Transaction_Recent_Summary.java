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
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Transaction.TransactionDataSource;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


public class Fragment_Transaction_Recent_Summary extends Fragment {
    List<Transaction> values;
    List<Customer> valueCustomers;
    List<Supplier> valueSuppliers;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.recent_transaction_summary, container, false);
        TransactionDataSource dataSource = new TransactionDataSource(getActivity());
        CustomerDataSource customerDataSource = new CustomerDataSource(getActivity());
        SupplierDataSource supplierDataSource = new SupplierDataSource(getActivity());
        customerDataSource.open();
        supplierDataSource.open();
        dataSource.open();
        values = dataSource.getAllTransactions();
        valueCustomers = customerDataSource.getAllCustomers();
        valueSuppliers = supplierDataSource.getAllSuppliers();

        for (int i = 0; i < values.size(); i++) {
            if (i == 5) {
                break;
            }
            populateTable(values.get(i));
        }
        return view;
    }

    public void populateTable(Transaction transaction) {
        if (transaction != null) {
            TableRow tr1 = (TableRow) View.inflate(getActivity(), R.layout.transaction_list_item, null);
            TableLayout tl = (TableLayout) view.findViewById(R.id.transactionTable);
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
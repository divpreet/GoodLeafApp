package com.goodleaf.firstapp.goodleafapp.transaction;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
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

public class SingleTransactionSearchItem extends Fragment implements View.OnClickListener {
    View view;
    private Transaction transaction;
    Button saveButton;
    EditText transactionDescription;
    EditText transactionPrice;
    TextView transactionType;
    List<Customer> valueCustomers;
    List<Supplier> valueSuppliers;
    TextView transactionDate;
    TextView transactionWith;
    private static final String REQUIRED_MSG = "Required";
    private TransactionDataSource dataSource;
    RadioGroup radioDebitCredit;
    RadioButton radioDebit;
    RadioButton radioCredit;
    String debitCredit = "";

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.single_transaction_search_result, container, false);
        saveButton = (Button) view.findViewById(R.id.saveTransaction);
        saveButton.setOnClickListener(this);
        dataSource = new TransactionDataSource(getActivity());
        CustomerDataSource customerDataSource = new CustomerDataSource(getActivity());
        SupplierDataSource supplierDataSource = new SupplierDataSource(getActivity());
        customerDataSource.open();
        supplierDataSource.open();
        valueCustomers = customerDataSource.getAllCustomers();
        valueSuppliers = supplierDataSource.getAllSuppliers();
        dataSource.open();
        populateSingleResult(getTransaction());
        return view;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public void populateSingleResult(Transaction transaction) {
        if (transaction != null) {
            TableLayout t1 = (TableLayout) view.findViewById(R.id.transactionTableResult);
            TextView transactionNo = (TextView) t1.findViewById(R.id.transactionNumberResult);
            transactionNo.setText(transaction.getTransactionNo());
            transactionDate = (TextView) t1.findViewById(R.id.transactionDateResult);
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
            transactionDescription = (EditText) t1.findViewById(R.id.transactionDescriptionResult);
            transactionDescription.setText(transaction.getTransactionDesc());
            radioDebitCredit = (RadioGroup) view.findViewById(R.id.radioButtonGroupForDebitCreditLabel);
            radioDebit = (RadioButton) view.findViewById(R.id.radioButtonDebitResult);
            radioCredit = (RadioButton) view.findViewById(R.id.radioButtonCreditResult);
            radioDebit.setEnabled(false);
            radioCredit.setEnabled(false);
            radioDebitCredit.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch(checkedId) {
                        case R.id.radioButtonDebitResult:
                            debitCredit = "Debit";
                            break;
                        case R.id.radioButtonCreditResult:
                            debitCredit = "Credit";
                            break;
                    }
                }
            });
            transactionPrice = (EditText) t1.findViewById(R.id.transactionValueResult);
            if(!transaction.getCredit().equals("")) {
                debitCredit = "Credit";
                radioCredit.setChecked(true);
                transactionPrice.setText(transaction.getCredit());
            }else if(!transaction.getDebit().equals("")){
                radioDebit.setChecked(true);
                debitCredit = "Debit";
                transactionPrice.setText(transaction.getDebit());
            }
            transactionWith = (TextView) t1.findViewById(R.id.transactionWithResult);
            if(!transaction.getCustomerNo().equals("")) {
                for (int i = 0; i < valueCustomers.size(); i++) {
                    if(transaction.getCustomerNo().equals(valueCustomers.get(i).getCustomerNo())){
                        transactionWith.setText(valueCustomers.get(i).getCustomerName());
                        break;
                    }
                }
            }else if(!transaction.getSupplierNo().equals("")){
                for (int i = 0; i < valueSuppliers.size(); i++) {
                    if(transaction.getSupplierNo().equals(valueSuppliers.get(i).getSupplierNo())){
                        transactionWith.setText(valueSuppliers.get(i).getSupplierName());
                        break;
                    }
                }
            }
            transactionType = (TextView) t1.findViewById(R.id.transactionTypeResult);
            transactionType.setText(transaction.getTransactionType());

        }
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        if (v.getId() == R.id.saveTransaction) {
            transactionDescription.setError(null);
            transactionPrice.setError(null);
            transactionType.setError(null);
            if (transactionDescription.getText().toString().trim().length() <= 0) {
                transactionDescription.setError(REQUIRED_MSG);
                valid = false;
            }
            if (transactionPrice.getText().toString().trim().length() <= 0) {
                transactionPrice.setError(REQUIRED_MSG);
                valid = false;
            }
            if (valid) {
                String transactionNo = getTransaction().getTransactionNo();
                // save the new transaction to the database
                // save the new transaction to the database
                if(debitCredit.equals("Debit")) {
                    Transaction transaction = dataSource.updateTransaction(transactionNo,
                            getTransaction().getTransactionDate(),
                            transactionDescription.getText().toString().trim(),
                            getTransaction().getCustomerNo(),
                            getTransaction().getSupplierNo(),
                            getTransaction().getTransactionType(), transactionPrice.getText().toString().trim(), null);
                }else {
                    Transaction transaction = dataSource.updateTransaction(transactionNo,
                            getTransaction().getTransactionDate(),
                            transactionDescription.getText().toString().trim(),
                            getTransaction().getCustomerNo(),
                            getTransaction().getSupplierNo(),
                            getTransaction().getTransactionType(), null, transactionPrice.getText().toString().trim());
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

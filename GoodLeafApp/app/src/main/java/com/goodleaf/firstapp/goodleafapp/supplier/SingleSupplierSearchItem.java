package com.goodleaf.firstapp.goodleafapp.supplier;

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
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.Supplier;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.SupplierDataSource;

public class SingleSupplierSearchItem extends Fragment implements View.OnClickListener {
    View view;
    private Supplier supplier;
    Button saveButton;
    EditText supplierName;
    EditText businessName;
    EditText supplierAddress;
    EditText supplierContact;
    EditText supplierAccountNumber;
    EditText supplierAccountName;
    EditText supplierBankName;
    EditText supplierBankCode;
    private static final String REQUIRED_MSG = "Required";
    private SupplierDataSource dataSource;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.single_supplier_search_result, container, false);
        saveButton = (Button) view.findViewById(R.id.saveSupplier);
        saveButton.setOnClickListener(this);
        dataSource = new SupplierDataSource(getActivity());
        dataSource.open();
        populateSingleResult(getSupplier());
        return view;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void populateSingleResult(Supplier supplier) {
        if (supplier != null) {
            TableLayout t1 = (TableLayout) view.findViewById(R.id.supplierTableResult);
            TextView supplierNo = (TextView) t1.findViewById(R.id.supplierNumberResult);
            supplierNo.setText(supplier.getSupplierNo());
            supplierName = (EditText) t1.findViewById(R.id.supplierNameResult);
            supplierName.setText(supplier.getSupplierName());
            businessName = (EditText) t1.findViewById(R.id.supplierBusinessNameResult);
            businessName.setText(supplier.getBusinessName());
            supplierAddress = (EditText) t1.findViewById(R.id.supplierAddressResult);
            supplierAddress.setText(supplier.getSupplierAddress());
            supplierContact = (EditText) t1.findViewById(R.id.supplierContactResult);
            supplierContact.setText(supplier.getSupplierContact());
            supplierAccountNumber = (EditText) t1.findViewById(R.id.supplierAccountNumberResult);
            supplierAccountNumber.setText(supplier.getSupplierAccountNumber());
            supplierAccountName = (EditText) t1.findViewById(R.id.supplierAccountNameResult);
            supplierAccountName.setText(supplier.getSupplierAccountName());
            supplierBankName = (EditText) t1.findViewById(R.id.supplierBankNameResult);
            supplierBankName.setText(supplier.getSupplierBankName());
            supplierBankCode = (EditText) t1.findViewById(R.id.supplierBankCodeResult);
            supplierBankCode.setText(supplier.getSupplierBankCode());
        }
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        if (v.getId() == R.id.saveSupplier) {
            supplierName.setError(null);
            supplierAddress.setError(null);
            supplierContact.setError(null);
            if (supplierName.getText().toString().trim().length() <= 0) {
                supplierName.setError(REQUIRED_MSG);
                valid = false;
            }
            if (supplierAddress.getText().toString().trim().length() <= 0) {
                supplierAddress.setError(REQUIRED_MSG);
                valid = false;
            }
            if (supplierContact.getText().toString().trim().length() <= 0) {
                supplierContact.setError(REQUIRED_MSG);
                valid = false;
            }
            if (valid) {
                String supplierNo = getSupplier().getSupplierNo();
                // save the new supplier to the database
                Supplier supplier = dataSource.updateSupplier(supplierNo,
                        supplierName.getText().toString().trim(),
                        businessName.getText().toString().trim(),
                        supplierAddress.getText().toString().trim(),
                        supplierContact.getText().toString().trim(),
                        supplierAccountNumber.getText().toString().trim(),
                        supplierAccountName.getText().toString().trim(),
                        supplierBankName.getText().toString().trim(),
                        supplierBankCode.getText().toString().trim());
                Fragment fr = new Fragment_Supplier();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fr);
                fragmentTransaction.commit();
            }
        }
        
    }
}

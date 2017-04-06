package com.goodleaf.firstapp.goodleafapp.supplier;

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
import com.goodleaf.firstapp.goodleafapp.dbinteraction.OverallBalances.OverallBalancesDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.Supplier;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.SupplierDataSource;

import java.util.List;

public class Fragment_Create_Supplier extends android.app.Fragment implements View.OnClickListener {
    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    List<Supplier> values;

    Button upButton;
    View view;
    TextView supplierName;
    TextView businessName;
    TextView supplierAddress;
    TextView supplierContact;
    TextView supplierAccountNumber;
    TextView supplierAccountName;
    TextView supplierBankName;
    TextView supplierBankCode;
    private SupplierDataSource dataSource;
    private OverallBalancesDataSource overallBalancesDataSource;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_create_supplier, container, false);
        upButton = (Button) view.findViewById(R.id.createSupplier);
        upButton.setOnClickListener(this);
        supplierName = (TextView) view.findViewById(R.id.supplierNameCreate);
        supplierAddress = (TextView) view.findViewById(R.id.supplierAddressCreate);
        supplierContact = (TextView) view.findViewById(R.id.supplierContactCreate);
        businessName = (TextView) view.findViewById(R.id.businessNameCreate);
        supplierAccountNumber = (TextView) view.findViewById(R.id.supplierAccountNumber);
        supplierAccountName = (TextView) view.findViewById(R.id.supplierAccountName);
        supplierBankName = (TextView) view.findViewById(R.id.supplierBankName);
        supplierBankCode = (TextView) view.findViewById(R.id.supplierBankCode);
        dataSource = new SupplierDataSource(getActivity());
        overallBalancesDataSource = new OverallBalancesDataSource(getActivity());
        overallBalancesDataSource.open();
        dataSource.open();
        values = dataSource.getAllSuppliers();
        return view;
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        if (v.getId() == R.id.createSupplier) {
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
                String supplierNo;
                if (values.size() > 0) {
                    Supplier supp = values.get(values.size() - 1);
                    supplierNo = String.valueOf(Integer.parseInt(supp.getSupplierNo()) + 1);
                } else {
                    supplierNo = "1";
                }
                // save the new supplier to the database
                Supplier supplier = dataSource.createSupplier(supplierNo,
                        supplierName.getText().toString().trim(),
                        businessName.getText().toString().trim(),
                        supplierAddress.getText().toString().trim(),
                        supplierContact.getText().toString().trim(),
                        supplierAccountNumber.getText().toString().trim(),
                        supplierAccountName.getText().toString().trim(),
                        supplierBankName.getText().toString().trim(),
                        supplierBankCode.getText().toString().trim());
                overallBalancesDataSource.createOverallBalance(null,supplierNo,"0");
                Fragment fr = new Fragment_Supplier();
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

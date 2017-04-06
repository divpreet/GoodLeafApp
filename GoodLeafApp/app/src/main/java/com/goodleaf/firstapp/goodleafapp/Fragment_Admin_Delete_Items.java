package com.goodleaf.firstapp.goodleafapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.goodleaf.firstapp.goodleafapp.customer.Fragment_Delete_Customer;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.MySQLiteHelper;
import com.goodleaf.firstapp.goodleafapp.order.Fragment_Delete_Order;
import com.goodleaf.firstapp.goodleafapp.product.Fragment_Delete_Product;
import com.goodleaf.firstapp.goodleafapp.purchase.Fragment_Delete_Purchase;
import com.goodleaf.firstapp.goodleafapp.shipment.Fragment_Delete_Shipment;
import com.goodleaf.firstapp.goodleafapp.supplier.Fragment_Delete_Supplier;
import com.goodleaf.firstapp.goodleafapp.transaction.Fragment_Delete_Transaction;

public class Fragment_Admin_Delete_Items extends Fragment implements View.OnClickListener  {
    Button dropAndUpgrade;
    Button deleteOrderButton;
    Button deletePurchaseButton;
    Button deleteTransactionButton;
    Button deleteShipmentButton;
    Button deleteCustomerButton;
    Button deleteSupplierButton;
    Button deleteProductsButton;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_admin_delete_items, container, false);
        dropAndUpgrade = (Button)view.findViewById(R.id.dropAndUpgrade);
        dropAndUpgrade.setOnClickListener(this);
        deleteOrderButton = (Button) view.findViewById(R.id.deleteOrder);
        deleteOrderButton.setOnClickListener(this);
        deletePurchaseButton = (Button) view.findViewById(R.id.deletePurchase);
        deletePurchaseButton.setOnClickListener(this);
        deleteTransactionButton = (Button) view.findViewById(R.id.deleteTransaction);
        deleteTransactionButton.setOnClickListener(this);
        deleteShipmentButton = (Button) view.findViewById(R.id.deleteShipment);
        deleteShipmentButton.setOnClickListener(this);
        deleteCustomerButton = (Button) view.findViewById(R.id.deleteCustomer);
        deleteCustomerButton.setOnClickListener(this);
        deleteSupplierButton = (Button) view.findViewById(R.id.deleteSupplier);
        deleteSupplierButton.setOnClickListener(this);
        deleteProductsButton = (Button) view.findViewById(R.id.deleteProducts);
        deleteProductsButton.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dropAndUpgrade:
                dropAndUpgradeTables();
                loadFragment(new Fragment_Home());
                break;
            case R.id.deleteOrder:
                loadFragment(new Fragment_Delete_Order());
                break;
            case R.id.deletePurchase:
                loadFragment(new Fragment_Delete_Purchase());
                break;
            case R.id.deleteTransaction:
                loadFragment(new Fragment_Delete_Transaction());
                break;
            case R.id.deleteShipment:
                loadFragment(new Fragment_Delete_Shipment());
                break;
            case R.id.deleteCustomer:
                loadFragment(new Fragment_Delete_Customer());
                break;
            case R.id.deleteSupplier:
                loadFragment(new Fragment_Delete_Supplier());
                break;
            case R.id.deleteProducts:
                loadFragment(new Fragment_Delete_Product());
                break;
        }
    }

    private void dropAndUpgradeTables(){
        MySQLiteHelper dbHelp = new MySQLiteHelper(getActivity());
        dbHelp.onUpgrade(dbHelp.getWritableDatabase(),0,1);
    }

    private void loadFragment(Fragment fr){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_main, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

package com.goodleaf.firstapp.goodleafapp.shipment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.goodleaf.firstapp.goodleafapp.NoSearchResults;
import com.goodleaf.firstapp.goodleafapp.R;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Order.Order;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Order.OrderDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.Product;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.ProductDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Shipment.Shipment;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Shipment.ShipmentDataSource;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Search_Shipment extends android.app.Fragment implements View.OnClickListener {
    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    List<Shipment> values;

    Button upButton;
    View view;
    Spinner shipmentOrderNo;
    TextView shipmentNumber;
    TextView shipmentDate;
    Spinner shipmentProductName;
    private ShipmentDataSource dataSource;
    private List<Order> orders;
    private List<Product> products;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_search_shipment, container, false);
        upButton = (Button) view.findViewById(R.id.searchShipmentByFilter);
        upButton.setOnClickListener(this);

        shipmentNumber = (TextView) view.findViewById(R.id.shipmentNumberSearch);
        shipmentDate = (TextView) view.findViewById(R.id.shipmentDateSearch);
        OrderDataSource orderDataSource = new OrderDataSource(getActivity());
        orderDataSource.open();
        orders = orderDataSource.getAllOrders();

        shipmentOrderNo = (Spinner) view.findViewById(R.id.shipmentOrderNoSearch);

        ArrayList<String> options=new ArrayList<>();

        for(int i=0;i< orders.size();i++){
            options.add(orders.get(i).getOrderNo());
        }

// use default spinner item to show options in spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,options);
        shipmentOrderNo.setAdapter(adapter);

        ProductDataSource productDataSource = new ProductDataSource(getActivity());
        productDataSource.open();
        products = productDataSource.getAllProducts();

        shipmentProductName = (Spinner) view.findViewById(R.id.shipmentProductNameSearch);

        ArrayList<String> optionProducts=new ArrayList<>();

        for(int i=0;i< products.size();i++){
            optionProducts.add(products.get(i).getProductDescription());
        }

// use default spinner item to show options in spinner
        ArrayAdapter<String> adapterProduct = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,optionProducts);
        shipmentProductName.setAdapter(adapterProduct);


        dataSource = new ShipmentDataSource(getActivity());
        dataSource.open();
        //values = dataSource.getAllShipments();
        return view;
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        Order order;
        Product product;
        String orderNo = "";
        String productNo = "";
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        if (v.getId() == R.id.searchShipmentByFilter) {
            shipmentNumber.setError(null);
            shipmentDate.setError(null);
            if (shipmentOrderNo.getSelectedItem().toString().trim().length() <= 0 &&
                    shipmentNumber.getText().toString().trim().length() <= 0 &&
                    shipmentDate.getText().toString().trim().length() <= 0 &&
                    shipmentProductName.getSelectedItem().toString().trim().length() <= 0) {
                shipmentNumber.setError(REQUIRED_MSG);
                shipmentDate.setError(REQUIRED_MSG);
                valid = false;
            }
            if (valid) {


                for(int i=0;i< orders.size();i++){
                    order = orders.get(i);
                    if(order.getOrderNo().trim().equalsIgnoreCase(shipmentOrderNo.getSelectedItem().toString().trim())){
                        orderNo = order.getOrderNo();
                    }
                }
                for(int i=0;i< products.size();i++){
                    product = products.get(i);
                    if(product.getProductDescription().trim().equalsIgnoreCase(shipmentProductName.getSelectedItem().toString().trim())){
                        productNo = product.getProductNo();
                    }
                }
                values = dataSource.getShipments(shipmentNumber.getText().toString().trim(),
                        orderNo,
                        shipmentDate.getText().toString().trim(),
                        productNo);
                Fragment fr;
                if (values.size() == 1) {
                    fr = new SingleShipmentSearchItem();
                    ((SingleShipmentSearchItem) fr).setShipment(values.get(0));
                    createFragment(fr);
                } else if (values.size() > 1) {
                    fr = new ShipmentSearchResultsTableLayout();
                    ((ShipmentSearchResultsTableLayout) fr).setListOfShipments(values);
                    createFragment(fr);
                } else {
                    fr = new NoSearchResults();
                    createFragment(fr);
                }
            }
        }
    }

    private void createFragment(Fragment fr) {

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_shipment_search_result, fr);
        fragmentTransaction.commit();
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

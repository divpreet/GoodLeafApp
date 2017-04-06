package com.goodleaf.firstapp.goodleafapp.shipment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.goodleaf.firstapp.goodleafapp.R;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Order.Order;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Order.OrderDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Order.OrderDetails;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.OverallBalances.OverallBalancesDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.Product;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.ProductDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Shipment.Shipment;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Shipment.ShipmentDataSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Fragment_Create_Shipment extends android.app.Fragment implements View.OnClickListener {
    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    private static final String PRODUCT_QUANTITY_INVALID = "Pending products less than selected quantity";
    private static final String PRODUCT_NOT_PART_OF_ORDER = "Product not part of Order";
    List<Shipment> values;

    Button upButton;
    View view;
    Spinner orderNo;
    Spinner productName;
    EditText quantity;
    EditText miscCharges;
    private List<Product> products;
    private ShipmentDataSource dataSource;
    private OrderDataSource orderDataSource;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_create_shipment, container, false);
        upButton = (Button) view.findViewById(R.id.createShipment);
        upButton.setOnClickListener(this);
        orderNo = (Spinner) view.findViewById(R.id.shipmentOrderNoCreate);
        productName = (Spinner) view.findViewById(R.id.shipmentProductNameCreate);
        quantity = (EditText) view.findViewById(R.id.shipmentQuantityCreate);
        miscCharges = (EditText) view.findViewById(R.id.shipmentMiscChargesCreate);
        dataSource = new ShipmentDataSource(getActivity());
        ProductDataSource productDataSource = new ProductDataSource(getActivity());
        orderDataSource = new OrderDataSource(getActivity());
        productDataSource.open();
        orderDataSource.open();
        List<Order> orders = orderDataSource.getAllOrders();
        products = productDataSource.getAllProducts();

        ArrayList<String> options=new ArrayList<>();

        for(int i = 0; i< orders.size(); i++){
            options.add(orders.get(i).getOrderNo());
        }

// use default spinner item to show options in spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,options);
        orderNo.setAdapter(adapter);

        ArrayList<String> optionsProd = new ArrayList<>();
        for(int i=0;i< products.size();i++){
            optionsProd.add(products.get(i).getProductDescription());
        }
        ArrayAdapter<String> adapterProds = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,optionsProd);
        productName.setAdapter(adapterProds);

        dataSource.open();
        values = dataSource.getAllShipments();
        return view;
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        Product product;
        String productNo="";
        String price = "0";
        String orderNumber = "";
        String shipmentNo;
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String miscChargeValue;
        if (v.getId() == R.id.createShipment) {
            quantity.setError(null);
            if (quantity.getText().toString().trim().length() <= 0) {
                quantity.setError(REQUIRED_MSG);
                valid = false;
            }
            if (valid) {
                orderNumber = orderNo.getSelectedItem().toString().trim();
                if (values.size() > 0) {
                    Shipment shipment = values.get(values.size() - 1);
                    shipmentNo = String.valueOf(Integer.parseInt(shipment.getShipmentNo()) + 1);
                } else {
                    shipmentNo = "1";
                }
                for (int i = 0; i < products.size(); i++) {
                    product = products.get(i);
                    if (product.getProductDescription().trim().equalsIgnoreCase(productName.getSelectedItem().toString().trim())) {
                        productNo = product.getProductNo();
                        price = String.valueOf((Integer.parseInt(quantity.getText().toString().trim()) *
                                Integer.parseInt(product.getProductPrice())));
                    }
                }
                if(miscCharges.getText().toString().trim().length() <= 0){
                    miscChargeValue = "0";
                }else{
                    miscChargeValue = miscCharges.getText().toString().trim();
                }
                // save the new shipment to the database
                List<OrderDetails> orderDetails = orderDataSource.getAllOrderDetailsForOrder(orderNumber);
                OrderDetails od = null;
                for(int i = 0; i < orderDetails.size(); i++){
                    od = orderDetails.get(i);
                    if(od.getProductNo().equals(productNo)){
                        break;
                    }
                }
                if(od!=null && od.getQuantityPending() >= Integer.parseInt(quantity.getText().toString().trim())) {
                    orderDataSource.updateOrderDetails(od.getOrderDetailsNo(), od.getOrderNo(), od.getProductNo(), od.getQuantity(), od.getPrice(), od.getQuantityPending() - Integer.parseInt(quantity.getText().toString().trim()));
                    dataSource.createShipment(shipmentNo, formattedDate,
                            orderNumber, productNo, Integer.parseInt(quantity.getText().toString().trim()), price, miscChargeValue);
                    orderDataSource.updateOrderStatus(orderNumber);
                    Fragment fr = new Fragment_Shipments();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.content_main, fr);
                    fragmentTransaction.commit();
                }else if(od==null){
                    quantity.setError(PRODUCT_NOT_PART_OF_ORDER);
                }else{
                    quantity.setError(PRODUCT_QUANTITY_INVALID);
                }
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

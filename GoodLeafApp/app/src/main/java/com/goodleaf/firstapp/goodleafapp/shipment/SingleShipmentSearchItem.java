package com.goodleaf.firstapp.goodleafapp.shipment;

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
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Order.OrderDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Order.OrderDetails;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.Product;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.ProductDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Shipment.Shipment;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Shipment.ShipmentDataSource;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class SingleShipmentSearchItem extends Fragment implements View.OnClickListener {
    View view;
    private Shipment shipment;
    Button saveButton;
    TextView shipmentDate;
    TextView shipmentValue;
    TextView shipmentOrder;
    TextView shipmentNo;
    TextView shipmentProductName;
    EditText productQuantity;
    EditText miscCharges;
    private static final String REQUIRED_MSG = "Required";
    private ShipmentDataSource dataSource;
    private OrderDataSource orderDataSource;
    private List<Product> products;
    private Product product;
    private String price = "0";


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.single_shipment_search_result, container, false);
        saveButton = (Button) view.findViewById(R.id.saveShipment);
        saveButton.setOnClickListener(this);
        ProductDataSource productDataSource = new ProductDataSource(getActivity());
        orderDataSource = new OrderDataSource(getActivity());
        productDataSource.open();
        orderDataSource.open();
        products = productDataSource.getAllProducts();
        dataSource = new ShipmentDataSource(getActivity());
        dataSource.open();
        populateSingleResult(getShipment());
        return view;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public void populateSingleResult(Shipment shipment) {
        if (shipment != null) {
            TableLayout t1 = (TableLayout) view.findViewById(R.id.shipmentTableResult);
            shipmentNo = (TextView) t1.findViewById(R.id.shipmentNumberResult);
            shipmentNo.setText(shipment.getShipmentNo());
            shipmentDate = (TextView) t1.findViewById(R.id.shipmentDateResult);
            String date = shipment.getShipmentDate();
            try {
                DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                fromFormat.setLenient(false);
                DateFormat toFormat = new SimpleDateFormat("dd-MM-yyyy");
                toFormat.setLenient(false);
                date = toFormat.format(fromFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            shipmentDate.setText(date);
            shipmentProductName = (TextView) t1.findViewById(R.id.shipmentProductNameResult);
            for(int i=0;i<products.size();i++) {
                if(products.get(i).getProductNo().equals(shipment.getProductNo())) {
                    shipmentProductName.setText(products.get(i).getProductDescription());
                    break;
                }
            }
            productQuantity = (EditText) view.findViewById(R.id.shipmentProductQuantityResult);
            productQuantity.setText(shipment.getQuantity());
            productQuantity.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (!hasFocus) {
                        for (int i = 0; i < products.size(); i++) {
                            product = products.get(i);
                            if (product.getProductNo().equalsIgnoreCase(getShipment().getProductNo())) {
                                price = String.valueOf((Integer.parseInt(productQuantity.getText().toString().trim()) *
                                        Integer.parseInt(product.getProductPrice())));
                                shipmentValue.setText(price);
                                break;
                            }
                        }
                    }
                }
            });
            miscCharges = (EditText) view.findViewById(R.id.shipmentMiscChargesResult);
            miscCharges.setText(shipment.getMiscCharges());
            shipmentOrder = (TextView) t1.findViewById(R.id.shipmentOrderNoResult);
            shipmentOrder.setText(shipment.getOrderNo());
            shipmentValue = (TextView) t1.findViewById(R.id.shipmentValueResult);
            shipmentValue.setText(shipment.getShipmentEvaluation());
        }
    }
    
    @Override
    public void onClick(View v) {
        boolean valid = true;
        String miscChargeValue;
        if (v.getId() == R.id.saveShipment) {
            productQuantity.setError(null);
            miscCharges.setError(null);
            if (productQuantity.getText().toString().trim().length() <= 0) {
                productQuantity.setError(REQUIRED_MSG);
                valid = false;
            }
            if (miscCharges.getText().toString().trim().length() <= 0) {
                miscCharges.setError(REQUIRED_MSG);
                valid = false;
            }
            if (valid) {
                for (int i = 0; i < products.size(); i++) {
                    product = products.get(i);
                    if (product.getProductNo().equalsIgnoreCase(getShipment().getProductNo())) {
                        price = String.valueOf((Integer.parseInt(productQuantity.getText().toString().trim()) *
                                Integer.parseInt(product.getProductPrice())));
                        break;
                    }
                }
                if(miscCharges.getText().toString().trim().length() <= 0){
                    miscChargeValue = "0";
                }else{
                    miscChargeValue = miscCharges.getText().toString().trim();
                }
                // save the new shipment to the database
                dataSource.createShipment(getShipment().getShipmentNo(),getShipment().getShipmentDate(),getShipment().getOrderNo()
                        ,getShipment().getProductNo(),Integer.parseInt(productQuantity.getText().toString().trim()),price,miscChargeValue);
                List<OrderDetails> orderDetails = orderDataSource.getAllOrderDetailsForOrder(getShipment().getOrderNo());
                for(int i = 0; i < orderDetails.size(); i++){
                    OrderDetails od = orderDetails.get(i);
                    if(od.getProductNo().equals(getShipment().getProductNo())){
                        orderDataSource.updateOrderDetails(od.getOrderDetailsNo(),od.getOrderNo(),
                                od.getProductNo(),od.getQuantity(),od.getPrice(),
                                od.getQuantityPending() + getShipment().getQuantity() - Integer.parseInt(productQuantity.getText().toString().trim()));
                        break;
                    }
                }
                orderDataSource.updateOrderStatus(getShipment().getOrderNo());
                Fragment fr = new Fragment_Shipments();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fr);
                fragmentTransaction.commit();
            }
        }
    }
}
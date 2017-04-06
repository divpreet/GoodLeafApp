package com.goodleaf.firstapp.goodleafapp.order;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.goodleaf.firstapp.goodleafapp.R;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.Customer;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.CustomerDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Order.Order;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Order.OrderDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Order.OrderDetails;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.OverallBalances.OverallBalances;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.OverallBalances.OverallBalancesDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.Product;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.ProductDataSource;
import com.goodleaf.firstapp.goodleafapp.product.ProductQuantity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Fragment_Create_Order extends android.app.Fragment implements View.OnClickListener {
    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    List<Order> ordersList;
    List<OrderDetails> ordersDetailsList;

    Button createOrderButton;
    Button addProducts;
    View view;
    Spinner orderCustomerName;

    List<ProductQuantity> productList = new ArrayList<>();
    private OrderDataSource dataSource;
    private OverallBalancesDataSource overallBalancesDataSource;
    private List<Customer> customers;
    private List<Product> products;
    ArrayList<String> optionsProd=new ArrayList<>();
    int prodRows = 2;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_create_order, container, false);
        ProductDataSource productDataSource = new ProductDataSource(getActivity());
        CustomerDataSource customerDataSource = new CustomerDataSource(getActivity());
        overallBalancesDataSource = new OverallBalancesDataSource(getActivity());
        overallBalancesDataSource.open();
        productDataSource.open();
        customerDataSource.open();
        customers = customerDataSource.getAllCustomers();
        products = productDataSource.getAllProducts();

        createOrderButton = (Button) view.findViewById(R.id.createOrder);
        createOrderButton.setOnClickListener(this);
        addProducts = (Button) view.findViewById(R.id.addProducts);
        addProducts.setOnClickListener(this);

        orderCustomerName = (Spinner) view.findViewById(R.id.orderCustomerNameCreate);

        ArrayList<String> options=new ArrayList<>();

        for(int i=0;i< customers.size();i++){
            options.add(customers.get(i).getCustomerName());
        }

// use default spinner item to show options in spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,options);
        orderCustomerName.setAdapter(adapter);


        for(int i=0;i< products.size();i++){
            optionsProd.add(products.get(i).getProductDescription());
        }

        addProductFields(prodRows);

        dataSource = new OrderDataSource(getActivity());
        dataSource.open();
        ordersList = dataSource.getAllOrders();
        ordersDetailsList = dataSource.getAllOrderDetails();
        return view;
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        Product product;
        Customer customer;
        String customerNo="";
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String price = "0";
        String orderNo = "-1";
        String orderDetailsNo;
        if (ordersDetailsList.size() > 0) {
            OrderDetails od = ordersDetailsList.get(ordersDetailsList.size() - 1);
            orderDetailsNo = String.valueOf(Integer.parseInt(od.getOrderDetailsNo()) + 1);
        } else {
            orderDetailsNo = "1";
        }
        if (v.getId() == R.id.createOrder) {
            for(int productIndex = 0; productIndex < productList.size(); productIndex++) {
                ProductQuantity productQuan = productList.get(productIndex);
                TextView quantity = productQuan.getQuantity();
                Spinner orderProductDescription = productQuan.getOrderProductDescription();
                quantity.setError(null);
                if (orderCustomerName.getSelectedItem().toString().trim().length() <= 0) {
                    //orderCustomerName.set(REQUIRED_MSG);
                    valid = false;
                }
                if (orderProductDescription.getSelectedItem().toString().trim().length() <= 0) {
                    valid = false;
                }
                if (quantity.getText().toString().trim().length() <= 0) {
                    quantity.setError(REQUIRED_MSG);
                    valid = false;
                }
                if (valid) {
                    for (int i = 0; i < products.size(); i++) {
                        product = products.get(i);
                        if (product.getProductDescription().trim().equalsIgnoreCase(orderProductDescription.getSelectedItem().toString().trim())) {
                            price = String.valueOf(Integer.parseInt(price) + (Integer.parseInt(quantity.getText().toString().trim()) *
                                    Integer.parseInt(product.getProductPrice())));
                            productQuan.setProductNo(product.getProductNo());
                            productQuan.setProdQuantity(Integer.parseInt(quantity.getText().toString().trim()));
                            break;
                        }
                    }
                }
            }
            if (valid) {
                int size = productList.size();
                for(int productIndex = 0; productIndex < size; productIndex++) {
                    for(int prodIndex = productIndex + 1; prodIndex < size; prodIndex++) {
                        if(productList.get(productIndex).getProductNo().equals(productList.get(prodIndex).getProductNo())){
                            productList.get(productIndex).setProdQuantity(productList.get(productIndex).getProdQuantity() + productList.get(prodIndex).getProdQuantity());
                            productList.remove(prodIndex);
                            size--;
                        }
                    }
                }
                for(int productIndex = 0; productIndex < productList.size(); productIndex++) {
                    ProductQuantity productQuan = productList.get(productIndex);
                    String productNo = productQuan.getProductNo();
                    String prodPrice = "0";
                    for (int i = 0; i < products.size(); i++) {
                        product = products.get(i);
                        if (product.getProductNo().equalsIgnoreCase(productNo)) {
                            prodPrice = String.valueOf((productQuan.getProdQuantity()) *
                                    Integer.parseInt(product.getProductPrice()));
                            break;
                        }
                    }
                    if(orderNo.equals("-1")) {
                        for (int i = 0; i < customers.size(); i++) {
                            customer = customers.get(i);
                            if (customer.getCustomerName().trim().equalsIgnoreCase(orderCustomerName.getSelectedItem().toString().trim())) {
                                customerNo = customer.getCustomerNo();
                            }
                        }
                        if (ordersList.size() > 0) {
                            Order cust = ordersList.get(ordersList.size() - 1);
                            orderNo = String.valueOf(Integer.parseInt(cust.getOrderNo()) + 1);
                        } else {
                            orderNo = "1";
                        }
                        // save the new order to the database
                        dataSource.createOrder(orderNo, formattedDate, price, customerNo, "");
                    }
                    dataSource.createOrderDetails(orderDetailsNo, orderNo, productNo,
                            productQuan.getProdQuantity(), prodPrice, productQuan.getProdQuantity());
                    orderDetailsNo = String.valueOf(Integer.parseInt(orderDetailsNo) + 1);
                }
                OverallBalances balance = overallBalancesDataSource.getAllOverallBalancesForCustomer(customerNo);
                String balanceValue = String.valueOf(Integer.parseInt(balance.getBalance()) - Integer.parseInt(price));
                overallBalancesDataSource.updateOverallBalance(customerNo,null,balanceValue);
                Fragment fr = new Fragment_Order();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fr);
                fragmentTransaction.commit();
            }
        }else if(v.getId() == R.id.addProducts){
            prodRows += 4;
            addProductFields(prodRows);
        }
    }

    private void addProductFields(int row){
        TextView quantity;
        Spinner orderProductDescription;
        GridLayout grid = (GridLayout) view.findViewById(R.id.orderCreateGrid);

        final float scale = getActivity().getResources().getDisplayMetrics().density;
        int pixels = (int) (50 * scale + 0.5f);

        TextView textViewProdName = new TextView(getActivity());
        textViewProdName.setHeight(pixels);
        GridLayout.LayoutParams paramProdName =new GridLayout.LayoutParams();
        paramProdName.columnSpec = GridLayout.spec(0,4);
        paramProdName.rowSpec = GridLayout.spec(row,2);
        paramProdName.setGravity(Gravity.CENTER);
        textViewProdName.setLayoutParams(paramProdName);
        textViewProdName.setPadding(20,20,0,0);
        textViewProdName.setTextSize(13);
        //textViewProdName.setLabelFor(R.id.orderProductNameCreate);
        textViewProdName.setText(R.string.product_description);

        Spinner spinnerProdName = new Spinner(getActivity());
        //spinnerProdName.setMinimumHeight(pixels);
        GridLayout.LayoutParams paramProdNameSpinner =new GridLayout.LayoutParams();
        paramProdNameSpinner.columnSpec = GridLayout.spec(4,5);
        paramProdNameSpinner.rowSpec = GridLayout.spec(row,2);
        paramProdNameSpinner.height = pixels;
        spinnerProdName.setLayoutParams(paramProdNameSpinner);
        int idSpinner = spinnerProdName.generateViewId();
        spinnerProdName.setId(idSpinner);

        TextView textViewProdQuantity = new TextView(getActivity());
        textViewProdQuantity.setHeight(pixels);
        GridLayout.LayoutParams paramProdQuantity =new GridLayout.LayoutParams();
        paramProdQuantity.columnSpec = GridLayout.spec(0,4);
        paramProdQuantity.rowSpec = GridLayout.spec(row + 2,2);
        paramProdQuantity.setGravity(Gravity.CENTER);
        textViewProdQuantity.setLayoutParams(paramProdQuantity);
        textViewProdQuantity.setPadding(20,20,0,0);
        textViewProdQuantity.setTextSize(13);
        //textViewProdQuantity.setLabelFor(R.id.orderProductQuantityCreate);
        textViewProdQuantity.setText(R.string.quantity);


        EditText editText = new EditText(getActivity());
        editText.setHeight(pixels);
        GridLayout.LayoutParams paramProdQuantityEditText =new GridLayout.LayoutParams();
        paramProdQuantityEditText.columnSpec = GridLayout.spec(4,5);
        paramProdQuantityEditText.rowSpec = GridLayout.spec(row + 2,2);
        int quantityId = editText.generateViewId();
        editText.setId(quantityId);
        editText.setEms(10);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        grid.addView(textViewProdName,row);
        grid.addView(spinnerProdName,row+1);
        grid.addView(textViewProdQuantity,row+2);
        grid.addView(editText,row+3);

        /*ViewGroup.LayoutParams layoutParams = addProducts.getLayoutParams();
        addProducts.setLayoutParams(layoutParams.);*/

        orderProductDescription = (Spinner) view.findViewById(idSpinner);

// use default spinner item to show options in spinner
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,optionsProd);
        orderProductDescription.setAdapter(adapter1);

        quantity = (TextView) view.findViewById(quantityId);
        productList.add(new ProductQuantity(orderProductDescription,quantity));
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

package com.goodleaf.firstapp.goodleafapp.order;

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
import android.widget.TableLayout;
import android.widget.TableRow;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SingleOrderSearchItem extends Fragment implements View.OnClickListener {
    View view;
    private Order order;
    Button saveButton;
    TextView orderDate;
    TextView orderPrice;
    TextView orderCustomer;
    TextView status;
    private static final String REQUIRED_MSG = "Required";
    private OrderDataSource dataSource;
    private ProductDataSource productDataSource;
    private CustomerDataSource customerDataSource;
    private OverallBalancesDataSource overallBalancesDataSource;
    ArrayList<String> optionsProd=new ArrayList<>();
    private List<Product> products;

    List<ProductQuantity> productList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.single_order_search_result, container, false);
        saveButton = (Button) view.findViewById(R.id.saveOrder);
        saveButton.setOnClickListener(this);
        status = (TextView) view.findViewById(R.id.orderStatusResult);
        productDataSource = new ProductDataSource(getActivity());
        customerDataSource = new CustomerDataSource(getActivity());
        overallBalancesDataSource = new OverallBalancesDataSource(getActivity());
        overallBalancesDataSource.open();
        productDataSource.open();
        customerDataSource.open();
        products = productDataSource.getAllProducts();
        for(int i=0;i< products.size();i++){
            optionsProd.add(products.get(i).getProductDescription());
        }
        dataSource = new OrderDataSource(getActivity());
        dataSource.open();
        if(!getOrder().getStatus().equals("")){
            saveButton.setEnabled(false);
        }
        populateSingleResult(getOrder());
        return view;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void populateSingleResult(Order order) {
        if (order != null) {
            TableLayout t1 = (TableLayout) view.findViewById(R.id.orderTableResult);
            TextView orderNo = (TextView) t1.findViewById(R.id.orderNumberResult);
            orderNo.setText(order.getOrderNo());
            orderDate = (TextView) t1.findViewById(R.id.orderDateResult);
            String date = order.getOrderDate();
            try {
                DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                fromFormat.setLenient(false);
                DateFormat toFormat = new SimpleDateFormat("dd-MM-yyyy");
                toFormat.setLenient(false);
                date = toFormat.format(fromFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            orderDate.setText(date);
            orderPrice = (TextView) t1.findViewById(R.id.orderPriceResult);
            orderPrice.setText(order.getOrderPrice());
            orderCustomer = (TextView) t1.findViewById(R.id.customerResult);
            List<Customer> customers = customerDataSource.getCustomers(order.getCustomerNo(),null);

            orderCustomer.setText(customers.get(0).getCustomerName());
            List<OrderDetails> orderDetailsNo = dataSource.getAllOrderDetailsForOrder(order.getOrderNo());
            for(int i=0;i<orderDetailsNo.size();i++){
                populateTable(orderDetailsNo.get(i));
            }
            status.setText(order.getStatus());
        }
    }

    public void populateTable(OrderDetails orderDetails) {
        if (orderDetails != null) {
            TableRow trProductDetails = (TableRow) View.inflate(getActivity(), R.layout.order_product_details_single_item, null);
            TableLayout tl = (TableLayout) view.findViewById(R.id.orderTableResult);
            Spinner orderProductDescription = (Spinner) trProductDetails.findViewById(R.id.productResult);
            List<Product> products = productDataSource.getProducts(orderDetails.getProductNo(),null);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,optionsProd);
            orderProductDescription.setAdapter(adapter1);
            orderProductDescription.setSelection(adapter1.getPosition(products.get(0).getProductDescription()));
            tl.addView(trProductDetails,tl.getChildCount()-2);
            TableRow trProductQuantity = (TableRow) View.inflate(getActivity(), R.layout.order_product_quantity_single_item, null);
            TextView productQuantity = (TextView) trProductQuantity.findViewById(R.id.productQuantityResult);
            productQuantity.setText(String.valueOf(orderDetails.getQuantity()));
            tl.addView(trProductQuantity,tl.getChildCount()-2);
            ProductQuantity pq = new ProductQuantity(orderProductDescription,productQuantity);
            pq.setOrderDetails(orderDetails);
            productList.add(pq);
        }
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        Product product;
        String productNo="";
        String price = "0";
        if (v.getId() == R.id.saveOrder) {
            for(int productIndex = 0; productIndex < productList.size(); productIndex++) {
                ProductQuantity productQuan = productList.get(productIndex);
                TextView quantity = productQuan.getQuantity();
                Spinner orderProductDescription = productQuan.getOrderProductDescription();
                quantity.setError(null);
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
                            productQuan.setProdQuantity(Integer.parseInt(quantity.getText().toString().trim()));
                        }
                    }
                }
            }
            if (valid) {
                String orderNo = getOrder().getOrderNo();
                dataSource.updateOrder(orderNo, getOrder().getOrderDate(), price, getOrder().getCustomerNo(), status.getText().toString().trim());
                for(int productIndex = 0; productIndex < productList.size(); productIndex++) {
                    ProductQuantity productQuan = productList.get(productIndex);
                    TextView quantity = productQuan.getQuantity();
                    Spinner orderProductDescription = productQuan.getOrderProductDescription();
                    String prodPrice = "0";
                    for (int i = 0; i < products.size(); i++) {
                        product = products.get(i);
                        if (product.getProductDescription().trim().equalsIgnoreCase(orderProductDescription.getSelectedItem().toString().trim())) {
                            productNo = product.getProductNo();
                            prodPrice = String.valueOf((Integer.parseInt(quantity.getText().toString().trim()) *
                                    Integer.parseInt(product.getProductPrice())));
                        }
                    }
                    dataSource.updateOrderDetails(productQuan.getOrderDetails().getOrderDetailsNo(), orderNo, productNo,
                            Integer.parseInt(quantity.getText().toString().trim()), prodPrice, Integer.parseInt(quantity.getText().toString().trim()));
                }
                OverallBalances balance = overallBalancesDataSource.getAllOverallBalancesForCustomer(getOrder().getCustomerNo());
                String balanceValue = String.valueOf(Integer.parseInt(balance.getBalance()) +Integer.parseInt(getOrder().getOrderPrice()) - Integer.parseInt(price));
                overallBalancesDataSource.updateOverallBalance(getOrder().getCustomerNo(),null,balanceValue);
                Fragment fr = new Fragment_Order();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fr);
                fragmentTransaction.commit();
            }
        }
    }
}

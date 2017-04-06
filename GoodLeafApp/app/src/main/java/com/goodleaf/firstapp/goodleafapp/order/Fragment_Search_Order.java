package com.goodleaf.firstapp.goodleafapp.order;

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
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.Customer;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Customer.CustomerDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Order.Order;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Order.OrderDataSource;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Search_Order extends android.app.Fragment implements View.OnClickListener {
    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    List<Order> values;

    Button upButton;
    View view;
    Spinner orderCustomerName;
    TextView orderNumber;
    TextView orderDate;
    TextView orderStatus;
    private OrderDataSource dataSource;
    private List<Customer> customers;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_search_order, container, false);
        upButton = (Button) view.findViewById(R.id.searchOrderByFilter);
        upButton.setOnClickListener(this);

        orderNumber = (TextView) view.findViewById(R.id.orderNumberSearch);
        orderDate = (TextView) view.findViewById(R.id.orderDateSearch);
        orderStatus = (TextView) view.findViewById(R.id.orderStatusSearch);
        CustomerDataSource customerDataSource = new CustomerDataSource(getActivity());
        customerDataSource.open();
        customers = customerDataSource.getAllCustomers();

        orderCustomerName = (Spinner) view.findViewById(R.id.orderCustomerNameSearch);

        ArrayList<String> options=new ArrayList<>();

        for(int i=0;i< customers.size();i++){
            options.add(customers.get(i).getCustomerName());
        }

// use default spinner item to show options in spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,options);
        orderCustomerName.setAdapter(adapter);


        dataSource = new OrderDataSource(getActivity());
        dataSource.open();
        //values = dataSource.getAllOrders();
        return view;
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        Customer customer;
        String customerNo = "";
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        if (v.getId() == R.id.searchOrderByFilter) {
            orderNumber.setError(null);
            orderDate.setError(null);
            orderStatus.setError(null);
            if (orderCustomerName.getSelectedItem().toString().trim().length() <= 0 &&
                    orderNumber.getText().toString().trim().length() <= 0 &&
                    orderDate.getText().toString().trim().length() <= 0 &&
                    orderStatus.getText().toString().trim().length() <= 0) {
                orderNumber.setError(REQUIRED_MSG);
                orderDate.setError(REQUIRED_MSG);
                orderStatus.setError(REQUIRED_MSG);
                valid = false;
            }
            if (valid) {


                for(int i=0;i< customers.size();i++){
                    customer = customers.get(i);
                    if(customer.getCustomerName().trim().equalsIgnoreCase(orderCustomerName.getSelectedItem().toString().trim())){
                        customerNo = customer.getCustomerNo();
                    }
                }
                values = dataSource.getOrders(orderNumber.getText().toString().trim(),
                        customerNo,
                        orderDate.getText().toString().trim(),
                        orderStatus.getText().toString().trim());
                Fragment fr;
                if (values.size() == 1) {
                    fr = new SingleOrderSearchItem();
                    ((SingleOrderSearchItem) fr).setOrder(values.get(0));
                    createFragment(fr);
                } else if (values.size() > 1) {
                    fr = new OrderSearchResultsTableLayout();
                    ((OrderSearchResultsTableLayout) fr).setListOfOrders(values);
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
        fragmentTransaction.replace(R.id.content_order_search_result, fr);
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

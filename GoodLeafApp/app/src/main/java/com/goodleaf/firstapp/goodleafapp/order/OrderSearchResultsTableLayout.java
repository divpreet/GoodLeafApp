package com.goodleaf.firstapp.goodleafapp.order;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.goodleaf.firstapp.goodleafapp.R;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Order.Order;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class OrderSearchResultsTableLayout extends android.app.Fragment {
    View view;
    List<Order> listOfOrders;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.order_searchresults_tablelayout, container, false);
        if (getListOfOrders().size() > 1) {
            for (int i = 0; i < getListOfOrders().size(); i++) {
                populateTable(getListOfOrders().get(i));
            }
        }
        return view;
    }

    public List<Order> getListOfOrders() {
        return listOfOrders;
    }

    public void setListOfOrders(List<Order> listOfOrders) {
        this.listOfOrders = listOfOrders;
    }

    public void populateTable(Order order) {
        if (order != null) {
            TableRow tr1 = (TableRow) View.inflate(getActivity(), R.layout.order_list_item, null);
            TableLayout tl = (TableLayout) view.findViewById(R.id.orderTableLayout);
            TextView orderNo = (TextView) tr1.findViewById(R.id.orderNumberListItem);
            orderNo.setText(order.getOrderNo());
            TextView orderDate = (TextView) tr1.findViewById(R.id.orderDateListItem);
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
            TextView orderPrice = (TextView) tr1.findViewById(R.id.orderPriceListItem);
            orderPrice.setText(order.getOrderPrice());
            TextView orderStatus = (TextView) tr1.findViewById(R.id.orderStatusListItem);
            orderStatus.setText(order.getStatus());
            tr1.setTag(order);
            tr1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fr = new SingleOrderSearchItem();
                    ((SingleOrderSearchItem) fr).setOrder((Order) v.getTag());
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.content_order, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            tl.addView(tr1);
        }
    }
}

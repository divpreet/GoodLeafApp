package com.goodleaf.firstapp.goodleafapp.shipment;

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
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.Product;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.ProductDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Shipment.Shipment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ShipmentSearchResultsTableLayout extends android.app.Fragment {
    View view;
    List<Shipment> listOfShipments;
    private List<Product> products;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.shipment_searchresults_tablelayout, container, false);
        ProductDataSource productDataSource = new ProductDataSource(getActivity());
        productDataSource.open();
        products = productDataSource.getAllProducts();
        if (getListOfShipments().size() > 1) {
            for (int i = 0; i < getListOfShipments().size(); i++) {
                populateTable(getListOfShipments().get(i));
            }
        }
        return view;
    }

    public List<Shipment> getListOfShipments() {
        return listOfShipments;
    }

    public void setListOfShipments(List<Shipment> listOfShipments) {
        this.listOfShipments = listOfShipments;
    }

    public void populateTable(Shipment shipment) {
        if (shipment != null) {
            TableRow tr1 = (TableRow) View.inflate(getActivity(), R.layout.shipment_list_item, null);
            TableLayout tl = (TableLayout) view.findViewById(R.id.shipmentTableLayout);
            TextView shipmentNo = (TextView) tr1.findViewById(R.id.shipmentNumberListItem);
            shipmentNo.setText(shipment.getShipmentNo());
            TextView shipmentDate = (TextView) tr1.findViewById(R.id.shipmentDateListItem);
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
            TextView shipmentOrderNo = (TextView) tr1.findViewById(R.id.shipmentOrderNoListItem);
            shipmentOrderNo.setText(shipment.getOrderNo());
            TextView shipmentProductName = (TextView) tr1.findViewById(R.id.shipmentProductNameListItem);
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getProductNo().equals(shipment.getProductNo())) {
                    shipmentProductName.setText(products.get(i).getProductDescription());
                    break;
                }
            }
            TextView shipmentProductQuantity = (TextView) tr1.findViewById(R.id.shipmentProductQuantityListItem);
            shipmentProductQuantity.setText(shipment.getQuantity());
            tr1.setTag(shipment);
            tr1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fr = new SingleShipmentSearchItem();
                    ((SingleShipmentSearchItem) fr).setShipment((Shipment) v.getTag());
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.content_shipment, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            tl.addView(tr1);
        }
    }
}
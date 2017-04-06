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
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Shipment.ShipmentDataSource;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class Fragment_Shipment_Recent_Summary extends Fragment {
    List<Shipment> values;
    private List<Product> products;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.recent_shipment_summary, container, false);
        ShipmentDataSource dataSource = new ShipmentDataSource(getActivity());
        dataSource.open();
        ProductDataSource productDataSource = new ProductDataSource(getActivity());
        productDataSource.open();
        products = productDataSource.getAllProducts();
        values = dataSource.getAllShipments();

        for (int i = 0; i < values.size(); i++) {
            if (i == 5) {
                break;
            }
            populateTable(values.get(i));
        }
        return view;
    }

    public void populateTable(Shipment shipment) {
        if (shipment != null) {
            TableLayout tl = (TableLayout) view.findViewById(R.id.shipmentTable);
            TableRow tr1 = (TableRow) View.inflate(getActivity(), R.layout.shipment_list_item, null);
            TextView shipmentNo = (TextView) tr1.findViewById(R.id.shipmentNumberListItem);
            TextView shipmentDate = (TextView) tr1.findViewById(R.id.shipmentDateListItem);
            TextView shipmentOrderNo = (TextView) tr1.findViewById(R.id.shipmentOrderNoListItem);
            TextView shipmentProductName = (TextView) tr1.findViewById(R.id.shipmentProductNameListItem);
            TextView shipmentProductQuantity = (TextView) tr1.findViewById(R.id.shipmentProductQuantityListItem);
            shipmentNo.setText(shipment.getShipmentNo());
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
            shipmentOrderNo.setText(shipment.getOrderNo());
            shipmentProductQuantity.setText(String.valueOf(shipment.getQuantity()));
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getProductNo().equals(shipment.getProductNo())) {
                    shipmentProductName.setText(products.get(i).getProductDescription());
                    break;
                }
            }
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
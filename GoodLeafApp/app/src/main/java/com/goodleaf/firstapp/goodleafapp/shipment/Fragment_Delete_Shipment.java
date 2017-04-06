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
import android.widget.Button;
import android.widget.TextView;

import com.goodleaf.firstapp.goodleafapp.Fragment_Home;
import com.goodleaf.firstapp.goodleafapp.R;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Shipment.Shipment;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Shipment.ShipmentDataSource;

import java.util.List;

public class Fragment_Delete_Shipment extends Fragment implements View.OnClickListener {
    private static final String REQUIRED_MSG = "Required";
    Button deleteAll;
    Button searchAndDelete;
    View view;
    TextView shipmentNumber;
    ShipmentDataSource dataSource;
    List<Shipment> values;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_delete_shipment, container, false);
        deleteAll = (Button) view.findViewById(R.id.deleteAllShipments);
        deleteAll.setOnClickListener(this);
        searchAndDelete = (Button) view.findViewById(R.id.searchAndDelete);
        searchAndDelete.setOnClickListener(this);

        shipmentNumber = (TextView) view.findViewById(R.id.shipmentNumberDelete);
        dataSource = new ShipmentDataSource(getActivity());
        dataSource.open();
        //values = dataSource.getAllShipments();
        return view;
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        if (v.getId() == R.id.searchAndDelete) {
            shipmentNumber.setError(null);
            if (shipmentNumber.getText().toString().trim().length() <= 0) {
                shipmentNumber.setError(REQUIRED_MSG);
                valid = false;
            }
            if (valid) {
                values = dataSource.getShipments(shipmentNumber.getText().toString().trim(),null,null,null);
                if (values.size() == 1) {
                    dataSource.deleteShipment(values.get(0));
                    loadHomeFragment();
                }
            }
        }else if(v.getId() == R.id.deleteAllShipments){
            dataSource.deleteAllShipments();
            loadHomeFragment();
        }
    }
    private void loadHomeFragment(){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_main, new Fragment_Home());
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

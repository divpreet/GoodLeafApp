package com.goodleaf.firstapp.goodleafapp.product;

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
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.Product;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.ProductDataSource;

import java.util.List;

public class Fragment_Delete_Product extends Fragment implements View.OnClickListener {
    private static final String REQUIRED_MSG = "Required";
    Button deleteAll;
    Button searchAndDelete;
    View view;
    TextView productNumber;
    ProductDataSource dataSource;
    List<Product> values;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_delete_product, container, false);
        deleteAll = (Button) view.findViewById(R.id.deleteAllProducts);
        deleteAll.setOnClickListener(this);
        searchAndDelete = (Button) view.findViewById(R.id.searchAndDelete);
        searchAndDelete.setOnClickListener(this);

        productNumber = (TextView) view.findViewById(R.id.productNumberDelete);
        dataSource = new ProductDataSource(getActivity());
        dataSource.open();
        //values = dataSource.getAllProducts();
        return view;
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        if (v.getId() == R.id.searchAndDelete) {
            productNumber.setError(null);
            if (productNumber.getText().toString().trim().length() <= 0) {
                productNumber.setError(REQUIRED_MSG);
                valid = false;
            }
            if (valid) {
                values = dataSource.getProducts(productNumber.getText().toString().trim(),null);
                if (values.size() == 1) {
                    dataSource.deleteProduct(values.get(0));
                    loadHomeFragment();
                }
            }
        }else if(v.getId() == R.id.deleteAllProducts){
            dataSource.deleteAllProducts();
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

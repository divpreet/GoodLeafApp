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

import com.goodleaf.firstapp.goodleafapp.NoSearchResults;
import com.goodleaf.firstapp.goodleafapp.R;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.Product;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.ProductDataSource;

import java.util.List;

public class Fragment_Search_Product extends android.app.Fragment implements View.OnClickListener {
    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    List<Product> values;

    Button upButton;
    View view;
    TextView productName;
    TextView productNumber;
    private ProductDataSource dataSource;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_search_product, container, false);
        upButton = (Button) view.findViewById(R.id.searchProductByFilter);
        upButton.setOnClickListener(this);
        productName = (TextView) view.findViewById(R.id.productDescriptionSearch);
        productNumber = (TextView) view.findViewById(R.id.productNumberSearch);
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
        if (v.getId() == R.id.searchProductByFilter) {
            productName.setError(null);
            productNumber.setError(null);
            if (productName.getText().toString().trim().length() <= 0 && productNumber.getText().toString().trim().length() <= 0) {
                productName.setError(REQUIRED_MSG);
                productNumber.setError(REQUIRED_MSG);
                valid = false;
            }
            if (valid) {
                values = dataSource.getProducts(productNumber.getText().toString().trim(),
                        productName.getText().toString().trim());
                Fragment fr;
                if (values.size() == 1) {
                    fr = new SingleProductSearchItem();
                    ((SingleProductSearchItem) fr).setProduct(values.get(0));
                    createFragment(fr);
                } else if (values.size() > 1) {
                    fr = new ProductSearchResultsTableLayout();
                    ((ProductSearchResultsTableLayout) fr).setListOfProducts(values);
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
        fragmentTransaction.replace(R.id.content_customer_search_result, fr);
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

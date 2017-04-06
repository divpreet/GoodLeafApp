package com.goodleaf.firstapp.goodleafapp.product;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.goodleaf.firstapp.goodleafapp.R;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.Product;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.ProductDataSource;

import java.util.List;

public class Fragment_Create_Product extends android.app.Fragment implements View.OnClickListener {
    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    List<Product> values;

    Button upButton;
    View view;
    TextView productDescription;
    TextView productPrice;
    TextView productType;
    private ProductDataSource dataSource;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_create_product, container, false);
        upButton = (Button) view.findViewById(R.id.createProduct);
        upButton.setOnClickListener(this);
        productDescription = (TextView) view.findViewById(R.id.productDescriptionCreate);
        productPrice = (TextView) view.findViewById(R.id.productPriceCreate);
        productType = (TextView) view.findViewById(R.id.productTypeCreate);
        dataSource = new ProductDataSource(getActivity());
        dataSource.open();
        values = dataSource.getAllProducts();
        return view;
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        if (v.getId() == R.id.createProduct) {
            productDescription.setError(null);
            productPrice.setError(null);
            productType.setError(null);
            if (productDescription.getText().toString().trim().length() <= 0) {
                productDescription.setError(REQUIRED_MSG);
                valid = false;
            }
            if (productPrice.getText().toString().trim().length() <= 0) {
                productPrice.setError(REQUIRED_MSG);
                valid = false;
            }
            if (productType.getText().toString().trim().length() <= 0) {
                productType.setError(REQUIRED_MSG);
                valid = false;
            }
            if (valid) {
                String productNo;
                if (values.size() > 0) {
                    Product cust = values.get(values.size() - 1);
                    productNo = String.valueOf(Integer.parseInt(cust.getProductNo()) + 1);
                } else {
                    productNo = "1";
                }
                // save the new product to the database
                Product product = dataSource.createProduct(productNo,
                        productDescription.getText().toString().trim(),
                        productPrice.getText().toString().trim(),
                        productType.getText().toString().trim());
                Fragment fr = new Fragment_Products();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fr);
                fragmentTransaction.commit();
            }
        }
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

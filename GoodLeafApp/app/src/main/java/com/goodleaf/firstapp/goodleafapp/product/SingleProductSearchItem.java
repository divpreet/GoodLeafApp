package com.goodleaf.firstapp.goodleafapp.product;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import com.goodleaf.firstapp.goodleafapp.R;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.Product;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.ProductDataSource;

import java.util.List;

public class SingleProductSearchItem extends Fragment implements View.OnClickListener {
    View view;
    private Product product;
    Button saveButton;
    EditText productDescription;
    EditText productPrice;
    EditText productType;
    private static final String REQUIRED_MSG = "Required";
    private ProductDataSource dataSource;


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.single_product_search_result, container, false);
        saveButton = (Button) view.findViewById(R.id.saveProduct);
        saveButton.setOnClickListener(this);
        dataSource = new ProductDataSource(getActivity());
        dataSource.open();
        populateSingleResult(getProduct());
        return view;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void populateSingleResult(Product product) {
        if (product != null) {
            TableLayout t1 = (TableLayout) view.findViewById(R.id.productTableResult);
            TextView productNo = (TextView) t1.findViewById(R.id.productNumberResult);
            productNo.setText(product.getProductNo());
            productDescription = (EditText) t1.findViewById(R.id.productDescriptionResult);
            productDescription.setText(product.getProductDescription());
            productPrice = (EditText) t1.findViewById(R.id.productPriceResult);
            productPrice.setText(product.getProductPrice());
            productType = (EditText) t1.findViewById(R.id.productTypeResult);
            productType.setText(product.getProductType());
        }
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        if (v.getId() == R.id.saveProduct) {
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
                String productNo = getProduct().getProductNo();
                // save the new product to the database
                // save the new product to the database
                Product product = dataSource.updateProduct(productNo,
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
}

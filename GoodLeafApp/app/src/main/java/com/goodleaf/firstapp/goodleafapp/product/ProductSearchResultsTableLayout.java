package com.goodleaf.firstapp.goodleafapp.product;

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

import java.util.List;

public class ProductSearchResultsTableLayout extends android.app.Fragment {
    View view;
    List<Product> listOfProducts;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.product_searchresults_tablelayout, container, false);
        if (getListOfProducts().size() > 1) {
            for (int i = 0; i < getListOfProducts().size(); i++) {
                populateTable(getListOfProducts().get(i));
            }
        }
        return view;
    }

    public List<Product> getListOfProducts() {
        return listOfProducts;
    }

    public void setListOfProducts(List<Product> listOfProducts) {
        this.listOfProducts = listOfProducts;
    }

    public void populateTable(Product product) {
        if (product != null) {
            TableRow tr1 = (TableRow) View.inflate(getActivity(), R.layout.product_list_item, null);
            TableLayout tl = (TableLayout) view.findViewById(R.id.productTable);
            TextView productNo = (TextView) tr1.findViewById(R.id.productNumberListItem);
            productNo.setText(product.getProductNo());
            TextView productName = (TextView) tr1.findViewById(R.id.productDescriptionListItem);
            productName.setText(product.getProductDescription());
            TextView productPrice = (TextView) tr1.findViewById(R.id.productPriceListItem);
            productPrice.setText(product.getProductPrice());
            TextView productType = (TextView) tr1.findViewById(R.id.productTypeListItem);
            productType.setText(product.getProductType());
            tr1.setTag(product);
            tr1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fr = new SingleProductSearchItem();
                    ((SingleProductSearchItem) fr).setProduct((Product) v.getTag());
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.content_product, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            tl.addView(tr1);
        }
    }
}

package com.goodleaf.firstapp.goodleafapp.purchase;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.goodleaf.firstapp.goodleafapp.R;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.OverallBalances.OverallBalances;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.OverallBalances.OverallBalancesDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.Product;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Product.ProductDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Purchase.Purchase;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Purchase.PurchaseDataSource;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Purchase.PurchaseDetails;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.Supplier;
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Supplier.SupplierDataSource;
import com.goodleaf.firstapp.goodleafapp.product.ProductQuantity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SinglePurchaseSearchItem extends Fragment implements View.OnClickListener {
    View view;
    private Purchase purchase;
    Button saveButton;
    TextView purchaseDate;
    TextView purchasePrice;
    TextView purchaseSupplier;
    EditText status;
    private static final String REQUIRED_MSG = "Required";
    private PurchaseDataSource dataSource;
    private ProductDataSource productDataSource;
    private SupplierDataSource supplierDataSource;
    private OverallBalancesDataSource overallBalancesDataSource;
    ArrayList<String> optionsProd=new ArrayList<>();
    private List<Product> products;

    List<ProductQuantity> productList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.single_purchase_search_result, container, false);
        saveButton = (Button) view.findViewById(R.id.savePurchase);
        saveButton.setOnClickListener(this);
        status = (EditText) view.findViewById(R.id.purchaseStatusResult);
        productDataSource = new ProductDataSource(getActivity());
        supplierDataSource = new SupplierDataSource(getActivity());
        overallBalancesDataSource = new OverallBalancesDataSource(getActivity());
        overallBalancesDataSource.open();
        productDataSource.open();
        supplierDataSource.open();
        products = productDataSource.getAllProducts();
        for(int i=0;i< products.size();i++){
            optionsProd.add(products.get(i).getProductDescription());
        }
        dataSource = new PurchaseDataSource(getActivity());
        dataSource.open();
        populateSingleResult(getPurchase());
        return view;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public void populateSingleResult(Purchase purchase) {
        if (purchase != null) {
            TableLayout t1 = (TableLayout) view.findViewById(R.id.purchaseTableResult);
            TextView purchaseNo = (TextView) t1.findViewById(R.id.purchaseNumberResult);
            purchaseNo.setText(purchase.getPurchaseNo());
            purchaseDate = (TextView) t1.findViewById(R.id.purchaseDateResult);
            String date = purchase.getPurchaseDate();
            try {
                DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                fromFormat.setLenient(false);
                DateFormat toFormat = new SimpleDateFormat("dd-MM-yyyy");
                toFormat.setLenient(false);
                date = toFormat.format(fromFormat.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            purchaseDate.setText(date);
            purchasePrice = (TextView) t1.findViewById(R.id.purchasePriceResult);
            purchasePrice.setText(purchase.getPurchasePrice());
            purchaseSupplier = (TextView) t1.findViewById(R.id.supplierResult);
            List<Supplier> suppliers = supplierDataSource.getSuppliers(purchase.getSupplierNo(),null);

            purchaseSupplier.setText(suppliers.get(0).getSupplierName());
            List<PurchaseDetails> purchaseDetailsNo = dataSource.getAllPurchaseDetailsForPurchase(purchase.getPurchaseNo());
            for(int i=0;i<purchaseDetailsNo.size();i++){
                populateTable(purchaseDetailsNo.get(i));
            }
            status.setText(purchase.getStatus());
        }
    }

    public void populateTable(PurchaseDetails purchaseDetails) {
        if (purchaseDetails != null) {
            TableRow trProductDetails = (TableRow) View.inflate(getActivity(), R.layout.purchase_product_details_single_item, null);
            TableLayout tl = (TableLayout) view.findViewById(R.id.purchaseTableResult);
            Spinner purchaseProductDescription = (Spinner) trProductDetails.findViewById(R.id.productResult);
            List<Product> products = productDataSource.getProducts(purchaseDetails.getProductNo(),null);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,optionsProd);
            purchaseProductDescription.setAdapter(adapter1);
            purchaseProductDescription.setSelection(adapter1.getPosition(products.get(0).getProductDescription()));
            tl.addView(trProductDetails,tl.getChildCount()-2);
            TableRow trProductQuantity = (TableRow) View.inflate(getActivity(), R.layout.purchase_product_quantity_single_item, null);
            TextView productQuantity = (TextView) trProductQuantity.findViewById(R.id.productQuantityResult);
            productQuantity.setText(String.valueOf(purchaseDetails.getQuantity()));
            tl.addView(trProductQuantity,tl.getChildCount()-2);
            ProductQuantity pq = new ProductQuantity(purchaseProductDescription,productQuantity);
            pq.setPurchaseDetails(purchaseDetails);
            productList.add(pq);
        }
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        Product product;
        String productNo="";
        String price = "0";
        if (v.getId() == R.id.savePurchase) {
            for(int productIndex = 0; productIndex < productList.size(); productIndex++) {
                ProductQuantity productQuan = productList.get(productIndex);
                TextView quantity = productQuan.getQuantity();
                Spinner purchaseProductDescription = productQuan.getPurchaseProductDescription();
                quantity.setError(null);
                if (purchaseProductDescription.getSelectedItem().toString().trim().length() <= 0) {
                    valid = false;
                }
                if (quantity.getText().toString().trim().length() <= 0) {
                    quantity.setError(REQUIRED_MSG);
                    valid = false;
                }
                if (valid) {
                    for (int i = 0; i < products.size(); i++) {
                        product = products.get(i);
                        if (product.getProductDescription().trim().equalsIgnoreCase(purchaseProductDescription.getSelectedItem().toString().trim())) {
                            price = String.valueOf(Integer.parseInt(price) + (Integer.parseInt(quantity.getText().toString().trim()) *
                                    Integer.parseInt(product.getProductPrice())));
                        }
                    }
                }
            }
            if (valid) {
                String purchaseNo = getPurchase().getPurchaseNo();
                dataSource.updatePurchase(purchaseNo, getPurchase().getPurchaseDate(), price, getPurchase().getSupplierNo(), status.getText().toString().trim());
                for(int productIndex = 0; productIndex < productList.size(); productIndex++) {
                    ProductQuantity productQuan = productList.get(productIndex);
                    TextView quantity = productQuan.getQuantity();
                    Spinner purchaseProductDescription = productQuan.getPurchaseProductDescription();
                    String prodPrice = "0";
                    for (int i = 0; i < products.size(); i++) {
                        product = products.get(i);
                        if (product.getProductDescription().trim().equalsIgnoreCase(purchaseProductDescription.getSelectedItem().toString().trim())) {
                            productNo = product.getProductNo();
                            prodPrice = String.valueOf((Integer.parseInt(quantity.getText().toString().trim()) *
                                    Integer.parseInt(product.getProductPrice())));
                        }
                    }
                    dataSource.updatePurchaseDetails(productQuan.getPurchaseDetails().getPurchaseDetailsNo(), purchaseNo, productNo,
                            Integer.parseInt(quantity.getText().toString().trim()), prodPrice, Integer.parseInt(quantity.getText().toString().trim()));
                }
                OverallBalances balance = overallBalancesDataSource.getAllOverallBalancesForSupplier(getPurchase().getSupplierNo());
                String balanceValue = String.valueOf(Integer.parseInt(balance.getBalance()) + Integer.parseInt(price) - Integer.parseInt(getPurchase().getPurchasePrice()));
                overallBalancesDataSource.updateOverallBalance(null,getPurchase().getSupplierNo(),balanceValue);
                Fragment fr = new Fragment_Purchase();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fr);
                fragmentTransaction.commit();
            }
        }
    }
}
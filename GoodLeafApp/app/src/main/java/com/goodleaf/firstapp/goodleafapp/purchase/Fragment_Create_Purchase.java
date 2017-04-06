package com.goodleaf.firstapp.goodleafapp.purchase;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Spinner;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Fragment_Create_Purchase extends android.app.Fragment implements View.OnClickListener {
    // Error Messages
    private static final String REQUIRED_MSG = "Required";
    List<Purchase> purchasesList;
    List<PurchaseDetails> purchasesDetailsList;

    Button createPurchaseButton;
    Button addProducts;
    View view;
    Spinner purchaseSupplierName;

    List<ProductQuantity> productList = new ArrayList<>();
    private PurchaseDataSource dataSource;
    private OverallBalancesDataSource overallBalancesDataSource;
    private List<Supplier> suppliers;
    private List<Product> products;
    ArrayList<String> optionsProd=new ArrayList<>();
    int prodRows = 2;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(
                R.layout.fragment_create_purchase, container, false);
        ProductDataSource productDataSource = new ProductDataSource(getActivity());
        SupplierDataSource supplierDataSource = new SupplierDataSource(getActivity());
        overallBalancesDataSource = new OverallBalancesDataSource(getActivity());
        overallBalancesDataSource.open();
        productDataSource.open();
        supplierDataSource.open();
        suppliers = supplierDataSource.getAllSuppliers();
        products = productDataSource.getAllProducts();

        createPurchaseButton = (Button) view.findViewById(R.id.createPurchase);
        createPurchaseButton.setOnClickListener(this);
        addProducts = (Button) view.findViewById(R.id.addProducts);
        addProducts.setOnClickListener(this);

        purchaseSupplierName = (Spinner) view.findViewById(R.id.purchaseSupplierNameCreate);

        ArrayList<String> options=new ArrayList<>();

        for(int i=0;i< suppliers.size();i++){
            options.add(suppliers.get(i).getSupplierName());
        }

// use default spinner item to show options in spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,options);
        purchaseSupplierName.setAdapter(adapter);


        for(int i=0;i< products.size();i++){
            optionsProd.add(products.get(i).getProductDescription());
        }

        addProductFields(prodRows);

        dataSource = new PurchaseDataSource(getActivity());
        dataSource.open();
        purchasesList = dataSource.getAllPurchases();
        purchasesDetailsList = dataSource.getAllPurchaseDetails();
        return view;
    }

    @Override
    public void onClick(View v) {
        boolean valid = true;
        Product product;
        Supplier supplier;
        String supplierNo="";
        String productNo="";
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String price = "0";
        String purchaseNo = "-1";
        String purchaseDetailsNo;
        if (purchasesDetailsList.size() > 0) {
            PurchaseDetails od = purchasesDetailsList.get(purchasesDetailsList.size() - 1);
            purchaseDetailsNo = String.valueOf(Integer.parseInt(od.getPurchaseDetailsNo()) + 1);
        } else {
            purchaseDetailsNo = "1";
        }
        if (v.getId() == R.id.createPurchase) {
            for(int productIndex = 0; productIndex < productList.size(); productIndex++) {
                ProductQuantity productQuan = productList.get(productIndex);
                TextView quantity = productQuan.getQuantity();
                Spinner purchaseProductDescription = productQuan.getPurchaseProductDescription();
                quantity.setError(null);
                if (purchaseSupplierName.getSelectedItem().toString().trim().length() <= 0) {
                    //purchaseSupplierName.set(REQUIRED_MSG);
                    valid = false;
                }
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
                    if(purchaseNo.equals("-1")) {
                        for (int i = 0; i < suppliers.size(); i++) {
                            supplier = suppliers.get(i);
                            if (supplier.getSupplierName().trim().equalsIgnoreCase(purchaseSupplierName.getSelectedItem().toString().trim())) {
                                supplierNo = supplier.getSupplierNo();
                            }
                        }
                        if (purchasesList.size() > 0) {
                            Purchase cust = purchasesList.get(purchasesList.size() - 1);
                            purchaseNo = String.valueOf(Integer.parseInt(cust.getPurchaseNo()) + 1);
                        } else {
                            purchaseNo = "1";
                        }
                        // save the new purchase to the database
                        dataSource.createPurchase(purchaseNo, formattedDate, price, supplierNo, "");
                    }
                    dataSource.createPurchaseDetails(purchaseDetailsNo, purchaseNo, productNo,
                            Integer.parseInt(quantity.getText().toString().trim()), prodPrice, Integer.parseInt(quantity.getText().toString().trim()));
                    purchaseDetailsNo = String.valueOf(Integer.parseInt(purchaseDetailsNo) + 1);
                }
                OverallBalances balance = overallBalancesDataSource.getAllOverallBalancesForSupplier(supplierNo);
                String balanceValue = String.valueOf(Integer.parseInt(balance.getBalance()) + Integer.parseInt(price));
                overallBalancesDataSource.updateOverallBalance(null,supplierNo,balanceValue);
                Fragment fr = new Fragment_Purchase();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_main, fr);
                fragmentTransaction.commit();
            }
        }else if(v.getId() == R.id.addProducts){
            prodRows += 4;
            addProductFields(prodRows);
        }
    }

    private void addProductFields(int row){
        TextView quantity;
        Spinner purchaseProductDescription;
        GridLayout grid = (GridLayout) view.findViewById(R.id.purchaseCreateGrid);

        final float scale = getActivity().getResources().getDisplayMetrics().density;
        int pixels = (int) (50 * scale + 0.5f);

        TextView textViewProdName = new TextView(getActivity());
        textViewProdName.setHeight(pixels);
        GridLayout.LayoutParams paramProdName =new GridLayout.LayoutParams();
        paramProdName.columnSpec = GridLayout.spec(0,4);
        paramProdName.rowSpec = GridLayout.spec(row,2);
        paramProdName.setGravity(Gravity.CENTER);
        textViewProdName.setLayoutParams(paramProdName);
        textViewProdName.setPadding(20,20,0,0);
        textViewProdName.setTextSize(13);
        //textViewProdName.setLabelFor(R.id.purchaseProductNameCreate);
        textViewProdName.setText(R.string.product_description);

        Spinner spinnerProdName = new Spinner(getActivity());
        //spinnerProdName.setMinimumHeight(pixels);
        GridLayout.LayoutParams paramProdNameSpinner =new GridLayout.LayoutParams();
        paramProdNameSpinner.columnSpec = GridLayout.spec(4,5);
        paramProdNameSpinner.rowSpec = GridLayout.spec(row,2);
        spinnerProdName.setLayoutParams(paramProdNameSpinner);
        int idSpinner = spinnerProdName.generateViewId();
        spinnerProdName.setId(idSpinner);

        TextView textViewProdQuantity = new TextView(getActivity());
        textViewProdQuantity.setHeight(pixels);
        GridLayout.LayoutParams paramProdQuantity =new GridLayout.LayoutParams();
        paramProdQuantity.columnSpec = GridLayout.spec(0,4);
        paramProdQuantity.rowSpec = GridLayout.spec(row + 2,2);
        paramProdQuantity.setGravity(Gravity.CENTER);
        textViewProdQuantity.setLayoutParams(paramProdQuantity);
        textViewProdQuantity.setPadding(20,20,0,0);
        textViewProdQuantity.setTextSize(13);
        //textViewProdQuantity.setLabelFor(R.id.purchaseProductQuantityCreate);
        textViewProdQuantity.setText(R.string.quantity);


        EditText editText = new EditText(getActivity());
        editText.setHeight(pixels);
        GridLayout.LayoutParams paramProdQuantityEditText =new GridLayout.LayoutParams();
        paramProdQuantityEditText.columnSpec = GridLayout.spec(4,5);
        paramProdQuantityEditText.rowSpec = GridLayout.spec(row + 2,2);
        int quantityId = editText.generateViewId();
        editText.setId(quantityId);
        editText.setEms(10);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);

        grid.addView(textViewProdName,row);
        grid.addView(spinnerProdName,row+1);
        grid.addView(textViewProdQuantity,row+2);
        grid.addView(editText,row+3);

        /*ViewGroup.LayoutParams layoutParams = addProducts.getLayoutParams();
        addProducts.setLayoutParams(layoutParams.);*/

        purchaseProductDescription = (Spinner) view.findViewById(idSpinner);

// use default spinner item to show options in spinner
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,optionsProd);
        purchaseProductDescription.setAdapter(adapter1);

        quantity = (TextView) view.findViewById(quantityId);
        productList.add(new ProductQuantity(purchaseProductDescription,quantity));
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
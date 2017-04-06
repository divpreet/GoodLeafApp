package com.goodleaf.firstapp.goodleafapp.purchase;

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
import com.goodleaf.firstapp.goodleafapp.dbinteraction.Purchase.Purchase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class PurchaseSearchResultsTableLayout extends android.app.Fragment {
    View view;
    List<Purchase> listOfPurchases;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(
                R.layout.purchase_searchresults_tablelayout, container, false);
        if (getListOfPurchases().size() > 1) {
            for (int i = 0; i < getListOfPurchases().size(); i++) {
                populateTable(getListOfPurchases().get(i));
            }
        }
        return view;
    }

    public List<Purchase> getListOfPurchases() {
        return listOfPurchases;
    }

    public void setListOfPurchases(List<Purchase> listOfPurchases) {
        this.listOfPurchases = listOfPurchases;
    }

    public void populateTable(Purchase purchase) {
        if (purchase != null) {
            TableRow tr1 = (TableRow) View.inflate(getActivity(), R.layout.purchase_list_item, null);
            TableLayout tl = (TableLayout) view.findViewById(R.id.purchaseTable);
            TextView purchaseNo = (TextView) tr1.findViewById(R.id.purchaseNumberListItem);
            purchaseNo.setText(purchase.getPurchaseNo());
            TextView purchaseDate = (TextView) tr1.findViewById(R.id.purchaseDateListItem);
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
            TextView purchasePrice = (TextView) tr1.findViewById(R.id.purchasePriceListItem);
            purchasePrice.setText(purchase.getPurchasePrice());
            TextView purchaseStatus = (TextView) tr1.findViewById(R.id.purchaseStatusListItem);
            purchaseStatus.setText(purchase.getStatus());
            tr1.setTag(purchase);
            tr1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fr = new SinglePurchaseSearchItem();
                    ((SinglePurchaseSearchItem) fr).setPurchase((Purchase) v.getTag());
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.content_purchase, fr);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
            tl.addView(tr1);
        }
    }
}

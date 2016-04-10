package com.example.adnansakel.bdl_food_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.adnansakel.bdl_food_app.DataModel.OrderData;
import com.example.adnansakel.bdl_food_app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adnan Sakel on 4/8/2016.
 */
public class OrderListAdapter extends BaseAdapter{
    private Context context;
    private List<OrderData> orderList;
    public OrderListAdapter(Context context){
        orderList = new ArrayList<OrderData>();
        this.context = context;
    }

    public void addItem(OrderData orderData){
        orderList.add(orderData);
        notifyDataSetChanged();
    }

    private class ViewHolder{
        TextView txtProductID;
        TextView txtBuyFromorSellToID;
        TextView txtDishName;
        TextView txtLocation;
        TextView txtValidTill;
        TextView txtPrice;
    }
    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder vholder;

        if(convertView == null){
            vi = LayoutInflater.from(context).inflate(R.layout.item_order, null);
            vholder = new ViewHolder();

            vholder.txtProductID = (TextView)vi.findViewById(R.id.textViewProductID);
            vholder.txtBuyFromorSellToID = (TextView)vi.findViewById(R.id.textViewBuyfrom_SelltoID);
            vholder.txtValidTill = (TextView)vi.findViewById(R.id.textViewValidTill);
            vholder.txtLocation = (TextView)vi.findViewById(R.id.textViewLocation);
            vholder.txtDishName = (TextView)vi.findViewById(R.id.textViewDishName);
            vholder.txtPrice = (TextView)vi.findViewById(R.id.textViewPrice);

            vi.setTag(vholder);
        }
        else{
            vholder = (ViewHolder)vi.getTag();
        }

        //System.out.println("From adapter " + orderList.get(position).getPostID());
        vholder.txtProductID.setText("Product ID: "+orderList.get(position).getPostID());
        if(orderList.get(position).getBuyorSell().equals("Buy")){
            vholder.txtBuyFromorSellToID.setText("Bought from: "+orderList.get(position).getUserID());
        }
        else{
            vholder.txtBuyFromorSellToID.setText("Sold to: "+orderList.get(position).getUserID());
        }

        vholder.txtDishName.setText("Dish name: "+orderList.get(position).getDishName());
        vholder.txtValidTill.setText("Valid till: "+orderList.get(position).getValidTill());
        vholder.txtLocation.setText("Location: "+orderList.get(position).getLocation());
        vholder.txtPrice.setText(orderList.get(position).getPrice()+" SEK");

        return vi;
    }
}

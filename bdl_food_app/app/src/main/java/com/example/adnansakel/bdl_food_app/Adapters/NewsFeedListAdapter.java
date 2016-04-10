package com.example.adnansakel.bdl_food_app.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adnansakel.bdl_food_app.DataModel.NewsFeedData;
import com.example.adnansakel.bdl_food_app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adnan Sakel on 3/30/2016.
 */
public class NewsFeedListAdapter extends BaseAdapter{

    private List<NewsFeedData> newsFeedList;
    private Context context;
    public NewsFeedListAdapter(Context context){
        newsFeedList = new ArrayList<NewsFeedData>();
        this.context = context;
    }

    public void addItem(NewsFeedData newsFeed){
        newsFeedList.add(newsFeed);
        notifyDataSetChanged();
    }

    private class ViewHolder{
        TextView textViewDishName;
        TextView textViewLocation;
        TextView textViewOrderBefore;
        TextView textViewDishLeft;
        TextView textViewDishPrice;
        TextView textViewPostMessage;
        ImageView imageViewDishImage;
    }
    @Override
    public int getCount() {
        return newsFeedList.size();
    }

    @Override
    public Object getItem(int position) {
        return newsFeedList.get(position);
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
            vi = LayoutInflater.from(context).inflate(R.layout.item_newsfeed, null);
            vholder = new ViewHolder();

            vholder.textViewDishName = (TextView)vi.findViewById(R.id.textViewDishName);
            vholder.textViewLocation = (TextView)vi.findViewById(R.id.textViewLocation);
            vholder.textViewOrderBefore = (TextView)vi.findViewById(R.id.textViewOrderBefore);
            vholder.textViewDishLeft = (TextView)vi.findViewById(R.id.textViewDishLeft);
            vholder.textViewDishPrice = (TextView)vi.findViewById(R.id.textViewDishPrice);
            vholder.textViewPostMessage = (TextView)vi.findViewById(R.id.textViewPostMessage);
            vholder.imageViewDishImage = (ImageView)vi.findViewById(R.id.imageViewDishImage);

            vi.setTag(vholder);
        }
        else{
            vholder = (ViewHolder)vi.getTag();
        }

        vholder.textViewDishName.setText("Dish: "+newsFeedList.get(position).getDishName());
        vholder.textViewLocation.setText("Location: "+newsFeedList.get(position).getLocation());
        vholder.textViewDishLeft.setText(newsFeedList.get(position).getNumberofDishes()+" Left");
        vholder.textViewDishPrice.setText(newsFeedList.get(position).getPrice()+" SEK");
        vholder.textViewOrderBefore.setText("Order before: "+newsFeedList.get(position).getOrderBefore());
        vholder.textViewPostMessage.setText(newsFeedList.get(position).getPostMessage());
        return vi;
    }
}

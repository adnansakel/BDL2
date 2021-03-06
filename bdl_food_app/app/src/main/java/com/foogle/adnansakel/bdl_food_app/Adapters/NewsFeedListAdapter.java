package com.foogle.adnansakel.bdl_food_app.Adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.foogle.adnansakel.bdl_food_app.DataModel.NewsFeedData;
import com.foogle.adnansakel.bdl_food_app.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adnan Sakel on 3/30/2016.
 */
public class NewsFeedListAdapter extends BaseAdapter{
    private byte[] imageAsBytesl;
    public String Image_String;
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

    public void addItemAtBeginning(NewsFeedData newsFeed){
        newsFeedList.add(0,newsFeed);
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
        vholder.textViewDishPrice.setText(newsFeedList.get(position).getPrice() + " SEK");
        Image_String = newsFeedList.get(position).getImage();
        if(Image_String != null)
        imageAsBytesl = Base64.decode(Image_String, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(imageAsBytesl, 0, imageAsBytesl.length);
        vholder.imageViewDishImage.setImageBitmap(bmp);
        vholder.textViewOrderBefore.setText("Order before: "+newsFeedList.get(position).getOrderBefore());
        vholder.textViewPostMessage.setText(newsFeedList.get(position).getPostMessage());
        if(newsFeedList.get(position).getPostMessage().length()==0){
            vholder.textViewPostMessage.setVisibility(View.GONE);
        }
        else{
            vholder.textViewPostMessage.setVisibility(View.VISIBLE);
        }
        return vi;
    }
}

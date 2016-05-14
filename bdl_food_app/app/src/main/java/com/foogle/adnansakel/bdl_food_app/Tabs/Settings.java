package com.foogle.adnansakel.bdl_food_app.Tabs;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.foogle.adnansakel.bdl_food_app.R;

/**
 * Created by Adnan Sakel on 5/4/2016.
 */
public class Settings extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.settings_menu,container,false);
        return v;
    }
}

package com.firesoda.sodafood;
import android.os.Bundle;

//import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

public class Tercerfragment extends Fragment {

    public static Tercerfragment newInstance() {
        return new Tercerfragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tercerfragment , container, false);
    }

}

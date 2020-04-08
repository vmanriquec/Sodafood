package com.firesoda.sodafood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;


public class Menutab extends AppCompatActivity {

    /**
     * para gestionar cada uno de los fragmentos
     * */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * Para alojar el contenido de cada fragment
     * */
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menutab);

        //inicializamos
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    /**
     * Clase que devolvera un fragment segun la seccion donde se encuentre
     * */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0: return primerfragmentoparamenu.newInstance();
                case 1: return segundofragment.newInstance();
                case 2: return Tercerfragment.newInstance();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;//tenemos tres fragment
        }

    }

}
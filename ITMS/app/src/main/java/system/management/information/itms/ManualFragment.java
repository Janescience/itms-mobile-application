package system.management.information.itms;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Janescience on 10/16/2017.
 */

public class ManualFragment extends Fragment {

    private ViewPager mViewPager;
    private Typeface Fonts;


    public ManualFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_manual, container, false);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);

        Fonts = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Kanit-Light.ttf");

        mViewPager = (ViewPager) v.findViewById(R.id.viewPagePortfolio);
        ManualFragment.PagerAdapter pagerAdapter = new ManualFragment.PagerAdapter(getChildFragmentManager(),getContext());
        mViewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        for(int i = 0; i < tabLayout.getTabCount(); i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }

        return v;
    }

    @Override
    public void onResume() { super.onResume();}

    public boolean onCreateOptionsMenu(Menu menu){

        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }





    private class PagerAdapter extends FragmentPagerAdapter {

        String tabTitles[] = new String[]{"0", "1", "2","3","4","5","6"};
        Context context;

        public PagerAdapter(FragmentManager fm, Context context){
            super(fm);
            this.context = context;
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            switch(position) {
                case 0:
                    return new IndexManualFragment();
                case 1:
                    return new WorkHistoryFragment();
                case 2:
                    return new AcademicWorkFragment();
                case 3:
                    return new AcademicWorkFragment();
                case 4:
                    return new AcademicWorkFragment();
                case 5:
                    return new AcademicWorkFragment();
                case 6:
                    return new AcademicWorkFragment();
            }
            return null;
        }





        @Override
        public CharSequence getPageTitle(int position){
            return tabTitles[position];
        }

        public View getTabView(int position){
            View tab = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            tv.setTypeface(Fonts);
            tv.setText(tabTitles[position]);
            return tab;
        }
    }
}

package system.management.information.itms;


import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class PortfolioFragment extends Fragment {

    private ViewPager mViewPager;
    private Typeface Fonts;
    private TextView txtPageToolBar;


    public PortfolioFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem backItem = menu.findItem(R.id.action_back);
        backItem.setVisible(true);
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_portfolio, container, false);
        txtPageToolBar = (TextView) v.findViewById(R.id.txtPageToolBar) ;


        Fonts = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Kanit-Light.ttf");

        mViewPager = (ViewPager) v.findViewById(R.id.viewPagePortfolio);
        PagerAdapter pagerAdapter = new PagerAdapter(getChildFragmentManager(),getContext());
        mViewPager.setAdapter(pagerAdapter);

        txtPageToolBar.setTypeface(Fonts);

        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(mViewPager);

        Toolbar toolbar = (Toolbar) v.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              getActivity().onBackPressed();
            }
        });



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

        String tabTitles[] = new String[]{"ประวัติการศึกษา", "ประวัติการทำงาน", "ผลงานวิชาการ"};
        Context context;

        public PagerAdapter(FragmentManager fm,Context context){
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
                    return new EducationHistoryFragment();
                case 1:
                    return new WorkHistoryFragment();
                case 2:
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

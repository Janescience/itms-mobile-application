package system.management.information.itms.Ui.Adapters;

/**
 * Created by Janescience on 11/24/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import system.management.information.itms.Ui.Fragment.UserFragment;

public class UserListingPagerAdapter  extends FragmentPagerAdapter {
    private static final Fragment[] sFragments = new Fragment[]{/*UsersFragment.newInstance(UsersFragment.TYPE_CHATS),*/
            UserFragment.newInstance(UserFragment.TYPE_ALL)};
    private static final String[] sTitles = new String[]{/*"Chats",*/
            "All Users"};

    public UserListingPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return sFragments[position];
    }

    @Override
    public int getCount() {
        return sFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return sTitles[position];
    }
}

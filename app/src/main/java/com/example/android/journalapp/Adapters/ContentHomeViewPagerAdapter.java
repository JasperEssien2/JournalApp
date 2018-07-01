package com.example.android.journalapp.Adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.android.journalapp.Fragments.MyJournalFragment;
import com.example.android.journalapp.Fragments.SharedJournalsFragment;
import com.example.android.journalapp.R;

public class ContentHomeViewPagerAdapter extends FragmentStatePagerAdapter {

    private final int NUMBER_OF_PAGES = 3;
    private Context mContext;
    private TabLayout mTabLayout;

    public ContentHomeViewPagerAdapter(Context context, FragmentManager fm, TabLayout tabLayout) {
        super(fm);
        mContext = context;
        mTabLayout = tabLayout;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new MyJournalFragment();
            case 1:
                return new SharedJournalsFragment();
            case 2:
                return new SharedJournalsFragment();
            default:
                throw new IllegalArgumentException();
        }
    }

    @Override
    public int getCount() {
        return NUMBER_OF_PAGES;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return getStringFromResource(R.string.title_tab_my_journal);
            case 1:
                return getStringFromResource(R.string.title_tab_shared_journal);
            case 2:
                return getStringFromResource(R.string.title_tab_feed_journal);
            default:
                return null;
        }
    }

    private String getStringFromResource(int stringResource){
        String string =
            mContext.getResources()
                .getString(stringResource);
        return string;
    }
}

package com.roman.yoursound.ui.search;

import android.content.Context;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class SampleFragmentPagerAdapter extends FragmentStatePagerAdapter {
    final int PAGE_COUNT = 2;
    private String tabTitles[] = new String[] { "Sounds", "People" };
    private Context context;
    SearchTabFragment pageFragment;
    public ArrayList<SearchTabFragment> searchFragments = new ArrayList<>();


    public SampleFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override public int getCount() {
        return PAGE_COUNT;
    }

    @Override public SearchTabFragment getItem(int position) {
        pageFragment = SearchTabFragment.newInstance(position + 1);
        searchFragments.add(pageFragment);
        return pageFragment;
    }

    @Override public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }
}

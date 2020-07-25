package com.roman.yoursound.ui.search;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.roman.yoursound.R;

public class SearchFragment extends Fragment {

    //Views
    Button searchBtn;
    EditText searchET;
    ViewPager viewPager;
    TabLayout tabLayout;
    SearchFragment searchFragment;

    String searchFor = "";
    SearchTabFragment searchSounds;
    SearchTabFragment searchPeople;
    SampleFragmentPagerAdapter pagerAdapter;
    public static FragmentManager fragmentManager;

    private SearchViewModel SearchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel =
                ViewModelProviders.of(this).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);

        //initialize views
        searchBtn = root.findViewById(R.id.search_search_btn);
        searchET = root.findViewById(R.id.editText_search_search);
        viewPager = root.findViewById(R.id.search_viewPager);
        tabLayout = root.findViewById(R.id.search_tabLayout);
        searchFragment = this;
        fragmentManager = getFragmentManager();

        //tabs
        pagerAdapter = new SampleFragmentPagerAdapter(getActivity().getSupportFragmentManager(), getActivity());
        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        //search
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide keyboard
                View view = getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                searchSounds = pagerAdapter.searchFragments.get(0);
                searchPeople = pagerAdapter.searchFragments.get(1);
                searchFor = searchET.getText().toString();
                if (!searchFor.isEmpty()){
                    SearchGetSounds searchGetSounds = new SearchGetSounds(searchFor, searchFragment);
                    searchGetSounds.execute();
                    SearchGetUsers searchGetUsers = new SearchGetUsers(searchFor, searchFragment);
                    searchGetUsers.execute();
                }
            }
        });

        return root;
    }

    public void soundsOnServerResponse(String response) {
        searchSounds.setResultTracks(response);
    }

    public void usersOnServerResponse(String response) {
        searchPeople.setResultUsers(response);
    }


}

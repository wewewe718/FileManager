package com.example.filemanager.fragment;

import android.content.res.Configuration;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.filemanager.R;
import com.example.filemanager.adapter.QuickAccessItemsAdapter;
import com.example.filemanager.databinding.FragmentQuickAccessBinding;

public class QuickAccessFragment extends BaseFragment {
    private FragmentQuickAccessBinding binding;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_quick_access, container, false);

        initQuickAccessItemsView();

        return binding.getRoot();
    }


    private void initQuickAccessItemsView() {
        // Calculate number of columns
        int columnNumber = 3;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            columnNumber = 5;
        }

        binding.quickAccessItemsGridView.setNumColumns(columnNumber);
        binding.quickAccessItemsGridView.setAdapter(new QuickAccessItemsAdapter(getContext(), () -> {}));
    }
}

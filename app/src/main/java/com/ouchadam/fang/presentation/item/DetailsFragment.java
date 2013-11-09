package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.frankiesardo.icepick.bundle.Bundles;
import com.novoda.notils.caster.Classes;
import com.ouchadam.fang.presentation.panel.SlidingPanelExposer;

public class DetailsFragment extends Fragment {

    private SlidingPanelExposer panelController;

    public static DetailsFragment newInstance(long itemId) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("itemId", itemId);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        panelController = Classes.from(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long itemId = getArguments().getLong("itemId");
        // todo query with id;
    }
}

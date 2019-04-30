package msr.zerone.tourhelper.cameraandgallery;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import msr.zerone.tourhelper.R;

import static msr.zerone.tourhelper.cameraandgallery.GalleryFragment.pos;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullImageViewFragment extends Fragment {
    private ViewPager viewPager;

    private int position;

    public FullImageViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_image_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = view.findViewById(R.id.view_pager);

        ImageCollection collection = new ImageCollection();


        FullImageViewAdapter adapter = new FullImageViewAdapter(getContext(),collection.matches());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(pos);

    }

}

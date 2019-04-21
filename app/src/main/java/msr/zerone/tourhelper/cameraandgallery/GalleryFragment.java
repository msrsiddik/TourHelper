package msr.zerone.tourhelper.cameraandgallery;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FilenameFilter;

import msr.zerone.tourhelper.FragmentInter;
import msr.zerone.tourhelper.R;

import static msr.zerone.tourhelper.MainActivity.currentPhotoPath;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment {
    private ImageView emptyImage;
    private GridView photo_gridview;
    private ViewPager view_pagerGa;

    public GalleryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        photo_gridview = view.findViewById(R.id.photo_gridview);
        emptyImage = view.findViewById(R.id.emptyImage);
        view_pagerGa = view.findViewById(R.id.view_pagerGa);

        ImageCollection collection = new ImageCollection();
        final File[] photos = collection.matches();

//        if (matches.length > 0) {
            emptyImage.setVisibility(View.INVISIBLE);
            PhotoGridViewAdapter adapter = new PhotoGridViewAdapter(getContext(), photos);
            photo_gridview.setAdapter(adapter);
//        }

        photo_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
//                Toast.makeText(getContext(), "Grid Item " + (position + 1) + " Selected", Toast.LENGTH_LONG).show();
                FragmentInter inter = (FragmentInter) getActivity();
                inter.gotoFullImageViewFragment();
            }
        });

    }

}

interface SelectPosition{
    void select(int pos);
}

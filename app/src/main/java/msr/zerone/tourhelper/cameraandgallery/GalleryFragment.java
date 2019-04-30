package msr.zerone.tourhelper.cameraandgallery;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import msr.zerone.tourhelper.FragmentInter;
import msr.zerone.tourhelper.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class GalleryFragment extends Fragment{
    private ImageView emptyImage;
    private GridView photo_gridview;
    private SwipeRefreshLayout refreshGallery;

    private ImageCollection collection;
    private File[] photos;
    private File deleteItem;

    public static int pos;

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
        refreshGallery = view.findViewById(R.id.refreshGallery);

//        collection = new ImageCollection();
//        photos = collection.matches();

        gridSetItem();

        refreshGallery.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                gridSetItem();
                refreshGallery.setRefreshing(false);
            }
        });

        photo_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                pos = position;

                FragmentInter inter = (FragmentInter) getActivity();
                inter.gotoFullImageViewFragment();
            }
        });

        photo_gridview.setLongClickable(true);
        photo_gridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getContext(), "Delete "+(position+1), Toast.LENGTH_SHORT).show();
                registerForContextMenu(photo_gridview);
                deleteItem = photos[position];
                return false;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        gridSetItem();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select the Action");
        menu.add(0,v.getId(),0,"Delete (Only local storage)");
        menu.add(0,v.getId(),0,"Delete (local and cloud storage)");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        CharSequence i = item.getTitle();
        if (i.equals("Delete (Only local storage)")) {
            deleteItem.delete();
            gridSetItem();
        }else if (i.equals("Delete (local and cloud storage)")){
            collection.deletePhoto(deleteItem, getContext());
            deleteItem.delete();
            gridSetItem();
        }
        return true;
    }

    private void gridSetItem(){
        collection = new ImageCollection();
        photos = collection.matches();
        if (photos.length == 0) {
            emptyImage.setVisibility(View.VISIBLE);
        }else {
            emptyImage.setVisibility(View.GONE);
            PhotoGridViewAdapter adapter = new PhotoGridViewAdapter(getContext(), photos);
            photo_gridview.setAdapter(adapter);
        }
    }

}

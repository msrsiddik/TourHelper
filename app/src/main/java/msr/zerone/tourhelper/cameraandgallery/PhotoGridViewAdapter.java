package msr.zerone.tourhelper.cameraandgallery;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class PhotoGridViewAdapter extends BaseAdapter {
    private Context mContext;
    private File[] matches;


    public PhotoGridViewAdapter(Context c, File[] matches) {
        mContext = c;
        this.matches = matches;
    }

    public int getCount() {
        return matches.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView mImageView;

        Uri uri = Uri.fromFile(matches[position]);

        if (convertView == null) {
            mImageView = new ImageView(mContext);
            mImageView.setLayoutParams(new GridView.LayoutParams(350, 350));
            mImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageView.setPadding(16, 16, 16, 16);
        } else {
            mImageView = (ImageView) convertView;
        }
        Picasso.get().load(uri).resize(150,150).into(mImageView);
//        mImageView.setImageURI(uri);
        return mImageView;
    }
}
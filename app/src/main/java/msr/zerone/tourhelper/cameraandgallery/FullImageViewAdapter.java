package msr.zerone.tourhelper.cameraandgallery;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class FullImageViewAdapter extends PagerAdapter{
    private Context context;
    private File[] matches;
    private int select;

    public FullImageViewAdapter(Context context, File[] matches) {
        this.context = context;
        this.matches = matches;
    }

    @Override
    public int getCount() {
        return matches.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        Uri uri = Uri.fromFile(matches[position]);
        Picasso.get().load(uri).fit().centerCrop().into(imageView);
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

}

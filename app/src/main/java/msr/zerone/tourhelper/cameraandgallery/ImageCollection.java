package msr.zerone.tourhelper.cameraandgallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;

public class ImageCollection {

    private File f = null;

    public ImageCollection() {
    }

    public File[] matches(){
        File[] matches = null;
        try {
            f = new File(Environment.getExternalStorageDirectory()+"/Android/data/msr.zerone.tourhelper/files/Pictures");

            matches = f.listFiles(new FilenameFilter()
            {
                public boolean accept(File dir, String name)
                {
                    return name.endsWith(".jpg") || name.endsWith(".jpeg");
                }
            });

        } catch(Exception e) {
            // if any error occurs
            e.printStackTrace();
        }
        return matches;
    }
}

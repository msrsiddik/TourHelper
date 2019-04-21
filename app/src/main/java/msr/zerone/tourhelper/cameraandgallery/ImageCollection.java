package msr.zerone.tourhelper.cameraandgallery;

import android.os.Environment;

import java.io.File;
import java.io.FilenameFilter;

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
                    return name.endsWith(".jpg");
                }
            });

        } catch(Exception e) {
            // if any error occurs
            e.printStackTrace();
        }
        return matches;
    }
}

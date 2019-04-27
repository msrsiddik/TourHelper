package msr.zerone.tourhelper.cameraandgallery;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FilenameFilter;

import static msr.zerone.tourhelper.THfirebase.fAuth;
import static msr.zerone.tourhelper.THfirebase.photoReference;

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

    public void uploadPhoto(final Context context){
        for(File file : matches()) {
            Uri uri = Uri.fromFile(file);
            StorageReference photoRef = photoReference.child(fAuth.getUid() +"/" + uri.getLastPathSegment());
            UploadTask uploadTask = photoRef.putFile(uri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(context, "Complete", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    public void deletePhoto(File file, final Context context){
        Uri uri = Uri.fromFile(file);
        StorageReference desertRef = photoReference.child(fAuth.getUid() +"/" + uri.getLastPathSegment());
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context, "Delete Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

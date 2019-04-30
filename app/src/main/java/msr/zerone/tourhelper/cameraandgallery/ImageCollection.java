package msr.zerone.tourhelper.cameraandgallery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;

import static com.google.firebase.auth.FirebaseAuth.getInstance;
import static msr.zerone.tourhelper.THfirebase.allPhotosReference;
import static msr.zerone.tourhelper.THfirebase.fAuth;
import static msr.zerone.tourhelper.THfirebase.fDatabase;
import static msr.zerone.tourhelper.THfirebase.fUser;
import static msr.zerone.tourhelper.THfirebase.photoReference;

public class ImageCollection {

    private File f = null;
    private boolean shouldRun = true;

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

        for(final File file : matches()) {
            final Uri uri = Uri.fromFile(file);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),bmOptions);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 30, baos);
            byte[] data = baos.toByteArray();

            StorageReference photoRef = photoReference.child(fAuth.getUid() +"/" + uri.getLastPathSegment());
//            UploadTask uploadTask = photoRef.putBytes(uri);
            UploadTask uploadTask = photoRef.putBytes(data);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Toast.makeText(context, "Complete", Toast.LENGTH_SHORT).show();
//                    savePhotoRef(uri.getLastPathSegment());
                }
            });
        }
    }

    private void savePhotoRef(String photoName){
        String uid = fAuth.getCurrentUser().getUid();
        String id = allPhotosReference.push().getKey();
        PhotoRefModel refModel = new PhotoRefModel(id, photoName);
        allPhotosReference.child(uid).child(id).setValue(refModel);
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

    public void syncPhotos(){

//        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    throw task.getException();
//                }
//
//                // Continue with the task to get the download URL
//                return ref.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    Uri downloadUri = task.getResult();
//                } else {
//                    // Handle failures
//                    // ...
//                }
//            }
//        });

    }

    class PhotoRefModel{
        private String id, photoName;

        public PhotoRefModel() {
        }

        public PhotoRefModel(String id, String photoName) {
            this.id = id;
            this.photoName = photoName;
        }

        public String getId() {
            return id;
        }

        public void setUid(String id) {
            this.id = id;
        }

        public String getPhotoName() {
            return photoName;
        }

        public void setPhotoName(String photoName) {
            this.photoName = photoName;
        }
    }
}

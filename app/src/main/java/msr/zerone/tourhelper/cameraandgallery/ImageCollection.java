package msr.zerone.tourhelper.cameraandgallery;

import android.app.DownloadManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import msr.zerone.tourhelper.R;

import static msr.zerone.tourhelper.THfirebase.allPhotosReference;
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
                    Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show();
                }
            }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    savePhotoRef(fAuth.getUid(), uri.getLastPathSegment().replace(".jpg",""));
                    Toast.makeText(context, "Upload Complete", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void savePhotoRef(String uid, String photoName){
        String id = photoName;
        PhotoRefModel refModel = new PhotoRefModel(photoName);
        allPhotosReference.child(uid).child(id).setValue(refModel);
    }

    public void deletePhoto(File file, final Context context){
        final Uri uri = Uri.fromFile(file);
        StorageReference desertRef = photoReference.child(fAuth.getUid() +"/" + uri.getLastPathSegment());
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                String photoName = uri.getLastPathSegment().replace(".jpg","");
                allPhotosReference.child(fAuth.getUid()).child(photoName).removeValue();

                Toast.makeText(context, "Delete Success", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void syncPhoto(final Context context){
        final List<PhotoRefModel> photoRefModelList = new ArrayList<>();
        allPhotosReference.child(fAuth.getUid()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PhotoRefModel photoRefModel = dataSnapshot.getValue(PhotoRefModel.class);
                photoRefModelList.add(photoRefModel);

                final String photoName = photoRefModel.getPhotoName();

                photoReference.child(fAuth.getUid()).child(photoName+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        downloadPhoto(context, photoName, ".jpg",
                                "/Pictures", uri.toString());
                    }
                });

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void downloadPhoto(Context context, String photoName, String photoExtension, String photoSaveDirectory, String url){
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, photoSaveDirectory, photoName+photoExtension);

        downloadManager.enqueue(request);
    }

}

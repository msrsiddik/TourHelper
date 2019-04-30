package msr.zerone.tourhelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class THfirebase {
    public static FirebaseAuth fAuth = FirebaseAuth.getInstance();
    public static FirebaseUser fUser = fAuth.getCurrentUser();

    public static FirebaseDatabase fDatabase = FirebaseDatabase.getInstance();
    public static DatabaseReference userReference = fDatabase.getReference("AppUser");
    public static DatabaseReference eventReference = fDatabase.getReference("Event");
    public static DatabaseReference budgetReference = fDatabase.getReference("Budget");
    public static DatabaseReference costReference = fDatabase.getReference("Cost");
    public static DatabaseReference allPhotosReference = fDatabase.getReference("PhotoRef");

    public static FirebaseStorage fStorage = FirebaseStorage.getInstance();
    public static StorageReference photoReference = fStorage.getReference("Photos");
}

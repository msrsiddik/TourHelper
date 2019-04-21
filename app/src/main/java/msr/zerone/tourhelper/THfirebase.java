package msr.zerone.tourhelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class THfirebase {
    public static final FirebaseDatabase FIREBASE_DATABASE = FirebaseDatabase.getInstance();
    public static final DatabaseReference APP_USER_REFERENCE = FIREBASE_DATABASE.getReference("AppUser");
    public static final DatabaseReference EVENT_REFERENCE = FIREBASE_DATABASE.getReference("Event");
    public static final FirebaseAuth FIREBASE_AUTH = FirebaseAuth.getInstance();
    public static final FirebaseUser FIREBASE_USER = FirebaseAuth.getInstance().getCurrentUser();
}

package msr.zerone.tourhelper.userfragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import msr.zerone.tourhelper.FragmentInter;
import msr.zerone.tourhelper.R;
import msr.zerone.tourhelper.THfirebase;
import msr.zerone.tourhelper.userfragment.model.RegistrationModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private TextInputLayout loginInputEmailId, loginInputPassId;
    private Button loginBtnId, signUpBtnId;

//    private FirebaseDatabase database;
//    private DatabaseReference reference;
//    private DatabaseReference userRef;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loginInputEmailId = view.findViewById(R.id.loginInputEmailId);
        loginInputPassId = view.findViewById(R.id.loginInputPassId);
        loginBtnId = view.findViewById(R.id.loginBtnId);
        signUpBtnId = view.findViewById(R.id.signUpBtnId);

//        database = FirebaseDatabase.getInstance();
//        reference = database.getReference("AppUser");

        loginBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                THfirebase.APP_USER_REFERENCE.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        RegistrationModel model = dataSnapshot.getValue(RegistrationModel.class);
                        Log.e("ValueEventListener", "onDataChange: "+model.getEmail());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("DatabaseError", "onDataChange: "+databaseError.getMessage());

                    }
                });
            }
        });

        signUpBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentInter fragmentInter = (FragmentInter) getActivity();
                fragmentInter.gotoRegistrationFragment();
            }
        });
    }
}

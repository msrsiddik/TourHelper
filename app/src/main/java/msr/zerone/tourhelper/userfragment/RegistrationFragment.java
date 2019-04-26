package msr.zerone.tourhelper.userfragment;


import android.content.Context;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import msr.zerone.tourhelper.R;
import msr.zerone.tourhelper.userfragment.model.RegistrationModel;

import static android.support.constraint.Constraints.TAG;
import static msr.zerone.tourhelper.THfirebase.fAuth;
import static msr.zerone.tourhelper.THfirebase.fUser;
import static msr.zerone.tourhelper.THfirebase.userReference;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegistrationFragment extends Fragment {
    private Context context;
    private TextInputLayout fullNameInputId, regiEmailInputId, regiPhoneNumberInputId, regiPassInputId;
    private Button registrationBtnId;

    public RegistrationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        FirebaseApp.initializeApp(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fullNameInputId = view.findViewById(R.id.fullNameInputId);
        regiEmailInputId = view.findViewById(R.id.regiEmailInputId);
        regiPhoneNumberInputId = view.findViewById(R.id.regiPhoneNumberInputId);
        regiPassInputId = view.findViewById(R.id.regiPassInputId);
        registrationBtnId = view.findViewById(R.id.registrationBtnId);

        registrationBtnId.setOnClickListener(registationListener);

    }

    View.OnClickListener registationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = fullNameInputId.getEditText().getText().toString();
            String phone = regiPhoneNumberInputId.getEditText().getText().toString();
            String email = regiEmailInputId.getEditText().getText().toString();
            String pass = regiPassInputId.getEditText().getText().toString();

            final RegistrationModel model = new RegistrationModel(name, phone, email, pass);

            if (!name.isEmpty() && !phone.isEmpty() && !email.isEmpty() && !pass.isEmpty() ) {
                fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isComplete()) {
                            Log.e(TAG, "onComplete: ");
                            fUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (!fUser.getUid().isEmpty()) {
                                String id = fUser.getUid();
                                userReference.child(id).setValue(model);
                            }
                        }
                        else {
                            Log.e(TAG, "Not Complete: ");
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, ""+e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };

}

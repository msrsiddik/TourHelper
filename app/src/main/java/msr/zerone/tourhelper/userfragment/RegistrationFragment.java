package msr.zerone.tourhelper.userfragment;

import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import msr.zerone.tourhelper.FragmentInter;
import msr.zerone.tourhelper.R;
import msr.zerone.tourhelper.networkinfo.NetworkUtil;
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
    private Button pickFromGallery, registrationBtnId;

    int GALLERY_REQUEST_CODE = 1;

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
        NetworkUtil.isOffline(context, container);

        return inflater.inflate(R.layout.fragment_registration, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fullNameInputId = view.findViewById(R.id.fullNameInputId);
        regiEmailInputId = view.findViewById(R.id.regiEmailInputId);
        regiPhoneNumberInputId = view.findViewById(R.id.regiPhoneNumberInputId);
        regiPassInputId = view.findViewById(R.id.regiPassInputId);
        pickFromGallery = view.findViewById(R.id.pickFromGallery);
        registrationBtnId = view.findViewById(R.id.registrationBtnId);

        pickFromGallery.setOnClickListener(pickFromGalleryListener);
        registrationBtnId.setOnClickListener(registationListener);

    }

    View.OnClickListener pickFromGalleryListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                String[] mimeTypes = {"image/jpeg", "image/png"};
                intent.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
                startActivityForResult(intent,GALLERY_REQUEST_CODE);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    View.OnClickListener registationListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String name = fullNameInputId.getEditText().getText().toString();
            String phone = regiPhoneNumberInputId.getEditText().getText().toString();
            String email = regiEmailInputId.getEditText().getText().toString();
            String pass = regiPassInputId.getEditText().getText().toString();

            final RegistrationModel model = new RegistrationModel(name, email, phone, pass);

            if (!name.isEmpty() && !phone.isEmpty() && !email.isEmpty() && !pass.isEmpty() ) {
                fAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch(FirebaseAuthWeakPasswordException e) {
                                regiPassInputId.setError("greater than 6 character");
                                regiPassInputId.requestFocus();
                            } catch(FirebaseAuthInvalidCredentialsException e) {
                                regiEmailInputId.setError("Enter valid email");
                                regiEmailInputId.requestFocus();
                            } catch(FirebaseAuthUserCollisionException e) {
                                regiEmailInputId.setError("Already Register User");
                                regiEmailInputId.requestFocus();
                            } catch(Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                        else if (task.isComplete()){
                            if (!fAuth.getUid().isEmpty()) {
                                fUser = FirebaseAuth.getInstance().getCurrentUser();
                                String id = fUser.getUid();
                                userReference.child(id).setValue(model);
                                FragmentInter inter = (FragmentInter) getActivity();
                                inter.gotoDashboardFragment();
                                inter.login(true);
                            }
                        }
                    }
                });
            }else {
                Toast.makeText(context, "All filed fill up", Toast.LENGTH_SHORT).show();
            }
        }
    };

}

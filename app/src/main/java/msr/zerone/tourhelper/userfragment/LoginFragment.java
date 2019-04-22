package msr.zerone.tourhelper.userfragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

import msr.zerone.tourhelper.FragmentInter;
import msr.zerone.tourhelper.R;

import static msr.zerone.tourhelper.THfirebase.fAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private TextInputLayout loginInputEmailId, loginInputPassId;
    private Button loginBtnId, signUpBtnId;

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

        loginBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginInputEmailId.getEditText().getText().toString();
                String pass = loginInputPassId.getEditText().getText().toString();
                fAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FragmentInter inter = (FragmentInter) getActivity();
                            inter.login(true);
                            inter.gotoDashboardFragment();
                            Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                        }
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

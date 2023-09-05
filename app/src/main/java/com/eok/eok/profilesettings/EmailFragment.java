package com.eok.eok.profilesettings;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;


import com.eok.eok.MainScreen;
import com.eok.eok.ProfileSetting;
import com.eok.eok.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class EmailFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    EditText password;
    EditText newEmail;
    EditText againEmail;

    boolean check = false;//Validate Password Method

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_email, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button changeButton = view.findViewById(R.id.changeEmailButton);
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeEmail(view);
            }
        });
        AppCompatImageView backButton = view.findViewById(R.id.imageView4);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back(view);
            }
        });
        password=view.findViewById(R.id.passwordEmailText);
        newEmail=view.findViewById(R.id.newEmailText);
        againEmail=view.findViewById(R.id.againEmailText);

    }

    public void changeEmail(View view){
        String passwordString = password.getText().toString();
        String newEmailString = newEmail.getText().toString();
        String againEmailString = againEmail.getText().toString();
        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(passwordString.equals("")||newEmailString.equals("")||againEmailString.equals("")){
            Toast.makeText(getContext(), "Please Enter Password and Email", Toast.LENGTH_LONG).show();
        }
        else if(!validatePassword()){
            Toast.makeText(getContext(), "Your password does not match", Toast.LENGTH_LONG).show();
        }
        else if(!newEmailString.equals(againEmailString)){
            Toast.makeText(getContext(), "New emails do not match", Toast.LENGTH_LONG).show();
        }
        else if(!EmailFragment.isValidEmail(newEmailString)){
            Toast.makeText(getContext(), "Invalid email", Toast.LENGTH_LONG).show();
        }
        else{
            user.updateEmail(newEmailString);
            HashMap<String,Object> iconData = new HashMap<>();
            iconData.put("email",newEmail);
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            DocumentReference documentReference = firebaseFirestore.collection("Users").document(user.getUid());
            documentReference.update(iconData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Intent intent = new Intent(getActivity(), MainScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            });
            Toast.makeText(getContext(), "Successfully changed", Toast.LENGTH_LONG).show();
            Intent intent=new Intent(getContext(), MainScreen.class);
            startActivity(intent);
        }

    }
    public void back(View v)
    {
        Intent intent = new Intent(getContext(), ProfileSetting.class);
        startActivity(intent);

    }

    private boolean validatePassword(){
        String oldPasswordString =  password.getText().toString();
        FirebaseUser user = firebaseAuth.getCurrentUser();


        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail().toString(),oldPasswordString);
        user.reauthenticate(credential).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                check=true;
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                check=false;
            }
        });
        return  check;
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}
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
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;
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

public class NameFragment extends Fragment {

        FirebaseAuth firebaseAuth;

        EditText newEmail;

        boolean check = false;//Validate Password Method

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth=FirebaseAuth.getInstance();
    }

        public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragmen_name, container, false);
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

        newEmail=view.findViewById(R.id.newName);


    }
    public void back(View v)
    {
        Intent intent = new Intent(getContext(), ProfileSetting.class);
        startActivity(intent);

    }

        public void changeEmail(View view){

        String newEmailString = newEmail.getText().toString();

        FirebaseUser user = firebaseAuth.getCurrentUser();

        if(newEmailString.equals("")){
            Toast.makeText(getContext(), "Please enter a valid username", Toast.LENGTH_LONG).show();
        }

        else{
            HashMap<String,Object> iconData = new HashMap<>();
            iconData.put("name",newEmailString);
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





}

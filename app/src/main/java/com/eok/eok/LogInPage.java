package com.eok.eok;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.eok.eok.Models.User;
import com.eok.eok.databinding.ActivityMainBinding;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class LogInPage extends AppCompatActivity {

	private ActivityMainBinding binding;
	private FirebaseAuth auth;
	private FirebaseFirestore fstore;
	private Switch simpleSwitch;
	TextView textView;
	static boolean switchState;
	private static final int RC_SIGN_IN = 100;
	private GoogleSignInClient googleSignInClient;

	@Override
	protected void onStart() {
		super.onStart();
		SharedPreferences preferences = getSharedPreferences("switch",MODE_PRIVATE);
		boolean sign = preferences.getBoolean("remember",false);
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		System.out.println(user!= null);
		System.out.println(sign);
		if(user != null && sign == true)
		{
			checkIsAdmin(auth.getCurrentUser().getUid());
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {


		System.out.println(isTabletDevice());
		super.onCreate(savedInstanceState);
		binding = ActivityMainBinding.inflate(getLayoutInflater());
		View view = binding.getRoot();
		setContentView(view);


		if (isTabletDevice()) {

			binding.description.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));
			binding.loginButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));
			binding.switch1.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));
			binding.donTHaveAnAccountJoinUs.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));
			binding.welcomeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.welcometext_size_high_resolution));
			binding.emailText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));
			binding.passwordText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));
			binding.emailText.setPadding((int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh));
			binding.passwordText.setPadding((int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh),(int)getResources().getDimension(R.dimen.paddingh));
			binding.signInWithGoogle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_high_resolution));


		} else {

			binding.description.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_low_resolution));
			binding.loginButton.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_low_resolution));
			binding.switch1.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_low_resolution));
			binding.donTHaveAnAccountJoinUs.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_low_resolution));
			binding.welcomeText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.welcometext_size_low_resolution));
			binding.emailText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_low_resolution));
			binding.passwordText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_low_resolution));
			binding.emailText.setPadding((int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding));
			binding.passwordText.setPadding((int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding),(int)getResources().getDimension(R.dimen.padding));
			binding.signInWithGoogle.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_size_low_resolution));



		}


		auth = FirebaseAuth.getInstance();
		fstore = FirebaseFirestore.getInstance();


		simpleSwitch = (Switch) findViewById(binding.switch1.getId());
		simpleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					switchState = simpleSwitch.isChecked();

					SharedPreferences preferences = getSharedPreferences("switch",MODE_PRIVATE);
					SharedPreferences.Editor editor = preferences.edit();
					editor.putBoolean("remember",switchState);
					editor.apply();

			}
		});
		GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();

		googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);



		FirebaseUser user = auth.getCurrentUser();

		ImageView image = binding.imageView2;
		image.setImageResource(R.drawable.visibleeye);
		image.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(binding.passwordText.getTransformationMethod().equals(HideReturnsTransformationMethod.getInstance()))
				{
					binding.passwordText.setTransformationMethod(PasswordTransformationMethod.getInstance());
					image.setImageResource(R.drawable.visibleeye);

				}
				else
				{
					binding.passwordText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					image.setImageResource(R.drawable.indir);
				}
			}
		});


	}

	public void google(View v)
	{
		Intent intent = googleSignInClient.getSignInIntent();
		startActivityForResult(intent,RC_SIGN_IN);
	}

	public void signUpClicked(View view)
	{
		Intent intent = new Intent(LogInPage.this , Register.class);
		startActivity(intent);
		finish();

	}
	public void signInClicked(View view)
	{
		String email = binding.emailText.getText().toString();
		String password = binding.passwordText.getText().toString();

		if(email.equals("") || password.equals("")){
			Toast.makeText(this ,"Password or email cannot be empty !",Toast.LENGTH_LONG).show();
		}
		else {
			auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
				@Override
				public void onSuccess(AuthResult authResult) {
					checkIsAdmin(authResult.getUser().getUid());
				}
			}).addOnFailureListener(new OnFailureListener() {
				@Override
				public void onFailure(@NonNull Exception e) {
					Toast.makeText(LogInPage.this,e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
				}
			});
		}
	}
	private void checkIsAdmin(String uid)
	{

		DocumentReference df = fstore.collection("Users").document(uid);
		df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
			@Override
			public void onSuccess(DocumentSnapshot documentSnapshot) {
				Log.d("TAG","onSuccess: "+documentSnapshot.getData());

				if((long)documentSnapshot.get("isAdmin") == 1)
				{
					Intent intent = new Intent(LogInPage.this , AdminPanel.class);
					startActivity(intent);
					finish();
				}
				else {
					Intent intent = new Intent(LogInPage.this , MainScreen.class);
					startActivity(intent);
					finish();
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == RC_SIGN_IN)
		{
			Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				GoogleSignInAccount account = accountTask.getResult(ApiException.class);
				firebaseAuthWithGoogleAccount(account);


			}catch (Exception e)
			{

			}
		}
	}
	public void firebaseAuthWithGoogleAccount(GoogleSignInAccount account){
		AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(),null);
		auth.signInWithCredential(credential)
				.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
					@Override
					public void onSuccess(AuthResult authResult) {
						FirebaseUser firebaseUser = auth.getCurrentUser();
						String uid = firebaseUser.getUid();
						String email = firebaseUser.getEmail();
						String name = "";

						for (int i = 0; i < email.length(); i++) {
							if(email.charAt(i)=='@')
							{
								name = email.substring(0,i);
							}
						}

						if(authResult.getAdditionalUserInfo().isNewUser())
						{
							DocumentReference df = fstore.collection("Users").document(uid);


							Map<String,Object> userInfo = new HashMap<>();
							userInfo.put("name",name);
							userInfo.put("email",email);
							userInfo.put("hotPursuitRecord",0);
							userInfo.put("timeRushRecord",0);
							userInfo.put("isAdmin",0);
							userInfo.put("userPhotoUrl","default");


							df.set(userInfo);
						}
						else{

						}

						checkIsAdmin(uid);
					}
				}).addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {

					}
				});
	}
	public boolean isTabletDevice() {
		int screenSize = Configuration.SCREENLAYOUT_SIZE_MASK &
				getResources().getConfiguration().screenLayout;

		return screenSize == Configuration.SCREENLAYOUT_SIZE_LARGE ||
				screenSize == Configuration.SCREENLAYOUT_SIZE_XLARGE;
	}
}
package com.group18.android.reminderapplication

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.group18.android.reminderapplication.databinding.ActivitySignInBinding

class SignInActivity : AppCompatActivity() {
	private lateinit var binding: ActivitySignInBinding
	private val signIn: ActivityResultLauncher<Intent> =
		registerForActivityResult(FirebaseAuthUIActivityResultContract(), this::onSignInResult)

	override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
		super.onCreate(savedInstanceState, persistentState)

		// Uses view binding
		binding = ActivitySignInBinding.inflate(layoutInflater)
		setContentView(binding.root)
	}

	public override fun onStart() {
		super.onStart()

		if (Firebase.auth.currentUser == null) {
			val signInIntent = AuthUI.getInstance()
				.createSignInIntentBuilder()
				.setLogo(R.mipmap.ic_launcher)
				.setAvailableProviders(listOf(
					AuthUI.IdpConfig.EmailBuilder().build(),
					AuthUI.IdpConfig.GoogleBuilder().build()
				))
				.build()

			signIn.launch(signInIntent)
		} else {
			startActivity(Intent(this, MainActivity::class.java))
			finish()
		}
	}

	private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
		if (result.resultCode == RESULT_OK) {
			Log.d(TAG, "Sign-in successful")
		} else {
			Toast.makeText(this, "An error occurred while signing in!", Toast.LENGTH_LONG).show()

			val response = result.idpResponse
			if (response == null) {
				Log.w(TAG, "Sign-in canceled")
			} else {
				Log.w("Sign-in error!", response.error)
			}
		}
	}

	companion object {
		private const val TAG = "SignInActivity"
	}
}
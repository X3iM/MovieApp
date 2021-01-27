package com.android.hackathon.movieapp.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.hackathon.movieapp.R
import com.google.firebase.auth.FirebaseAuth

class LogInActivity : AppCompatActivity() {

    private lateinit var rightToLeftAnimation: Animation
    private lateinit var fromBottomAnimation: Animation
    private lateinit var auth: FirebaseAuth
    private lateinit var toolBar: Toolbar
    private lateinit var chatName: TextView
    private lateinit var emailEditText: EditText
    private lateinit var passEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var logInButton: Button
    private lateinit var toggleLoginSingUpTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        auth = FirebaseAuth.getInstance()
        emailEditText = findViewById(R.id.emailEditText)
        passEditText = findViewById(R.id.passEditText)
        nameEditText = findViewById(R.id.nameEditText)
        logInButton = findViewById(R.id.logInButton)
        toolBar = findViewById(R.id.toolBar)
        chatName = findViewById(R.id.textView)
        fromBottomAnimation = AnimationUtils.loadAnimation(this, R.anim.frombottom)
        logInButton.startAnimation(fromBottomAnimation)
        rightToLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.righttoleft)
        toolBar.animation = rightToLeftAnimation
        chatName.animation = rightToLeftAnimation
        emailEditText.animation = rightToLeftAnimation
        passEditText.animation = rightToLeftAnimation
        nameEditText.animation = rightToLeftAnimation

        toggleLoginSingUpTextView = findViewById(R.id.toggleLoginLogInTextView)
        toggleLoginSingUpTextView.setOnClickListener{
            startActivity(Intent(applicationContext, SignUpActivity::class.java))
            finish()
        }

        logInButton.setOnClickListener(View.OnClickListener {
            val email = emailEditText.text.toString()
            val userName = nameEditText.text.toString()
            val password = passEditText.text.toString()
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(
                    password
                )
            ) {
                logIn(email, password)
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Authentication success", Toast.LENGTH_LONG).show()
                val user = auth.currentUser
                val intent = Intent(this, ProfileActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
                finish()
                //                            updateUI(user);
            } else {
                Toast.makeText(this, "Authentication failed.", Toast.LENGTH_SHORT).show()
                //                            updateUI(null);
                // ...
            }

            // ...
        }
    }
}
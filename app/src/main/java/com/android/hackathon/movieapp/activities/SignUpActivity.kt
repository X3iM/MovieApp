package com.android.hackathon.movieapp.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.android.hackathon.movieapp.R
import com.android.hackathon.movieapp.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var emailEditText: EditText
    private lateinit var passEditText: EditText
    private lateinit var checkPassEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var toggleLoginSingUpTextView: TextView
    private lateinit var signUpButton: Button
    private lateinit var toolbar: Toolbar
    private lateinit var mProgressBar: ProgressBar
    private lateinit var database: FirebaseDatabase
    private lateinit var usersDBReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        toolbar = findViewById(R.id.sign_up_toolbar)
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "Create account"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mProgressBar = findViewById(R.id.sign_up_progress_bar)
        mProgressBar.visibility = View.GONE
        emailEditText = findViewById(R.id.emailEditText)
        passEditText = findViewById(R.id.passEditText)
        checkPassEditText = findViewById(R.id.checkPassEditText)
        nameEditText = findViewById(R.id.nameEditText)

        toggleLoginSingUpTextView = findViewById(R.id.toggleLoginSignUpTextView)
        toggleLoginSingUpTextView.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
        }

        signUpButton = findViewById(R.id.signUpButton)
        signUpButton.setBackgroundColor(-0x9a9d6)
        signUpButton.setOnClickListener(View.OnClickListener {
            val email = emailEditText.text.toString()
            val userName = nameEditText.text.toString()
            val password = passEditText.text.toString()
            val checkPassword = checkPassEditText.text.toString()
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(userName)) {
                if (password.trim { it <= ' ' }.length < 7) Toast.makeText(
                    this@SignUpActivity,
                    "Password mast by at least 7 characters",
                    Toast.LENGTH_LONG
                ).show() else if (password == checkPassword) {
                    mProgressBar.visibility = View.VISIBLE
                    loginSignUpUser(email, password.trim { it <= ' ' })
                } else {
                    Toast.makeText(this, "Password don't match", Toast.LENGTH_LONG)
                        .show()
                }
            } else {
                Toast.makeText(
                    this,
                    "Please add your username and E-mail",
                    Toast.LENGTH_LONG
                ).show()
            }
        })

//        if (auth.getCurrentUser() != null)
//            startActivity(new Intent(SignUpActivity.this, UserListActivity.class));
    }

    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, ProfileActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()
        }
    }

    private fun loginSignUpUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    mProgressBar.visibility = View.GONE
                    Toast.makeText(
                        this,
                        "Authentication success.",
                        Toast.LENGTH_LONG
                    )
                        .show()
                    val user = auth.currentUser
                    usersDBReference = database.reference.child("users").child(user!!.uid)
                    val intent = Intent(this, ProfileActivity::class.java)
                    intent.putExtra("userName", nameEditText.text.toString().trim { it <= ' ' })
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    createUser(user)
                    mProgressBar.visibility = View.GONE
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Authentication failed.", Toast.LENGTH_LONG)
                        .show()
                }

                // ...
            }
    }

    private fun createUser(fbUser: FirebaseUser?) {
        val user =
            fbUser?.email?.let { it ->
                User(
                    fbUser.uid,
                    nameEditText.text.toString().trim { it <= ' ' },
                    it
                )
            }
        usersDBReference.setValue(user)
    }
}
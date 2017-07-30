package com.decisionkitchen.decisionkitchen

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.facebook.*

import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*

import java.util.Arrays


class MainActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    internal var callbackManager: CallbackManager = CallbackManager.Factory.create()

    private fun loadData(user: FirebaseUser) {

        Toast.makeText(applicationContext, user.email + " " + user.uid,
                Toast.LENGTH_SHORT).show();

        Log.w("test", user.uid);

    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        val credential:AuthCredential = FacebookAuthProvider.getCredential(token.token);

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {

            override fun onComplete(task: Task<AuthResult>) {
                if (task.isSuccessful) {

                    Toast.makeText(applicationContext, "Google Authentication succeeded.",
                            Toast.LENGTH_SHORT).show();


                    val user:FirebaseUser? = mAuth.currentUser;

                    if(user != null) loadData(user);

                } else {

                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext, "Google Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val currentUser = mAuth.currentUser
        if (currentUser == null) {

            LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
                override fun onSuccess(loginResult: LoginResult) {
                    handleFacebookAccessToken(loginResult.accessToken)
                }

                override fun onCancel() {
                    applicationContext.startActivity(Intent(applicationContext, LoginActivity::class.java))
                }

                override fun onError(error: FacebookException) {
                    Toast.makeText(applicationContext, "FB Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    /* throw error */
                }
            })

            LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
        } else {
            loadData(currentUser);
        }

        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu items for use in the action bar
        val inflater = menuInflater
        inflater.inflate(R.menu.main_activity_actions, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle presses on the action bar items
        when (item.itemId) {
            R.id.action_search -> return true
            R.id.action_signout -> {
                LoginManager.getInstance().logOut()
                applicationContext.startActivity(Intent(applicationContext, LoginActivity::class.java))

                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    /** Called when the user touches the button  */
    fun goTest(view: View) {
        val intent:Intent = Intent(getBaseContext(), GroupActivity::class.java)
        intent.putExtra("GROUP_ID", "test")
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

}

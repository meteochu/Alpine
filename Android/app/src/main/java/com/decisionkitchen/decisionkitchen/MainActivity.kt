package com.decisionkitchen.decisionkitchen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import com.facebook.*

import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.*

import java.util.Arrays


class MainActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    internal var callbackManager: CallbackManager = CallbackManager.Factory.create()

    private var user:FirebaseUser? = null;

    private fun getActivity(): Activity {
        return this
    }

    private fun getContext(): Context {
        return this
    }

    private fun loadData(user: FirebaseUser?) {

        if (user == null) return
        Log.e("test", "test")
        val uid:String = user.uid
        val ref:DatabaseReference = FirebaseDatabase.getInstance().getReference("groups")
        ref.addListenerForSingleValueEvent( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var scrollView:ScrollView = getActivity().findViewById(R.id.groups) as ScrollView

                if (!snapshot.hasChildren()) {
                    val noChats: TextView = TextView(getActivity())
                    noChats.setText(R.string.no_groups_text)
                    noChats.setPadding(0, 20, 0, 0)
                    noChats.gravity = Gravity.CENTER
                    noChats.textSize = 20f

                    scrollView.removeAllViews()
                    scrollView.addView(noChats)

                } else {
                    val groups = snapshot.children
                    val layout = LinearLayout(getContext())
                    val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    layout.layoutParams = layoutParams
                    for (data in groups) {
                        val groupLayout = LinearLayout(layout.context)
                        groupLayout.orientation = LinearLayout.VERTICAL
                        val groupLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        groupLayout.layoutParams = groupLayoutParams
                        val title = TextView(layout.context)
                        val group = data.getValue(Group::class.java)
                        title.text = data.key
                        title.textSize = 20f
                        title.width
                        title.setTextColor(Color.BLACK)
                        title.setPadding(0, 5, 0, 15)
                        groupLayout.addView(title)
                        val password = TextView(layout.context)
                        password.text = group!!.password
                        password.textSize = 13f
                        password.setPadding(0, 5, 0, 15)
                        password.setTextColor(Color.GRAY)
                        groupLayout.addView(password)

                        val divider = LinearLayout(layout.context)
                        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2)
                        divider.setBackgroundColor(Color.BLACK)
                        divider.layoutParams = params
                        groupLayout.addView(divider)

                        layout.addView(groupLayout)
                        groupLayout.setOnClickListener(object : View.OnClickListener {
                            override fun onClick(v: View?) {
                                goTest(data.key)
                            }
                        })
                    }
                    scrollView.removeAllViews()
                    scrollView.addView(layout)
                }

                var linearLayout:LinearLayout = LinearLayout(getContext())

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error loading: ", error.toString())
            }

        })
    }

    private fun handleFacebookAccessToken(token: AccessToken) {

        val credential:AuthCredential = FacebookAuthProvider.getCredential(token.token);

        mAuth.signInWithCredential(credential).addOnCompleteListener(this, object : OnCompleteListener<AuthResult> {

            override fun onComplete(task: Task<AuthResult>) {
                if (task.isSuccessful) {

                    Toast.makeText(applicationContext, "Google Authentication succeeded.",
                            Toast.LENGTH_SHORT).show();


                    user = mAuth.currentUser;


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
    fun goTest(group: String) {
        val intent:Intent = Intent(getBaseContext(), GroupActivity::class.java)
        intent.putExtra("GROUP_ID", group)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }

}

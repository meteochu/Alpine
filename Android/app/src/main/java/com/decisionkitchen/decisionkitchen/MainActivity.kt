package com.decisionkitchen.decisionkitchen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import com.facebook.drawee.backends.pipeline.Fresco

import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.*
import com.google.firebase.database.*
import org.json.JSONObject

import java.util.Arrays


class MainActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    internal var callbackManager: CallbackManager = CallbackManager.Factory.create()

    private var user:FirebaseUser? = null

    private var mRecyclerView:RecyclerView? = null
    private var mAdapter:RecyclerView.Adapter<GroupAdapter.ViewHolder>? = null
    private var mLayoutManager:RecyclerView.LayoutManager? = null

    private fun getActivity(): Activity {
        return this
    }

    private fun getContext(): Context {
        return this
    }

    public fun getUser(): FirebaseUser? {
        return user;
    }

    private fun getMainActivity(): MainActivity {
        return this;
    }

    private fun loadData(user: FirebaseUser?) {

        if (user == null) return
        Log.e("test", "test")
        val uid:String = user.uid
        val ref:DatabaseReference = FirebaseDatabase.getInstance().getReference("groups")
        ref.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var groups:List<Group> = ArrayList<Group>()

                if (!snapshot.hasChildren())
                    return

                val data = snapshot.children
                for (point in data) {
                    val group = point.getValue(Group::class.java)
                    if (group != null) {
                        groups += group
                    }
                }

                mAdapter = GroupAdapter(groups, mRecyclerView, getMainActivity())
                mRecyclerView!!.adapter = mAdapter;

//                var scrollView:ScrollView = getActivity().findViewById(R.id.groups) as ScrollView
//
//                if (!snapshot.hasChildren()) {
//                    val noChats: TextView = TextView(getActivity())
//                    noChats.setText(R.string.no_groups_text)
//                    noChats.setPadding(0, 20, 0, 0)
//                    noChats.gravity = Gravity.CENTER
//                    noChats.textSize = 20f
//
//                    scrollView.removeAllViews()
//                    scrollView.addView(noChats)
//
//                } else {
//                    val groups = snapshot.children
//                    val layout = LinearLayout(getContext())
//                    val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//                    layout.layoutParams = layoutParams
//                    for (data in groups) {
//                        val groupLayout = LinearLayout(layout.context)
//                        groupLayout.orientation = LinearLayout.VERTICAL
//                        val groupLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
//                        groupLayout.layoutParams = groupLayoutParams
//                        val title = TextView(layout.context)
//
//                        val group = data.getValue(Group::class.java)
//                        title.text = group!!.name
//                        title.textSize = 20f
//                        title.width
//                        title.setTextColor(Color.BLACK)
//                        title.setPadding(0, 5, 0, 15)
//                        groupLayout.addView(title)
//
//                        val password = TextView(layout.context)
//                        password.text = group.password
//                        password.textSize = 13f
//                        password.setPadding(0, 5, 0, 15)
//                        password.setTextColor(Color.GRAY)
//                        groupLayout.addView(password)
//
//                        val divider = LinearLayout(layout.context)
//                        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 2)
//                        divider.setBackgroundColor(Color.BLACK)
//                        divider.layoutParams = params
//                        groupLayout.addView(divider)
//
//                        layout.addView(groupLayout)
//                        groupLayout.setOnClickListener(object : View.OnClickListener {
//                            override fun onClick(v: View?) {
//                                goTest(data.key)
//                            }
//                        })
//                    }
//                    scrollView.removeAllViews()
//                    scrollView.addView(layout)
//                }
//
//                var linearLayout:LinearLayout = LinearLayout(getContext())

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

                    Toast.makeText(applicationContext, "Facebook Authentication succeeded.",
                            Toast.LENGTH_SHORT).show()
                    user = mAuth.currentUser

                    loadData(user)

                    val ref = FirebaseDatabase.getInstance().getReference("users")
                    ref.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {
                            Log.e("error", p0.toString())

                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            val u = user!!
                            if (snapshot.hasChild(u.uid))
                                return

                            val request : GraphRequest = GraphRequest.newMeRequest(token, GraphRequest.GraphJSONObjectCallback { json, response -> run {
                                ref.child(u.uid).child("img").setValue(json.getJSONObject("picture").getJSONObject("data").get("url"))
                                ref.child(u.uid).child("id").setValue(u.uid)
                                ref.child(u.uid).child("email").setValue(u.email)
                                ref.child(u.uid).child("name").setValue(json.get("name"))
                                ref.child(u.uid).child("first_name").setValue(json.get("first_name"))
                                ref.child(u.uid).child("last_name").setValue(json.get("last_name"))
                            }});
                            val parameters : Bundle = Bundle();
                            parameters.putString("fields", "name, picture, first_name, last_name");
                            request.parameters = parameters;
                            request.executeAsync();

                        }

                    })

                } else {

                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext, "Google Authentication failed.",
                            Toast.LENGTH_SHORT).show()
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
            loadData(currentUser)
        }

        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        mRecyclerView = findViewById(R.id.my_recycler_view) as RecyclerView
        mRecyclerView!!.setHasFixedSize(true)

        val rParams:LinearLayout.LayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        mRecyclerView!!.layoutParams = rParams

        mLayoutManager = LinearLayoutManager(getContext())
        mRecyclerView!!.layoutManager = mLayoutManager

        mLayoutManager!!.setMeasuredDimension(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT)

        mAdapter = GroupAdapter(ArrayList<Group>(), mRecyclerView, getMainActivity())
        mRecyclerView!!.adapter = mAdapter
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

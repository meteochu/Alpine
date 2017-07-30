package com.decisionkitchen.decisionkitchen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth


class GroupActivity : Activity() {

    private fun getContext(): Context {
        return this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_group)

        val database = FirebaseDatabase.getInstance()
        val groupRef = database.getReference("groups/" + getIntent().getStringExtra("GROUP_ID"))

        val toolbar : Toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = "Loading..."

        val groupListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val group : Group = dataSnapshot.getValue<Group>(Group::class.java)!!
                toolbar.title = group.name

                findViewById(R.id.loader).visibility = View.INVISIBLE
                findViewById(R.id.content).visibility = View.VISIBLE


                val auth: FirebaseAuth = FirebaseAuth.getInstance()

                val layout = LinearLayout(getContext())
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layout.layoutParams = layoutParams
                for (member_id in group.members!!) {
                    val groupLayout = LinearLayout(layout.context)
                    groupLayout.orientation = LinearLayout.VERTICAL
                    val groupLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    groupLayout.layoutParams = groupLayoutParams
                    val title = TextView(layout.context)

                    val group = data.getValue(Group::class.java)
                    title.text = group!!.name
                    title.textSize = 20f
                    title.width
                    title.setTextColor(Color.BLACK)
                    title.setPadding(0, 5, 0, 15)
                    groupLayout.addView(title)

                    val password = TextView(layout.context)
                    password.text = group.password
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

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("dk", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }

        toolbar.setNavigationOnClickListener {
            groupRef.removeEventListener(groupListener)
            applicationContext.startActivity(Intent(applicationContext, MainActivity::class.java))
        }

        groupRef.addValueEventListener(groupListener)
    }
}
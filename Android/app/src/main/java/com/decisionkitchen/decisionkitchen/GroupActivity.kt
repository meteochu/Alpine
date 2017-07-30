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
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.ScrollView
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
                val memberScrollView : HorizontalScrollView = findViewById(R.id.members) as HorizontalScrollView
                memberScrollView.removeAllViews()
                toolbar.title = group.name

                findViewById(R.id.loader).visibility = View.INVISIBLE
                findViewById(R.id.content).visibility = View.VISIBLE


                val auth: FirebaseAuth = FirebaseAuth.getInstance()

                val memberLayout = LinearLayout(getContext())
                val memberLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                memberLayout.layoutParams = memberLayoutParams
                memberLayout.orientation = LinearLayout.HORIZONTAL
                for (member_id in group.members!!) {
                    database.getReference("users/" + member_id).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(memberSnapshot: DataSnapshot) {
                            val member : User = memberSnapshot.getValue<User>(User::class.java)!!
                            val title = TextView(memberLayout.context)
                            title.text = member.name
                            memberLayout.addView(title)
                        }
                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                }
                memberScrollView.addView(memberLayout)


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
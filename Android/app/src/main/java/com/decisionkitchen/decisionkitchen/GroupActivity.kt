package com.decisionkitchen.decisionkitchen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener

class GroupActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_group)

        val database = FirebaseDatabase.getInstance()
        val groupRef = database.getReference("groups/" + getIntent().getStringExtra("GROUP_ID"))

        val toolbar : Toolbar = findViewById(R.id.toolbar) as Toolbar
        toolbar.title = "Loading..."

        val groupListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val group : Group = dataSnapshot.getValue<Group>(Group::class.java)!!
                toolbar.title = group.name
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
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
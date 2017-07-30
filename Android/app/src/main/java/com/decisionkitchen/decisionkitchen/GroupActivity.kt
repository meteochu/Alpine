package com.decisionkitchen.decisionkitchen

import android.app.Activity
import android.os.Bundle
import android.util.Log
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
        val groupListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Get Post object and use the values to update the UI
                val group : Group? = dataSnapshot.getValue<Group>(Group::class.java)
                if (group != null) {
                    Log.w("test", group.password + "test")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("dk", "loadPost:onCancelled", databaseError.toException())
                // ...
            }
        }
        groupRef.addValueEventListener(groupListener)
    }
}

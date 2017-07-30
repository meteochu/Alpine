package com.decisionkitchen.decisionkitchen

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.database.*

class SignUpActivity : AppCompatActivity() {

    var uid: String? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        uid = intent.getStringExtra("USER_ID")
        setContentView(R.layout.activity_sign_up)
    }

    public fun submit(v: View) {
        val groupName = (findViewById(R.id.group_name) as TextView).text.toString()
        val password = (findViewById(R.id.group_pass) as TextView).text.toString()

        Log.e("blah", groupName + " " + password)

        if (groupName.trim().isBlank() || password.trim().isBlank()) {
            Toast.makeText(this, "Invalid group name or password. Try again.", Toast.LENGTH_SHORT).show()
        } else {
            val ref = FirebaseDatabase.getInstance().getReference("groups")
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.e("error", error.toString())
                }

                override fun onDataChange(snapshot: DataSnapshot) {
                    val data = snapshot.children
                    for (groupData in data) {
                        val group = groupData.getValue(Group::class.java)!!
                        if (group.name.equals(groupName, true) && group.password.equals(password, true)) {
                            group.members!!.add(uid as UserID)
                            ref.child(group.id).setValue(group)
                            return onBackPressed()
                        }
                    }
                    val members = ArrayList<String>()
                    members.add(uid as UserID)
                    val newRef = ref.push()
                    val newGroup = Group(password, members, groupName, HashMap(), newRef.key, ArrayList())
                    newRef.setValue(newGroup).addOnCompleteListener(object: OnCompleteListener<Void> {
                        override fun onComplete(p0: Task<Void>) {
                            onBackPressed()
                        }

                    })
                }

            })
        }
    }
}

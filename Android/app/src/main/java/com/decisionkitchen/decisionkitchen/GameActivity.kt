package com.decisionkitchen.decisionkitchen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*

class GameActivity : Activity() {

    private var group : Group? = null
    private var groupRef: DatabaseReference? = null
    private var question: Int = 0
    private var gameId: Int? = null
    private var user: FirebaseUser? = null
    private var qOrder: ArrayList<Int> = ArrayList<Int>()

    fun render() {
        if (group != null && groupRef != null && qOrder != null && user != null && gameId != null) {

        }
    }

    fun nextQuestion() {
        val game = group!!.games!![gameId!!]
        Log.w("text", qOrder.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val database = FirebaseDatabase.getInstance()
        user = FirebaseAuth.getInstance().currentUser;
        groupRef = database.getReference("groups/" + getIntent().getStringExtra("GROUP_ID"))

        val groupListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                group = dataSnapshot.getValue<Group>(Group::class.java)!!
                if (gameId == null) {
                    for (index in 0 .. (group!!.games!!.size - 1)) {
                        val game = group!!.games!![index]
                        Log.w("test", game.toString())
                        if (game.meta!!.end == null && (game.responses == null || !game.responses.containsKey(user!!.uid))) {
                            gameId = index
                            groupRef!!.child("games").child(index.toString()).child("responses").child(user!!.uid)
                                    .setValue(IntArray(game.categories!!.size).toList())

                            for (i in 0 .. (game.categories.size - 1)) {
                                qOrder.add(i)
                            }

                            Collections.shuffle(qOrder);
                            nextQuestion()
                        } else {
                            applicationContext.startActivity(Intent(applicationContext, MainActivity::class.java))
                        }
                    }
                } else {
                    render()
                }
            }
        }

        groupRef!!.addValueEventListener(groupListener)
    }
}

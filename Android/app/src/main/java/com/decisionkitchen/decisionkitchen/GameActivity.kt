package com.decisionkitchen.decisionkitchen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import java.util.*

data class Question (val title: String, val options: Array<String>);

class GameActivity : Activity() {

    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: RecyclerView.Adapter<GameAdapter.ViewHolder>? = null
    private var mLayoutManager: RecyclerView.LayoutManager? = null

    private var group : Group? = null
    private var groupRef: DatabaseReference? = null
    private var question: Int = -1
    private var gameId: Int? = null
    private var user: FirebaseUser? = null
    private var qOrder: ArrayList<Int> = ArrayList<Int>()
    private var questions: ArrayList<Question> = arrayListOf(
        Question("How much do you want to spend?", arrayOf("$", "$$", "$$$", "$$$$")),
        Question("What kind of food do you want?", arrayOf("Breakfast & Brunch", "Chinese", "Diners", "Fast Food", "Hot Pot", "Italian", "Japanese", "Korean", "Mongolian", "Pizza", "Steakhouses", "Sushi Bars", "American (Traditional)", "Vegetarian")),
        Question("Delivery of dine-in?", arrayOf("Delivery", "Dine-in"))
    )
    private var responses: ArrayList<ArrayList<Int>> = ArrayList<ArrayList<Int>>();

    public fun getContext(): Context {
        return this
    }

    public fun getGameActivity(): GameActivity {
        return this
    }

    fun render() {
        if (question == questions.size) {
            applicationContext.startActivity(Intent(applicationContext, FinishedActivity::class.java))
        } else if (group != null && groupRef != null && qOrder != null && question != -1 && user != null && gameId != null) {
            val q = questions[question]
            (findViewById(R.id.question) as TextView).text = q.title

            var options:List<String> = ArrayList<String>()
            for (question in q.options) {
                options += question
            }

                mAdapter = GameAdapter(options, responses[question], mRecyclerView, getGameActivity(), q.title)
                mRecyclerView!!.adapter = mAdapter

            }

    }

    fun nextQuestion() {
        val game = group!!.games!![gameId!!]
        question++
        if (question < questions.size) render()
        else {
            groupRef!!.child("games").child(gameId.toString()).child("responses").child(user!!.uid).setValue(responses)
        }
    }

    public fun nextQuestion(view: View) {
        nextQuestion()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        responses = ArrayList<ArrayList<Int>>(questions.size);

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
                        if (game.meta!!.end == null && (game.responses == null || !game.responses.containsKey(user!!.uid))) {
                            gameId = index
                            for (i in 0 .. (questions.size - 1)) {
                                qOrder.add(i)
                                val tmp = ArrayList<Int>(questions[i].options.size)
                                for (j in 0 .. questions[i].options.size - 1) {
                                    tmp.add(0)
                                }
                                responses.add(tmp);
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

        mRecyclerView = findViewById(R.id.checkbox_recycler) as RecyclerView
        mRecyclerView!!.setHasFixedSize(true)

        val rParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT)
        mRecyclerView!!.layoutParams = rParams

        mLayoutManager = LinearLayoutManager(getContext())
        mRecyclerView!!.layoutManager = mLayoutManager

        mLayoutManager!!.setMeasuredDimension(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT)

        mAdapter = GameAdapter(ArrayList<String>(), ArrayList<Int>(), mRecyclerView, getGameActivity(), ""  )
        mRecyclerView!!.adapter = mAdapter
    }
}

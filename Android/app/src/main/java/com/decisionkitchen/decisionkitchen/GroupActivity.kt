package com.decisionkitchen.decisionkitchen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.text.Layout
import android.util.Log
import android.view.Gravity
import android.view.View
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
import com.facebook.drawee.view.SimpleDraweeView
import com.google.firebase.auth.FirebaseAuth
import com.facebook.drawee.generic.RoundingParams
import com.github.kevinsawicki.timeago.TimeAgo
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

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

                var loadCount : Int = 0

                val auth: FirebaseAuth = FirebaseAuth.getInstance()

                val roundingParams = RoundingParams.fromCornersRadius(5f)
                roundingParams.roundAsCircle = true

                val membersLayout = LinearLayout(getContext())
                val membersLayoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                membersLayout.layoutParams = membersLayoutParams
                membersLayout.orientation = LinearLayout.HORIZONTAL
                for (member_id in group.members!!) {
                    database.getReference("users/" + member_id).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(memberSnapshot: DataSnapshot) {

                            val userLayout = LinearLayout(membersLayout.context)
                            userLayout.orientation = LinearLayout.VERTICAL
                            userLayout.gravity = Gravity.CENTER_HORIZONTAL
                            userLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)

                            val member : User = memberSnapshot.getValue<User>(User::class.java)!!

                            val profile = SimpleDraweeView(userLayout.context)
                            profile.setImageURI(member.img)
                            profile.hierarchy.roundingParams = roundingParams
                            val params = ViewGroup.LayoutParams(120, 120)
                            profile.layoutParams = params;
                            val marginParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(profile.layoutParams);
                            marginParams.setMargins(50, 30, 50, 20)
                            profile.layoutParams = marginParams
                            userLayout.addView(profile)

                            val name = TextView(userLayout.context)
                            name.text = member.first_name
                            name.textSize = 11.0F
                            name.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
                            name.setPadding(0, 0, 0, 30)
                            userLayout.addView(name)

                            membersLayout.addView(userLayout)

                            loadCount ++

                            if (loadCount == group.members.size) {
                                findViewById(R.id.loader).visibility = View.INVISIBLE
                                findViewById(R.id.content).visibility = View.VISIBLE
                            }

                        }
                        override fun onCancelled(databaseError: DatabaseError) {}
                    })
                }
                memberScrollView.addView(membersLayout)

                val mainContent : LinearLayout = findViewById(R.id.group_content) as LinearLayout
                mainContent.removeAllViews()

                if (group.games != null) {
                    for (game in group.games) {

                        val restaurant = group.restaurants!![game.result!!.restaurant_id]!!

                        val cardLayout = LinearLayout(mainContent.context)
                        cardLayout.orientation = LinearLayout.HORIZONTAL
                        cardLayout.gravity = Gravity.TOP
                        cardLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                        val profile = SimpleDraweeView(cardLayout.context)
                        profile.setImageURI("https://unsplash.it/200")
                        profile.hierarchy.roundingParams = roundingParams
                        val params = ViewGroup.LayoutParams(230, 230)
                        profile.layoutParams = params;
                        val marginParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(profile.layoutParams);
                        marginParams.setMargins(50, 50, 50, 50)
                        profile.layoutParams = marginParams
                        cardLayout.addView(profile)

                        val cardContentLayout = LinearLayout(mainContent.context)
                        cardContentLayout.orientation = LinearLayout.VERTICAL
                        cardContentLayout.gravity = Gravity.TOP
                        cardContentLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        (cardContentLayout.layoutParams as LinearLayout.LayoutParams).setMargins(0, 50, 50, 50)

                        val name = TextView(cardContentLayout.context)
                        name.text = restaurant.name
                        name.textSize = 20.0F
                        name.typeface = Typeface.DEFAULT_BOLD
                        name.setTextColor(Color.BLACK)
                        name.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
                        name.setPadding(0, 0, 0, 10)
                        cardContentLayout.addView(name)

                        val address = TextView(cardContentLayout.context)
                        address.text = restaurant.address
                        address.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
                        address.setPadding(0, 0, 0, 20)
                        cardContentLayout.addView(address)

                        val date = TextView(cardContentLayout.context)
                        date.text = DateTime(game.meta!!.end).toString(DateTimeFormat.shortDateTime())
                        date.textSize = 15.0F
                        date.typeface = Typeface.create("sans-serif-light", Typeface.NORMAL)
                        date.setTextColor(Color.rgb(150, 150, 150))
                        date.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT)
                        cardContentLayout.addView(date)

                        cardLayout.addView(cardContentLayout)

                        mainContent.addView(cardLayout)

                    }
                }

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

    public fun createGame(view: View) {
        val intent:Intent = Intent(getBaseContext(), RestaurantActivity::class.java)
        intent.putExtra("GROUP_ID", getIntent().getStringExtra("GROUP_ID"))
        startActivity(intent)

        val ref = FirebaseDatabase.getInstance().getReference("groups/" + getIntent().getStringExtra("GROUP_ID"))
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                Log.e("error", p0.toString())
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                val group = snapshot.getValue(Group::class.java)!!
                val game = Game(GameMeta(null, ISODateTimeFormat.dateTime().print(DateTime())))
                group.games!!.add(game)
                ref.setValue(group)
            }

        })
    }

}
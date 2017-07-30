package com.decisionkitchen.decisionkitchen

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.mancj.materialsearchbar.MaterialSearchBar
import com.google.android.gms.internal.ok
import org.json.JSONObject
import com.android.volley.AuthFailureError
import com.facebook.drawee.generic.RoundingParams
import com.facebook.drawee.view.SimpleDraweeView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.net.URLEncoder


class RestaurantActivity : Activity() {

    private var searchRes : ArrayList<Restaurant>? = null
    private var group : Group? = null

    private var mFusedLocationClient: FusedLocationProviderClient? = null
    fun checkPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//Can add more as per requirement

            ActivityCompat.requestPermissions(this,
                    arrayOf<String>(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION),
                    123)
        }
    }

    fun showResults() {
        if (group != null && searchRes != null) {

            val roundingParams = RoundingParams.fromCornersRadius(5f)
            roundingParams.roundAsCircle = true

            val scrollView : LinearLayout = findViewById(R.id.scrollView) as LinearLayout
            scrollView.removeAllViews()

            for (restaurant in searchRes!!) {

                val cardLayout = LinearLayout(scrollView.context)
                cardLayout.orientation = LinearLayout.HORIZONTAL
                cardLayout.gravity = Gravity.TOP
                cardLayout.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

                val profile = SimpleDraweeView(cardLayout.context)
                profile.setImageURI(restaurant.image)
                profile.hierarchy.roundingParams = roundingParams
                val params = ViewGroup.LayoutParams(230, 230)
                profile.layoutParams = params;
                val marginParams: LinearLayout.LayoutParams = LinearLayout.LayoutParams(profile.layoutParams);
                marginParams.setMargins(50, 50, 50, 50)
                profile.layoutParams = marginParams
                cardLayout.addView(profile)

                val cardContentLayout = LinearLayout(scrollView.context)
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

                cardLayout.addView(cardContentLayout)

                scrollView.addView(cardLayout)

            }


        }
    }


    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val database = FirebaseDatabase.getInstance()
        val groupRef = database.getReference("groups/" + getIntent().getStringExtra("GROUP_ID"))

        val groupListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError?) {
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                group = dataSnapshot.getValue<Group>(Group::class.java)!!
                showResults()
            }
        }

        groupRef.addValueEventListener(groupListener)

        val queue = Volley.newRequestQueue(this)
        val tokenRequest = object : StringRequest(Request.Method.POST, "https://api.yelp.com/oauth2/token",
                Response.Listener<String> { response ->
                    // response
                    val jObject = JSONObject(response)
                    val token = jObject.get("access_token")

                    setContentView(R.layout.activity_restaurant)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
                        checkPermission();
                    }
                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    mFusedLocationClient!!.lastLocation.addOnSuccessListener(this) { location ->
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {

                            val searchbar: MaterialSearchBar = findViewById(R.id.searchbar) as MaterialSearchBar
                            searchbar.enableSearch()
                            searchbar.setOnSearchActionListener( object: MaterialSearchBar.OnSearchActionListener {
                                override fun onSearchStateChanged(enabled: Boolean) {
                                }

                                override fun onButtonClicked(buttonCode: Int) {
                                }

                                override fun onSearchConfirmed(text: CharSequence?) {
                                    searchbar.disableSearch()
                                    val searchRequest = object : StringRequest(Request.Method.GET,
                                            "https://api.yelp.com/v3/businesses/search?term=" + URLEncoder.encode(text.toString())
                                                    + "&latitude=" + location.latitude
                                                    + "&longitude=" + location.longitude
                                                    + "&radius=12500"
                                                    + "&categories=food",
                                            Response.Listener<String> { response ->
                                                val searchResults = JSONObject(response).getJSONArray("businesses")
                                                val results = ArrayList<Restaurant>()
                                                for (i in 0..(searchResults.length() - 1)) {
                                                    val business = searchResults.getJSONObject(i)
                                                    results.add(Restaurant(
                                                            id = business.get("id").toString(),
                                                            address = business.getJSONObject("location").get("address1").toString(),
                                                            city = business.getJSONObject("location").get("city").toString(),
                                                            state = business.getJSONObject("location").get("state").toString(),
                                                            zip = business.getJSONObject("location").get("zip_code").toString(),
                                                            name = business.get("name").toString(),
                                                            image = business.get("image_url").toString()
                                                    ))
                                                }

                                                searchRes = results
                                                showResults()

                                            },
                                            Response.ErrorListener { error ->
                                                Log.d("ERROR", "error => " + error.toString())
                                            }
                                    ) {
                                        @Throws(AuthFailureError::class)
                                        override fun getHeaders(): Map<String, String> {
                                            val params = HashMap<String, String>()
                                            params.put("Authorization", "Bearer " + token)
                                            return params
                                        }
                                    }
                                    queue.add(searchRequest)

                                }

                            })
                        }
                    }


                },
                Response.ErrorListener { error ->
                    // error
                    Log.d("Error.Response", error.toString())
                }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params.put("grant_type", "client_credentials")
                params.put("client_id", "xvLjyr0x6-NFtf-gdE-GMg")
                params.put("client_secret", "lKDtAKGZrO666hlJZOBe8B0aAe6EMjjOxkASP91Yl48HTXoBrR2evFwirDnhl48n")

                return params
            }
        }
        queue.add(tokenRequest)


    }
}

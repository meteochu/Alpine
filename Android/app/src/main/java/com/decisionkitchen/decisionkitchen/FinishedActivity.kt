package com.decisionkitchen.decisionkitchen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View

class FinishedActivity : Activity() {
    public fun goHome(view: View) {
        applicationContext.startActivity(Intent(applicationContext, MainActivity::class.java))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished)
    }
}

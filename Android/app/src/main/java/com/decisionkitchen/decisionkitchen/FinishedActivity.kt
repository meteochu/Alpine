package com.decisionkitchen.decisionkitchen

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View

class FinishedActivity : Activity() {
    public fun goHome(view: View) {
        val intent: Intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        applicationContext.startActivity(intent)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_finished)
    }
}

package com.mindera.skeletoid.activities.unfreeze.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View

class UnfreezeScreenActivity : AppCompatActivity() {

    lateinit var view: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        view = View(this)
        view.setBackgroundColor(Color.TRANSPARENT)
        view.setOnClickListener {
            it.post {
                finish()
            }
        }
        setContentView(view)
        overridePendingTransition(0, 0)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        view.post {
            view.performClick()
        }
    }
}
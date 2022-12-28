package com.app.swagse.polls

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import com.app.swagse.R

class PollsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val fm = supportFragmentManager
        val ft = fm.beginTransaction()

        setContentView(R.layout.activity_polls)
        if (intent.getBooleanExtra("create" , false)){
            ft.replace(R.id.frame_polls, PollFragment())
            supportActionBar!!.title = "Create Poll"
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ft.commit()
        }else{
            supportActionBar!!.title = "Live Poll's"
            ft.replace(R.id.frame_polls, ShowPollsFragment())
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            ft.commit()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
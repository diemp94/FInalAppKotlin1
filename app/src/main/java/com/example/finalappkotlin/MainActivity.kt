package com.example.finalappkotlin

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.finalappkotlin.adapters.PagerAdapter
import com.example.finalappkotlin.fragments.ChatFragment
import com.example.finalappkotlin.fragments.InfoFragment
import com.example.finalappkotlin.fragments.RatesFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var prevBottomSelected: MenuItem? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpPagerAdapter(getPagerAdapter())
        setUpBottomNavegationBar()
    }

    private fun getPagerAdapter(): PagerAdapter {
        val adapter = PagerAdapter(supportFragmentManager)
        adapter.addFragment(InfoFragment())
        adapter.addFragment(RatesFragment())
        adapter.addFragment(ChatFragment())
        return adapter
    }

    private fun setUpPagerAdapter(adapter: PagerAdapter) {
        vpMainActivity.adapter = adapter
        vpMainActivity.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                if (prevBottomSelected == null) {
                    bottomNavigationView.menu.getItem(0).isChecked = false
                } else {
                    prevBottomSelected!!.isChecked = false
                }
                bottomNavigationView.menu.getItem(position).isChecked = true
                prevBottomSelected = bottomNavigationView.menu.getItem(position)
            }
        })
    }

    private fun setUpBottomNavegationBar() {
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_info -> {
                    vpMainActivity.currentItem = 0; true
                }
                R.id.bottom_nav_rates -> {
                    vpMainActivity.currentItem = 1; true
                }
                R.id.bottom_nav_chat -> {
                    vpMainActivity.currentItem = 2; true
                }
                else -> false

            }
        }
    }
}

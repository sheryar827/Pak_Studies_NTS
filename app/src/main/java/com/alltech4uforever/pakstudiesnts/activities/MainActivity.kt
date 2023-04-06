package com.alltech4uforever.pakstudiesnts.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.alltech4uforever.pakstudiesnts.R
import com.alltech4uforever.pakstudiesnts.databinding.ActivityMainBinding
import com.alltech4uforever.pakstudiesnts.fragments.QuizCategoryFragment
import com.alltech4uforever.pakstudiesnts.fragments.QuizFragment
import com.alltech4uforever.pakstudiesnts.fragments.QuizListFragment
import com.alltech4uforever.pakstudiesnts.fragments.QuizModeFragment
import com.alltech4uforever.pakstudiesnts.utils.QuizMode


class MainActivity : AppCompatActivity(),
    QuizModeFragment.QuizTypeFragmentInterface,
    QuizListFragment.QuizListFragmentListener,
    QuizCategoryFragment.QuizCategoryFragmentListener{

    private lateinit var _binding : ActivityMainBinding
    private var hiddenToolbar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.AppTheme)

        // create instance of the ActivityMainBinding,
        // as we have only one layout activity_main.xml
        _binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(_binding.root)

        val window = this.window

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        fixMinDrawerMargin(_binding.drawerLayout)
        setSupportActionBar(_binding.activityContentMain.toolBar)


        if (savedInstanceState == null) {
            supportActionBar!!.title = getString(R.string.app_name)
            replaceFragment(QuizCategoryFragment(), QuizCategoryFragment.TAG)
        }else {
            hiddenToolbar = savedInstanceState.getBoolean("toolbarHidden")

        }


        setUpToolbar(hiddenToolbar)

    }

    private fun fixMinDrawerMargin(drawerLayout: DrawerLayout) {
        try {
            val f = DrawerLayout::class.java.getDeclaredField("mMinDrawerMargin")
            f.isAccessible = true
            f.set(drawerLayout, 0)
            drawerLayout.requestLayout()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setUpToolbar(hide: Boolean) {
        hiddenToolbar = hide
        if (hide) {
            supportActionBar!!.setDisplayShowHomeEnabled(true)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp)
            supportActionBar!!.title = getString(R.string.prf_settings)
            _binding.activityContentMain.toolBar.setNavigationOnClickListener {
                //onFragmentBackPressed()
                setUpToolbar(false)
            }
        } else {
            supportActionBar!!.title = getString(R.string.quiz_ctg)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_menu_24)
            _binding.activityContentMain.toolBar.setNavigationOnClickListener {
                _binding.drawerLayout.openDrawer(
                    GravityCompat.START
                )
            }
        }
    }

    private fun onMenuClose() {
        _binding.drawerLayout.closeDrawers()
    }


    private fun replaceFragment(frag: Fragment?, key: String?) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, frag!!, key)
            .commit()
        if(supportFragmentManager.fragments.isNotEmpty())
            supportFragmentManager.beginTransaction().addToBackStack(null).commit()
    }


    override fun noTimeMode(categoryName: String) {

        val frag = supportFragmentManager.findFragmentByTag(QuizListFragment.TAG)
        if (!(frag != null && frag.isVisible)) {
            supportActionBar!!.title = getString(R.string.no_time_mode)
            replaceFragment(QuizListFragment.newInstance(QuizMode.NO_TIME_MODE.ordinal, categoryName), QuizListFragment.TAG)
        }
    }

    override fun timeMode() {
        TODO("Not yet implemented")
    }

    override fun beatTheTimeMode() {
        TODO("Not yet implemented")
    }

    override fun preparationMode() {
        TODO("Not yet implemented")
    }

    override fun startQuiz(pageKey: Int, categoryName: String) {
        val frag = supportFragmentManager.findFragmentByTag(QuizFragment.TAG)
        if (!(frag != null && frag.isVisible)) {
            supportActionBar!!.title = "Quiz $pageKey"
            replaceFragment(QuizFragment.newInstance(pageKey, categoryName), QuizFragment.TAG)
        }
    }

    override fun quizCategorySelected(categoryName: String) {
        val frag = supportFragmentManager.findFragmentByTag(QuizModeFragment.TAG)
        if (!(frag != null && frag.isVisible)) {
            supportActionBar!!.title = getString(R.string.quiz_type)
            replaceFragment(QuizModeFragment.newInstance(categoryName), QuizModeFragment.TAG)
        }
    }
}
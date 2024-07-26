package com.alltech4uforever.pakstudiesnts.activities

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.alltech4uforever.pakstudiesnts.R
import com.alltech4uforever.pakstudiesnts.databinding.ActivityMainBinding
import com.alltech4uforever.pakstudiesnts.fragments.QuizCategoryFragment
import com.alltech4uforever.pakstudiesnts.fragments.QuizModeFragment


class MainActivity : AppCompatActivity(),
    QuizModeFragment.QuizTypeFragmentInterface,
    //QuizListFragment.QuizListFragmentListener,
    /*QuizFragment.QuizFragmentInterface,*/
    QuizCategoryFragment.QuizCategoryFragmentListener{

    private lateinit var _binding : ActivityMainBinding
    private var hiddenToolbar = false

    //private lateinit var getQuestions : GetQuiz

    //private var questionList : ArrayList<QuizModel.QuestionModel>? = ArrayList()

    //private val defaultQuizNum = 1

    //private var questionPosition = 0

    //private var coinsCount: Int = 0

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
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
            _binding.activityContentMain.toolBar.setNavigationOnClickListener{
                this.onBackPressed()
            }
            /*supportActionBar!!.setHomeAsUpIndicator(R.drawable.baseline_menu_24)*/
            /*_binding.activityContentMain.toolBar.setNavigationOnClickListener {
                _binding.drawerLayout.openDrawer(
                    GravityCompat.START
                )
            }*/
        }
    }

    /*private fun onMenuClose() {
        _binding.drawerLayout.closeDrawers()
    }*/


    private fun replaceFragment(frag: Fragment?, key: String?) {

        /*supportFragmentManager.popBackStack(QuizCategoryFragment.TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE)*/

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container, frag!!, key)
            .addToBackStack(key)
            .commit()
        /*if(supportFragmentManager.fragments.isNotEmpty())
            supportFragmentManager.beginTransaction().addToBackStack(null).commit()*/
    }

    /*private fun updateQuestionFragment(frag: Fragment?){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, frag!!, QuizFragment.TAG)
            .addToBackStack(QuizFragment.TAG)
            .commit()
    }*/

    override fun noTimeMode(categoryName: String) {

        val intent = Intent(this@MainActivity, QuizzesActivity::class.java)
        val b = Bundle()
        b.putInt("QUIZMODE", 0)
        b.putString("CATEGORYNAME", categoryName) //Your id

        intent.putExtras(b) //Put your id to your next Intent

        startActivity(intent)

        /*getQuestions = GetQuiz.getInstance(this)!!
        getQuestions.open()

        _binding.activityContentMain.frameLayout2.visibility = View.VISIBLE

        questionList = ArrayList()

        questionList?.addAll(getQuestions.readQues(1, categoryName))
        questionList?.shuffle()

        // 10 questions per quiz
        val totalQuizzes = getQuestions.quesCount(categoryName)/10

        getQuestions.close()

        _binding.activityContentMain.tvTotalCount.text =  totalQuizzes.toString()
        _binding.activityContentMain.etPageNum.setText(defaultQuizNum.toString())

        // limit total num of digits enter in edittext field
        _binding.activityContentMain.etPageNum.filters = arrayOf<InputFilter>(InputFilterMinMax(defaultQuizNum.toString(), totalQuizzes.toString()))


        updateQuestion()*/

    }

    /*private fun updateQuestion() {
        //val frag = supportFragmentManager.findFragmentByTag(QuizFragment.TAG)
        //if (!(frag != null && frag.isVisible)) {
            supportActionBar!!.title = getString(R.string.no_time_mode)
            supportActionBar!!.subtitle = String.format(
                String.format(
                    "Question %d/%d",
                    questionPosition + 1,
                    questionList?.size
                ))
            //replaceFragment(QuizListFragment.newInstance(QuizMode.NO_TIME_MODE.ordinal, categoryName), QuizListFragment.TAG)
        replaceFragment(QuizFragment.newInstance(
            questionList?.get(questionPosition)?.Question!!,
                questionList?.get(questionPosition)?.OptionA!!,
                questionList?.get(questionPosition)?.OptionB!!,
                questionList?.get(questionPosition)?.OptionC!!,
                questionList?.get(questionPosition)?.OptionD!!,
                questionList?.get(questionPosition)?.Answer!!), QuizFragment.TAG)

            questionPosition++
        //}
    }*/

    override fun timeMode(categoryName: String) {
        val intent = Intent(this@MainActivity, QuizzesActivity::class.java)
        val b = Bundle()
        b.putInt("QUIZMODE", 2)
        b.putString("CATEGORYNAME", categoryName) //Your id

        intent.putExtras(b) //Put your id to your next Intent

        startActivity(intent)

    }

    override fun beatTheTimeMode(categoryName: String) {
        val intent = Intent(this@MainActivity, QuizzesActivity::class.java)
        val b = Bundle()
        b.putInt("QUIZMODE", 3)
        b.putString("CATEGORYNAME", categoryName) //Your id

        intent.putExtras(b) //Put your id to your next Intent

        startActivity(intent)
    }

    override fun preparationMode(categoryName: String) {
        val intent = Intent(this@MainActivity, QuizzesActivity::class.java)
        val b = Bundle()
        b.putInt("QUIZMODE", 1)
        b.putString("CATEGORYNAME", categoryName) //Your id

        intent.putExtras(b) //Put your id to your next Intent

        startActivity(intent)
    }

    /*override fun startQuiz(pageKey: Int, categoryName: String) {
        val frag = supportFragmentManager.findFragmentByTag(QuizFragment.TAG)
        if (!(frag != null && frag.isVisible)) {
            supportActionBar!!.title = "Quiz $pageKey"
            replaceFragment(QuizFragment.newInstance(questionList[questionPosition].Question!!,
                questionList[questionPosition].OptionA!!,
                questionList[questionPosition].OptionB!!,
                questionList[questionPosition].OptionC!!,
                questionList[questionPosition].OptionD!!,
                questionList[questionPosition].Answer!!), QuizFragment.TAG)
        }
    }*/

    override fun quizCategorySelected(categoryName: String) {
        val frag = supportFragmentManager.findFragmentByTag(QuizModeFragment.TAG)
        if (!(frag != null && frag.isVisible)) {
            supportActionBar!!.title = getString(R.string.quiz_type)
            replaceFragment(QuizModeFragment.newInstance(categoryName), QuizModeFragment.TAG)
        }
    }

    /*override fun onFinished(correct: Boolean) {
        if (correct) {
            coinsCount++
        }
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (questionPosition < questionList!!.size) {
                    updateQuestion()
                } else {
                    // implment dialog for displsying the status  correct/missed questions
                    Toast.makeText(this@MainActivity, "Completed the Quiz", Toast.LENGTH_SHORT)
                        .show()
                    //updateUserCoins()
                    //showCelebrationDialog()
                    handler.removeCallbacksAndMessages(this)
                }
            }
        }, 2000)
    }*/

    override fun onBackPressed() {

        val fragments = supportFragmentManager.backStackEntryCount
        if (fragments == 1) {
            finish()
        } else if (supportFragmentManager.backStackEntryCount > 1) {
            supportFragmentManager.popBackStack()
        } else {
            onBackPressedDispatcher.onBackPressed()
        }

        setTitleBar()
    }

    private fun setTitleBar() {
        val fragmentManager = supportFragmentManager
        val fragments = fragmentManager.fragments
        if (fragments != null) {
            for (fragment in fragments) {
                if (fragment != null && fragment.isVisible){
                    supportActionBar!!.title = fragment.tag
                }
            }
        }

    }

    /*override fun onBackPressed() {
        val fm = this.supportFragmentManager
        if (fm.backStackEntryCount > 0) {
            Log.i("MainActivity", "popping backstack")
            fm.popBackStack()
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super")
            onBackPressedDispatcher.onBackPressed()
        }
    }*/
}
package com.alltech4uforever.pakstudiesnts.activities

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.text.InputFilter
import android.view.View
import android.view.View.OnClickListener
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alltech4uforever.pakstudiesnts.R
import com.alltech4uforever.pakstudiesnts.database.GetQuiz
import com.alltech4uforever.pakstudiesnts.databinding.ActivityQuizzesBinding
import com.alltech4uforever.pakstudiesnts.fragments.NoTimeModeQuizFragment
import com.alltech4uforever.pakstudiesnts.fragments.PracticeFragment
import com.alltech4uforever.pakstudiesnts.fragments.QuizFragment
import com.alltech4uforever.pakstudiesnts.models.QuizModel
import com.alltech4uforever.pakstudiesnts.models.SharedViewModel
import com.alltech4uforever.pakstudiesnts.utils.InputFilterMinMax
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Objects

class QuizzesActivity : AppCompatActivity()
    , QuizFragment.QuizFragmentInterface
    , NoTimeModeQuizFragment.NoTimeModeQuizFragmentInterface
    , OnClickListener{

    private lateinit var _binding : ActivityQuizzesBinding
    private var questionList : ArrayList<QuizModel.QuestionModel>? = ArrayList()
    private var defaultQuizNum = 1
    private var totalQuizzes = 1
    private var questionPosition = 0
    private var counter: CountDownTimer? = null
    private var coinsCount: Int = 0
    private lateinit var categoryName: String
    private var mode = 0
    val quizTime: Long = 60000
    val timeSec: Long = 1000
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private var celebrationDialog: AlertDialog? = null
    private var quesListSize: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme(R.style.AppTheme)

        _binding = ActivityQuizzesBinding.inflate(layoutInflater)

        setContentView(_binding.root)

        val window = this.window

        window.statusBarColor = ContextCompat.getColor(this, R.color.black)

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

        setSupportActionBar(_binding.toolBar)

        _binding.ivNext.setOnClickListener(this)
        _binding.ivPrev.setOnClickListener(this)
        _binding.ivGo.setOnClickListener(this)

        val b = intent.extras

        if (b != null) {
            mode  = b.getInt("QUIZMODE", 0)
            categoryName = b.getString("CATEGORYNAME").toString()

            getPracticeQuestions()

            /*if(mode == 0) {
                getQuestions()
            }
            else{
                getPracticeQuestions()
            }*/
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    exitOnBackPressed()
                }
            })

        /*if (Build.VERSION.SDK_INT >= 33) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(
                OnBackInvokedDispatcher.PRIORITY_DEFAULT
            ) {

                exitOnBackPressed()
            }
        } else {

        }*/



    }

    fun getProgress(l: Long): Int {
        return ( 60 - l.toInt() / 1000) * 100 / 60
    }

    private fun getPracticeQuestions() {
        val getQuestions = GetQuiz.getInstance(this)!!
        getQuestions.open()

        _binding.frameLayout2.visibility = View.VISIBLE
        // 10 questions per quiz
        totalQuizzes = getQuestions.quesCount(categoryName)/10

        val lastQuestion = defaultQuizNum * 10
        val firstQuestion = (lastQuestion - 10) + 1

        _binding.tvTotalCount.text =  totalQuizzes.toString()
        _binding.etPageNum.setText(defaultQuizNum.toString())

        // limit total num of digits enter in edittext field
        _binding.etPageNum.filters = arrayOf<InputFilter>(InputFilterMinMax("1", totalQuizzes.toString()))

        when (mode) {
            0 -> {
                //getQuestions()
                replaceFragment(NoTimeModeQuizFragment.newInstance(firstQuestion, categoryName), NoTimeModeQuizFragment.TAG)
            }
            2 -> {
                getQuestions()
            }

            3 -> {
                //if (counter != null) counter = null
                resetCounter()
                _binding.progressBar.progressDrawable = ContextCompat.getDrawable(applicationContext
                    ,R.drawable.custom_progress_bar_horizontal)

                counter = object : CountDownTimer(quizTime, timeSec) {
                    override fun onTick(l: Long) {
                        if (getProgress(l) >= 75) {
                            _binding.progressBar.progressDrawable = ContextCompat.getDrawable(applicationContext
                                ,R.drawable.custom_progressbar_red_horizontal)
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            _binding.progressBar.setProgress(getProgress(l), true)
                        } else {
                            _binding.progressBar.progress = getProgress(l)
                        }
                    }

                    override fun onFinish() {
                        _binding.progressBar.progressDrawable = ContextCompat.getDrawable(applicationContext
                            ,R.drawable.custom_progressbar_red_horizontal)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            _binding.progressBar.setProgress(100, true)
                        } else {
                            _binding.progressBar.progress = 100
                        }
                        var correctAns: Int = 0
                        var quesCount: Int = 0

                        val sharedViewModel = ViewModelProvider(this@QuizzesActivity)[SharedViewModel::class.java]
                        sharedViewModel.correctAns.observe(this@QuizzesActivity, Observer {
                            correctAns = it
                        })
                        sharedViewModel.quesCount.observe(this@QuizzesActivity, Observer{
                            quesCount = it
                        })

                        showCelebrationDialog(correctAns, quesCount)
                        //processAnswer()
                    }
                }.start()
                replaceFragment(NoTimeModeQuizFragment.newInstance(firstQuestion, categoryName), NoTimeModeQuizFragment.TAG)
            }

            else -> {
                //getPracticeQuestions()
                replaceFragment(PracticeFragment.newInstance(firstQuestion, categoryName), PracticeFragment.TAG)
            }
        }
    }

    private fun getQuestions() {

        val b = intent.extras
        var categoryName = "" // or other values

        if (b != null)
            categoryName = b.getString("CATEGORYNAME").toString()

        val getQuestions = GetQuiz.getInstance(this)!!
        getQuestions.open()

        _binding.frameLayout2.visibility = View.VISIBLE


        val lastQuestion = defaultQuizNum * 10
        val firstQuestion = (lastQuestion - 10) + 1

        questionList = ArrayList()

        questionList?.addAll(getQuestions.readQues(firstQuestion, categoryName))

        questionList?.shuffle()

        getQuestions.close()

        _binding.tvTotalCount.text =  totalQuizzes.toString()
        _binding.etPageNum.setText(defaultQuizNum.toString())

        // limit total num of digits enter in edittext field
        _binding.etPageNum.filters = arrayOf<InputFilter>(InputFilterMinMax("1", totalQuizzes.toString()))


        updateQuestion()
    }

    private fun updateQuestion() {
        //val frag = supportFragmentManager.findFragmentByTag(QuizFragment.TAG)
        //if (!(frag != null && frag.isVisible)) {
        supportActionBar!!.title = getString(R.string.app_name)
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
    }

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

    override fun onFinished(correct: Boolean) {
        if (correct) {
            coinsCount++
        }

        coroutineScope.launch {

            delay(2000)

            if (questionPosition < questionList!!.size) {
                updateQuestion()
            } else {
                showCelebrationDialog(coinsCount, questionList!!.size)
                // implment dialog for displsying the status  correct/missed questions
                /*Toast.makeText(this@QuizzesActivity, "Completed the Quiz", Toast.LENGTH_SHORT)
                    .show()*/
                //updateUserCoins()
                //showCelebrationDialog()
                //handler.removeCallbacksAndMessages(this)
            }

        }
        //val handler = Handler()
        /*handler.postDelayed(object : Runnable {
            override fun run() {
                if (questionPosition < questionList!!.size) {
                    updateQuestion()
                } else {
                    // implment dialog for displsying the status  correct/missed questions
                    Toast.makeText(this@QuizzesActivity, "Completed the Quiz", Toast.LENGTH_SHORT)
                        .show()
                    //updateUserCoins()
                    //showCelebrationDialog()
                    handler.removeCallbacksAndMessages(this)
                }
            }
        }, 2000)*/
    }

    private fun determineTitle(correctAns: Int, quesCount: Int): String? {
        //val size: Int = questionList!!.size
        return if (correctAns == quesCount) {
            "Greetings"
        } else if (correctAns <= quesCount / 2) {
            "Too bad"
        } else {
            "Nice Job"
        }
    }

    private fun showCelebrationDialog(correctAns: Int, quesCount: Int) {

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setCancelable(false)
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.celebration_dialog, null)
        alertDialog.setView(dialogView)
        val img = dialogView.findViewById<ImageView>(R.id.celeb_background)
        Glide.with(this)
            .asGif()
            .load(R.raw.celeb_background)
            .into(img)
        val celebScore = dialogView.findViewById<TextView>(R.id.celeb_score)
        val celebTitle = dialogView.findViewById<TextView>(R.id.celeb_title)
        val celebText = dialogView.findViewById<TextView>(R.id.celeb_text)
        celebScore.text = String.format("%d/%d", correctAns, quesCount)
        celebTitle.text = determineTitle(correctAns, quesCount)
        celebText.text =
            String.format("You got %d out of %d questions", correctAns, quesCount)
       /* val celebButton = dialogView.findViewById<Button>(R.id.celeb_btn)
        celebButton.setOnClickListener { view: View? ->
            celebration_dialog.dismiss()
            nextQuiz()
        }*/
        val exitButton = dialogView.findViewById<Button>(R.id.celeb_exit_btn)
        exitButton.setOnClickListener {
            celebrationDialog!!.dismiss()
        }
        alertDialog.setCancelable(false)
        celebrationDialog = alertDialog.create()
        Objects.requireNonNull<Window>(celebrationDialog!!.window).setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )
        celebrationDialog!!.show()
    }

     fun exitOnBackPressed() {
        resetCounter()
        finish()
    }


    private fun resetCounter(){
        if (counter != null) {
            counter!!.cancel()
            counter = null
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.ivNext -> {
                defaultQuizNum += 1
                if(defaultQuizNum > totalQuizzes) {
                    defaultQuizNum = totalQuizzes
                }

                questionPosition = 0
                getPracticeQuestions()
                /*if(mode == 0)
                getQuestions()
                else
                    getPracticeQuestions()*/
            }
            R.id.ivPrev -> {
                    defaultQuizNum -= 1
                    if(defaultQuizNum < 1) {
                        defaultQuizNum = 1
                    }

                    questionPosition = 0
                getPracticeQuestions()
                /*if(mode == 0)
                    getQuestions()
                else
                    getPracticeQuestions()*/
            }

            R.id.ivGo -> {
                defaultQuizNum = Integer.parseInt(_binding.etPageNum.text.toString())
                questionPosition = 0
                getPracticeQuestions()
                /*if(mode == 0)
                    getQuestions()
                else
                    getPracticeQuestions()*/
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    override fun onNoTimeModeFinished(correctAns: Int, quesCount: Int) {
        resetCounter()
        showCelebrationDialog(correctAns, quesCount)
    }
}
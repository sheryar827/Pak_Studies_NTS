package com.alltech4uforever.pakstudiesnts.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.alltech4uforever.pakstudiesnts.R
import com.alltech4uforever.pakstudiesnts.adapters.QuizListAdapter
import com.alltech4uforever.pakstudiesnts.database.GetQuiz
import com.alltech4uforever.pakstudiesnts.databinding.FragmentPracticeBinding
import com.alltech4uforever.pakstudiesnts.models.QuizModel
import com.alltech4uforever.pakstudiesnts.utils.RecyclerViewItemDecoration


class PracticeFragment : Fragment() {
    private lateinit var _binding: FragmentPracticeBinding

    private var questionList : ArrayList<QuizModel>? = ArrayList()

    private val adapterList by lazy { QuizListAdapter() }

    //private var btns = ArrayList<RadioButton>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPracticeBinding.inflate(inflater, container, false)

        _binding.rvPractice.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = adapterList
            addItemDecoration(RecyclerViewItemDecoration(requireContext(), R.drawable.divider))
        }


        getQuestions()

        itemClickListener()

        return _binding.root
    }

    private fun itemClickListener() {
        adapterList.itemClickListener = { view, item, _ ->

            when(item){
                is QuizModel.QuestionModel -> checkAnswer(view, item)
                else -> throw java.lang.IllegalArgumentException("Invalid ViewType")
            }
        }
    }

    private fun checkAnswer(view: View, item: QuizModel.QuestionModel) {
        val optionSelected = (view as RadioButton).text.toString().trim()
        val correct = optionSelected == item.Answer?.trim()
        if(correct){
            view.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))
        }else{
            view.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            view.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
            Toast.makeText(requireContext(), item.Answer?.trim(), Toast.LENGTH_LONG).show()
        }
    }




    private fun getQuestions() {

        //val b = intent.extras
        //var categoryName = "" // or other values

        /*if (b != null)
            categoryName = b.getString("CATEGORYNAME").toString()*/

        val getQuestions = GetQuiz.getInstance(requireContext())!!
        getQuestions.open()

        //_binding.frameLayout2.visibility = View.VISIBLE




        // 10 questions per quiz
        //totalQuizzes = getQuestions.quesCount(categoryName)/10

        //val lastQuestion = defaultQuizNum * 10
        //val firstQuestion = (lastQuestion - 10) + 1

        questionList = ArrayList()

        val firstQuestion =  arguments?.getInt(
            FIRST_QUES,
            1)

        questionList?.addAll(getQuestions.readQues(firstQuestion!!, arguments?.getString(
            QuizModeFragment.QUIZ_CATG,
            QuizModeFragment.DEFAULT_CATG
        )!!))

        questionList?.shuffle()

        getQuestions.close()

        adapterList.updateList(questionList!!)

        //_binding.tvTotalCount.text =  totalQuizzes.toString()
        //_binding.etPageNum.setText(defaultQuizNum.toString())

        // limit total num of digits enter in edittext field
        //_binding.etPageNum.filters = arrayOf<InputFilter>(InputFilterMinMax("1", totalQuizzes.toString()))


        //updateQuestion()
    }

    companion object {
        //const val DEFAULT_CATG = "english"
        const val TAG = "PracticeFragment"
        private const val QUIZ_CATG = "quiz_category"
        const val FIRST_QUES = "first_question"
        fun newInstance(firstQuestion: Int, quizCategory: String): PracticeFragment {
            val args = Bundle()
            args.putString(QUIZ_CATG , quizCategory)
            args.putInt(FIRST_QUES, firstQuestion)
            val fragment = PracticeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
package com.alltech4uforever.pakstudiesnts.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alltech4uforever.pakstudiesnts.R
import com.alltech4uforever.pakstudiesnts.adapters.QuizListAdapter
import com.alltech4uforever.pakstudiesnts.database.GetQuiz
import com.alltech4uforever.pakstudiesnts.databinding.FragmentNoTimeModeQuizBinding
import com.alltech4uforever.pakstudiesnts.models.QuizModel
import com.alltech4uforever.pakstudiesnts.models.SharedViewModel
import com.alltech4uforever.pakstudiesnts.utils.RecyclerViewItemDecoration


class NoTimeModeQuizFragment : Fragment() {
    private lateinit var _binding: FragmentNoTimeModeQuizBinding
    private var mListener: NoTimeModeQuizFragmentInterface? = null

    private var questionList : ArrayList<QuizModel>? = ArrayList()

    private val adapterList by lazy { QuizListAdapter() }

    private var coinsCount: Int = 0
    private var ansClickCounter: Int = 0

   //private var btns = ArrayList<RadioButton>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentNoTimeModeQuizBinding.inflate(inflater, container, false)

        _binding.rvNoTimeMode.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = adapterList
            addItemDecoration(RecyclerViewItemDecoration(requireContext(), R.drawable.divider))
        }


        getQuestions()

        itemClickListener()

        return _binding.root
    }

    private fun itemClickListener() {
        adapterList.itemClickListener1 = { view, correctOption, item, _ ->
            when(item){
                is QuizModel.QuesModelNOTime -> checkAnswer(view, correctOption, item)
                else -> throw java.lang.IllegalArgumentException("Invalid ViewType")
            }
        }
    }

    private fun checkAnswer(view: View, correctOption:View, item: QuizModel.QuesModelNOTime) {
        val optionSelected = (view as RadioButton).text.toString().trim()
        val correct = optionSelected == item.Answer?.trim()

        ansClickCounter++

        if(ansClickCounter == adapterList.itemCount){
            mListener?.onNoTimeModeFinished(coinsCount, adapterList.itemCount)
        }

        if(correct){
            coinsCount++
            view.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            //view.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.holo_green_dark))
            view.background = ContextCompat.getDrawable(requireContext(),R.drawable.button_valid)
        }else{
            view.setTextColor(ContextCompat.getColor(requireContext(), android.R.color.white))
            //view.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.holo_red_dark))
            view.background = ContextCompat.getDrawable(requireContext(),R.drawable.button_invalid)
            correctOption.background = ContextCompat.getDrawable(requireContext(),R.drawable.button_valid)
            //Toast.makeText(requireContext(), item.Answer?.trim(), Toast.LENGTH_LONG).show()
            /*adapterList.correctOption?.background = ContextCompat.getDrawable(requireContext()
                , R.drawable.button_valid)*/
        }

        var model = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        model.correctAns.value = coinsCount
        model.quesCount.value = questionList!!.size

        //var sharedViewModel = SharedViewModel()

        //sharedViewModel.sharedData.value = coinsCount
    }

    private fun getQuestions() {

        val quizMode = arguments?.getInt(QUIZ_MODE,0)
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

        if(quizMode != 4)
        questionList?.addAll(getQuestions.readQuesNoTime(firstQuestion!!, arguments?.getString(
            QuizModeFragment.QUIZ_CATG,
            QuizModeFragment.DEFAULT_CATG
        )!!))

        //get random questions
        else
            questionList?.addAll(getQuestions.readRandomQues(firstQuestion!!, arguments?.getString(
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is NoTimeModeQuizFragmentInterface) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                        + " must implement OnFragmentInteractionListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }



    interface NoTimeModeQuizFragmentInterface{
        fun onNoTimeModeFinished(correctAns: Int, quesCount: Int)
    }

    companion object {
        //const val DEFAULT_CATG = "english"
        const val TAG = "NoTimeModeQuizFragment"
        private const val QUIZ_CATG = "quiz_category"
        const val FIRST_QUES = "first_question"
        private const val QUIZ_MODE = "quiz_mode"
        fun newInstance(firstQuestion: Int, quizMode: Int, quizCategory: String): NoTimeModeQuizFragment {
            val args = Bundle()
            args.putString(QUIZ_CATG , quizCategory)
            args.putInt(QUIZ_MODE, quizMode)
            args.putInt(FIRST_QUES, firstQuestion)
            val fragment = NoTimeModeQuizFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
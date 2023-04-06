package com.alltech4uforever.pakstudiesnts.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.alltech4uforever.pakstudiesnts.R
import com.alltech4uforever.pakstudiesnts.database.GetQuiz
import com.alltech4uforever.pakstudiesnts.databinding.FragmentQuizBinding
import com.alltech4uforever.pakstudiesnts.models.QuizModel
import java.util.*


class QuizFragment : Fragment() {

    private lateinit var _binding: FragmentQuizBinding
    /*private var mListener: QuizFragmentInterface? = null*/
    private var ans: String = ""
    private var quesNumber = 0
    private lateinit var getQuestions : GetQuiz
    private lateinit var questionList : ArrayList<QuizModel.QuestionModel>
    private var btns = ArrayList<RadioButton>()
    private var answer: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getQuestions = GetQuiz.getInstance(requireContext())!!
        getQuestions.open()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentQuizBinding.inflate(inflater,
                                                container,
                                                false)

        val pageKey = arguments?.getInt(PAGE_KEY, 0)!!
        val categoryName = arguments?.getString(QuizModeFragment.QUIZ_CATG, "english")

        questionList = ArrayList()


        //Get the quiz end point
        //val quesendPoint: Int = pageKey * 10 + 1

        //Get the qui starting point
        var quesstartPoint: Int = (pageKey - 1) * 10
        if (quesstartPoint == 0) {
            quesstartPoint = 1
        }


        questionList.addAll(getQuestions.readQues(quesstartPoint, categoryName!!))
        questionList.shuffle()
        getQuestions.close()


        btns =
            arrayListOf(_binding.optionA,
                _binding.optionB,
                _binding.optionC,
                _binding.optionD)

        val quesText: String = questionList[0].Question

        val optA: String = questionList[0].OptionA.trim()
        val optB: String = questionList[0].OptionB.trim()
        val optC: String = questionList[0].OptionC.trim()
        val optD: String = questionList[0].OptionD.trim()

        val option = arrayOfNulls<String>(4)
        option[0] = optA
        option[1] = optB
        option[2] = optC
        option[3] = optD

        val options = listOf(*option)
        Collections.shuffle(options)

        //ques.setText(question.getQuestion().trim());
        _binding.quizQuestion.text = quesText
        _binding.optionA.text = optA
        _binding.optionB.text = optB
        _binding.optionC.text = optC
        _binding.optionD.text = optD

        answer = questionList[0].Answer.trim()

        radioButtonClicked()

        // Inflate the layout for this fragment
        return _binding.root
    }

    private fun radioButtonClicked() {
        _binding.quizGroup.setOnCheckedChangeListener { radioGroup, _ ->
            onButtonClicked(getRadioButton(radioGroup.checkedRadioButtonId))
            //disable radiobuttons after click
            for (j in 0 until radioGroup.childCount) {
                radioGroup.getChildAt(j).isEnabled = false
            }
            processAnswer()
        }
    }

    private fun processAnswer() {
        _binding.quizGroup.isEnabled = false
        val correct: Boolean
        val optionSelected =
            Objects.requireNonNull<RadioButton>(getRadioButton(_binding.quizGroup.getCheckedRadioButtonId())).text.toString()
                .trim { it <= ' ' }
        correct = optionSelected == answer
        if (correct) {
            getRadioButton(_binding.quizGroup.checkedRadioButtonId).setTextColor(
                ContextCompat.getColor(requireContext(),
                    android.R.color.white
                )
            )
            getRadioButton(_binding.quizGroup.checkedRadioButtonId).background =
                ContextCompat.getDrawable(requireContext(),R.drawable.button_valid)
        } else {
            getRadioButton(_binding.quizGroup.checkedRadioButtonId).setTextColor(
                ContextCompat.getColor(requireContext(),
                    android.R.color.white
                )
            )
            getRadioButton(_binding.quizGroup.checkedRadioButtonId).background =
                ContextCompat.getDrawable(requireContext(),R.drawable.button_invalid)
            determineCorrectAnswer()
        }
        //mListener.onFinished(correct)
    }

    private fun determineCorrectAnswer() {
        for (btn in btns) {
            if (btn.text.toString().trim { it <= ' ' } == answer) {

                    btn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.color.white
                        )
                    )
                btn.background = ContextCompat.getDrawable(requireContext(), R.drawable.button_valid)
                break
            }
        }
    }

    private fun getRadioButton(id: Int): RadioButton {
        var temp: RadioButton? = null
        when (id) {
            R.id.option_a -> temp = _binding.optionA
            R.id.option_b -> temp = _binding.optionB
            R.id.option_c -> temp = _binding.optionC
            R.id.option_d -> temp = _binding.optionD
        }
        return temp!!
    }

    private fun onButtonClicked(btn: RadioButton) {
        for (item in btns) {
            item.setTextColor(
                ContextCompat.getColorStateList(
                    requireContext(),
                    R.color.white
                )
            )
            item.isChecked = false
        }
        btn.setTextColor(
            ContextCompat.getColorStateList(
                requireContext(),
                R.color.colorPrimary
            )
        )
        btn.isChecked = true
    }


    companion object {
        const val TAG = "QuizFragment"
        const val PAGE_KEY = "page_key"
        fun newInstance(pageKey: Int, categoryName: String): QuizFragment {
            val args = Bundle()
            args.putInt(PAGE_KEY, pageKey)
            args.putString(QuizModeFragment.QUIZ_CATG, categoryName)
            val fragment = QuizFragment()
            fragment.arguments = args
            return fragment
        }
    }



}
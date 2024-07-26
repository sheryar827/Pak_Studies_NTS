package com.alltech4uforever.pakstudiesnts.fragments

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.alltech4uforever.pakstudiesnts.R
import com.alltech4uforever.pakstudiesnts.databinding.FragmentQuizBinding
import java.util.Objects


class QuizFragment : Fragment() {

    private lateinit var _binding: FragmentQuizBinding
    private var mListener: QuizFragmentInterface? = null
    //private lateinit var question: QuizModel.QuestionModel
    //private var ans: String = ""
    //private var quesNumber = 0

    var counter: CountDownTimer? = null
    private val quizTime: Long = 15000
    private val timeSec: Long = 1000
    private var counterPosition: Long = 0

    private var btns = ArrayList<RadioButton>()
    private var answer: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //getQuestions = GetQuiz.getInstance(requireContext())!!
        //getQuestions.open()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentQuizBinding.inflate(inflater,
                                                container,
                                                false)




        //val categoryName = arguments?.getString(QuizModeFragment.QUIZ_CATG, QuizModeFragment.DEFAULT_CATG)

        //questionList = ArrayList()


        // 10 questions per quiz
        //val totalQuizzes = getQuestions.quesCount(categoryName!!)/10

        //_binding.tvTotalCount.text =  totalQuizzes.toString()
        //_binding.etPageNum.setText(defaultQuizNum.toString())


        // limit total num of digits enter in edittext field
        //_binding.etPageNum.filters = arrayOf<InputFilter>(LengthFilter(totalQuizzes.toString().length))


        //Get the quiz end point
        //val quesendPoint: Int = pageKey * 10 + 1

        //Get the qui starting point
//        var quesstartPoint: Int = (pageKey - 1) * 10
//        if (quesstartPoint == 0) {
//            quesstartPoint = 1
//        }


        //questionList.addAll(getQuestions.readQues(defaultQuizNum, categoryName))
        //questionList.shuffle()
        //getQuestions.close()

        //question = requireArguments()!!.getParcelable("question")!!


        btns =
            arrayListOf(_binding.optionA,
                _binding.optionB,
                _binding.optionC,
                _binding.optionD)

        val quesText: String? = arguments?.getString(QUESTION, "")

        val optA: String? = arguments?.getString(OPTIONA, "")
        val optB: String? = arguments?.getString(OPTIONB, "")
        val optC: String? = arguments?.getString(OPTIONC, "")
        val optD: String? = arguments?.getString(OPTIOND, "")

        val option = arrayOfNulls<String>(4)
        option[0] = optA
        option[1] = optB
        option[2] = optC
        option[3] = optD

        val options = mutableListOf(*option)
        options.shuffle()

        //ques.setText(question.getQuestion().trim());
        _binding.quizQuestion.text = quesText
        _binding.optionA.text = optA
        _binding.optionB.text = optB
        _binding.optionC.text = optC
        _binding.optionD.text = optD

        answer = arguments?.getString(ANSWER, "")

        radioButtonClicked()

        if (counter != null) counter = null
        counter = object : CountDownTimer(quizTime, timeSec) {
            override fun onTick(l: Long) {
                counterPosition = l
                _binding.txtProgress.text = String.format("%d", l / 1000)
                if (getProgress(l) >= 75) {
                    _binding.progressBar.progressDrawable = ContextCompat.getDrawable(requireContext()
                            ,R.drawable.custom_progressbar_red)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    _binding.progressBar.setProgress(getProgress(l), true)
                } else {
                    _binding.progressBar.progress = getProgress(l)
                }
            }

            override fun onFinish() {
                _binding.txtProgress.text = String.format("%d", 0)
                _binding.progressBar.progressDrawable = ContextCompat.getDrawable(requireContext()
                    ,R.drawable.custom_progressbar_red)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    _binding.progressBar.setProgress(100, true)
                } else {
                    _binding.progressBar.progress = 100
                }
                processAnswer()
            }
        }.start()

        // Inflate the layout for this fragment
        return _binding.root
    }

    fun getProgress(l: Long): Int {
        return (15 - l.toInt() / 1000) * 100 / 15
    }

    private fun radioButtonClicked() {
        _binding.quizGroup.setOnCheckedChangeListener { radioGroup, _ ->
            onButtonClicked(getRadioButton(radioGroup.checkedRadioButtonId)!!)
            //disable radiobuttons after click
            for (j in 0 until radioGroup.childCount) {
                radioGroup.getChildAt(j).isEnabled = false
            }
            processAnswer()
        }
    }

    fun stopCounter() {
        counter?.cancel()
        _binding.txtProgress.text = String.format("%d", 0)
        _binding.progressBar.progress = 0
        _binding.quizGroup.isEnabled = false
    }

    private fun processAnswer() {

        stopCounter()
        /*if (counter != null) counter!!.cancel()
        _binding.txtProgress.text = String.format("%d", 0)
        _binding.progressBar.progress = 0
        _binding.quizGroup.isEnabled = false*/

        val correct: Boolean

        if(getRadioButton(_binding.quizGroup.checkedRadioButtonId) != null) {
            val optionSelected =
                Objects.requireNonNull(getRadioButton(_binding.quizGroup.checkedRadioButtonId))?.text.toString()
                    .trim { it <= ' ' }
            correct = optionSelected == answer
            if (correct) {
                getRadioButton(_binding.quizGroup.checkedRadioButtonId)?.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.white
                    )
                )
                getRadioButton(_binding.quizGroup.checkedRadioButtonId)?.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.button_valid)
            } else {
                getRadioButton(_binding.quizGroup.checkedRadioButtonId)?.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        android.R.color.white
                    )
                )
                getRadioButton(_binding.quizGroup.checkedRadioButtonId)?.background =
                    ContextCompat.getDrawable(requireContext(), R.drawable.button_invalid)
                determineCorrectAnswer()
            }
        }else{
            correct = false
            determineCorrectAnswer()
        }
        mListener?.onFinished(correct)
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

    private fun getRadioButton(id: Int): RadioButton? {
        var temp: RadioButton? = null
        when (id) {
            R.id.option_a -> temp = _binding.optionA
            R.id.option_b -> temp = _binding.optionB
            R.id.option_c -> temp = _binding.optionC
            R.id.option_d -> temp = _binding.optionD
        }
        return temp
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

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is QuizFragmentInterface) {
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
        if (counter != null) {
            counter!!.cancel()
            counter = null
        }
    }



    interface QuizFragmentInterface{
        fun onFinished(correct: Boolean)
    }

    interface CounterControlInterface {
        fun stopCounter(counter: CountDownTimer)
    }


    companion object {
        const val TAG = "QuizFragment"

        private const val QUESTION = "Question"
        private const val OPTIONA = "OptionA"
        private const val OPTIONB = "OptionB"
        private const val OPTIONC = "OptionC"
        private const val OPTIOND = "OptionD"
        private const val ANSWER = "Answer"
        fun newInstance(
            question: String,
            optionA: String,
            optionB: String,
            optionC: String,
            optionD: String,
            answer: String,
        ): QuizFragment {
            val args = Bundle()
            args.putString(QUESTION, question)
            args.putString(OPTIONA, optionA)
            args.putString(OPTIONB, optionB)
            args.putString(OPTIONC, optionC)
            args.putString(OPTIOND, optionD)
            args.putString(ANSWER, answer)
            val fragment = QuizFragment()
            fragment.arguments = args
            return fragment
        }
    }



}
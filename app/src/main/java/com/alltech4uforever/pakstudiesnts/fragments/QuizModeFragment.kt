package com.alltech4uforever.pakstudiesnts.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alltech4uforever.pakstudiesnts.databinding.FragmentQuizTypeBinding


class QuizModeFragment : Fragment(),
    View.OnClickListener {

    private  lateinit var _binding: FragmentQuizTypeBinding
    private var mListener: QuizTypeFragmentInterface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentQuizTypeBinding.inflate(inflater, container, false)

        _binding.optionNotimemode.setOnClickListener(this)
        _binding.optionTimemode.setOnClickListener(this)
        _binding.optionBeatthetimemode.setOnClickListener(this)
        _binding.optionPrepmode.setOnClickListener(this)
        _binding.optionChapmode.setOnClickListener(this)

        // Inflate the layout for this fragment
        return _binding.root
    }



    override fun onClick(view: View) {
        when (view) {
            _binding.optionNotimemode -> {
                mListener?.noTimeMode(arguments?.getString(QUIZ_CATG, DEFAULT_CATG)!!)
            }
            _binding.optionTimemode -> {
                /*Toast.makeText(requireContext(), "CLICK", Toast.LENGTH_LONG).show()*/
                mListener?.timeMode(arguments?.getString(QUIZ_CATG, DEFAULT_CATG)!!)
            }

            _binding.optionBeatthetimemode -> {
                mListener?.beatTheTimeMode(arguments?.getString(QUIZ_CATG, DEFAULT_CATG)!!)
            }

            _binding.optionPrepmode -> {
                mListener?.preparationMode(arguments?.getString(QUIZ_CATG, DEFAULT_CATG)!!)
            }

            _binding.optionChapmode -> {
                mListener?.challengeMode(arguments?.getString(QUIZ_CATG, DEFAULT_CATG)!!)
            }

        }
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is QuizTypeFragmentInterface) {
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

    interface QuizTypeFragmentInterface{
        fun noTimeMode(quizCategory: String)
        fun timeMode(quizCategory: String)
        fun beatTheTimeMode(quizCategory: String)
        fun preparationMode(quizCategory: String)
        fun challengeMode(quizCategory: String)
    }

    companion object {
        const val DEFAULT_CATG = "english"
        const val TAG = "QuizTypeFragment"
        const val QUIZ_CATG = "quiz_category"
        fun newInstance(quizCategory: String): QuizModeFragment {
            val args = Bundle()
            args.putString(QUIZ_CATG , quizCategory)
            val fragment = QuizModeFragment()
            fragment.arguments = args
            return fragment
        }
    }


}
package com.alltech4uforever.pakstudiesnts.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alltech4uforever.pakstudiesnts.R
import com.alltech4uforever.pakstudiesnts.databinding.FragmentQuizTypeBinding


class QuizTypeFragment : Fragment(),
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

        _binding.btnNoTimeMode.setOnClickListener(this)
        _binding.btnTimeMode.setOnClickListener(this)
        _binding.btnBeatTheTimeMode.setOnClickListener(this)
        _binding.btnPreparationMode.setOnClickListener(this)

        // Inflate the layout for this fragment
        return _binding.root
    }



    override fun onClick(view: View) {
        when (view) {
            _binding.btnNoTimeMode -> {
                mListener?.noTimeMode()
            }
            _binding.btnTimeMode -> {
                mListener?.timeMode()
            }

            _binding.btnBeatTheTimeMode -> {
                mListener?.beatTheTimeMode()
            }

            _binding.btnPreparationMode -> {
                mListener?.preparationMode()
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
        fun noTimeMode()
        fun timeMode()
        fun beatTheTimeMode()
        fun preparationMode()
    }

    companion object {
        const val TAG = "QuizTypeFragment"
    }


}
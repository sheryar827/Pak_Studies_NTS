package com.alltech4uforever.pakstudiesnts.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.alltech4uforever.pakstudiesnts.R
import com.alltech4uforever.pakstudiesnts.adapters.QuizListAdapter
import com.alltech4uforever.pakstudiesnts.database.GetQuiz
import com.alltech4uforever.pakstudiesnts.databinding.FragmentQuizListBinding
import com.alltech4uforever.pakstudiesnts.models.QuizModel
import com.alltech4uforever.pakstudiesnts.utils.RecyclerViewItemDecoration


class QuizListFragment : Fragment() {

    private var mListener: QuizListFragmentListener? = null

    private lateinit var _binding: FragmentQuizListBinding

    private lateinit var getQuizCount : GetQuiz

    private lateinit var quizList: ArrayList<QuizModel>

    private val adapterList by lazy { QuizListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getQuizCount = GetQuiz.getInstance(requireContext())!!
        getQuizCount.open()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentQuizListBinding.inflate(inflater, container, false)

        _binding.quizListRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = adapterList
            addItemDecoration(RecyclerViewItemDecoration(requireContext(), R.drawable.divider))
        }

        adapterList.itemClickListener = { _, item, _ ->
            //view.startAnimation(btnAnim)
            when(item){
                is QuizModel.QuizListModel -> mListener?.startQuiz(item.page)
                else -> throw java.lang.IllegalArgumentException("Invalid ViewType")
            }
            //Log.d("CLICKED", message)
            //Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        populateQuizList(getQuizCount.quesCount())

        getQuizCount.close()

        return _binding.root
    }

    private fun populateQuizList(quesCount: Int) {
        quizList = ArrayList()
        for (i in 1 .. quesCount/10){
            val quiz = QuizModel.QuizListModel("Quiz $i", i)
            quizList.add(quiz)
        }

        adapterList.updateList(quizList)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is QuizListFragmentListener) {
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

    interface QuizListFragmentListener{
        fun startQuiz(pageKey: Int)
    }

    companion object {
        const val TAG = "QuizListFragment"

        fun newInstance(quizMode: Int): QuizListFragment {
            val args = Bundle()
            args.putInt("quiz_mode", quizMode)
            val fragment = QuizListFragment()
            fragment.arguments = args
            return fragment
        }

    }
}
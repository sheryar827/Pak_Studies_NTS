package com.alltech4uforever.pakstudiesnts.fragments

import android.content.Context
import android.os.Bundle
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

        //getQuizCount.getTableName()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentQuizListBinding.inflate(inflater, container, false)

        val categoryName = arguments?.getString(QuizModeFragment.QUIZ_CATG, "english")

        _binding.quizListRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = adapterList
            addItemDecoration(RecyclerViewItemDecoration(requireContext(), R.drawable.divider))
        }

        adapterList.itemClickListener = { _, item, _ ->
            //view.startAnimation(btnAnim)
            when(item){
                is QuizModel.QuizListModel -> mListener?.startQuiz(item.page, categoryName!!)
                else -> throw java.lang.IllegalArgumentException("Invalid ViewType")
            }
            //Log.d("CLICKED", message)
            //Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        //populateQuizList(getQuizCount.quesCount())

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
        fun startQuiz(pageKey: Int, categoryName: String)
    }

    companion object {
        const val TAG = "QuizListFragment"
        const val QUIZ_MODE = "quiz_mode"
        fun newInstance(quizMode: Int, categoryName: String): QuizListFragment {
            val args = Bundle()
            args.putInt(QUIZ_MODE, quizMode)
            args.putString(QuizModeFragment.QUIZ_CATG, categoryName)
            val fragment = QuizListFragment()
            fragment.arguments = args
            return fragment
        }

    }
}
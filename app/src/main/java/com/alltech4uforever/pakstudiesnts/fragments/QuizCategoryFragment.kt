package com.alltech4uforever.pakstudiesnts.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.alltech4uforever.pakstudiesnts.adapters.QuizListAdapter
import com.alltech4uforever.pakstudiesnts.database.GetQuiz
import com.alltech4uforever.pakstudiesnts.databinding.FragmentQuizCategoryBinding
import com.alltech4uforever.pakstudiesnts.models.QuizModel


class QuizCategoryFragment : Fragment() {

    private var mListener: QuizCategoryFragmentListener? = null

    private lateinit var _binding: FragmentQuizCategoryBinding

    private lateinit var getQuizCategories : GetQuiz

    private lateinit var quizCategoryList: ArrayList<QuizModel>

    private val adapterList by lazy { QuizListAdapter() }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getQuizCategories = GetQuiz.getInstance(requireContext())!!
        getQuizCategories.open()

        quizCategoryList = ArrayList()

        quizCategoryList.addAll(getQuizCategories.getTableName())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentQuizCategoryBinding.inflate(inflater, container, false)

        _binding.rvQuizCategory.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = adapterList
            //addItemDecoration(RecyclerViewItemDecoration(requireContext(), R.drawable.divider))
        }

        adapterList.itemClickListener = { _, item, _ ->
            //view.startAnimation(btnAnim)
            when(item){
                is QuizModel.QuizCategoryModel -> mListener?.quizCategorySelected(item.categoryName!!)
                else -> throw java.lang.IllegalArgumentException("Invalid ViewType")
            }
        }


        adapterList.updateList(quizCategoryList)

        // Inflate the layout for this fragment
        return _binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is QuizCategoryFragmentListener) {
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

    interface QuizCategoryFragmentListener{
        fun quizCategorySelected(categoryName: String)
    }

    companion object {
        const val TAG = "QuizCategoryFragment"
    }
}
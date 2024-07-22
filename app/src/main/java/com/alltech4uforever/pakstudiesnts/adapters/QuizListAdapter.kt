package com.alltech4uforever.pakstudiesnts.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alltech4uforever.pakstudiesnts.R
import com.alltech4uforever.pakstudiesnts.databinding.RvNotimeModelBinding
import com.alltech4uforever.pakstudiesnts.databinding.RvPracticeModelBinding
import com.alltech4uforever.pakstudiesnts.databinding.RvQuizCategoryModelBinding
import com.alltech4uforever.pakstudiesnts.databinding.RvQuizlistModelBinding
import com.alltech4uforever.pakstudiesnts.models.QuizModel

class QuizListAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var itemList = ArrayList<QuizModel>()

    var itemClickListener: ((view: View, item: QuizModel, position: Int) -> Unit)? = null
    var itemClickListener1: ((view:View, correctOption:View, item:QuizModel, position:Int) -> Unit)? = null
    //var correctOption: View? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            R.layout.rv_quizlist_model -> ViewHolder.QuizListViewHolder(
                RvQuizlistModelBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            R.layout.rv_quiz_category_model -> ViewHolder.QuizCategoryViewHolder(
                RvQuizCategoryModelBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            R.layout.rv_practice_model -> ViewHolder.PracticeViewHolder(
                RvPracticeModelBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            R.layout.rv_notime_model -> ViewHolder.NoTimeViewHolder(
                RvNotimeModelBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            else -> throw java.lang.IllegalArgumentException("Invalid ViewType")
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemClickListener = itemClickListener
        holder.itemClickListener1 = itemClickListener1
        //holder.correctOption = correctOption
        // Here you apply the animation when the view is bound
        /*setAnimation(holder.itemView, position)*/
        when(holder){
            is ViewHolder.QuizListViewHolder -> holder.bind(itemList[position] as QuizModel.QuizListModel)
            is ViewHolder.QuizCategoryViewHolder -> holder.bind(itemList[position] as QuizModel.QuizCategoryModel)
            is ViewHolder.PracticeViewHolder -> holder.bind(itemList[position] as QuizModel.QuestionModel)
            is ViewHolder.NoTimeViewHolder -> holder.bind(itemList[position] as QuizModel.QuesModelNOTime)
            //else -> throw java.lang.IllegalArgumentException("Invalid ViewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
         /*Log.d("INVALID ITEM", itemList[position].toString())*/
        return when(itemList[position]){
            is QuizModel.QuizListModel -> R.layout.rv_quizlist_model
            is QuizModel.QuizCategoryModel -> R.layout.rv_quiz_category_model
            is QuizModel.QuestionModel -> R.layout.rv_practice_model
            is QuizModel.QuesModelNOTime -> R.layout.rv_notime_model
            //else->throw IllegalStateException("Invalid Item")
        }
    }

    fun updateList(updatedList: ArrayList<QuizModel>){
        //val diffCallback = SubsCallback(itemList, updatedList)
        // val diffSubs = DiffUtil.calculateDiff(diffCallback)
        itemList.clear()
        itemList.addAll(updatedList)
        //diffSubs.dispatchUpdatesTo(this)
        notifyDataSetChanged()
    }
}
package com.alltech4uforever.pakstudiesnts.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.alltech4uforever.pakstudiesnts.R
import com.alltech4uforever.pakstudiesnts.databinding.RvQuizlistModelBinding
import com.alltech4uforever.pakstudiesnts.models.QuizModel

class QuizListAdapter : RecyclerView.Adapter<ViewHolder>() {

    var itemList = ArrayList<QuizModel>()

    var itemClickListener: ((view: View, item: QuizModel, position: Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when(viewType) {
            R.layout.rv_quizlist_model -> ViewHolder.QuizListViewHolder(
                RvQuizlistModelBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> throw java.lang.IllegalArgumentException("Invalid ViewType")
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemClickListener = itemClickListener
        // Here you apply the animation when the view is bound
        /*setAnimation(holder.itemView, position)*/
        when(holder){
            is ViewHolder.QuizListViewHolder -> holder.bind(itemList[position] as QuizModel.QuizListModel)
        }
    }

    override fun getItemViewType(position: Int): Int {
        /* Log.d("INVALID ITEM", itemList[position].toString())*/
        return when(itemList[position]){
            is QuizModel.QuizListModel -> R.layout.rv_quizlist_model
            else->throw IllegalStateException("Invalid Item")
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
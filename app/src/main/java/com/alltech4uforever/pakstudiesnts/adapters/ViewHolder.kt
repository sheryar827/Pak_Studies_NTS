package com.alltech4uforever.pakstudiesnts.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.alltech4uforever.pakstudiesnts.databinding.RvQuizCategoryModelBinding
import com.alltech4uforever.pakstudiesnts.databinding.RvQuizlistModelBinding
import com.alltech4uforever.pakstudiesnts.models.QuizModel

sealed class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root){

    var itemClickListener: ((view: View, item: QuizModel, position: Int) -> Unit)? = null

    class QuizListViewHolder(private val binding: RvQuizlistModelBinding): ViewHolder(binding){
        fun bind(item: QuizModel.QuizListModel){

            binding.item = item

            binding.root.setOnClickListener {
                itemClickListener?.invoke(it, item, adapterPosition)
            }

        }
    }

    class QuizCategoryViewHolder(private val binding: RvQuizCategoryModelBinding): ViewHolder(binding){
        fun bind(item: QuizModel.QuizCategoryModel){

            binding.category = item

            binding.root.setOnClickListener {
                itemClickListener?.invoke(it, item, adapterPosition)
            }

        }
    }
}
package com.alltech4uforever.pakstudiesnts.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.alltech4uforever.pakstudiesnts.databinding.RvNotimeModelBinding
import com.alltech4uforever.pakstudiesnts.databinding.RvPracticeModelBinding
import com.alltech4uforever.pakstudiesnts.databinding.RvQuizCategoryModelBinding
import com.alltech4uforever.pakstudiesnts.databinding.RvQuizlistModelBinding
import com.alltech4uforever.pakstudiesnts.models.QuizModel

sealed class ViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root){

    var itemClickListener: ((view: View, item: QuizModel, position: Int) -> Unit)? = null
    var itemClickListener1: ((view:View, correctOption: View, item:QuizModel, position: Int) -> Unit)? = null

    //var correctOption:View? = null

    class QuizListViewHolder(private val binding: RvQuizlistModelBinding): ViewHolder(binding){
        fun bind(item: QuizModel.QuizListModel){

            binding.item = item

            binding.root.setOnClickListener {
                itemClickListener?.invoke(it, item, adapterPosition)
            }

        }
    }

    class PracticeViewHolder(private val binding: RvPracticeModelBinding): ViewHolder(binding){
        /*val btns = arrayOf(
            binding.optionA,
            binding.optionB,
            binding.optionC,
            binding.optionD
        )*/
        fun bind(item: QuizModel.QuestionModel){

            binding.item = item

        }

    }

    class NoTimeViewHolder(private val binding: RvNotimeModelBinding): ViewHolder(binding){
        val btns = arrayOf(
            binding.optionA,
            binding.optionB,
            binding.optionC,
            binding.optionD
        )


        fun bind(item: QuizModel.QuesModelNOTime){

            binding.notime = item

            var co:View? = null


            binding.optionA.setOnClickListener { it ->
                for(i in btns){
                    if(i.text.toString().trim() == item.Answer?.trim()){
                        co = i
                        break
                    }
                }
                itemClickListener1?.invoke(it, co!!,item, adapterPosition)
                for(i in btns){
                    i.isClickable = false
                }

                //binding.tvAnswer.isVisible = true

            }

            binding.optionB.setOnClickListener {
                for(i in btns){
                    if(i.text.toString().trim() == item.Answer?.trim()){
                        co = i
                        break
                    }
                }
                itemClickListener1?.invoke(it, co!!,item, adapterPosition)
                for(i in btns){
                    i.isClickable = false
                }

                //binding.tvAnswer.isVisible = true

            }

            binding.optionC.setOnClickListener {
                for(i in btns){
                    if(i.text.toString().trim() == item.Answer?.trim()){
                        co = i
                        break
                    }
                }
                itemClickListener1?.invoke(it, co!!,item, adapterPosition)
                for(i in btns){
                    i.isClickable = false

                }

                //binding.tvAnswer.isVisible = true

            }

            binding.optionD.setOnClickListener {
                for(i in btns){
                    if(i.text.toString().trim() == item.Answer?.trim()){
                        co = i
                        break
                    }
                }
                itemClickListener1?.invoke(it, co!!,item, adapterPosition)
                for(i in btns){
                    i.isClickable = false
                    /*if(i.text.toString().trim() == item.Answer?.trim())
                        correctOption = i*/
                }

                //binding.tvAnswer.isVisible = true
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
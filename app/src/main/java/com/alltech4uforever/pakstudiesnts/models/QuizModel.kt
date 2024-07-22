package com.alltech4uforever.pakstudiesnts.models


sealed class QuizModel{
    data class QuizListModel(val name: String? = null,
                             val page: Int = 0
    ): QuizModel()

    data class QuizCategoryModel(val categoryName: String? = null): QuizModel()


    data class QuestionModel(
        val _id: Int = 0,
        val Question: String? = "",
        val OptionA: String? = "",
        val OptionB: String? = "",
        val OptionC: String? = "",
        val OptionD: String? = "",
        val Answer: String? = ""
    ): QuizModel()

    data class QuesModelNOTime(
        val _id: Int = 0,
        val Question: String? = "",
        val OptionA: String? = "",
        val OptionB: String? = "",
        val OptionC: String? = "",
        val OptionD: String? = "",
        val Answer: String? = ""
    ): QuizModel()

}

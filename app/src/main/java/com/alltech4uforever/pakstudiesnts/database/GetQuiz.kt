package com.alltech4uforever.pakstudiesnts.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.alltech4uforever.pakstudiesnts.models.QuizModel

class GetQuiz(context: Context) {
    private val openHelper: SQLiteOpenHelper
    private var database: SQLiteDatabase? = null

    init {
        openHelper = DatabaseOpenHelper(context)
    }

    fun open() {
        database = openHelper.readableDatabase
    }

    fun close() {
        if (database != null) {
            database!!.close()
        }
    }

    fun readQuestion(i: Int): String //Used to read the data from the Des.db file where id is given and we choose id randomly
    {
        var ans =
            "" //string that contains the required field  note that Ans is just a local string not related to Answer or Option...
        val c = database!!.rawQuery(
            "SELECT $Question FROM $Table_name WHERE $uid = $i",
            null
        ) //cursor to that query
        ans = if (c.moveToFirst()) c.getString(0) else ""
        c.close()

        return ans
    }

    fun readQues(quesStart:Int): ArrayList<QuizModel.QuestionModel>{
        var questionList = ArrayList<QuizModel.QuestionModel>()

        val c = database!!.rawQuery(
            "SELECT * FROM $Table_name LIMIT $QUESLIMIT OFFSET $quesStart",
            null
        ) //cursor to that query

        // Get 10 Questions from database
            if (c.moveToFirst()) {
                do {
                    // on below line we are adding the data from
                    // cursor to our array list.
                    val question = QuizModel.QuestionModel(
                        c.getInt(0),
                        c.getString(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getString(5),
                        c.getString(6)
                    )
                    questionList.add(question)
                } while (c.moveToNext())

            }

            c.close()

        return questionList
    }

    fun readOptionA(i: Int): String //Used to read the data from the Des.db file where id is given and we choose id randomly
    {
        var ans =
            "" //string that contains the required field  note that Ans is just a local string not related to Answer or Option...
        val c = database!!.rawQuery(
            "SELECT $OptionA FROM $Table_name WHERE $uid = $i",
            null
        ) //cursor to that query
        ans = if (c.moveToFirst()) c.getString(0) else ""
        c.close()
        return ans
    }

    fun readOptionB(i: Int): String //Used to read the data from the Des.db file where id is given and we choose id randomly
    {
        var ans =
            "" //string that contains the required field  note that Ans is just a local string not related to Answer or Option...
        val c = database!!.rawQuery(
            "SELECT $OptionB FROM $Table_name WHERE $uid = $i",
            null
        ) //cursor to that query
        ans = if (c.moveToFirst()) c.getString(0) else ""
        c.close()
        return ans
    }

    fun readOptionC(i: Int): String //Used to read the data from the Des.db file where id is given and we choose id randomly
    {
        var ans =
            "" //string that contains the required field  note that Ans is just a local string not related to Answer or Option...
        val c = database!!.rawQuery(
            "SELECT $OptionC FROM $Table_name WHERE $uid = $i",
            null
        ) //cursor to that query
        ans = if (c.moveToFirst()) c.getString(0) else ""
        c.close()
        return ans
    }

    fun readOptionD(i: Int): String //Used to read the data from the Des.db file where id is given and we choose id randomly
    {
        var ans =
            "" //string that contains the required field  note that Ans is just a local string not related to Answer or Option...
        val c = database!!.rawQuery(
            "SELECT $OptionD FROM $Table_name WHERE $uid = $i",
            null
        ) //cursor to that query
        ans = if (c.moveToFirst()) c.getString(0) else ""
        c.close()
        return ans
    }

    fun readAnswer(i: Int): String //Used to read the data from the Des.db file where id is given and we choose id randomly
    {
        var ans = "" //string that contains the required field
        val c = database!!.rawQuery(
            "SELECT $Answer FROM $Table_name WHERE $uid = $i",
            null
        ) //cursor to that query
        ans = if (c.moveToFirst()) c.getString(0) else ""
        c.close()
        return ans
    }

    // return no of rows
    fun quesCount() : Int{

        val c = database!!.rawQuery(
            "SELECT COUNT(*) FROM $Table_name",
            null
        )

        val count = if(c.moveToFirst()) c.getInt(0) else 0

        c.close()

        return count

    }

    companion object {
        private var instance: GetQuiz? = null
        private const val Table_name = "pakstudies" //name of table
        private const val uid = "_id" //name of column1
        private const val Question = "Question" //name of column2
        private const val OptionA = "OptionA" //name of column3
        private const val OptionB = "OptionB" //name of column4
        private const val OptionC = "OptionC" //name of column5
        private const val OptionD = "OptionD" //name of column6
        private const val Answer = "Answer" //name of column7
        private const val QUESLIMIT = 10 // 10 Question in one time
        fun getInstance(context: Context): GetQuiz? {
            if (instance == null) {
                instance = GetQuiz(context)
            }
            return instance
        }
    }
}
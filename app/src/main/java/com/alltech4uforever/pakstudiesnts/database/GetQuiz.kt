package com.alltech4uforever.pakstudiesnts.database

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
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

    /*fun readQuestion(i: Int): String //Used to read the data from the Des.db file where id is given and we choose id randomly
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
    }*/

    fun readQues(quesStart:Int, tableName: String): ArrayList<QuizModel.QuestionModel>{
        val questionList = ArrayList<QuizModel.QuestionModel>()

        val c = database!!.rawQuery(
            "SELECT * FROM $tableName LIMIT $QUESLIMIT OFFSET $quesStart",
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

    fun readQuesNoTime(quesStart:Int, tableName: String): ArrayList<QuizModel.QuesModelNOTime>{
        val questionList = ArrayList<QuizModel.QuesModelNOTime>()

        val c = database!!.rawQuery(
            "SELECT * FROM $tableName LIMIT $QUESLIMIT OFFSET $quesStart",
            null
        ) //cursor to that query

        // Get 10 Questions from database
        if (c.moveToFirst()) {
            do {
                // on below line we are adding the data from
                // cursor to our array list.
                val question = QuizModel.QuesModelNOTime(
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

    /*fun readOptionA(i: Int): String //Used to read the data from the Des.db file where id is given and we choose id randomly
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
    }*/

    /*fun readOptionB(i: Int): String //Used to read the data from the Des.db file where id is given and we choose id randomly
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
    }*/

    /*fun readOptionC(i: Int): String //Used to read the data from the Des.db file where id is given and we choose id randomly
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
    }*/

    /*fun readOptionD(i: Int): String //Used to read the data from the Des.db file where id is given and we choose id randomly
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
    }*/

    /*fun readAnswer(i: Int): String //Used to read the data from the Des.db file where id is given and we choose id randomly
    {
        var ans = "" //string that contains the required field
        val c = database!!.rawQuery(
            "SELECT $Answer FROM $Table_name WHERE $uid = $i",
            null
        ) //cursor to that query
        ans = if (c.moveToFirst()) c.getString(0) else ""
        c.close()
        return ans
    }*/

    // return no of rows
    fun quesCount(tableName: String) : Int{

        val c = database!!.rawQuery(
            "SELECT COUNT(*) FROM $tableName",
            null
        )

        val count = if(c.moveToFirst()) c.getInt(0) else 0

        c.close()

        return count

    }

    fun getTableName() : ArrayList<QuizModel.QuizCategoryModel>{
        val catgoryList =  ArrayList<QuizModel.QuizCategoryModel>()
        val c: Cursor = database!!.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null)

        if (c.moveToFirst()) {
            while (!c.isAfterLast) {
                catgoryList.add(QuizModel.QuizCategoryModel(c.getString(0)))
                c.moveToNext()
            }
        }

        c.close()

        // removing non usable tables
        catgoryList.remove(QuizModel.QuizCategoryModel(sqlite_sequence))
        catgoryList.remove(QuizModel.QuizCategoryModel(android_metadata))

        return catgoryList
    }

    companion object {
        private var instance: GetQuiz? = null
        private const val sqlite_sequence = "sqlite_sequence"
        private const val android_metadata = "android_metadata"
        //private const val Table_name = "" //name of table
        //private const val uid = "id" //name of column1
        //private const val Question = "question" //name of column2
        //private const val OptionA = "optiona" //name of column3
        //private const val OptionB = "optionb" //name of column4
        //private const val OptionC = "optionc" //name of column5
        //private const val OptionD = "optiond" //name of column6
        //private const val Answer = "answer" //name of column7
        private const val QUESLIMIT = 10 // 10 Question in one time
        fun getInstance(context: Context): GetQuiz? {
            if (instance == null) {
                instance = GetQuiz(context)
            }
            return instance
        }
    }
}
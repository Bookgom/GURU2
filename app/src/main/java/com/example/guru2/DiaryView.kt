package com.example.guru2

import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DiaryView : AppCompatActivity() {
    lateinit var dbManager: DBManager
    lateinit var sqlitedb : SQLiteDatabase

    lateinit var dateView : TextView
    lateinit var todayView: TextView
    lateinit var contextView: TextView
//    lateinit var fabToEdit : FloatingActionButton

    lateinit var str_date : String
    lateinit var str_today : String
    lateinit var str_context : String

    lateinit var deleteBtn : Button
//    lateinit var editBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_view)

        dateView = findViewById(R.id.dateView)
        todayView = findViewById(R.id.todayView)
        contextView = findViewById(R.id.contextView)
        deleteBtn = findViewById(R.id.deleteBtn)
//        editBtn = findViewById(R.id.editBtn)
        val intent = intent
        str_date = intent.getStringExtra("intent_date").toString()

        dbManager = DBManager(this, "diary", null, 1)
        sqlitedb = dbManager.readableDatabase

        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM diary WHERE date = '"+str_date+"';", null)

        if(cursor.moveToNext()){
            str_today = cursor.getString(cursor.getColumnIndex("todayText")).toString()
            str_context = cursor.getString(cursor.getColumnIndex("context")).toString()
        }

//        editBtn.setOnClickListener {
//            val intent = Intent(this, DiaryView::class.java)
//            intent.putExtra("intent_date",str_date)
//            startActivity(intent)
//            sqlitedb=dbManager.writableDatabase
//            sqlitedb.execSQL("UPDATE diary SET todayText = "+todayView.text+" WHERE date = '"+dateView.text.toString()+"';")
//            sqlitedb.close()
//
//        }

        deleteBtn.setOnClickListener {

            sqlitedb=dbManager.writableDatabase
            sqlitedb.execSQL("DELETE FROM diary WHERE date = '"+dateView.text.toString()+"';")
            sqlitedb.close()
            onBackPressed()
        }



        cursor.close()
        sqlitedb.close()
        dbManager.close()

        dateView.text = str_date
        todayView.text = str_today
        contextView.text = str_context + "\n"
    }


}
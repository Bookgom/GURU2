package com.example.guru2

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView

class DiaryList : AppCompatActivity() {
    lateinit var dbManager : DBManager
    lateinit var sqlitedb : SQLiteDatabase
    lateinit var layout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()

        setContentView(R.layout.activity_diary_list)


        dbManager = DBManager(this, "diary", null, 1)
        sqlitedb = dbManager.readableDatabase

        layout = findViewById(R.id.diary)
        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM diary;", null)

        var num : Int = 0
        while(cursor.moveToNext()){
            var str_date = cursor.getString(cursor.getColumnIndex("date")).toString()
            var str_today = cursor.getString(cursor.getColumnIndex("todayText")).toString()
            var str_context = cursor.getString(cursor.getColumnIndex("context")).toString()


            var layout_item : LinearLayout = LinearLayout(this)
            layout_item.orientation=LinearLayout.VERTICAL
            layout_item.setPadding(20,10,20,10)
            layout_item.id=num
            layout_item.setTag(str_date)

            var dateView : TextView = TextView(this)
            dateView.text = str_date
            dateView.textSize = 30F
            dateView.setBackgroundColor(Color.LTGRAY)
            layout_item.addView(dateView)

            var todayView : TextView = TextView(this)
            todayView.text = str_today
            layout_item.addView(todayView)

            var contextView : TextView = TextView(this)
            contextView.text = str_context
            contextView.setSingleLine(true)
            layout_item.addView(contextView)

            layout_item.setOnClickListener{
                val intent = Intent(this, DiaryView::class.java)
                intent.putExtra("intent_date",str_date)
                startActivity(intent)
            }
            layout.addView(layout_item)
            num++
        }
        cursor.close()
        sqlitedb.close()
        dbManager.close()
    }


}
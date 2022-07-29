package com.example.guru2

import android.app.DatePickerDialog
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.database.sqlite.SQLiteException


class Diary : AppCompatActivity() {
    lateinit var dateBtn : Button
    lateinit var textView_date : TextView
    lateinit var dbManager: DBManager
    lateinit var sqlitedb : SQLiteDatabase
    lateinit var finishBtn : Button
    lateinit var edtToday : EditText
    lateinit var edtContext : EditText

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        textView_date = findViewById(R.id.date)
        dateBtn = findViewById(R.id.dateBtn)
        finishBtn = findViewById(R.id.finishBtn)
        edtToday = findViewById(R.id.edtToday)
        edtContext = findViewById(R.id.edtContext)
        dbManager = DBManager(this, "diary", null, 1)

        val current : LocalDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
        textView_date.setText(current.format(formatter))
        //추가코드 여기부터

//        val intent = intent
//        var str_date = intent.getStringExtra("intent_date").toString()
//        if(str_date!=null) {
//            sqlitedb = dbManager.readableDatabase
//
//            val cursor: Cursor
//            cursor = sqlitedb.rawQuery("SELECT * FROM diary WHERE date = '" + str_date + "';", null)
//
//            if (cursor.moveToNext()) {
//                var str_today = cursor.getString(cursor.getColumnIndex("todayText")).toString()
//                var str_context = cursor.getString(cursor.getColumnIndex("context")).toString()
//
//                dateBtn.setText(str_date)
//                edtToday.setText(str_today)
//                edtContext.setText(str_context)
//            }
//        }
        //여기까지

            dateBtn.setOnClickListener {
                val today = GregorianCalendar()
                val year: Int = today.get(Calendar.YEAR)
                val month: Int = today.get(Calendar.MONTH)
                val date: Int = today.get(Calendar.DATE)
                val dlg = DatePickerDialog(this, object : DatePickerDialog.OnDateSetListener {
                    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                        textView_date.setText("${year}년 ${month+1}월 ${dayOfMonth}일")
                    }
                }, year, month, date)
                dlg.show()
            }

        finishBtn.setOnClickListener {
            var str_date : String = textView_date.text.toString()
            var str_todayText : String = edtToday.text.toString()
            var str_context : String = edtContext.text.toString()


            sqlitedb = dbManager.writableDatabase
            if(sqlitedb!=null){
                try {
                    sqlitedb.execSQL("INSERT INTO diary VALUES ('"+str_date+"', '"+str_todayText+"', '"+str_context+"');")
                }catch (e : SQLiteException){
                    Toast.makeText(this,"이미 오늘의 일기를 작성했습니다.\n변경을 원하면 해당 일기를 제거하세요.", Toast.LENGTH_LONG).show()
                }
            }





            sqlitedb.close()

            val intent = Intent(this, DiaryView::class.java)
            intent.putExtra("intent_date", str_date);
            startActivity(intent)
        }
    }
}
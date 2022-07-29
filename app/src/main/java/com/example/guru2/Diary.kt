package com.example.guru2

import android.app.DatePickerDialog
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.icu.util.Calendar
import android.icu.util.GregorianCalendar
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


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
            sqlitedb.execSQL("INSERT INTO diary VALUES ('"+str_date+"', '"+str_todayText+"', "+str_context+"');")
            sqlitedb.close()

            val intent = Intent(this, DiaryView::class.java)
            intent.putExtra("intent_date", str_date);
            startActivity(intent)
        }
    }
}
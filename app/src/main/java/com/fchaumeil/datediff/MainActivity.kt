package com.fchaumeil.datediff

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {

    private val prefs by lazy { getSharedPreferences("datediff_prefs", MODE_PRIVATE) }
    private val storeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val displayFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupDateRow(1, R.id.tv_date1, R.id.btn_pick1)
        setupDateRow(2, R.id.tv_date2, R.id.btn_pick2)
        setupDateRow(3, R.id.tv_date3, R.id.btn_pick3)
    }

    private fun setupDateRow(num: Int, tvId: Int, btnId: Int) {
        val tv = findViewById<TextView>(tvId)
        val btn = findViewById<Button>(btnId)
        refreshLabel(num, tv)

        btn.setOnClickListener {
            val current = loadDate(num) ?: LocalDate.now()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    val picked = LocalDate.of(year, month + 1, day)
                    prefs.edit().putString("date$num", picked.format(storeFormatter)).apply()
                    refreshLabel(num, tv)
                },
                current.year,
                current.monthValue - 1,
                current.dayOfMonth
            ).show()
        }
    }

    private fun refreshLabel(num: Int, tv: TextView) {
        val date = loadDate(num)
        tv.text = date?.format(displayFormatter) ?: getString(R.string.not_set)
    }

    private fun loadDate(num: Int): LocalDate? {
        val s = prefs.getString("date$num", null) ?: return null
        return runCatching { LocalDate.parse(s, storeFormatter) }.getOrNull()
    }
}

package com.fchaumeil.datediff

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.abs

class CompareActivity : AppCompatActivity() {

    private val prefs by lazy { getSharedPreferences("datediff_prefs", MODE_PRIVATE) }
    private val storeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val displayFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare)

        @Suppress("DEPRECATION")
        val imageUri: Uri? = intent.getParcelableExtra(Intent.EXTRA_STREAM)
        val photoDate = imageUri?.let { readExifDate(it) }

        val tvPhotoDate = findViewById<TextView>(R.id.tv_photo_date)
        val tvResult = findViewById<TextView>(R.id.tv_result)

        tvPhotoDate.text = if (photoDate != null) {
            getString(R.string.photo_date_label, photoDate.format(displayFormatter))
        } else {
            getString(R.string.photo_date_unknown)
        }

        setupButton(1, R.id.btn_date1, photoDate, tvResult)
        setupButton(2, R.id.btn_date2, photoDate, tvResult)
        setupButton(3, R.id.btn_date3, photoDate, tvResult)
    }

    private fun setupButton(num: Int, btnId: Int, photoDate: LocalDate?, tvResult: TextView) {
        val btn = findViewById<Button>(btnId)
        val refDate = loadDate(num)

        if (refDate != null) {
            btn.text = getString(R.string.btn_label, num, refDate.format(displayFormatter))
        } else {
            btn.text = getString(R.string.btn_label_not_set, num)
            btn.isEnabled = false
        }

        btn.setOnClickListener {
            if (photoDate == null) {
                tvResult.text = getString(R.string.error_no_exif)
                return@setOnClickListener
            }
            // refDate is non-null here (button disabled otherwise)
            val days = ChronoUnit.DAYS.between(refDate!!, photoDate)
            tvResult.text = when {
                days == 0L -> getString(R.string.result_same_day, num, refDate.format(displayFormatter))
                days > 0L  -> getString(R.string.result_after,  photoDate.format(displayFormatter), days,      num, refDate.format(displayFormatter))
                else       -> getString(R.string.result_before, photoDate.format(displayFormatter), abs(days), num, refDate.format(displayFormatter))
            }
        }
    }

    private fun readExifDate(uri: Uri): LocalDate? {
        return try {
            contentResolver.openInputStream(uri)?.use { stream ->
                val exif = ExifInterface(stream)
                // Prefer original capture date, fall back to digitized / generic datetime
                val raw = exif.getAttribute(ExifInterface.TAG_DATETIME_ORIGINAL)
                    ?: exif.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED)
                    ?: exif.getAttribute(ExifInterface.TAG_DATETIME)
                    ?: return null
                // EXIF format: "yyyy:MM:dd HH:mm:ss"
                val datePart = raw.take(10).replace(':', '-')
                runCatching { LocalDate.parse(datePart, storeFormatter) }.getOrNull()
            }
        } catch (_: IOException) {
            null
        }
    }

    private fun loadDate(num: Int): LocalDate? {
        val s = prefs.getString("date$num", null) ?: return null
        return runCatching { LocalDate.parse(s, storeFormatter) }.getOrNull()
    }
}

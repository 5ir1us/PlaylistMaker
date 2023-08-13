package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //выход из настроек в главное меню
        setContentView(R.layout.activity_settings)

        val backFromSettings = findViewById<ImageView>(R.id.buttonSittingBack)
        backFromSettings.setOnClickListener {
            finish()
        }
        //поделиться приложением
        val shareButton = findViewById<TextView>(R.id.share)

        shareButton.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            val shareApp =
                "https://practicum.yandex.ru/profile/android-developer/?from=mobile_landing"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareApp)
            startActivity(shareIntent)

        }
        //написать в пподдержку
        val emailButton = findViewById<TextView>(R.id.message_support)

        val builder = Uri.Builder() //собрал uri пример из практикума не работает
        builder.scheme("mailto")
            .appendPath("zod15ru@gmail.com")
            .appendQueryParameter(
                "subject",
                "Сообщение разработчикам и разработчица приложения PlayList Maker"
            )
            .appendQueryParameter("body", "Спасибо разрабам за крутое приложение!!!")
        val uri = builder.build()

        emailButton.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = uri
            startActivity(emailIntent)
        }

        //пользовательское соглашение
        val agreementButton = findViewById<TextView>(R.id.terms_user)
        agreementButton.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW)
//            agreementIntent.type = "text/html"
            val agreementUrl = "https://yandex.ru/legal/practicum_offer/"
            agreementIntent.data = Uri.parse(agreementUrl)
            startActivity(agreementIntent)
        }


    }
}
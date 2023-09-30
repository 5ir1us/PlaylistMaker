package com.example.playlistmaker.activity

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding//байндинг
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater) //байндинг
        setContentView(binding.root)

        //строковые ресурсы черех getstring
        val context : Context = this
        val shareText = context.getString(R.string.share_message)
        val mail = context.getString(R.string.mail_support)
        val bodySupport = context.getString(R.string.body_support)
        val supportText = context.getString(R.string.message_support)
        val termsText = context.getString(R.string.terms_user_message)

        //выход из настроек в главное меню
        binding.buttonSittingBack.setOnClickListener {
            finish()
        }

        //поделиться приложением
        binding.share.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText)
            startActivity(shareIntent)

        }
        //написать в пподдержку
        val builder = Uri.Builder() //собрал uri/, пример из практикума не работает
        builder.scheme("mailto")
            .appendPath(mail)
            .appendQueryParameter(
                "subject", supportText
            )
            .appendQueryParameter("body", bodySupport)
        val uri = builder.build()

        binding.messageSupport.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = uri
            startActivity(emailIntent)
        }

        //пользовательское соглашение
        binding.termsUser.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            agreementIntent.data = Uri.parse(termsText)
            startActivity(agreementIntent)
        }

    }
}
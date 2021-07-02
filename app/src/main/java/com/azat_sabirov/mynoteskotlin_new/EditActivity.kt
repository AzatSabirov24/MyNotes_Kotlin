package com.azat_sabirov.mynoteskotlin_new

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.azat_sabirov.mynoteskotlin_new.databinding.EditActivityBinding
import com.azat_sabirov.mynoteskotlin_new.db.MyDbManager
import com.azat_sabirov.mynoteskotlin_new.db.MyIntentConstants

class EditActivity : AppCompatActivity() {
    private val imageRequestCode = 10
    private val myDbManager = MyDbManager(this)
    var imageTempUri = "empty"
    lateinit var binding: EditActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntents()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == imageRequestCode && data != null) {
            binding.imMainImage.setImageURI(data.data)
            imageTempUri = data.data.toString()
        }
    }

    fun onClickAddImage(view: View) {
        binding.apply {
            mainImageLayout.visibility = View.VISIBLE
            fbAddImage.visibility = View.GONE
        }
    }

    fun onClickChooseImage(view: View) {
        val i = Intent(Intent.ACTION_OPEN_DOCUMENT)
        i.type = "image/*"
        i.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
        startActivityForResult(i, imageRequestCode)

    }

    fun deleteImage(view: View) {
        binding.apply {
            mainImageLayout.visibility = View.GONE
            fbAddImage.visibility = View.VISIBLE
        }
    }

    fun onClickSave(view: View) {
        binding.apply {
            val edTitle = edTitle.text.toString()
            val edContent = edContent.text.toString()

            if (edTitle.isNotEmpty() && edContent.isNotEmpty())
                myDbManager.insertToDb(edTitle, edContent, imageTempUri)
        }
        finish()
    }

    fun getIntents() {
        val i = intent
        if (i != null) {
            if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null) {
                binding.apply {
                    edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                    edContent.setText(i.getStringExtra(MyIntentConstants.I_CONTENT_KEY))
                    if (i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty") {
                        imMainImage.setImageURI(Uri.parse(i.getStringExtra(MyIntentConstants.I_URI_KEY)))
                        mainImageLayout.visibility = View.VISIBLE
                        fbAddImage.visibility = View.GONE
                    }
                }
            }
        }
    }
}
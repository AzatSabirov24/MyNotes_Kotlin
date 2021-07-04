package com.azat_sabirov.mynoteskotlin_new

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.azat_sabirov.mynoteskotlin_new.databinding.EditActivityBinding
import com.azat_sabirov.mynoteskotlin_new.db.MyDbManager
import com.azat_sabirov.mynoteskotlin_new.db.MyIntentConstants
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity() {
    private val imageRequestCode = 10
    private var id = 0
    private val myDbManager = MyDbManager(this)
    var imageTempUri = "empty"
    var isEditState = false
    lateinit var binding: EditActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = EditActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntents()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_act_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit_note -> {
                binding.apply {
                    edTitle.isEnabled = true
                    edContent.isEnabled = true
                    imBtnEditImage.visibility = View.VISIBLE
                    imBtnDeleteImage.visibility = View.VISIBLE
                }
                return true
            }
            R.id.add_image -> {
                val i = Intent(Intent.ACTION_OPEN_DOCUMENT)
                i.type = "image/*"
                startActivityForResult(i, imageRequestCode)
                binding.mainImageLayout.visibility = View.VISIBLE
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        binding.imBtnEditImage.visibility = View.GONE
        binding.imBtnDeleteImage.visibility = View.GONE
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
            contentResolver.takePersistableUriPermission(
                data.data!!, Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
    }

    fun onClickChooseImage(view: View) {
        val i = Intent(Intent.ACTION_OPEN_DOCUMENT)
        i.type = "image/*"
        startActivityForResult(i, imageRequestCode)

    }

    fun deleteImage(view: View) {
        binding.apply {
            mainImageLayout.visibility = View.GONE
            imageTempUri = "empty"
        }
    }

    fun onClickSave(view: View) {
        binding.apply {
            val edTitle = edTitle.text.toString()
            val edContent = edContent.text.toString()

            if (edTitle.isNotEmpty() && edContent.isNotEmpty()) {
                if (isEditState)
                    myDbManager.updateItem(edTitle, edContent, imageTempUri, id, getCurrentTime())
                else myDbManager.insertToDb(edTitle, edContent, imageTempUri, getCurrentTime())
            }
            finish()
        }
    }

    private fun getIntents() {
        binding.apply {
            val i = intent
            if (i != null) {
                if (i.getStringExtra(MyIntentConstants.I_TITLE_KEY) != null) {
                    edTitle.setText(i.getStringExtra(MyIntentConstants.I_TITLE_KEY))
                    edContent.setText(i.getStringExtra(MyIntentConstants.I_CONTENT_KEY))
                    id = i.getIntExtra(MyIntentConstants.I_ID_KEY, 0)
                    isEditState = true
                    edTitle.isEnabled = false
                    edContent.isEnabled = false
                    if (i.getStringExtra(MyIntentConstants.I_URI_KEY) != "empty") {
                        imageTempUri = i.getStringExtra(MyIntentConstants.I_URI_KEY)!!
                        imMainImage.setImageURI(Uri.parse(imageTempUri))
                        mainImageLayout.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    private fun getCurrentTime(): String{
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("dd-MM-yy kk:mm", Locale.getDefault())
        return formatter.format(time)
    }
}
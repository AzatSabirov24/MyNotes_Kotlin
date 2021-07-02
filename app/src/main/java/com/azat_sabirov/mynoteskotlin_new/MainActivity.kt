package com.azat_sabirov.mynoteskotlin_new

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.azat_sabirov.mynoteskotlin_new.databinding.ActivityMainBinding
import com.azat_sabirov.mynoteskotlin_new.db.MyAdapter
import com.azat_sabirov.mynoteskotlin_new.db.MyDbManager

class MainActivity : AppCompatActivity() {
    private val myDbManager = MyDbManager(this)
    val myAdapter = MyAdapter(ArrayList(), this)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter()
    }

    override fun onDestroy() {
        super.onDestroy()
        myDbManager.closeDb()
    }

    fun onClickNew(view: View) {
        val i = Intent(this, EditActivity::class.java)
        startActivity(i)
    }

    private fun init() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = myAdapter
    }

    private fun fillAdapter() {
        myAdapter.upgradeAdapter(myDbManager.readDb())
        binding.tvNoElements.visibility = View.GONE
    }

}
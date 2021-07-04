package com.azat_sabirov.mynoteskotlin_new

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azat_sabirov.mynoteskotlin_new.databinding.ActivityMainBinding
import com.azat_sabirov.mynoteskotlin_new.db.MyAdapter
import com.azat_sabirov.mynoteskotlin_new.db.MyDbManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val myDbManager = MyDbManager(this)
    private var job: Job? = null
    val myAdapter = MyAdapter(ArrayList(), this)
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
        initSearchView()
    }

    override fun onResume() {
        super.onResume()
        myDbManager.openDb()
        fillAdapter("")
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
        val swapHelper = getSwapMg()
        swapHelper.attachToRecyclerView(binding.recyclerView)
        binding.recyclerView.adapter = myAdapter
    }

    private fun fillAdapter(text: String) {
        job?.cancel()
        job = CoroutineScope(Dispatchers.Main).launch {
            val list = myDbManager.readDb(text)
            myAdapter.upgradeAdapter(list)
            if (list.size > 0)
                binding.tvNoElements.visibility = View.GONE
        }
    }

    private fun getSwapMg(): ItemTouchHelper {
        return ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                myAdapter.removeItem(viewHolder.adapterPosition, myDbManager)
            }
        })
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    fillAdapter(newText)
                }
                return true
            }
        })
    }
}
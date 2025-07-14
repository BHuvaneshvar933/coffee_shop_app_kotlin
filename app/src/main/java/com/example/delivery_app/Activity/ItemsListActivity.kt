package com.example.delivery_app.Activity

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.delivery_app.Adapter.ItemListCategoryAdapter
import com.example.delivery_app.R
import com.example.delivery_app.ViewModel.MainViewModel
import com.example.delivery_app.databinding.ActivityItemsListBinding

class ItemsListActivity : AppCompatActivity() {
    lateinit var binding : ActivityItemsListBinding
    private val viewModel = MainViewModel()
    private var id : String=""
    private var title: String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityItemsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getBundle()
        initList()
    }

    private fun initList() {
        binding.apply {
            progressBar3.visibility = View.VISIBLE
            viewModel.loadItems(id).observe(this@ItemsListActivity, Observer{
                listView.layoutManager=
                    GridLayoutManager(this@ItemsListActivity,2)
                listView.adapter = ItemListCategoryAdapter(it)
                progressBar3.visibility=View.GONE
            })
            backBtn.setOnClickListener { finish() }
        }
    }

    private fun getBundle() {
        id = intent.getStringExtra("id")!!
        title = intent.getStringExtra("title")!!

        binding.categoryTxt.text = title

    }
}
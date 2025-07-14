package com.example.delivery_app.Adapter

import android.R
import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.delivery_app.Activity.ItemsListActivity
import com.example.delivery_app.Domain.CategoryModel
import com.example.delivery_app.databinding.ViewholderCategoryBinding

class CategoryAdapter(val items: MutableList<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private lateinit var context: Context
    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    inner class ViewHolder(val binding: ViewholderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.titleCat.text = item.title

        // Highlight if selected
        if (selectedPosition == position) {
            holder.binding.titleCat.setBackgroundResource(com.example.delivery_app.R.drawable.grey_full_corner_bg)
            holder.binding.titleCat.setTextColor(context.resources.getColor(R.color.white))
        } else {
            holder.binding.titleCat.setBackgroundResource(com.example.delivery_app.R.drawable.white_full_corner_bg)
            holder.binding.titleCat.setTextColor(context.resources.getColor(com.example.delivery_app.R.color.darkBrown))
        }

        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition
            selectedPosition = position
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)

            // Optional delayed intent
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(context, ItemsListActivity::class.java).apply {
                    putExtra("id",item.id.toString())
                    putExtra("title",item.title)

                }
                ContextCompat.startActivity(context,intent,null)
            }, 500)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
package com.example.delivery_app.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.delivery_app.Domain.ItemsModel
import com.example.delivery_app.Helper.ChangeNumberItemsListener
import com.example.delivery_app.Helper.ManagmentCart
import com.example.delivery_app.databinding.ViewholderCartBinding

class CartAdapter(
    private val listItemSelected : ArrayList<ItemsModel>,
    context : Context,
    var changeNumberItemsListener: ChangeNumberItemsListener?=null
) : RecyclerView.Adapter<CartAdapter.Viewholder>() {
    class Viewholder (val binding : ViewholderCartBinding) : RecyclerView.ViewHolder(binding.root){
        init {
            binding.plusBtn.setOnClickListener {
                Log.d("CartAdapter", "PLUS clicked inside ViewHolder")
            }
        }
    }

    private val managmentCart = ManagmentCart(context)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartAdapter.Viewholder {
        val binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: CartAdapter.Viewholder, position: Int) {
        val item = listItemSelected[position]

        holder.binding.titleTxt.text = item.title
        holder.binding.feeEachItem.text = "$${item.price}"
        holder.binding.totalEachItem.text = "$${item.numberInCart*item.price}"
        holder.binding.numberInCartTxt.text = item.numberInCart.toString()
        holder.binding.plusBtn.setOnTouchListener { v, event ->
            Log.d("CartAdapter", "TOUCH event detected on plusBtn")
            false // allow normal click to proceed too
        }
        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.binding.picCart)

        holder.binding.plusBtn.setOnClickListener {
            managmentCart.plusItem(listItemSelected,position,object : ChangeNumberItemsListener{
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }

            })
        }

        holder.binding.minusBtn.setOnClickListener {
            managmentCart.minusItem(listItemSelected,position,object : ChangeNumberItemsListener{
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }
            })
        }

        holder.binding.removeItemBtn.setOnClickListener {
            managmentCart.removeItem(listItemSelected,position,object : ChangeNumberItemsListener{
                override fun onChanged() {
                    notifyDataSetChanged()
                    changeNumberItemsListener?.onChanged()
                }
            })
        }
    }

    override fun getItemCount(): Int = listItemSelected.size

}
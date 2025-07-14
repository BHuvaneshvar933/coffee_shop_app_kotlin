package com.example.delivery_app.Activity

import com.example.delivery_app.R
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.delivery_app.Domain.ItemsModel
import com.example.delivery_app.Helper.ManagmentCart
import com.example.delivery_app.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    lateinit var binding : ActivityDetailBinding
    private lateinit var item : ItemsModel
    private lateinit var managementCart : ManagmentCart
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagmentCart(this)

        bundle()
        initSizeList()
    }

    private fun initSizeList() {
        binding.apply {
            smallBtn.setOnClickListener {
                smallBtn.setBackgroundResource(R.drawable.brown_storke_bg)
                mediumBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(0)
            }

            mediumBtn.setOnClickListener {
                mediumBtn.setBackgroundResource(R.drawable.brown_storke_bg)
               smallBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(0)
            }

            largeBtn.setOnClickListener {
                largeBtn.setBackgroundResource(R.drawable.brown_storke_bg)
                smallBtn.setBackgroundResource(0)
                mediumBtn.setBackgroundResource(0)
            }

        }
    }

    private fun bundle() {
        binding.apply {
            item= intent.getSerializableExtra("object") as ItemsModel

            Glide.with(this@DetailActivity)
                .load(item.picUrl[0])
                .into(binding.picMain)

            titleTxt.text = item.title
            descriptionTxt.text = item.description
            priceTxt.text = "$"+item.price
            ratingTxt.text = item.rating.toString()
            addToCart.setOnClickListener {
                item.numberInCart = Integer.valueOf(
                    numberInCartTxt.text.toString()
                )
                managementCart.insertItems(item)

            }

            backBtn.setOnClickListener { finish() }

            plusBtn.setOnClickListener {
                numberInCartTxt.text = (item.numberInCart + 1).toString()
                item.numberInCart++
            }

            minusBtn.setOnClickListener {
                if(item.numberInCart > 0 ) {
                    numberInCartTxt.text = (item.numberInCart - 1).toString()
                    item.numberInCart--
                }
            }

        }
    }
}
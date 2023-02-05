package com.conestoga.groupproject.view.adapters

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage


@BindingAdapter("imageUrl")
fun loadImage(view: ImageView, imageUrl: String?) {
    imageUrl?.let { image ->
        FirebaseStorage.getInstance().getReferenceFromUrl(image).let {
            Glide.with(view.context)
                .load(it)
                .into(view)
        }
    }
}

@BindingAdapter("price")
fun loadPrice(view: TextView, price: Double?) {
    if (price != null) {
        view.text = "$$price"
    }
}

@BindingAdapter("quantity")
fun loadQuantity(view: TextView, quantity: Int?) {
    if (quantity != null) {
        view.text = "x$quantity"
    }
}

@BindingAdapter("nameToInitials")
fun loadInitials(view: TextView, name: String?) {
    if (name != null) {
        val initials = name.split(" ").map { it.first() }.joinToString("")
        view.text = initials
    }
}
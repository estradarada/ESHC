package com.example.eshc.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.eshc.R
import com.example.eshc.model.Items
import com.example.eshc.onboarding.screens.bottomNavigation.FragmentView
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import kotlinx.android.synthetic.main.recycler_item.view.*


class FireItemAdapter<T, U>(options: FirestoreRecyclerOptions<Items>)
    : FirestoreRecyclerAdapter<Items, FireItemAdapter.ItemViewHolder>(options) {

    private lateinit var context: Context

    override fun onViewAttachedToWindow(holder: ItemViewHolder) {
        holder.recyclerItemContainer.setOnClickListener {
            val item = getItem(holder.adapterPosition)
            FragmentView.popUpFragmentClick(item)
        }
    }

    override fun onViewDetachedFromWindow(holder: ItemViewHolder) {
        holder.recyclerItemContainer.setOnClickListener(null)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_item, parent, false
        )
        context = parent.context
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int, model: Items) {

        holder.recyclerItemContainer.animation =
            AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation)

        holder.objectName.text = model.objectName
        holder.kurator.text = model.kurator
        holder.objectPhone.text = model.objectPhone
        holder.mobilePhone.text = model.mobilePhone
        holder.address.text = model.address
        holder.worker08.text = model.worker08
        holder.serverTimestamp.text = model.worker15
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val objectName: TextView = itemView.name_txt
        val kurator: TextView = itemView.kurator_txt
        val objectPhone: TextView = itemView.phone
        val mobilePhone: TextView = itemView.mobile
        val address: TextView = itemView.address_txt
        val worker08: TextView = itemView.worker08_txt
        val serverTimestamp: TextView = itemView.serverTimestamp_txt
        val recyclerItemContainer: ConstraintLayout = itemView.rv_item_container
    }

}
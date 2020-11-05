package com.example.eshc.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.eshc.R
import com.example.eshc.model.Guards
import kotlinx.android.synthetic.main.recycler_guard_late.view.*

class AdapterGuardLate(private val itemList: MutableList<Guards>) :
    RecyclerView.Adapter<AdapterGuardLate.SimpleViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.recycler_guard_late, parent, false
        )
        context = parent.context
        return SimpleViewHolder(view)
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {
        holder.rvGuardLateContainer.animation =
            AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation)

        holder.guardLateName.text = itemList[position].guardName
        holder.guardLateKurator.text = itemList[position].guardKurator
        holder.guardLatePhone.text = itemList[position].guardPhone
        holder.guardLatePhone_2.text = itemList[position].guardPhone_2
        holder.guardLateWork.text = itemList[position].workPlace
        holder.guardLateTime.text = itemList[position].guardLateTime
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    class SimpleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rvGuardLateContainer = itemView.rvGuardLateContainer
        val rvGuardLateEditImg = itemView.rvGuardLateEditImg
        val guardLateName = itemView.guardLateName
        val guardLateKurator = itemView.guardLateKurator
        val guardLatePhone = itemView.guardLatePhone
        val guardLateWork = itemView.guardLateWork
        val guardLatePhone_2 = itemView.guardLatePhone2
        val guardLateTime = itemView.guardLateTime

    }
}
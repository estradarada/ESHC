package com.example.eshc.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.eshc.R
import com.example.eshc.databinding.RecyclerGuardBinding
import com.example.eshc.model.Guards
import com.example.eshc.onboarding.screens.bottomNavigation.main.FragmentGuard
import com.example.eshc.utilits.TAG
import com.google.android.material.imageview.ShapeableImageView
import java.util.*

class AdapterGuard : RecyclerView.Adapter<AdapterGuard.SimpleViewHolder>(), Filterable {
    private lateinit var mContext: Context
    private var mList = mutableListOf<Guards>()
    private var mListFiltered = mutableListOf<Guards>()

    override fun onViewAttachedToWindow(holder: SimpleViewHolder) {
        holder.guardImg.setOnClickListener {
            val guard = mListFiltered[holder.adapterPosition]
            FragmentGuard.popUpFragmentClick(guard)
        }

        holder.guardPhone.setOnClickListener {
            val guardPhoneNumber = mListFiltered[holder.adapterPosition].guardPhone
            FragmentGuard.startPhoneDial(guardPhoneNumber)
        }

        holder.guardPhone2.setOnClickListener {
            val guardPhoneNumber2 = mListFiltered[holder.adapterPosition].guardPhone_2
            FragmentGuard.startPhoneDial(guardPhoneNumber2)
        }
    }

    override fun onViewDetachedFromWindow(holder: SimpleViewHolder) {
        holder.guardImg.setOnClickListener(null)
        holder.guardPhone.setOnClickListener(null)
        holder.guardPhone2.setOnClickListener(null)
        super.onViewDetachedFromWindow(holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimpleViewHolder {

        val view = SimpleViewHolder(
            RecyclerGuardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
        mContext = parent.context
        return view
    }

    override fun onBindViewHolder(holder: SimpleViewHolder, position: Int) {

        holder.guardImg.animation =
            AnimationUtils.loadAnimation(mContext, R.anim.fade_transition_animation)
        holder.guardRvContainer.animation =
            AnimationUtils.loadAnimation(mContext, R.anim.fade_scale_animation)

        holder.guardName.text = mListFiltered[position].guardName
        holder.guardPhone.text = mListFiltered[position].guardPhone
        holder.guardPhone2.text = mListFiltered[position].guardPhone_2
        holder.guardKurator.text = mListFiltered[position].guardKurator
        holder.guardWorkPlace.text = mListFiltered[position].guardWorkPlace
    }

    override fun getItemCount(): Int {
        return mListFiltered.size
    }

    class SimpleViewHolder(binding: RecyclerGuardBinding) : RecyclerView.ViewHolder(binding.root) {
        val guardName: TextView = binding.guardNameTxt
        val guardPhone: TextView = binding.guardPhoneTxt
        val guardPhone2: TextView = binding.guardPhone2Txt
        val guardKurator: TextView = binding.guardKuratorTxt
        val guardWorkPlace: TextView = binding.guardWorkTxt
        val guardRvContainer: ConstraintLayout = binding.recyclerItemGuardContainer
        val guardImg: ShapeableImageView = binding.ivGuardLogo
    }

    fun setList(list: MutableList<Guards>) {
        mList = list
        mListFiltered = list
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val key = charSequence.toString().toLowerCase(Locale.ROOT).trim()
                Log.d(TAG, "performFiltering: + $key")
                mListFiltered = if (key.isEmpty()) {
                    mList
                } else {
                    val newList = mutableListOf<Guards>()
                    for (guard in mList) {
                        val name = guard.guardName.toLowerCase(Locale.ROOT).trim()
                        if (name.contains(key)) {
                            newList.add(guard)
                        }
                    }
                    newList
                }
                val filterResults = FilterResults()
                filterResults.values = mListFiltered
                filterResults.count = mListFiltered.size
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence?,
                filterResults: FilterResults?
            ) {
                mListFiltered = filterResults?.values as MutableList<Guards>
                notifyDataSetChanged()
            }
        }
    }

    fun removeItem(viewHolder: RecyclerView.ViewHolder) {
        mList.removeAt(viewHolder.adapterPosition)
        notifyItemRemoved(viewHolder.adapterPosition)
    }

    fun insertItem(position: Int, guard: Guards) {
        mList.add(position, guard)
        notifyItemInserted(position)
    }
}
package com.example.eshc.utilits

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.eshc.adapters.AdapterGuard
import com.example.eshc.model.Guards
import com.example.eshc.model.Items
import com.google.firebase.firestore.DocumentChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


private var swipeBackground = ColorDrawable(Color.RED)

private lateinit var mKey: String
private lateinit var mListItems: MutableList<Items>
private lateinit var mViewHolder: RecyclerView.ViewHolder
private lateinit var mToolbar: Toolbar
private lateinit var mRecyclerView: RecyclerView
private lateinit var mSearchView: SearchView
private lateinit var mAdapter: AdapterGuard
private lateinit var deleteIcon: Drawable


fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_LONG).show()
}


 fun insertItemChangesRoom(){
    collectionITEMS_REF
        .addSnapshotListener { value, error ->
            if (value != null) {
                for (dc in value.documentChanges) {
                    if (dc.type == DocumentChange.Type.MODIFIED) {
                        val item = dc.document.toObject(Items::class.java)

                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                item.state = stateChanged
                                REPOSITORY_ROOM.insertItem(item)
                                Log.d(TAG, "insertItemChangesRoom: + ${item.state}")
                            } catch (e: Exception) {
                                withContext(Dispatchers.Main) {
                                    e.message?.let { showToast(it) }
                                }
                            }
                        }
                    }
                }
            } else showToast(error?.message.toString())
        }
}


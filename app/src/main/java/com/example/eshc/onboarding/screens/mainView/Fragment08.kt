package com.example.eshc.onboarding.screens.mainView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.eshc.adapters.AdapterItems
import com.example.eshc.databinding.Fragment08Binding
import com.example.eshc.model.Items
import com.example.eshc.utilits.*
import com.google.firebase.firestore.DocumentChange
import kotlinx.coroutines.*
import java.util.*


class Fragment08 : Fragment() {

    private var timeStart08: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeEnd08: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeRange08: Boolean = false

    private var _binding: Fragment08Binding? = null
    private val mBinding get() = _binding!!
    private var mMutableList = mutableListOf<Items>()
    private var currentTime: Date = Date()
    private var timeStartLongType: Long = 0
    private var timeEndLongType: Long = 0

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapterItems: AdapterItems
    private lateinit var mDeferred: Deferred<MutableList<Items>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getData08()
    }

    private fun getData08() {

        mDeferred = CoroutineScope(Dispatchers.IO).async {
            try {
                val list = async { REPOSITORY_ROOM.getMainItemList08() }
                mMutableList = list.await() as MutableList<Items>
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.message?.let { showToast(it) }
                }
            }
            mMutableList
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = Fragment08Binding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
    }

    override fun onStart() {
        super.onStart()
        setCurrentTime()
        setListToAdapter()
        if (timeRange08) {
            getChanges()
        }
    }

    private fun initialise() {
        mRecyclerView = mBinding.rvFragment08
        mAdapterItems = AdapterItems()
        mRecyclerView.adapter = mAdapterItems
    }

    private fun setCurrentTime() {
        val currentTime = Calendar.getInstance(Locale.getDefault()).time

        timeStart08.set(Calendar.HOUR_OF_DAY, 7)
        timeStart08.set(Calendar.MINUTE, 0)
        timeStart08.set(Calendar.SECOND, 0)
        timeEnd08.set(Calendar.HOUR_OF_DAY, 11)
        timeEnd08.set(Calendar.MINUTE, 0)
        timeEnd08.set(Calendar.SECOND, 0)

        if ((currentTime.after(timeStart08.time)) && (currentTime.before(timeEnd08.time))) {
            timeRange08 = true
        }

        timeStartLongType = timeStart08.time.time
        timeEndLongType = timeEnd08.time.time
    }

    private fun setListToAdapter() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                mMutableList = mDeferred.await()

                if (timeRange08) {

                    val list = REPOSITORY_ROOM
                        .getAllChangedItemsWhereTimeBetween(timeStartLongType, timeEndLongType)

                    for (item in list) {
                        val name = item.objectName
                        val newIterator: MutableIterator<Items> = mMutableList.iterator()

                        while (newIterator.hasNext()) {
                            val it = newIterator.next()
                            if (it.objectName == name) {
                                newIterator.remove()
                            }
                        }
                    }
                }

                withContext(Dispatchers.Main) {
                    mAdapterItems.setList(mMutableList)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.message?.let { showToast(it) }
                }
            }
        }
    }

    private fun getChanges() {
        collectionITEMS_REF
            .whereEqualTo(field_08, "true")
            .addSnapshotListener { value, error ->
                if (value != null) {
                    for (dc in value.documentChanges) {
                        if (dc.type == DocumentChange.Type.MODIFIED) {

                            val currentTimeLongType = currentTime.time

                            val item = dc.document.toObject(Items::class.java)
                            val name = item.objectName
                            item.itemLongTime = currentTimeLongType

                            val newIterator: MutableIterator<Items> = mMutableList.iterator()
                            while (newIterator.hasNext()) {
                                val it = newIterator.next()
                                if (it.objectName == name) {

                                    saveChangedItemToRoom(item)

                                    val index: Int = mMutableList.lastIndexOf(it)
                                    newIterator.remove()
                                    mAdapterItems.removeItem(index, it, mMutableList)
                                }
                            }
                        }
                    }
                } else showToast(error?.message.toString())
            }
    }

    private fun saveChangedItemToRoom(item: Items) {
        CoroutineScope(Dispatchers.IO).launch {
            try {

                ITEM.order08 = item.order08
                ITEM.item_id = item.item_id
                ITEM.objectName = item.objectName
                ITEM.itemLongTime = item.itemLongTime
                ITEM.serverTimeStamp = item.serverTimeStamp
                ITEM.worker08 = item.worker08
                ITEM.kurator = item.kurator
                ITEM.state = stateChanged

                REPOSITORY_ROOM.insertItem(ITEM)

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.message?.let { showToast(it) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mRecyclerView.adapter = null
        _binding = null
    }
}
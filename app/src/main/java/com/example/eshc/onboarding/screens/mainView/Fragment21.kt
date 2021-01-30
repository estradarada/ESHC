package com.example.eshc.onboarding.screens.mainView

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.eshc.adapters.AdapterItems
import com.example.eshc.databinding.Fragment21Binding
import com.example.eshc.model.Items
import com.example.eshc.utilits.*
import com.google.firebase.firestore.DocumentChange
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class Fragment21 : Fragment() {

    private var _binding: Fragment21Binding? = null
    private val mBinding get() = _binding!!
    private var mMutableList = mutableListOf<Items>()
    private var currentDate: String = String()
    private var currentTime: Date = Date()
    private var timeStart: Calendar = Calendar.getInstance(Locale.getDefault())
    private var timeEnd: Calendar = Calendar.getInstance(Locale.getDefault())
    private lateinit var mDeferred: Deferred<MutableList<Items>>
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapterItems: AdapterItems

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       getData21()
    }

    private fun getData21() {

        mDeferred = CoroutineScope(Dispatchers.IO).async {
            try {
                val list = async { REPOSITORY_ROOM.getMainItemList21() }
                mMutableList = list.await() as MutableList<Items>
                Log.d(TAG, "onCreate_21: +  ${mMutableList.size}")
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
        // Inflate the layout for this fragment
        _binding = Fragment21Binding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart21: ")
        setCurrentTime()
        initialization()
        setListToAdapter()
        if ((currentTime.after(timeStart.time)) && (currentTime.before(timeEnd.time))) {
            getChanges()
        }
    }

    private fun setListToAdapter() {
        CoroutineScope(Dispatchers.IO).launch {
            mMutableList = mDeferred.await()
            mAdapterItems.setList(mMutableList)
            Log.d(TAG, "mDeferred21: + ${mMutableList.size} ")
        }
    }

    private fun setCurrentTime() {
        currentDate = SimpleDateFormat("HH:mm, dd/MM/yyyy", Locale.getDefault())
            .format(Date())
        currentTime = Calendar.getInstance(Locale.getDefault()).time
        Log.d(TAG, "date_1:  + $currentDate")

        timeStart.set(Calendar.HOUR_OF_DAY, 19)
        timeStart.set(Calendar.MINUTE, 0)
        timeStart.set(Calendar.SECOND, 0)
        timeEnd.set(Calendar.HOUR_OF_DAY, 22)
        timeEnd.set(Calendar.MINUTE, 50)
        timeEnd.set(Calendar.SECOND, 0)
        //  Log.d(TAG, "set:+ ${currentTime}  + ${timeStart.time} + ${timeEnd.time}")
    }


    private fun initialization() {
        mRecyclerView = mBinding.rvFragment21
        mAdapterItems = AdapterItems()
        mRecyclerView.adapter = mAdapterItems
    }

    private fun getChanges() {
        collectionITEMS_REF
            .addSnapshotListener { value, error ->
                if (value != null) {
                    for (dc in value.documentChanges) {
                        if (dc.type == DocumentChange.Type.MODIFIED) {

                            val snapTime = SimpleDateFormat(
                                "HH:mm, dd/MM/yyyy",
                                Locale.getDefault()
                            )
                                .format(Date())
                            Log.d(TAG, "date_21:  + $snapTime")

                            val item = dc.document.toObject(Items::class.java)
                            item.serverTimeStamp = snapTime
                            val name = item.objectName
                            Log.d(
                                TAG,
                                "snap21 +${mMutableList.size} + ${item.address} + ${item.state} "
                            )

                            val newIterator: MutableIterator<Items> = mMutableList.iterator()
                            while (newIterator.hasNext()) {
                                val it = newIterator.next()
                                if (it.objectName == name) {
                                    saveToRoom(item)
                                    val index: Int = mMutableList.lastIndexOf(it)
                                    newIterator.remove()
                                    mAdapterItems.removeItem(index, it, mMutableList)

                                    Log.d(
                                        TAG,
                                        "RemovedSnapshotListener21: " + mMutableList.size
                                    )
                                }
                            }
                        }
                    }
                } else showToast(error?.message.toString())
            }
    }

    private fun saveToRoom(item: Items) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                item.state = stateChanged
                REPOSITORY_ROOM.insertItem(item)
                Log.d(TAG, "insertItemChangesRoom21: + ${item.state}")
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    e.message?.let { showToast(it) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mRecyclerView.adapter = null
    }
}
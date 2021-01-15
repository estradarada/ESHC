package com.example.eshc.onboarding.screens.refactorFragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.eshc.R
import com.example.eshc.databinding.FragmentAddNewItemBinding
import com.example.eshc.model.Items
import com.example.eshc.utilits.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil
import java.util.*

class AddNewItemFragment : Fragment() {
    private var _binding: FragmentAddNewItemBinding? = null
    private val mBinding get() = _binding!!
    private lateinit var mToolbar: Toolbar
    private lateinit var mEdtxtObjectName: EditText
    private lateinit var mEdtxtAddress: EditText
    private lateinit var mEdtxtPhone: EditText
    private lateinit var mEdtxtMobile: EditText
    private lateinit var mEdtxtKurator: EditText
    private lateinit var checkBox08: CheckBox
    private lateinit var checkBox15: CheckBox
    private lateinit var checkBox21: CheckBox
    private lateinit var checkBox00: CheckBox
    private lateinit var checkBox02: CheckBox
    private lateinit var checkBox04: CheckBox
    private lateinit var checkBox06: CheckBox
    private lateinit var mButtonSave: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddNewItemBinding.inflate(
            layoutInflater,
            container, false
        )
        return mBinding.root
    }

    override fun onStart() {
        super.onStart()
        initialization()
        mButtonSave.setOnClickListener {
            val item = getNewItem()
            if (item.objectName.isNotEmpty()) {
                addNewItem(item)
            }
            UIUtil.hideKeyboard(context as Activity)
        }
    }

    private fun initialization() {
        mToolbar = mBinding.fragmentAddNewItemToolbar
        mToolbar.setupWithNavController(findNavController())
        mEdtxtObjectName = mBinding.fragmentAddNewItemName
        mEdtxtAddress = mBinding.fragmentAddNewItemAddress
        mEdtxtPhone = mBinding.fragmentAddNewItemPhone
        mEdtxtMobile = mBinding.fragmentAddNewItemMobilePhone
        mEdtxtKurator = mBinding.fragmentAddNewItemKurator
        checkBox08 = mBinding.fragmentAddItemField08
        checkBox15 = mBinding.fragmentAddItemField15
        checkBox21 = mBinding.fragmentAddItemField21
        checkBox00 = mBinding.fragmentAddItemField00
        checkBox02 = mBinding.fragmentAddItemField02
        checkBox04 = mBinding.fragmentAddItemField04
        checkBox06 = mBinding.fragmentAddItemField06
        mButtonSave = mBinding.fragmentAddNewItemButtonAdd
    }

    private fun getNewItem(): Items {
        val name = mEdtxtObjectName.text.toString().trim()
        val address = mEdtxtAddress.text.toString().trim()
        val phone = mEdtxtPhone.text.toString().trim()
        val mobile = mEdtxtMobile.text.toString().trim()
        val kurator = mEdtxtKurator.text.toString().trim()

        when {
            name.isEmpty() -> {
                ITEM.objectName = ""
                showToast("Введите имя охранника")
            }
            else -> ITEM.objectName = name
        }

        ITEM.address = address
        ITEM.objectPhone = phone
        ITEM.mobilePhone = mobile
        ITEM.kurator = kurator

        when {
            checkBox08.isChecked -> ITEM.order08 = "true"
            !checkBox08.isChecked -> ITEM.order08 = "false"
        }
        when {
            checkBox15.isChecked -> ITEM.order15 = "true"
            !checkBox15.isChecked -> ITEM.order15 = "false"
        }
        when {
            checkBox21.isChecked -> ITEM.order21 = "true"
            !checkBox21.isChecked -> ITEM.order21 = "false"
        }
        when {
            checkBox00.isChecked -> ITEM.order00 = "true"
            !checkBox00.isChecked -> ITEM.order00 = "false"
        }
        when {
            checkBox02.isChecked -> ITEM.order02 = "true"
            !checkBox02.isChecked -> ITEM.order02 = "false"
        }
        when {
            checkBox04.isChecked -> ITEM.order04 = "true"
            !checkBox04.isChecked -> ITEM.order04 = "false"
        }
        when {
            checkBox06.isChecked -> ITEM.order06 = "true"
            !checkBox06.isChecked -> ITEM.order06 = "false"
        }
        ITEM.state = stateMain

        return ITEM
    }

    private fun addNewItem(item: Items) {
        val newName = item.objectName.toLowerCase(Locale.ROOT).trim()
        Log.d(TAG, "newName: + $newName  ")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val roomList = REPOSITORY_ROOM.getMainItemList()
                Log.d(TAG, "roomList: + ${roomList.size}  ")

                for (doc in roomList) {
                    val oldName = doc.objectName
                        .toLowerCase(Locale.ROOT).trim()

                    if (oldName == newName) {
                        Log.d(TAG, "equal: + $oldName + $newName ")
                        withContext(Dispatchers.Main) {
                            showToast(" Объект с таким именем уже существует")
                        }
                        return@launch
                    }
                }

                val docRef = collectionITEMS_REF.add(item).await()
                val key = docRef.id
                collectionITEMS_REF.document(key)
                    .update(item_fire_id, key, item_worker15, "Создан новый объект").await()
                item.item_id = key
                item.state = stateMain
                REPOSITORY_ROOM.insertItem(item)

                //  Log.d(TAG, "addNewItem: ${ITEM.item_id} + ${ITEM.objectName} + ${ITEM.state}")
                withContext(Dispatchers.Main) {
                    APP_ACTIVITY.navController
                        .navigate(R.id.action_addNewItemFragment_to_viewPagerFragment)
                    showToast("Добавлен новый объект ${ITEM.objectName}")
                }

            } catch (e: Exception){
                withContext(Dispatchers.Main) {
                    e.message?.let { showToast(it) }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
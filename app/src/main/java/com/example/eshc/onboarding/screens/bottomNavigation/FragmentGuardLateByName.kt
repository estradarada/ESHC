package com.example.eshc.onboarding.screens.bottomNavigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.eshc.adapters.AdapterGuardLate
import com.example.eshc.databinding.FragmentGuardLateByNameBinding
import com.example.eshc.model.Guards
import com.example.eshc.utilits.REPOSITORY_ROOM
import com.example.eshc.utilits.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentGuardLateByName : Fragment() {

    private var _binding: FragmentGuardLateByNameBinding? = null
    private val mBinding get() = _binding!!
    private var name: String = ""

    private lateinit var mAdapter: AdapterGuardLate
    private lateinit var mToolbar: Toolbar
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mCurrentGuardLate: Guards

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGuardLateByNameBinding.inflate(layoutInflater, container, false)
        mCurrentGuardLate = arguments?.getSerializable("guard") as Guards
        name = mCurrentGuardLate.guardName
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initialise()
        getData(name)
    }

    /*
    override fun onStart() {
        super.onStart()
        initialise()
        getData(name)
    }

     */

    private fun initialise() {
        mAdapter = AdapterGuardLate()
        mToolbar = mBinding.fragmentGuardLateByNameToolbar
        mRecyclerView = mBinding.rvFragmentGuardLateByName
        mRecyclerView.adapter = mAdapter
        mToolbar.title = name
        mToolbar.setupWithNavController(findNavController())
    }

    private fun getData(name: String) = CoroutineScope(Dispatchers.IO).launch {

        try {
            val list = REPOSITORY_ROOM.singleGuardLateByName(name)
            val mutableList = list.toMutableList()
            withContext(Dispatchers.Main) {
                mAdapter.setList(mutableList.asReversed())
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                e.message?.let { showToast(it) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mRecyclerView.adapter = null
        _binding = null
    }
}
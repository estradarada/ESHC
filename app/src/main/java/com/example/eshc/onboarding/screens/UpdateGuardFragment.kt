package com.example.eshc.onboarding.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.eshc.R
import com.example.eshc.databinding.FragmentUpdateGaurdBinding
import com.example.eshc.databinding.FragmentUpdateItemBinding


class UpdateGuardFragment : Fragment() {

    private var _binding: FragmentUpdateGaurdBinding ? = null
    private val mBinding get() = _binding!!
    private lateinit var mToolbar: Toolbar

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateGaurdBinding.inflate(layoutInflater,
            container,false)
        return mBinding.root
    }
    override fun onStart() {
        super.onStart()
        initialization()
        mBinding.fragmentUpdateGuardEdtxtAddress.hint = "где то далеко в Guard"
        mBinding.fragmentUpdateGuardEdtxtName.hint = "Шишков Алексей в Guard"
    }

    private fun initialization() {
        mToolbar = mBinding.fragmentUpdateGuardToolbar
        mToolbar.setupWithNavController(findNavController())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
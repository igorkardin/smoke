package com.simbirsoft.smoke.main

import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.simbirsoft.smoke.R
import com.simbirsoft.smoke.databinding.FragmentMainBinding

class BottomNavFragment : Fragment(R.layout.fragment_main) {
    val bottomNavFragmentBinding by viewBinding<FragmentMainBinding>()
}
package com.simbirsoft.smoke.ui.main

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.simbirsoft.smoke.R
import com.simbirsoft.smoke.databinding.FragmentMainBinding
import com.simbirsoft.smoke.ui.BaseFragment
import com.simbirsoft.smoke.ui.setupWithNavController

class BottomNavFragment : BaseFragment(R.layout.fragment_main) {
    private val binding by viewBinding<FragmentMainBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val currentNavController = setupBottomNavigationBar()
        currentNavController.observe(viewLifecycleOwner) { controller ->
            controller ?: return@observe
            updateToolbarTitle(controller)
        }
    }

    private fun updateToolbarTitle(controller: NavController) {
        binding.toolbar.title = when (controller.graph.id) {
            R.id.hookah_nav_graph -> getString(R.string.hookah)
            R.id.shops_nav_graph -> getString(R.string.shops)
            R.id.video_nav_graph -> getString(R.string.video)
            else -> throw IllegalStateException("No graph")
        }
    }

    private fun setupBottomNavigationBar(): LiveData<NavController> =
        binding.bottomNavigationView.setupWithNavController(
            navGraphIds = listOf(
                R.navigation.hookah_nav_graph, R.navigation.shops_nav_graph, R.navigation.video_nav_graph
            ),
            fragmentManager = childFragmentManager,
            containerId = R.id.bottom_container,
        )

}
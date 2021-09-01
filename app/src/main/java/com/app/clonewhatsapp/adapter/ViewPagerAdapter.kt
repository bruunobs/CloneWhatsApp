package com.app.clonewhatsapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.app.clonewhatsapp.fragment.*


class ViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle): FragmentStateAdapter(fragmentManager,lifecycle)  {
    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 ->{
                CameraFragment()

            }
            1 ->{
                ConversasFragment()
            }
            2 ->{
                StatusFragment()
            }
            3 ->{
                ChamadasFragment()
            }
            else ->{
                Fragment()
            }
        }
    }

}
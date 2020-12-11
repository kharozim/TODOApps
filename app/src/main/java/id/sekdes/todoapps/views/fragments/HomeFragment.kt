package id.sekdes.todoapps.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import id.sekdes.todoapps.R
import id.sekdes.todoapps.databinding.FragmentHomeBinding
import id.sekdes.todoapps.models.PageModel
import id.sekdes.todoapps.views.adapters.PagerAdapter

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: PagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setView()
        return binding.root
    }

    private fun setView(){
        binding.apply {

            val pager = listOf(
                PageModel("Todo", ListFragment()),
                PageModel("Missed", MissedFragment()),
                PageModel("Past", PastFragment())
            )
            adapter = PagerAdapter(pager, childFragmentManager, lifecycle)

            vpHome.adapter = adapter

            TabLayoutMediator(tlHome, vpHome) { tab, index ->
                tab.text = pager[index].title
            }.attach()

            fabAdd.setOnClickListener {
                findNavController().navigate(R.id.action_homeFragment_to_addFragment)
            }
        }
    }
}

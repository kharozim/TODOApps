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
import kotlinx.android.synthetic.main.fragment_home.*

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
                PageModel("Important", ImportantFragment()),
                PageModel("Past", PastFragment())
            )
            adapter = PagerAdapter(pager, childFragmentManager, lifecycle)

            vpHome.adapter = adapter

            TabLayoutMediator(tlHome, vpHome) { tab, index ->
                tab.text = pager[index].title
            }.attach()


            topAppBar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_add -> {
                        findNavController().navigate(R.id.action_homeFragment_to_addFragment)
                        true
                    }
                    else -> false
                }
            }
        }
    }
}

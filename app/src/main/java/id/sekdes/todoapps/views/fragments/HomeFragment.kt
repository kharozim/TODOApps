package id.sekdes.todoapps.views.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.tabs.TabLayoutMediator
import id.sekdes.todoapps.R
import id.sekdes.todoapps.databinding.FragmentHomeBinding
import id.sekdes.todoapps.models.PageModel
import id.sekdes.todoapps.views.adapters.PagerAdapter
import kotlinx.android.synthetic.main.fragment_add.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: PagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false).apply {

            val pager = listOf(
                PageModel("Todo", ListFragment()),
                PageModel("Important", ImportantFragment()),
                PageModel("Past", PastFragment())
            )
            val adapter = PagerAdapter(pager, childFragmentManager, lifecycle)

            vpHome.adapter = adapter

            TabLayoutMediator(tlHome, vpHome) { tab, index ->
                tab.text = pager[index].title
            }.attach()
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.top_app_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return NavigationUI.onNavDestinationSelected(item, requireView().findNavController()) || super.onOptionsItemSelected(item)
    }
}

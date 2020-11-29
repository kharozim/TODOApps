package id.sekdes.todoapps.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.sekdes.todoapps.R
import id.sekdes.todoapps.databinding.FragmentDetailBinding
import id.sekdes.todoapps.databinding.FragmentHomeBinding

class DetailFragment : Fragment() {


    private lateinit var binding : FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(layoutInflater, container, false).apply {






        }
        return binding.root
    }
}
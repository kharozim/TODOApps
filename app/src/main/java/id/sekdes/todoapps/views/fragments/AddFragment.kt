package id.sekdes.todoapps.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import id.sekdes.todoapps.R
import id.sekdes.todoapps.databinding.FragmentAddBinding
import id.sekdes.todoapps.databinding.FragmentHomeBinding


class AddFragment : Fragment() {

    private lateinit var binding : FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater, container, false).apply {






        }
        return binding.root
    }
}
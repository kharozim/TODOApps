package id.sekdes.todoapps.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.igreenwood.loupe.Loupe
import id.sekdes.todoapps.R
import id.sekdes.todoapps.databinding.FragmentImageBinding


class ImageFragment : Fragment() {
    lateinit var binding: FragmentImageBinding
    private val args by navArgs<ImageFragmentArgs>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImageBinding.inflate(inflater, container, false)
        binding.apply {
            Glide.with(requireContext())
                .load( args.image)
                .into(imageDetail)

            Loupe.create(imageDetail, imageDetailContainer) {
                useFlingToDismissGesture = false
                onViewTranslateListener = object : Loupe.OnViewTranslateListener {

                    override fun onStart(view: ImageView) {}

                    override fun onViewTranslate(view: ImageView, amount: Float) {}

                    override fun onRestore(view: ImageView) {}

                    override fun onDismiss(view: ImageView) {}
                }
            }
        }

        return binding.root
    }


}
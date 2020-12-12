package id.sekdes.todoapps.views.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.igreenwood.loupe.Loupe
import id.sekdes.todoapps.R
import id.sekdes.todoapps.databinding.ItemListAddImageBinding
import id.sekdes.todoapps.databinding.ItemListAddImageMoreBinding
import id.sekdes.todoapps.databinding.ItemListTodoBinding
import id.sekdes.todoapps.models.TodoModel


class ImageAdapter(
    private val context: Context,
    private val listener: ImageListener
) : RecyclerView.Adapter<ImageAdapter.MyViewHolder>() {

    var imageList = mutableListOf<Uri>()


   inner class MyViewHolder(
        val itemBinding: ItemListAddImageBinding,
        private val listener: ImageListener
    ) : RecyclerView.ViewHolder(itemBinding.root) {
        private var binding: ItemListAddImageBinding? = null

        fun bindData(uri: Uri) {
            this.binding = itemBinding

            itemBinding.apply {

                Glide.with(context)
                    .load(uri)
                    .into(ivImage)

                Loupe.create(ivImage, imageContainer) {
                    useFlingToDismissGesture = false
                    onViewTranslateListener = object : Loupe.OnViewTranslateListener {

                        override fun onStart(view: ImageView) {}

                        override fun onViewTranslate(view: ImageView, amount: Float) {}

                        override fun onRestore(view: ImageView) {}

                        override fun onDismiss(view: ImageView) {}
                    }
                }

                imageButton.setOnClickListener { listener.onDelete(uri) }
            }

        }
    }

    fun updateData(uri: Uri) {
        val index = imageList.indexOfFirst { it == uri }
        if (index != -1) {
            imageList[index] = uri
            notifyItemChanged(index)
        }
    }

    fun setData(item: MutableList<Uri>) {
        this.imageList = item
        notifyDataSetChanged()
    }

    interface ImageListener {
        fun onClick(uri: Uri)
        fun onDelete(uri: Uri)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(context)
        val binding: ItemListAddImageBinding =
            ItemListAddImageBinding.inflate(inflater, parent, false)

        return MyViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(imageList[position])

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    fun deleteData(uri: Uri) {
        val index = imageList.indexOfFirst { it == uri }
        if (index != -1) {
            imageList.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
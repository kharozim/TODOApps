package id.sekdes.todoapps.views.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import id.sekdes.todoapps.databinding.ItemListAddImageBinding
import id.sekdes.todoapps.databinding.ItemListDetailImageBinding


class ImageDetailAdapter(
    private val context: Context
) : RecyclerView.Adapter<ImageDetailAdapter.MyViewHolder>() {

    var imageList = mutableListOf<Uri>()


    inner class MyViewHolder(val binding: ItemListDetailImageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bindData(uri: Uri) {
            binding.run {

                Glide.with(binding.root)
                    .load(uri)
                    .into(ivImage)

            }
        }
    }

/*    fun updateData(uri: Uri) {
        val index = imageList.indexOfFirst { it == uri }
        if (index != -1) {
            imageList[index] = uri
            notifyItemChanged(index)
        }
    }*/

    fun setData(item: MutableList<Uri>) {
        this.imageList = item
        notifyDataSetChanged()
    }

    /*  interface ImageListener {
          fun onClick(uri: Uri)
          fun onDelete(uri: Uri)
      }*/

    /* fun deleteData(uri: Uri) {
        val index = imageList.indexOfFirst { it == uri }
        if (index != -1) {
            imageList.removeAt(index)
            notifyItemRemoved(index)
        }
    }*/


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemListDetailImageBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindData(imageList[position])

    }

    override fun getItemCount(): Int {
        return imageList.size
    }


}
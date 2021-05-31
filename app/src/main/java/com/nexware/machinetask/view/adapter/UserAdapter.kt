package com.nexware.machinetask.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nexware.machinetask.R
import com.nexware.machinetask.room.User
import java.util.*
import kotlin.collections.ArrayList

class UserAdapter (
    val context: Context,
    items: List<User>
) : RecyclerView.Adapter<UserAdapter.ViewHolder>(), Filterable {


    private val original_list: List<User> = items
    private var ListFiltered: List<User> = items

    override fun onBindViewHolder(holder: ViewHolder, p1: Int) {
        holder.name.text = ListFiltered[p1].first_name+" "+ListFiltered[p1].last_name
        holder.email.text = ListFiltered[p1].email

        Glide.with(context)
            .load(ListFiltered[p1].avatar)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(holder.userImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_items, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ListFiltered.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userImage: ImageView = itemView.findViewById(R.id.user_image)
        val name: TextView = itemView.findViewById(R.id.name)
        val email: TextView =itemView.findViewById(R.id.email)
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) ListFiltered = original_list else {
                    val filteredList = ArrayList<User>()
                    original_list
                        .filter {
                            it.first_name.toLowerCase(Locale.getDefault())
                                .contains(charString.toLowerCase(Locale.getDefault())) or it.last_name.contains(
                                constraint!!
                            ) or it.email.contains(
                                constraint
                            )
                        }
                        .forEach { filteredList.add(it) }
                    ListFiltered = filteredList
                }
                return FilterResults().apply { values = ListFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                ListFiltered = results?.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }

}
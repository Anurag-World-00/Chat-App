package com.codinghits.chatapp

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val context: Context, private val userList: ArrayList<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textName: TextView = itemView.findViewById(R.id.txt_name)

        fun bind(user: User) {
            textName.text = user.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.user_layout, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.bind(currentUser)

        holder.itemView.setOnClickListener {
            try {
                // Ensure currentUser's name and uid are not null
                val name = currentUser.name ?: "Unknown User"
                val uid = currentUser.uid ?: ""

                Log.d("UserAdapter", "Starting ChatActivity with name: $name, uid: $uid")

                val intent = Intent(context, ChatActivity::class.java).apply {
                    putExtra("name", name)
                    putExtra("uid", uid)
                    // Add this flag
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Error starting ChatActivity", Toast.LENGTH_SHORT).show()
                Log.e("UserAdapter", "Error starting ChatActivity", e)
            }
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}

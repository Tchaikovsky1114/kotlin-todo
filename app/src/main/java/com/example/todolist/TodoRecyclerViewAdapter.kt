package com.example.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.databinding.ItemTodoBinding
import com.example.todolist.db.TodoEntity

class TodoRecyclerViewAdapter(private val todoList : ArrayList<TodoEntity>, private val listener: OnItemLongClickListener): RecyclerView.Adapter<TodoRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding : ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvImportance = binding.tvImportance
        val tvTitle = binding.tvTitle
        val root = binding.root
    }

    // MyViewHolder를 객체로 반환. (뷰 객체 생성)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: ItemTodoBinding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    // 방금 만든 뷰홀더와 데이터를 묶어주는 역할
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val todoData = todoList[position]

        when (todoData.importance) {
            1 -> holder.tvImportance.setBackgroundResource(R.color.red_400)

            2 -> holder.tvImportance.setBackgroundResource((R.color.yellow_400))

            3 -> holder.tvImportance.setBackgroundResource((R.color.green))

        }
        holder.tvImportance.text = todoData.importance.toString()
        holder.tvTitle.text = todoData.title

        holder.root.setOnLongClickListener {
            listener.onLongClick(position)
            false
        }

    }

    // 아이템의 개수를 반환함.
    override fun getItemCount(): Int {
        return todoList.size
    }
}
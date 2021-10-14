package com.example.to_doapp.ui.alltodos

import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.to_doapp.R
import com.example.to_doapp.data.Task
import com.example.to_doapp.data.TodoItem
import com.example.to_doapp.databinding.ItemTodoBinding
import com.example.to_doapp.utils.Comparators

class AllTodoAdapter(private val listener: AddEditTask):
    ListAdapter<TodoItem, AllTodoAdapter.TodoViewHolder>(Comparators.TODO_COMPARATOR) {

    private var subTaskAdapter: SubTaskAdapter?= null

    inner class TodoViewHolder(val binding: ItemTodoBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindTodo(todoItem: TodoItem) {
            binding.apply {
                todoTitle.text = todoItem.title
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val addTodoViewHolder = TodoViewHolder(binding)
        binding.todoTitle.setOnClickListener {
            listener.addEditTask(getItem(addTodoViewHolder.adapterPosition))
        }
        binding.todoMore.setOnClickListener {
            val wrapper = ContextThemeWrapper(parent.context, R.style.Widget_AppCompat_PopupMenu)
            val popup = PopupMenu(wrapper, it, Gravity.END)
            popup.inflate(R.menu.menu_todo)
            popup.setOnMenuItemClickListener { item ->
                when(item.itemId) {
                    R.id.remove_todo -> {
                        listener.removeTodo(getItem(addTodoViewHolder.adapterPosition))
                        Toast.makeText(parent.context, "Todo Removed", Toast.LENGTH_SHORT)
                            .show()
                    }
                    R.id.edit_todo -> {
                        listener.addEditTask(getItem(addTodoViewHolder.adapterPosition))
                    }
                }
                return@setOnMenuItemClickListener true
            }
            popup.show()
        }
        return addTodoViewHolder
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val todoItem = getItem(position)
        holder.bindTodo(todoItem)

        subTaskAdapter = SubTaskAdapter(todoItem, listener)
        holder.binding.subTaskRecyclerview.apply {
            adapter = subTaskAdapter
            layoutManager = LinearLayoutManager(holder.binding.root.context)
        }

    }
}

interface AddEditTask {
    fun addEditTask(todoItem: TodoItem)
    fun updateSubTaskCompletion(todoItem: TodoItem, position: Int, isChecked: Boolean)
    fun removeSubTask(todoItemId: Int, tasks: List<Task>)
    fun removeTodo(todoItem: TodoItem)
}
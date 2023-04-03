package com.example.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.room.Dao
import com.example.todolist.databinding.ActivityAddTodoBinding
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.db.AppDatabase
import com.example.todolist.db.TodoDao

import com.example.todolist.db.TodoEntity

class AddTodoActivity : AppCompatActivity() {

    // 여기서는 activity_add_todo.xml에 존재하는 뷰를 바인딩함.
    private lateinit var binding : ActivityAddTodoBinding
    private lateinit var db : AppDatabase
    private lateinit var todoDao: TodoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
        todoDao = db.getTodoDao()


        binding.btnComplete.setOnClickListener {
            insertTodo()
        }
    }

    private var impData = 0;
    private fun insertTodo() {
        val todoTitle = binding.edittextTitle.text.toString()

        when(binding.radioGroup.checkedRadioButtonId) {
            R.id.btn_high -> impData = 1;
            R.id.btn_middle -> impData = 2;
            R.id.btn_low -> impData = 3;
        }
        if(impData == 0 || todoTitle.isBlank()) {
            Toast.makeText(this, "모든 항목을 채워주세요", Toast.LENGTH_SHORT).show()
        }else {
            Thread {
                todoDao.insertTodo(TodoEntity(id = null, todoTitle, impData))
                runOnUiThread {
                    Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                    // addTodo activity 종료 메인엑티비티로 전환
                    finish()
                }
            }.start()
        }
    }
}
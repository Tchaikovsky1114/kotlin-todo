package com.example.todolist

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.db.AppDatabase
import com.example.todolist.db.TodoDao
import com.example.todolist.db.TodoEntity

class MainActivity : AppCompatActivity(), OnItemLongClickListener {

    // findViewById를 사용하지 않고 뷰를 바인딩하기 위해 ViewBinding 라이브러리 사용
    // 여기서는 activity_main.xml에 존재하는 뷰를 바인딩함.
    // 1. xml(layout)의 뷰들을 바인딩 할 변수 설정
    private lateinit var binding: ActivityMainBinding
    private lateinit var db : AppDatabase
    private lateinit var todoDao: TodoDao
    private lateinit var todoList: ArrayList<TodoEntity>
    // 작성한 adapter(TodoRecyclerViewAdapter)를 가져온다
    private lateinit var adapter : TodoRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//      2. inflate : xml에 표기된 레이아웃들을 메모리에 객체화한다.
//       => XML에 작성된 코드를 객체화해서 해당 클래스에서 객체의 프로퍼티를 꺼내오듯이 사용한다.
        binding = ActivityMainBinding.inflate(layoutInflater)
//      3. setContentView를 호출하며 인수로 binding에 묶인 xml들을 객체화한다.
//      이후 xml에 기입된 UI 요소들을 끌어와 쓸 수 있게된다.
        setContentView(binding.root)
        db = AppDatabase.getInstance(this)!!
        todoDao = db.getTodoDao()
        getAllTodoList()
        binding.btnAddTodo.setOnClickListener {
            val intent = Intent(this, AddTodoActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getAllTodoList() {
        Thread {
            todoList = ArrayList(todoDao.getAllTodo())
            setRecyclerView()
        }.start()
    }

    // UI 변경이기 때문에 메인쓰레드(UI쓰레드)에서 사용해야 함.
    private fun setRecyclerView() {
        runOnUiThread {
            adapter = TodoRecyclerViewAdapter(todoList, this)
            binding.recyclerview.adapter = adapter
            binding.recyclerview.layoutManager = LinearLayoutManager(this)
        }
    }

    override fun onRestart() {
        super.onRestart()
        getAllTodoList()
    }

    override fun onLongClick(position: Int) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.alert_title))
        builder.setMessage(getString(R.string.alert_message))
        builder.setNegativeButton(getString(R.string.alert_no), null)
        builder.setPositiveButton(getString(R.string.alert_yes),
            object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    deleteTodo(position)
                }
            })
        builder.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun deleteTodo(position: Int) {
        Thread{
            todoDao.deleteTodo(todoList[position])
            todoList.removeAt(position)

            runOnUiThread {
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "삭제 되었습니다.",Toast.LENGTH_SHORT).show()
            }
        }.start()
    }
}

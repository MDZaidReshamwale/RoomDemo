package com.demo.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.Observer
//import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager.VERTICAL
import com.demo.roomdemo.db.RoomAppDb
import com.demo.roomdemo.db.UserEntity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RecyclerViewAdapter.RowClickListener {

    lateinit var recyclerViewAdapter: RecyclerViewAdapter
//    lateinit var viewModel: MainActivityViewModel
lateinit var allUsers : MutableLiveData<List<UserEntity>>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerViewAdapter = RecyclerViewAdapter(this@MainActivity)
            adapter = recyclerViewAdapter
            val divider = DividerItemDecoration(applicationContext, VERTICAL)
            addItemDecoration(divider)
            val userDao = RoomAppDb.getAppDatabase((getApplication()))?.userDao()
            allUsers = MutableLiveData()
            val list = userDao?.getAllUserInfo()

            allUsers.postValue(list)

        }

//        fun getAllUsers() {
//            val userDao = RoomAppDb.getAppDatabase((getApplication()))?.userDao()
//            val list = userDao?.getAllUserInfo()
//
//            allUsers.postValue(list)
//        }

//        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel::class.java)
//        viewModel.getAllUsersObservers().observe(this, Observer {
//            recyclerViewAdapter.setListData(ArrayList(it))
//            recyclerViewAdapter.notifyDataSetChanged()
//        })


        saveButton.setOnClickListener {
            val name  = nameInput.text.toString()
            val email  = emailInput.text.toString()
            val phone = phoneInput.text.toString()
            if(saveButton.text.equals("Save")) {
                val user = UserEntity(0, name, email, phone)
                val userDao = RoomAppDb.getAppDatabase(getApplication())?.userDao()
                userDao?.insertUser(user)

            } else {
                val user = UserEntity(nameInput.getTag(nameInput.id).toString().toInt(), name, email, phone)

                val userDao = RoomAppDb.getAppDatabase(getApplication())?.userDao()
                userDao?.updateUser(user)
                allUsers
                saveButton.setText("Save")
            }
            nameInput.setText("")
            emailInput.setText("")
        }
    }



    override fun onDeleteUserClickListener(user: UserEntity) {
        val userDao = RoomAppDb.getAppDatabase(getApplication())?.userDao()
        userDao?.deleteUser(user)
        allUsers
    }

    override fun onItemClickListener(user: UserEntity) {
        nameInput.setText(user.name)
        emailInput.setText(user.email)
        phoneInput.setText(user.phone)
        nameInput.setTag(nameInput.id, user.id)
        saveButton.setText("Update")
    }
}
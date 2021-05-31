package com.nexware.machinetask.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.airbnb.lottie.LottieAnimationView
import com.ferfalk.simplesearchview.SimpleSearchView
import com.nexware.machinetask.R
import com.nexware.machinetask.room.AppDatabase
import com.nexware.machinetask.room.User
import com.nexware.machinetask.util.AppConstant
import com.nexware.machinetask.view.adapter.UserAdapter
import com.nexware.machinetask.viewmodel.UserViewModel

class UserActivity:AppCompatActivity() {
    private lateinit var viewmodel: UserViewModel
    private val searchView: SimpleSearchView by lazy { findViewById(R.id.searchView) }
    private val loader: LottieAnimationView by lazy { findViewById(R.id.loader) }
    private val recyclerview: RecyclerView by lazy { findViewById(R.id.recyclerview) }

    private lateinit var datas:ArrayList<User>

    private lateinit var adapter: UserAdapter

    private var db: AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        this.viewmodel = ViewModelProvider(this).get(UserViewModel::class.java)
        datas= ArrayList()
        this.db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "user_database").allowMainThreadQueries().build()
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerview.layoutManager= LinearLayoutManager(this)
        adapter= UserAdapter(this, datas)
        recyclerview.adapter=adapter

        viewmodel.mShowApiError.observeForever {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }

        viewmodel.mShowNetworkError.observeForever {
            Toast.makeText(this, AppConstant.NoInternet, Toast.LENGTH_SHORT).show()
        }

        viewmodel.mShowProgressBar.observeForever {
            if(it){
                loader.visibility= View.VISIBLE
            }else{
                loader.visibility= View.GONE
            }
        }

        viewmodel.getUser().observeForever {
            if (it != null) {
                val data: ArrayList<User> = ArrayList()
                for (datas in it.data) {
                    data.add(
                        User(
                            datas.email,
                            datas.first_name,
                            datas.last_name,
                            datas.avatar,
                            datas.id
                        )
                    )
                }
                add_room(data)
            }

        }
        searchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("SimpleSearchView", "Submit:$query")
                adapter.filter.filter(query)

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                Log.d("SimpleSearchView", "Text changed:$newText")
                adapter.filter.filter(newText)
                return false
            }

            override fun onQueryTextCleared(): Boolean {
                Log.d("SimpleSearchView", "Text cleared")
                adapter.notifyDataSetChanged()
                return false
            }
        })

    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        setupSearchView(menu)
        return true
    }
    private fun setupSearchView(menu: Menu) {
        val item: MenuItem = menu.findItem(R.id.action_search)
        searchView.setMenuItem(item)

    }

    override fun onBackPressed() {
        if (searchView.onBackPressed()) {
            return
        }
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (searchView.onActivityResult(requestCode, resultCode, data)) {
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
    fun add_room(data:ArrayList<User>){
        db!!.userDao().insertAllUsers(data)
        get_data()
    }

    fun get_data(){
        datas.clear()
        datas.addAll(db!!.userDao().getData())
        recyclerview.adapter!!.notifyDataSetChanged()
    }
}
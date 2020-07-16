package com.nandom.udemyroomproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nandom.udemyroomproject.databinding.ActivityMainBinding
import com.nandom.udemyroomproject.db.SubscriberDatabase
import com.nandom.udemyroomproject.db.SubscriberRepository

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivityTAG"
    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)

        val factory = SubscriberViewModelFactory(repository)

        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)
        binding.myviewmodel = subscriberViewModel

        binding.lifecycleOwner = this

        initRecyclerView()


    }

    private fun displaySubscribers(){
        subscriberViewModel.subscribers.observe(this, Observer {
            binding.subscriberRecyclerView.adapter = MyRecyclerViewAdapter(it)
            Log.d(TAG, "displaySubscribers: ${it.toString()}")
        })
    }

    private fun initRecyclerView(){
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
        displaySubscribers()

    }
}
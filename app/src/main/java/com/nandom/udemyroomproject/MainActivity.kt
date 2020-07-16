package com.nandom.udemyroomproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.nandom.udemyroomproject.databinding.ActivityMainBinding
import com.nandom.udemyroomproject.db.Subscriber
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

        subscriberViewModel.message.observe(this, Observer { it ->
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun displaySubscribers(){
        subscriberViewModel.subscribers.observe(this, Observer {
            binding.subscriberRecyclerView.adapter = MyRecyclerViewAdapter(it) { selectedItem:Subscriber->listItemClicked(selectedItem)}
            Log.d(TAG, "displaySubscribers: $it")
        })
    }

    private fun initRecyclerView(){
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
        displaySubscribers()

    }

    private fun listItemClicked(subscriber: Subscriber){
//        Toast.makeText(this, "Selected name is ${subscriber.name} ", Toast.LENGTH_LONG).show()
        subscriberViewModel.initUpdateAndDelete(subscriber)
    }
}
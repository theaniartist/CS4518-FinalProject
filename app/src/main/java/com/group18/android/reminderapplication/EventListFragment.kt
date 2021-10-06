package com.group18.android.reminderapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import java.util.*

private const val TAG = "EventListFragment"
private const val REQUEST_CARD = 0

class EventListFragment : Fragment() {

    private lateinit var eventRecyclerView: RecyclerView
    private var adapter: EventAdapter? = EventAdapter(emptyList())

    private val eventListViewModel: EventListViewModel by lazy {
        ViewModelProviders.of(this).get(EventListViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event_list, container, false)
        eventRecyclerView =
            view.findViewById(R.id.event_recycler_view) as RecyclerView
        eventRecyclerView.layoutManager = LinearLayoutManager(context)
        eventRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventListViewModel.eventListLiveData.observe(
            viewLifecycleOwner,
            Observer { events ->
                events?.let {
                    Log.i(TAG, "Got events ${events.size}")
                    updateUI(events)
                }
            })
    }

    private fun updateUI(events: List<Event>) {
        adapter = EventAdapter(events)
        eventRecyclerView.adapter = adapter
    }

    private inner class EventHolder(view: View) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var event: Event

        private val titleTextView: TextView = itemView.findViewById(R.id.event_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.event_date)
        private val eventImageView:ImageView = itemView.findViewById(R.id.event_view)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(event: Event) {
            this.event = event
            titleTextView.text = this.event.title
            dateTextView.text = this.event.date.toString()
        }

        override fun onClick(v: View) {
            val intent = Intent(this@EventListFragment.context, SubActivity::class.java)
            startActivityForResult(intent, REQUEST_CARD)
            eventImageView.setImageResource(R.drawable.ic_check)
        }
    }

    private inner class EventAdapter(var events: List<Event>) :
        RecyclerView.Adapter<EventHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : EventHolder {
            val view = layoutInflater.inflate(R.layout.list_item_event, parent, false)
            return EventHolder(view)
        }

        override fun getItemCount() = events.size

        override fun onBindViewHolder(holder: EventHolder, position: Int) {
            val event = events[position]
            holder.bind(event)
        }
    }

    companion object {
        fun newInstance(): EventListFragment {
            return EventListFragment()
        }
    }
}
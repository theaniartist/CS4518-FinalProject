package com.group18.android.reminderapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

private const val TAG = "EventListFragment"
private const val REQUEST_CARD = 0

class EventListFragment : Fragment() {

    private lateinit var eventRecyclerView: RecyclerView
    private var adapter: EventAdapter? = null

    private val eventListViewModel: EventListViewModel by lazy {
        ViewModelProviders.of(this).get(EventListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total crimes: ${eventListViewModel.events.size}")
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

        updateUI()

        return view
    }

    private fun updateUI() {
        val events = eventListViewModel.events
        adapter = EventAdapter(events)
        eventRecyclerView.adapter = adapter
    }

    private inner class EventHolder(view: View) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var event: Event

        val titleTextView: TextView = itemView.findViewById(R.id.event_title)
        val dateTextView = itemView.findViewById<TextView>(R.id.event_date)

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
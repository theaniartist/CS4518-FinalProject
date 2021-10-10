package com.group18.android.reminderapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
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

    interface Callbacks {
        fun onEventSelected(eventId: UUID)
    }
    private var callbacks: Callbacks? = null

    private lateinit var eventRecyclerView: RecyclerView
    private var adapter: EventAdapter? = EventAdapter(emptyList())

    private val eventListViewModel: EventListViewModel by lazy {
        ViewModelProviders.of(this).get(EventListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_event_list, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.new_event -> {
                val event = Event()
                eventListViewModel.addEvent(event)
                callbacks?.onEventSelected(event.id)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun updateUI(events: List<Event>) {
        adapter = EventAdapter(events)
        eventRecyclerView.adapter = adapter
    }

    private inner class EventHolder(view: View) :
        RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var event: Event

        private val titleTextView: TextView = itemView.findViewById(R.id.event_title)
        private val emailTextView: TextView = itemView.findViewById(R.id.event_email)
        private val dateTextView: TextView = itemView.findViewById(R.id.event_date)
        private val eventImageView:ImageView = itemView.findViewById(R.id.event_view)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(event: Event) {
            this.event = event
            titleTextView.text = this.event.title
            emailTextView.text = this.event.email
            dateTextView.text = this.event.date.toString()
        }

        override fun onClick(v: View) {
            eventImageView.setImageResource(R.drawable.ic_check)
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
package com.group18.android.reminderapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import java.util.*

private const val TAG = "EventFragment"
private const val ARG_EVENT_ID = "event_id"
private const val DIALOG_DATE = "DialogDate"
private const val REQUEST_DATE = 0

class EventFragment :Fragment(), DatePickerFragment.Callbacks {

    private lateinit var event: Event
    private lateinit var titleField: EditText
    private lateinit var emailField: EditText
    private lateinit var dateButton: Button

    private val eventViewModel: EventViewModel by lazy {
        ViewModelProviders.of(this).get(EventViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        event = Event()
        val eventId: UUID = arguments?.getSerializable(ARG_EVENT_ID) as UUID
        eventViewModel.loadEvent(eventId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_event, container, false)

        titleField = view.findViewById(R.id.event_title_edit) as EditText
        emailField = view.findViewById(R.id.event_email) as EditText
        dateButton = view.findViewById(R.id.event_date_button) as Button

        return view
    }

    override fun onStart() {
        super.onStart()
        val titleWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }
            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                event.title = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        val emailWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                sequence: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
                // This space intentionally left blank
            }
            override fun onTextChanged(
                sequence: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                event.email = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        titleField.addTextChangedListener(titleWatcher)
        emailField.addTextChangedListener(emailWatcher)

        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(event.date).apply {
                setTargetFragment(this@EventFragment, REQUEST_DATE)
                show(this@EventFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventViewModel.eventLiveData.observe(viewLifecycleOwner, { event ->
            event?.let {
                this.event = event
                updateUI()
            }
        })
    }

    override fun onStop() {
        super.onStop()
        eventViewModel.saveEvent(event)
    }

    override fun onDateSelected(date: Date) {
        event.date = date
        updateUI()
    }

    private fun updateUI() {
        Log.d("EventFragment", "event.title")
        Log.d("EventFragment", event.title.length.toString())
        titleField.setText(event.title)
        emailField.setText(event.email)
        dateButton.text = event.date.toString()
    }

    companion object {
        fun newInstance(crimeId: UUID): EventFragment {
            val args = Bundle().apply {
                putSerializable(ARG_EVENT_ID, crimeId)
            }
            return EventFragment().apply {
                arguments = args
            }
        }
    }
}
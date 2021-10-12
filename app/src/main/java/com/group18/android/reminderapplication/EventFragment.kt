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

    private lateinit var titleField: EditText
    private lateinit var emailField: EditText
    private lateinit var dateButton: Button
    private lateinit var submitButton: Button

    private var title: String = ""
    private var email: String = ""
    private var date: Date = Date()


    private val eventViewModel: EventViewModel by lazy {
        ViewModelProviders.of(this).get(EventViewModel::class.java)
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
        submitButton = view.findViewById(R.id.event_submit) as Button

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
                title = sequence.toString()
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
                email = sequence.toString()
            }
            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }

        titleField.addTextChangedListener(titleWatcher)
        emailField.addTextChangedListener(emailWatcher)

        dateButton.setOnClickListener {
            DatePickerFragment.newInstance(date).apply {
                setTargetFragment(this@EventFragment, REQUEST_DATE)
                show(this@EventFragment.requireFragmentManager(), DIALOG_DATE)
            }
        }

        submitButton.setOnClickListener {
            var emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
            if(title.isEmpty() || email.isEmpty()) {
                Toast.makeText(requireActivity().applicationContext, "Please enter a title and an email", Toast.LENGTH_SHORT).show()
            } else {
                if(email.matches(emailPattern.toRegex())) {
                    val event = Event(UUID.randomUUID(), title, email, date)
                    eventViewModel.addEvent(event)
                    requireActivity().supportFragmentManager.popBackStack()
                } else {
                    Toast.makeText(requireActivity().applicationContext, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventViewModel.eventLiveData.observe(viewLifecycleOwner, { event ->
            event?.let {
                updateUI()
            }
        })
    }

    override fun onDateSelected(date: Date) {
        this.date = date
        updateUI()
    }

    private fun updateUI() {
        titleField.setText(title)
        emailField.setText(email)
        dateButton.text = date.toString()
    }
}
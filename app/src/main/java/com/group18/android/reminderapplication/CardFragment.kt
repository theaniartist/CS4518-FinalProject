package com.group18.android.reminderapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import java.util.*

private const val TAG = "CardFragment"
private const val ARG_CARD_ID = "card_id"

class CardFragment : Fragment() {

    private lateinit var card: Card
    private lateinit var titleField: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        card = Card()
        val cardId: UUID = arguments?.getSerializable(ARG_CARD_ID) as UUID
        Log.d(TAG, "args bundle card ID: $cardId")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_card, container, false)

        titleField = view.findViewById(R.id.card_title) as EditText

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
                card.title = sequence.toString()
            }

            override fun afterTextChanged(sequence: Editable?) {
                // This one too
            }
        }
        titleField.addTextChangedListener(titleWatcher)
    }

    companion object {
        fun newInstance(cardId: UUID): CardFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CARD_ID, cardId)
            }
            return CardFragment().apply {
                arguments = args
            }
        }
    }
}
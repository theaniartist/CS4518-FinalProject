package com.group18.android.reminderapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

private const val TAG = "CardListFragment"

class CardListFragment : Fragment() {

    private lateinit var cardRecyclerView: RecyclerView
    private var adapter: CardAdapter? = null

    private val cardListViewModel: CardListViewModel by lazy {
        ViewModelProviders.of(this).get(CardListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total card:  ${cardListViewModel.cards.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_card_list, container, false)

        cardRecyclerView =
            view.findViewById(R.id.card_recycler_view) as RecyclerView
        cardRecyclerView.layoutManager = LinearLayoutManager(context)

        updateUI()

        return view
    }

    private fun updateUI() {
        val cards = cardListViewModel.cards
        adapter = CardAdapter(cards)
        cardRecyclerView.adapter = adapter
    }

    private inner class CardHolder(view: View) : RecyclerView.ViewHolder(view),
    View.OnClickListener {
        private lateinit var card: Card
        private val titleTextView: TextView = itemView.findViewById(R.id.card_title)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(card: Card) {
            this.card = card
            titleTextView.text = this.card.title
        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${card.title} pressed!", Toast.LENGTH_SHORT)
                .show()
        }

    }

    private inner class CardAdapter(var cards: List<Card>)
        : RecyclerView.Adapter<CardHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : CardHolder {
            val view = layoutInflater.inflate(R.layout.list_item_card, parent, false)
            return CardHolder(view)
        }
        override fun getItemCount() = cards.size

        override fun onBindViewHolder(holder: CardHolder, position: Int) {
            val card = cards[position]
            holder.bind(card)
        }
    }

    companion object {
        fun newInstance(): CardListFragment {
            return CardListFragment()
        }
    }
}
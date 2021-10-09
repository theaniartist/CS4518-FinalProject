package com.group18.android.reminderapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.lifecycle.Observer
import com.group18.android.reminderapplication.model.Card
import java.util.*

private const val TAG = "CardListFragment"

class CardListFragment : Fragment() {

    interface Callbacks {
        fun onCardSelected(cardId: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var cardRecyclerView: RecyclerView
    private var adapter: CardAdapter? = CardAdapter(emptyList())

    private val cardListViewModel: CardListViewModel by lazy {
        ViewModelProviders.of(this).get(CardListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
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
        cardRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // TODO: This needs to be changed to work with Firebase
//        cardListViewModel.cardListLiveData.observe(viewLifecycleOwner,
//            Observer { cards ->
//                cards?.let {
//                    Log.i(TAG, "Got cards ${cards.size}")
//                    updateUI(cards)
//                }
//            })
    }

    private fun updateUI(cards: List<Card>) {
        adapter = CardAdapter(cards)
        cardRecyclerView.adapter = adapter
    }

    private inner class CardHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnClickListener {

        private lateinit var card: Card
        private val titleTextView: TextView = itemView.findViewById(R.id.card_title)
        val descTextView: TextView = itemView.findViewById(R.id.card_description)
        private val cardImageView: ImageView = itemView.findViewById(R.id.card_image)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(card: Card) {
            this.card = card
            titleTextView.text = this.card.title
            descTextView.text = this.card.desc

            when {
                titleTextView.text.toString() == "Happy Birthday Card" -> {
                    cardImageView.setImageResource(R.drawable.ic_birthday)
                }
                titleTextView.text.toString() == "Happy Anniversary Card" -> {
                    cardImageView.setImageResource(R.drawable.ic_anniversary)
                }
                titleTextView.text.toString() == "Graduation Card" -> {
                    cardImageView.setImageResource(R.drawable.ic_grad)
                }
                titleTextView.text.toString() == "Christmas Card" -> {
                    cardImageView.setImageResource(R.drawable.ic_christmas)
                }
                titleTextView.text.toString() == "Happy New Year's Card" -> {
                    cardImageView.setImageResource(R.drawable.ic_firework)
                }
                titleTextView.text.toString() == "Happy Mother's Day Card" -> {
                    cardImageView.setImageResource(R.drawable.ic_mom)
                }
                titleTextView.text.toString() == "Happy Father's Day Card" -> {
                    cardImageView.setImageResource(R.drawable.ic_dad)
                }
                titleTextView.text.toString() == "Valentine's Day Card" -> {
                    cardImageView.setImageResource(R.drawable.ic_heart)
                }
                titleTextView.text.toString() == "Wedding Card" -> {
                    cardImageView.setImageResource(R.drawable.ic_ring)
                }
                else -> {cardImageView.setImageResource(R.drawable.ic_halloween)}
            }
        }

        override fun onClick(v: View) {
            Toast.makeText(context, "${card.title} pressed!", Toast.LENGTH_SHORT)
                .show()
            callbacks?.onCardSelected(card.id)
        }

    }

    private inner class CardAdapter(var cards: List<Card>) : RecyclerView.Adapter<CardHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
                : CardHolder {
            val view = layoutInflater.inflate(R.layout.list_item_card, parent, false)
            return CardHolder(view)
        }

        override fun getItemCount() = cards.size

        override fun onBindViewHolder(holder: CardHolder, position: Int) {
            holder.setIsRecyclable(false)
            val card = cards[position]
            holder.bind(card)
        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    companion object {
        fun newInstance(): CardListFragment {
            return CardListFragment()
        }
    }
}
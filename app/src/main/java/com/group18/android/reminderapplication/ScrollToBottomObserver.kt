package com.group18.android.reminderapplication

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ScrollToBottomObserver(
	private val recycler: RecyclerView,
	private val adapter: MessageAdapter,
	private val manager: LinearLayoutManager
) : RecyclerView.AdapterDataObserver() {
	override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
		super.onItemRangeInserted(positionStart, itemCount)

		val count = adapter.itemCount
		val lastVisiblePosition = manager.findLastCompletelyVisibleItemPosition()
		val loading = lastVisiblePosition == -1
		val atBottom = positionStart >= count - 1 && lastVisiblePosition == positionStart - 1

		if (loading || atBottom) {
			recycler.scrollToPosition(positionStart)
		}
	}
}
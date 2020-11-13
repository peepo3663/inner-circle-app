package edu.bu.metcs673.project.util

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object UIUtil {

    /** Helper method to view the chat messages.
     * Scrolls to the bottom of the screen every time the user types  */
    fun isTextVisible(recyclerView: RecyclerView, itemCount: Int): Boolean {
        val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        val positionOfLastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition()
        return positionOfLastVisibleItem >= itemCount
    }
}
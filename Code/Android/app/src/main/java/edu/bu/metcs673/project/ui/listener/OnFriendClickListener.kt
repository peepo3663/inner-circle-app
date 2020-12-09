package edu.bu.metcs673.project.ui.listener

import edu.bu.metcs673.project.model.user.User

interface OnFriendClickListener {
    fun onItemClicked(friend: User)
}
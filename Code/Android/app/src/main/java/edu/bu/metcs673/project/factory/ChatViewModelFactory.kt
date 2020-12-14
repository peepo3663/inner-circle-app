package edu.bu.metcs673.project.factory

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import edu.bu.metcs673.project.ui.chat.ChatViewModel

class ChatViewModelFactory(val application: Application, val chatRoomId: String): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatViewModel(application, chatRoomId) as T
    }
}
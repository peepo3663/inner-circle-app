package edu.bu.metcs673.project.adapter.chat

import android.content.BroadcastReceiver

class IndividualChat
{

    private var sender:String=""
    private var receiver:String=""
    private var text:String=""

    constructor()

    constructor(
            sender:String,
            receiver: String,
            text: String
    )
    {
        this.sender=sender
        this.receiver=receiver
        this.text=text

    }

    fun getSender():String?
    {
        return sender
    }

    fun setSender(sender:String?)
    {
        this.sender =  sender!!
    }

    fun getReceiver():String?
    {
        return receiver
    }

    fun setReceiver(receiver:String?)
    {
        this.receiver =  receiver!!
    }

    fun getMessage():String
    {
        return text
    }
}
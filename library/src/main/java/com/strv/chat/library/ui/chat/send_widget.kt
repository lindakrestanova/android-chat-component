package com.strv.chat.library.ui.chat

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.fragment.app.FragmentActivity
import com.strv.chat.library.R
import com.strv.chat.library.domain.client.ChatClient
import com.strv.chat.library.domain.client.observer.Observer
import com.strv.chat.library.domain.model.MessageModelRequest
import com.strv.chat.library.domain.model.MessageModelRequest.ImageMessageModel.Image
import com.strv.chat.library.domain.provider.ConversationProvider
import com.strv.chat.library.domain.provider.MediaProvider
import com.strv.chat.library.domain.provider.MemberProvider
import com.strv.chat.library.ui.openCamera
import com.strv.chat.library.ui.reset
import com.strv.chat.library.ui.selector
import com.strv.chat.library.ui.view.DIALOG_PHOTO_PICKER
import strv.ktools.logE
import strv.ktools.logI
import strv.ktools.logMe

class SendWidget @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var chatClient: ChatClient

    private lateinit var memberProvider: MemberProvider
    private lateinit var conversationProvider: ConversationProvider
    private lateinit var mediaProvider: MediaProvider

    private val buttonSend by lazy {
        findViewById<ImageButton>(R.id.ib_send)
    }

    private val editInput by lazy {
        findViewById<EditText>(R.id.et_message_input)
    }

    private val buttonImage by lazy {
        findViewById<ImageButton>(R.id.ib_image)
    }

    init {
        LayoutInflater.from(context).inflate(R.layout.layout_send_widget, this)
        buttonSendListener()
        buttonCameraListener()
    }

    operator fun invoke(
        chatClient: ChatClient,
        conversationProvider: ConversationProvider,
        memberProvider: MemberProvider,
        mediaProvider: MediaProvider,
        config: Builder.() -> Unit = {}
    ) {
        Builder(chatClient, conversationProvider, memberProvider, mediaProvider).apply(config)
            .build()
    }

    private fun buttonSendListener() {
        buttonSend.setOnClickListener {
            if (editInput.text.isNotBlank()) {
                sendTextMessage(memberProvider.currentUserId(), editInput.text.toString())
                editInput.reset()
            }
        }
    }

    private fun buttonCameraListener() {
        buttonImage.setOnClickListener {
            showPhotoPickerDialog()
        }
    }

    private fun showPhotoPickerDialog() {
        selector("Choose photo", arrayOf("Take photo", "Select from library")) {
            onClick { position ->
                when (position) {
                    //todo how to pass application name here
                    0 -> activity?.openCamera(mediaProvider.imageUri())
                    1 -> "Gallery".logMe()
                }
            }
        }.show((context as FragmentActivity).supportFragmentManager, DIALOG_PHOTO_PICKER)
    }

    fun sendImageMessage(messageUrl: String) {
        sendMessage(
            MessageModelRequest.ImageMessageModel(
                senderId = memberProvider.currentUserId(),
                conversationId = conversationProvider.conversationId,
                image = Image(0.0, 0.0, messageUrl)
            )
        )
    }

    private fun sendTextMessage(userId: String, message: String) {
        sendMessage(
            MessageModelRequest.TextMessageModel(
                senderId = userId,
                conversationId = conversationProvider.conversationId,
                text = message
            )
        )
    }

    private fun sendMessage(message: MessageModelRequest) {
        chatClient.sendMessage(message,
            object : Observer<Void?> {
                override fun onSuccess(response: Void?) {
                    logI("Message has been sent")
                }

                override fun onError(error: Throwable) {
                    logE(error.localizedMessage)
                }
            }
        )
    }

    inner class Builder(
        val chatClient: ChatClient,
        val conversationProvider: ConversationProvider,
        val memberProvider: MemberProvider,
        val mediaProvider: MediaProvider
    ) {

        fun build() {
            this@SendWidget.chatClient = chatClient
            this@SendWidget.memberProvider = memberProvider
            this@SendWidget.conversationProvider = conversationProvider
            this@SendWidget.mediaProvider = mediaProvider
        }
    }
}
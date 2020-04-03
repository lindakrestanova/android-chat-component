# Conversation list

Conversation list is a customizable component that displays
conversations of a user and listens for realtime updates.

## How to use

To make the component work correctly, you need to perform the following
steps.

### Add `ConversationRecyclerView` widget into your xml layout

```xml
<com.strv.chat.core.core.ui.conversation.ConversationRecyclerView
    android:id="@+id/rv_chat"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
```
### Initialize the component
Call type-safe `init()` builder function in `onCreate()` method
of your Activity that allows creating Kotlin-based domain-specific
language (DSL) suitable for configuring the component.

**Optional properties**:
- `imageLoader: ImageLoader` 
  -   Defines how to upload a picture's urls to `ImageView`s.
- `onItemClick: OnClickAction<ConversationItemView>`
  -  Defines the action that is performed after a user clicks on a
     conversation.
- `viewHolderProvider: ConversationViewHolderProvider` 
  - Allows the adding of a custom implementations of `ViewHolders`.
     
```kotlin
conversationRecyclerView.init {
    imageLoader = loader
    onConversationClick = { conversation ->
        openChat(conversation.id, conversation.otherMemberIds)
    }
}   
```

### Get realtime updates
`ConversationRecyclerView` **is not lifecycle-aware**. You must
configure manually when to start/stop observing data by calling
`onStart()` and `onStop()` functions. It is recommended to start
listening in `onStart()` method and to stop listening in `onStop()`
method of your activity/fragment.

```kotlin
override fun onStart() {
    super.onStart()

    conversationRecyclerView.onStart()
        .onError { error ->
            Log.e(TAG, error.localizedMessage ?: "Unknown error", error)
        }.onNext {
            Log.d("TAG", "Conversation list has been changed")
        }
}

override fun onStop() {
    super.onStop()

    conversationRecyclerView.onStop()
}
```

## Customization

### Styling via attributes
- `app:crv_titleTextSize` - custom title text size
- `app:crv_titleTextColor` - custom title text color
- `app:crv_titleTextStyle` - custom title fond style
- `app:crv_messageTextSize` - custom message text size
- `app:crv_messageTextColor` - custom message text color


### Create your own holder
You can define your own holder classes with the help of
`ConversationViewHolderProvider` and `ConversationVHConfig`.

```kotlin
val vhConfig = ConversationVHConfig(
    layoutId = R.layout.item_conversation,
    constructor = { parent, _ ->
        MyConversationViewHolder(
            parent
        )
    })

val viewHolderProvider = ConversationViewHolderProvider(
    conversationVHConfig = vhConfig
)

conversationRecyclerView.viewHolderProvider = viewHolderProvider
```

You can either inherit from `ConversationViewHolder` class or from the
default `DefaultConversationViewHolder` in case you are not planning
to rewrite the behavior from scratch.

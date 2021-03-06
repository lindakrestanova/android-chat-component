package com.strv.chat.firestore.entity.creator

import com.strv.chat.core.domain.model.creator.Creator
import com.strv.chat.core.domain.model.creator.CreatorConfiguration
import com.strv.chat.firestore.entity.FirestoreSeenEntity

internal object SeenEntityCreator : Creator<FirestoreSeenEntity, SeenEntityConfiguration> {

    override val create: SeenEntityConfiguration.() -> FirestoreSeenEntity = {
        FirestoreSeenEntity(messageId)
    }
}

internal class SeenEntityConfiguration(
    val messageId: String
) : CreatorConfiguration
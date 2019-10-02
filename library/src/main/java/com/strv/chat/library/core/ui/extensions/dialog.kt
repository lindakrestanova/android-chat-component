package com.strv.chat.library.core.ui.extensions

import com.strv.chat.library.core.ui.view.ItemListDialogFragment

internal fun selector(title: String?, list: Array<String>, setup: ItemListDialogFragment.() -> Unit) =
    ItemListDialogFragment.newInstance(title, list).apply(setup)
package com.mindera.skeletoid.kt.extensions.views

import android.support.v7.widget.Toolbar

fun Toolbar.setupToolbarMenuItemClickListener(menuItemClickListener: Toolbar.OnMenuItemClickListener) {

    this.setOnMenuItemClickListener(menuItemClickListener)

    (0 until this.menu.size())
            .map { this.menu.getItem(it) }
            .forEach { item -> item.actionView?.setOnClickListener { menuItemClickListener.onMenuItemClick(item) } }
}
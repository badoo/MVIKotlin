package com.arkivanov.mvikotlin.timetravel.proto.internal.data.treenode

data class TreeNode<out T : Any>(
    val value: T,
    val children: List<TreeNode<T>> = emptyList()
)

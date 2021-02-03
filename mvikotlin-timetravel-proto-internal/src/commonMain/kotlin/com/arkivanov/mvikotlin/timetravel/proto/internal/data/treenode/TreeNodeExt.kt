package com.arkivanov.mvikotlin.timetravel.proto.internal.data.treenode

fun TreeNode<*>.toFormattedString(): String =
    buildString {
        appendNode(node = this@toFormattedString, indent = 0)
    }

private fun StringBuilder.appendNode(node: TreeNode<*>, indent: Int) {
    append(" ".repeat(indent))
    appendLine(node.value.toString())

    node.children.forEach {
        appendNode(node = it, indent = indent + 2)
    }
}

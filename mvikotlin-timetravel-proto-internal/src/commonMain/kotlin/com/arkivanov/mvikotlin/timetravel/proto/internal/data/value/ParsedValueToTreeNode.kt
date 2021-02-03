package com.arkivanov.mvikotlin.timetravel.proto.internal.data.value

import com.arkivanov.mvikotlin.timetravel.proto.internal.data.treenode.TreeNode

fun ParsedValue.toTreeNode(name: String? = null): TreeNode<ValueDescription> =
    when (this) {
        is ParsedValue.Primitive -> toTreeNode(name = name)
        is ParsedValue.Object -> toTreeNode(name = name)
    }

private fun ParsedValue.Primitive.toTreeNode(name: String? = null): TreeNode<ValueDescription> =
    when (this) {
        is ParsedValue.Primitive.Int -> TreeNode(name = name, type = "Int", value = value)
        is ParsedValue.Primitive.Long -> TreeNode(name = name, type = "Long", value = value)
        is ParsedValue.Primitive.Short -> TreeNode(name = name, type = "Short", value = value)
        is ParsedValue.Primitive.Byte -> TreeNode(name = name, type = "Byte", value = value)
        is ParsedValue.Primitive.Float -> TreeNode(name = name, type = "Float", value = value)
        is ParsedValue.Primitive.Double -> TreeNode(name = name, type = "Double", value = value)
        is ParsedValue.Primitive.Char -> TreeNode(name = name, type = "Char", value = value)
        is ParsedValue.Primitive.Boolean -> TreeNode(name = name, type = "Boolean", value = value)
    }

private fun ParsedValue.Object.toTreeNode(name: String? = null): TreeNode<ValueDescription> =
    when (this) {
        is ParsedValue.Object.Int -> TreeNode(name = name, type = "Int", value = "{Int} $value")
        is ParsedValue.Object.Long -> TreeNode(name = name, type = "Long", value = "{Long} $value")
        is ParsedValue.Object.Short -> TreeNode(name = name, type = "Short", value = "{Short} $value")
        is ParsedValue.Object.Byte -> TreeNode(name = name, type = "Byte", value = "{Byte} $value")
        is ParsedValue.Object.Float -> TreeNode(name = name, type = "Float", value = "{Float} $value")
        is ParsedValue.Object.Double -> TreeNode(name = name, type = "Double", value = "{Double} $value")
        is ParsedValue.Object.Char -> TreeNode(name = name, type = "Char", value = "{Char} $value")
        is ParsedValue.Object.Boolean -> TreeNode(name = name, type = "Boolean", value = "{Boolean} $value")
        is ParsedValue.Object.String -> TreeNode(name = name, type = "String", value = "\"$value\"")
        is ParsedValue.Object.IntArray -> toTreeNode(name)
        is ParsedValue.Object.LongArray -> toTreeNode(name)
        is ParsedValue.Object.ShortArray -> toTreeNode(name)
        is ParsedValue.Object.ByteArray -> toTreeNode(name)
        is ParsedValue.Object.FloatArray -> toTreeNode(name)
        is ParsedValue.Object.DoubleArray -> toTreeNode(name)
        is ParsedValue.Object.CharArray -> toTreeNode(name)
        is ParsedValue.Object.BooleanArray -> toTreeNode(name)
        is ParsedValue.Object.Array -> toTreeNode(name)
        is ParsedValue.Object.Iterable -> toTreeNode(name)
        is ParsedValue.Object.Map -> toTreeNode(name)
        is ParsedValue.Object.Other -> toTreeNode(name)
        is ParsedValue.Object.Unparsed -> TreeNode(name = name, type = type, value = value)
    }

private fun ParsedValue.Object.IntArray.toTreeNode(name: String?) =
    iterableTreeNode(
        name = name,
        type = "IntArray",
        value = value,
        size = { it.size },
        children = { mapIndexed { index, item -> TreeNode(type = "[$index] = $item") } }
    )

private fun ParsedValue.Object.LongArray.toTreeNode(name: String?) =
    iterableTreeNode(
        name = name,
        type = "LongArray",
        value = value,
        size = { it.size },
        children = { mapIndexed { index, item -> TreeNode(type = "[$index] = $item") } }
    )

private fun ParsedValue.Object.ShortArray.toTreeNode(name: String?) =
    iterableTreeNode(
        name = name,
        type = "ShortArray",
        value = value,
        size = { it.size },
        children = { mapIndexed { index, item -> TreeNode(type = "[$index] = $item") } }
    )

private fun ParsedValue.Object.ByteArray.toTreeNode(name: String?) =
    iterableTreeNode(
        name = name,
        type = "ByteArray",
        value = value,
        size = { it.size },
        children = { mapIndexed { index, item -> TreeNode(type = "[$index] = $item") } }
    )

private fun ParsedValue.Object.FloatArray.toTreeNode(name: String?) =
    iterableTreeNode(
        name = name,
        type = "FloatArray",
        value = value,
        size = { it.size },
        children = { mapIndexed { index, item -> TreeNode(type = "[$index] = $item") } }
    )

private fun ParsedValue.Object.DoubleArray.toTreeNode(name: String?) =
    iterableTreeNode(
        name = name,
        type = "DoubleArray",
        value = value,
        size = { it.size },
        children = { mapIndexed { index, item -> TreeNode(type = "[$index] = $item") } }
    )

private fun ParsedValue.Object.CharArray.toTreeNode(name: String?) =
    iterableTreeNode(
        name = name,
        type = "CharArray",
        value = value,
        size = { it.size },
        children = { mapIndexed { index, item -> TreeNode(type = "[$index] = $item") } }
    )

private fun ParsedValue.Object.BooleanArray.toTreeNode(name: String?) =
    iterableTreeNode(
        name = name,
        type = "BooleanArray",
        value = value,
        size = { it.size },
        children = { mapIndexed { index, item -> TreeNode(type = "[$index] = $item") } }
    )

private fun ParsedValue.Object.Array.toTreeNode(name: String?) =
    iterableTreeNode(
        name = name,
        type = type,
        value = value,
        size = { it.size },
        children = { mapIndexed { index, item -> item.toTreeNode(name = "[$index]") } }
    )

private fun ParsedValue.Object.Iterable.toTreeNode(name: String?) =
    iterableTreeNode(
        name = name,
        type = type,
        value = value,
        size = { it.size },
        children = { mapIndexed { index, item -> item.toTreeNode(name = "[$index]") } }
    )

private fun ParsedValue.Object.Map.toTreeNode(name: String?) =
    iterableTreeNode(
        name = name,
        type = type,
        value = value,
        size = { it.size },
        children = {
            map { (key, value) ->
                val keyNode = key.toTreeNode(name = "key")
                val valueNode = value.toTreeNode(name = "value")

                TreeNode(
                    value = ValueDescription(type = "${keyNode.value.value} -> ${valueNode.value.value}"),
                    children = listOf(keyNode, valueNode)
                )
            }
        }
    )

private fun ParsedValue.Object.Other.toTreeNode(name: String?): TreeNode<ValueDescription> =
    TreeNode(
        name = name,
        type = type,
        value = value?.let { "{$type}" } ?: "null",
        children = value?.map { (name, obj) -> obj.toTreeNode(name = name) } ?: emptyList()
    )

private inline fun <T : Any> iterableTreeNode(
    name: String?,
    type: String,
    value: T?,
    size: (T) -> Int,
    children: T.() -> List<TreeNode<ValueDescription>>
): TreeNode<ValueDescription> =
    TreeNode(
        name = name,
        type = type,
        value = if (value == null) "null" else "{$type[${size(value)}]}",
        children = value?.let(children) ?: emptyList()
    )

private fun TreeNode(
    name: String? = null,
    type: String,
    value: Any? = null,
    children: List<TreeNode<ValueDescription>> = emptyList()
): TreeNode<ValueDescription> =
    TreeNode(
        value = ValueDescription(name = name, type = type, value = value),
        children = children
    )

data class ValueDescription(
    val name: String? = null,
    val type: String,
    val value: Any? = null
) {
    private val text =
        buildString {
            if (name != null) {
                append(name)
                append(": ")
            }

            append(type)

            if (value != null) {
                append(" = ")
                append(value)
            }
        }

    override fun toString(): String = text
}

package com.hjaquaculture.feature._temp

import android.R.attr.enabled
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun PreviewM3EditableTag() {
    M3EditableTag("标签", {}, {})

}

@Composable
fun M3EditableTag(
    text: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    InputChip(
        selected = false, // M3 中 InputChip 通常不作为选中态使用，而是作为操作实体
        onClick = onEdit, // 点击整个标签触发编辑
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge
            )
        },
        // 左侧图标：编辑提示
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "编辑",
                modifier = Modifier.size(18.dp)
            )
        },
        // 右侧图标：删除操作
        trailingIcon = {
            IconButton(
                onClick = onDelete,
                modifier = Modifier.size(18.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "删除",
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    )
}
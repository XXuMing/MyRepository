package com.hjaquaculture.feature.relationship

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.hjaquaculture.common.base.PeopleSymbol
import com.hjaquaculture.feature.AuthAction
import com.hjaquaculture.feature.components.FilterGroupConfig
import com.hjaquaculture.feature.components.SearchFilterBar

@Composable
fun RelationshipScreen(
    vm: RelationshipViewModel = hiltViewModel(),
    onAction: (AuthAction) -> Unit,
    scaffoldPadding: PaddingValues,
) {
    val listItems = vm.relationshipPagingData.collectAsLazyPagingItems()

    var searchQuery by remember { mutableStateOf("") }
    var selectedSymbol by remember { mutableStateOf<PeopleSymbol?>(null) }

    Column(modifier = Modifier.fillMaxWidth()
        .padding(scaffoldPadding)
        .padding(horizontal = 12.dp)
    )
    {

        SearchFilterBar(
            searchQuery = searchQuery,
            onSearchQueryChange = { searchQuery = it },
            searchPlaceholder = "账户名/姓名/昵称/电话/角色",          // 每个页面可自定义
            filterGroups = listOf(
                FilterGroupConfig(
                    title = "业务主体",
                    options = PeopleSymbol.entries,
                    selected = selectedSymbol,
                    onSelect = { selectedSymbol = it }
                )
            )
        )

        LazyColumn {
            items(
                count = listItems.itemCount,
                key = listItems.itemKey { it.syntheticId }
            ){
                index ->
                val item = listItems[index]
                item?.let { RelationshipCard(item) }
            }
        }
    }

}

@Composable
fun RelationshipCard(people: PeopleVO){

    var isExpanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(text = "${people.symbol}: ${people.name}")
            Text(text = people.createdAt)
        }
        Row{
            Text(text = "电话：${people.phone}")
            Spacer(Modifier.weight(1f))
        }
        Row {
            Text(text = "地址：${people.address}")
        }
    }
}
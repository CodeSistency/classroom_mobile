package com.example.classroom.common.composables.lists

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.material.pullrefresh.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

//
//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//fun <T> PaginatedList(
//    items: List<T>,
//    isPaginated: Boolean = false,
//    pageSize: Int = 20,
//    currentPage: Int = 1,
//    isLoading: Boolean = false,
//    isRefreshing: Boolean = false,
//    errorMessage: String? = null,
//    isEndOfList: Boolean = false,
//    onRefresh: (() -> Unit)? = null,
//    onLoadMore: ((page: Int, pageSize: Int) -> Unit)? = null,
//    onRenderItem: @Composable (T) -> Unit,
//    modifier: Modifier = Modifier
//) {
//    var refreshingState by remember { mutableStateOf(isRefreshing) }
//    val pullRefreshState = rememberPullRefreshState(refreshingState, { onRefresh?.invoke() })
//
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .pullRefresh(pullRefreshState)
//    ) {
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(8.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            when {
//                errorMessage != null -> {
//                    item {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = errorMessage,
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = MaterialTheme.colorScheme.error
//                            )
//                        }
//                    }
//                }
//
//                items.isEmpty() -> {
//                    item {
//                        Box(
//                            modifier = Modifier
//                                .fillMaxWidth()
//                                .padding(16.dp),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = "No items available.",
//                                style = MaterialTheme.typography.bodyMedium,
//                                color = MaterialTheme.colorScheme.onBackground
//                            )
//                        }
//                    }
//                }
//
//                else -> {
//                    items(items) { item ->
//                        onRenderItem(item)
//                    }
//
//                    if (isPaginated) {
//                        when {
//                            isLoading -> {
//                                item {
//                                    Box(
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .padding(16.dp),
//                                        contentAlignment = Alignment.Center
//                                    ) {
//                                        CircularProgressIndicator()
//                                    }
//                                }
//                            }
//
//                            isEndOfList -> {
//                                item {
//                                    Box(
//                                        modifier = Modifier
//                                            .fillMaxWidth()
//                                            .padding(16.dp),
//                                        contentAlignment = Alignment.Center
//                                    ) {
//                                        Text(
//                                            text = "No more items to load.",
//                                            style = MaterialTheme.typography.bodySmall,
//                                            color = MaterialTheme.colorScheme.onBackground
//                                        )
//                                    }
//                                }
//                            }
//
//                            else -> {
//                                item {
//                                    LaunchedEffect(Unit) {
//                                        onLoadMore?.invoke(currentPage, pageSize)
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        PullRefreshIndicator(
//            refreshing = refreshingState,
//            state = pullRefreshState,
//            modifier = Modifier.align(Alignment.TopCenter),
//            contentColor = MaterialTheme.colorScheme.primary
//        )
//    }
//}
//@OptIn(ExperimentalMaterialApi::class)
//@Composable
//fun <T> PaginatedList(
//    initialItems: List<T>,
//    loadItems: suspend (page: Int, pageSize: Int) -> List<T>,
//    onRefresh: suspend () -> Unit,
//    pageSize: Int = 20,
//    modifier: Modifier = Modifier,
//    onRenderItem: @Composable (T, Int) -> Unit // Updated to pass the index as well
//) {
//    val scope = rememberCoroutineScope()
//
//    // Internal state management
//    var items by remember { mutableStateOf(initialItems) }
//    var currentPage by remember { mutableStateOf(1) }
//    var isLoading by remember { mutableStateOf(false) }
//    var isRefreshing by remember { mutableStateOf(false) }
//    var isEndOfList by remember { mutableStateOf(false) }
//    var errorMessage by remember { mutableStateOf<String?>(null) }
//
//    suspend fun loadItemsInternal() {
//        isLoading = true
//        Log.e("pagination state load items internally", "pagination state load items internally")
//
//        try {
//            val newItems = loadItems(currentPage, pageSize)
//            if (newItems.isEmpty()) {
//                isEndOfList = true
//            } else {
//                items = if (currentPage == 1) newItems else items + newItems
//                isEndOfList = newItems.size < pageSize
//            }
//        } catch (e: Exception) {
//            errorMessage = "Failed to load items: ${e.localizedMessage}"
//        } finally {
//            isLoading = false
//            isRefreshing = false
//        }
//    }
//
//    // Pull-to-refresh state
//    val pullRefreshState = rememberPullRefreshState(
//        refreshing = isRefreshing,
//        onRefresh = {
//            isRefreshing = true
//            errorMessage = null
//            currentPage = 1
//            items = emptyList()
//            scope.launch {
//                onRefresh()
////                loadItemsInternal()
//            }
//        }
//    )
//
//    // Trigger loading more items when the user scrolls to the end
//    LaunchedEffect(currentPage) {
//        if (!isRefreshing && !isEndOfList && !isLoading) {
//            Log.e("pagination state load items internally launch", "pagination state load items internally launch")
//
//            loadItemsInternal()
//        }
//    }
//
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .pullRefresh(pullRefreshState)
//    ) {
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(8.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            // Error message
//            errorMessage?.let {
//                item {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = it,
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.error
//                        )
//                    }
//                }
//            }
//
//            // No items available
//            if (items.isEmpty() && errorMessage == null && !isLoading) {
//                item {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "No items available.",
//                            style = MaterialTheme.typography.bodyMedium,
//                            color = MaterialTheme.colorScheme.onBackground
//                        )
//                    }
//                }
//            }
//
//            // Render items with index
//            itemsIndexed(items) { index, item ->
//                onRenderItem(item, index)
//            }
//
//            // Loading spinner or end-of-list message
//            if (isLoading && errorMessage == null) {
//                item {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        CircularProgressIndicator()
//                    }
//                }
//            } else if (isEndOfList) {
//                item {
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(16.dp),
//                        contentAlignment = Alignment.Center
//                    ) {
//                        Text(
//                            text = "No more items to load.",
//                            style = MaterialTheme.typography.bodySmall,
//                            color = MaterialTheme.colorScheme.onBackground
//                        )
//                    }
//                }
//            }
//        }
//
//        PullRefreshIndicator(
//            refreshing = isRefreshing,
//            state = pullRefreshState,
//            modifier = Modifier.align(Alignment.TopCenter),
//            contentColor = MaterialTheme.colorScheme.primary
//        )
//    }
//
//    // Infinite scrolling detection
//    val listState = rememberLazyListState()
//    LazyColumn(
//        state = listState,
//        modifier = Modifier.fillMaxSize()
//    ) {
//        // Items and additional UI go here
//    }
//
//    LaunchedEffect(listState) {
//        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
//            .distinctUntilChanged()
//            .collect { lastVisibleItemIndex ->
//                if (lastVisibleItemIndex == items.lastIndex && !isLoading && !isEndOfList) {
//                    currentPage++
//                }
//            }
//    }
//}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> PaginatedList(
    initialItems: List<T>,
    loadItems: suspend (page: Int, pageSize: Int) -> List<T>,
    onRefresh: suspend () -> Unit,
    onInit: suspend () -> Unit,
    pageSize: Int = 20,
    modifier: Modifier = Modifier,
    onRenderItem: @Composable (T, Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    // Internal state management
    var items by remember { mutableStateOf(initialItems) }
    var currentPage by remember { mutableStateOf(1) }
    var isLoading by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }
    var isEndOfList by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Lazy list state
    val listState = rememberLazyListState()

    suspend fun loadItemsInternal() {
        if (isLoading || isEndOfList) return // Prevent redundant calls
        isLoading = true

        try {
            val newItems = loadItems(currentPage, pageSize)
            if (newItems.isEmpty()) {
                isEndOfList = true
            } else {
                items = if (currentPage == 1) newItems else items + newItems
                isEndOfList = newItems.size < pageSize
            }
        } catch (e: Exception) {
            errorMessage = "Failed to load items: ${e.localizedMessage}"
        } finally {
            isLoading = false
            isRefreshing = false
        }
    }

//    LaunchedEffect(key1 = true, block = {
//        if (items.isEmpty()){
//
//            errorMessage = null
//            currentPage = 1
//            onInit()
//            loadItemsInternal()
//
//        }
//    })

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                errorMessage = null
                currentPage = 1
                items = emptyList()
                onRefresh()
                loadItemsInternal()
            }
        }
    )

    // Trigger loading more items when the user scrolls to the end
    LaunchedEffect(listState) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .distinctUntilChanged()
            .collect { lastVisibleItemIndex ->
                if (lastVisibleItemIndex == items.lastIndex && !isLoading && !isEndOfList) {
                    currentPage++
                    loadItemsInternal()
                }
            }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Error message
            errorMessage?.let {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // No items available
            if (items.isEmpty() && errorMessage == null && !isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No items available.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            // Render items with index
            itemsIndexed(items) { index, item ->
                onRenderItem(item, index)
            }

            // Loading spinner or end-of-list message
            if (isLoading && errorMessage == null) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            } else if (isEndOfList) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No more items to load.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter),
            contentColor = MaterialTheme.colorScheme.primary
        )
    }
}
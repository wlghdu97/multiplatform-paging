package com.kuuurt.paging.multiplatform.paginator

import com.kuuurt.paging.multiplatform.datasource.PageKeyedDataSource
import com.kuuurt.paging.multiplatform.helpers.asCommonFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */

@FlowPreview
@ExperimentalCoroutinesApi
@Deprecated(
    message = "Deprecated in Paging 3.0.0-alpha01",
    replaceWith = ReplaceWith(
        "Pager<K, V>",
        "com.kuuurt.paging.multiplatform.paginator"
    )
)
actual class PageKeyedPaginator<T: Any> actual constructor(
    private val clientScope: CoroutineScope,
    pageSize: Int,
    androidEnablePlaceHolders: Boolean,
    getCount: suspend () -> Int,
    getItems: suspend (Int, Int) -> List<T>
) : PaginatorDetails {
    internal actual val dataSourceFactory = PageKeyedDataSource.Factory(
        clientScope, getCount, getItems
    )

    val pagedList = dataSourceFactory
        .dataSource
        .flatMapLatest { it.items }
        .asCommonFlow()

    override val totalCount = dataSourceFactory
        .dataSource
        .flatMapLatest { it.totalCount }
        .asCommonFlow()

    override val getState = dataSourceFactory
        .dataSource
        .flatMapLatest { it.getState }
        .asCommonFlow()

    override fun refresh() {
        clientScope.launch {
            dataSourceFactory.dataSource.first().refresh()
        }
    }

    fun loadAfter() {
        clientScope.launch {
            dataSourceFactory.dataSource.first().loadAfter()
        }
    }

    fun loadBefore() {
        clientScope.launch {
            dataSourceFactory.dataSource.first().loadBefore()
        }
    }

    init {
        clientScope.launch {
            dataSourceFactory.dataSource.first().apply {
                this.pageSize = pageSize
                loadInitial()
            }
        }
    }
}
package com.kuuurt.paging.multiplatform.paginator

import com.kuuurt.paging.multiplatform.datasource.PositionalDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

/**
 * Copyright 2020, Kurt Renzo Acosta, All rights reserved.
 *
 * @author Kurt Renzo Acosta
 * @since 01/10/2020
 */

@FlowPreview
@ExperimentalCoroutinesApi
expect class PositionalPaginator<T: Any>(
    clientScope: CoroutineScope,
    pageSize: Int = 10,
    androidEnablePlaceHolders: Boolean = false,
    getCount: suspend () -> Int,
    getItems: suspend (Int, Int) -> List<T>
) : PaginatorDetails {
    internal val dataSourceFactory: PositionalDataSource.Factory<T>
}


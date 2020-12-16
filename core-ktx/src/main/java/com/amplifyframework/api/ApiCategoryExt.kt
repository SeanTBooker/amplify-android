/*
 * Copyright 2021 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * You may not use this file except in compliance with the License.
 * A copy of the License is located at
 *
 *  http://aws.amazon.com/apache2.0
 *
 * or in the "license" file accompanying this file. This file is distributed
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing
 * permissions and limitations under the License.
 */

@file:Suppress("unused")

package com.amplifyframework.api

import com.amplifyframework.api.SubscriptionConnectionState.CONNECTED
import com.amplifyframework.api.SubscriptionConnectionState.CONNECTING
import com.amplifyframework.api.SubscriptionConnectionState.DISCONNECTED
import com.amplifyframework.api.graphql.GraphQLRequest
import com.amplifyframework.api.graphql.GraphQLResponse
import com.amplifyframework.api.rest.RestOptions
import com.amplifyframework.api.rest.RestResponse
import com.amplifyframework.core.async.Cancelable

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Throws(ApiException::class)
suspend fun <R> ApiCategory.query(graphQlRequest: GraphQLRequest<R>): GraphQLResponse<R> {
    return suspendCancellableCoroutine { continuation ->
        val operation = query(graphQlRequest,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
        continuation.invokeOnCancellation { operation?.cancel() }
    }
}

@Throws(ApiException::class)
suspend fun <R> ApiCategory.query(apiName: String, graphQlRequest: GraphQLRequest<R>): GraphQLResponse<R> {
    return suspendCancellableCoroutine { continuation ->
        val operation = query(apiName, graphQlRequest,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
        continuation.invokeOnCancellation { operation?.cancel() }
    }
}

@Throws(ApiException::class)
suspend fun <R> ApiCategory.mutate(graphQlRequest: GraphQLRequest<R>): GraphQLResponse<R> {
    return suspendCancellableCoroutine { continuation ->
        val operation = mutate(graphQlRequest,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
        continuation.invokeOnCancellation { operation?.cancel() }
    }
}

@Throws(ApiException::class)
suspend fun <T> ApiCategory.mutate(apiName: String, graphQlRequest: GraphQLRequest<T>): GraphQLResponse<T> {
    return suspendCancellableCoroutine { continuation ->
        val operation = mutate(apiName, graphQlRequest,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
        continuation.invokeOnCancellation { operation?.cancel() }
    }
}

@FlowPreview
@ExperimentalCoroutinesApi
fun <T> ApiCategory.subscribe(graphQlRequest: GraphQLRequest<T>): GraphQLSubscriptionOperation<T> {
    val subscriptionChannel = ConflatedBroadcastChannel<GraphQLResponse<T>>()
    val connectionChannel = ConflatedBroadcastChannel<SubscriptionConnectionState>()
    connectionChannel.sendBlocking(CONNECTING)
    val operation = subscribe(graphQlRequest,
            { connectionChannel.sendBlocking(CONNECTED) },
            { subscriptionChannel.sendBlocking(it) },
            {
                subscriptionChannel.close(it)
                connectionChannel.sendBlocking(DISCONNECTED)
                connectionChannel.close(it)
            },
            {
                subscriptionChannel.close()
                connectionChannel.sendBlocking(DISCONNECTED)
                connectionChannel.close()
            }
    )
    return GraphQLSubscriptionOperation(
            subscriptionChannel.asFlow(),connectionChannel.asFlow(), operation as Cancelable
    )
}

@ExperimentalCoroutinesApi
@FlowPreview
fun <T> ApiCategory.subscribe(apiName: String, graphQlRequest: GraphQLRequest<T>): GraphQLSubscriptionOperation<T> {
    val subscriptionChannel = ConflatedBroadcastChannel<GraphQLResponse<T>>()
    val connectionChannel = ConflatedBroadcastChannel<SubscriptionConnectionState>()
    connectionChannel.sendBlocking(CONNECTING)
    val operation = subscribe(apiName, graphQlRequest,
            { connectionChannel.sendBlocking(CONNECTED) },
            { subscriptionChannel.sendBlocking(it) },
            {
                subscriptionChannel.close(it)
                connectionChannel.sendBlocking(DISCONNECTED)
                connectionChannel.close(it)
            },
            {
                subscriptionChannel.close()
                connectionChannel.sendBlocking(DISCONNECTED)
                connectionChannel.close()
            }
    )
    return GraphQLSubscriptionOperation(
            subscriptionChannel.asFlow(),connectionChannel.asFlow(), operation as Cancelable
    )
}

data class GraphQLSubscriptionOperation<T>(
        val events: Flow<GraphQLResponse<T>>,
        val connectionState: Flow<SubscriptionConnectionState>,
        private val cancelDelegate: Cancelable): Cancelable {
    override fun cancel() = cancelDelegate.cancel()
}

enum class SubscriptionConnectionState {
    CONNECTING,
    CONNECTED,
    DISCONNECTED
}

@Throws(ApiException::class)
suspend fun ApiCategory.get(request: RestOptions): RestResponse {
    return suspendCancellableCoroutine { continuation ->
        val operation = get(request,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
        continuation.invokeOnCancellation { operation?.cancel() }
    }
}

@Throws(ApiException::class)
suspend fun ApiCategory.get(apiName: String, request: RestOptions): RestResponse {
    return suspendCancellableCoroutine { continuation ->
        val operation = get(apiName, request,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
        continuation.invokeOnCancellation { operation?.cancel() }
    }
}

@Throws(ApiException::class)
suspend fun ApiCategory.put(request: RestOptions): RestResponse {
    return suspendCancellableCoroutine { continuation ->
        val operation = put(request,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
        continuation.invokeOnCancellation { operation?.cancel() }
    }
}

@Throws(ApiException::class)
suspend fun ApiCategory.put(apiName: String, request: RestOptions): RestResponse {
    return suspendCancellableCoroutine { continuation ->
        val operation = put(apiName, request,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
        continuation.invokeOnCancellation { operation?.cancel() }
    }
}

@Throws(ApiException::class)
suspend fun ApiCategory.post(request: RestOptions): RestResponse {
    return suspendCancellableCoroutine { continuation ->
        val operation = post(request,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
        continuation.invokeOnCancellation { operation?.cancel() }
    }
}

@Throws(ApiException::class)
suspend fun ApiCategory.post(apiName: String, request: RestOptions): RestResponse {
    return suspendCancellableCoroutine { continuation ->
        val operation = post(apiName, request,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
        continuation.invokeOnCancellation { operation?.cancel() }
    }
}

@Throws(ApiException::class)
suspend fun ApiCategory.delete(request: RestOptions): RestResponse {
    return suspendCancellableCoroutine { continuation ->
        val operation = delete(request,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
        continuation.invokeOnCancellation { operation?.cancel() }
    }
}

@Throws(ApiException::class)
suspend fun ApiCategory.delete(apiName: String, request: RestOptions): RestResponse {
    return suspendCancellableCoroutine { continuation ->
        val operation = delete(apiName, request,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
        continuation.invokeOnCancellation { operation?.cancel() }
    }
}

@Throws(ApiException::class)
suspend fun ApiCategory.head(request: RestOptions): RestResponse {
    return suspendCancellableCoroutine { continuation ->
        val operation = head(request,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
        continuation.invokeOnCancellation { operation?.cancel() }
    }
}

@Throws(ApiException::class)
suspend fun ApiCategory.head(apiName: String, request: RestOptions): RestResponse {
    return suspendCancellableCoroutine { continuation ->
        val operation = head(apiName, request,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
        continuation.invokeOnCancellation { operation?.cancel() }
    }
}

@Throws(ApiException::class)
suspend fun ApiCategory.patch(request: RestOptions): RestResponse {
    return suspendCancellableCoroutine { continuation ->
        val operation = patch(request,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
        continuation.invokeOnCancellation { operation?.cancel() }
    }
}

@Throws(ApiException::class)
suspend fun ApiCategory.patch(apiName: String, request: RestOptions): RestResponse {
    return suspendCancellableCoroutine { continuation ->
        val operation = patch(apiName, request,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
        continuation.invokeOnCancellation { operation?.cancel() }
    }
}
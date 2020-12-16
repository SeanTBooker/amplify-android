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

package com.amplifyframework.datastore

import com.amplifyframework.core.async.Cancelable
import com.amplifyframework.core.model.Model
import com.amplifyframework.core.model.query.QueryOptions
import com.amplifyframework.core.model.query.predicate.QueryPredicate
import com.amplifyframework.core.model.query.predicate.QueryPredicates

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Throws(DataStoreException::class)
suspend fun <T : Model> DataStoreCategory.save(item: T, predicate: QueryPredicate = QueryPredicates.all()) {
    return suspendCoroutine { continuation ->
        save(item, predicate,
                { continuation.resume(Unit) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(DataStoreException::class)
suspend fun <T : Model> DataStoreCategory.delete(item: T, predicate: QueryPredicate = QueryPredicates.all()) {
    return suspendCoroutine { continuation ->
        delete(item, predicate,
                { continuation.resume(Unit) },
                { continuation.resumeWithException(it) }
        )
    }
}

@ExperimentalCoroutinesApi
@Throws(DataStoreException::class)
fun <T : Model> DataStoreCategory.query(
        itemClass: Class<T>, predicate: QueryPredicate = QueryPredicates.all()): Flow<T> {
    return callbackFlow {
        query(itemClass, predicate,
                {
                    while (it.hasNext()) {
                        sendBlocking(it.next())
                    }
                    close()
                },
                { close(it) }
        )
        awaitClose {}
    }
}

@ExperimentalCoroutinesApi
@Throws(DataStoreException::class)
fun <T : Model> DataStoreCategory.query(itemClass: Class<T>, options: QueryOptions): Flow<T> {
    return callbackFlow {
        query(itemClass, options,
                {
                    while (it.hasNext()) {
                        sendBlocking(it.next())
                    }
                    close()
                },
                { close(it) }
        )
        awaitClose {}
    }
}

@ExperimentalCoroutinesApi
@Throws(DataStoreException::class)
fun DataStoreCategory.observe(): Flow<DataStoreItemChange<out Model>> {
    return callbackFlow {
        val cancelable = java.util.concurrent.atomic.AtomicReference<Cancelable?>()
        observe(
                { cancelable.set(it) },
                { change -> sendBlocking(change) },
                { failure -> close(failure) },
                { close() }
        )
        awaitClose { cancelable.get()?.cancel() }
    }
}

@ExperimentalCoroutinesApi
@Throws(DataStoreException::class)
fun <T : Model> DataStoreCategory.observe(itemClass: Class<T>): Flow<DataStoreItemChange<T>> {
    return callbackFlow {
        val cancelable = java.util.concurrent.atomic.AtomicReference<Cancelable?>()
        observe(
                itemClass,
                { cancelable.set(it) },
                { change -> sendBlocking(change) },
                { failure -> close(failure) },
                { close() }
        )
        awaitClose { cancelable.get()?.cancel() }
    }
}

@ExperimentalCoroutinesApi
@Throws(DataStoreException::class)
fun <T : Model> DataStoreCategory.observe(
        itemClass: Class<T>, uniqueId: String): Flow<DataStoreItemChange<T>> {
    return callbackFlow {
        val cancelable = java.util.concurrent.atomic.AtomicReference<Cancelable?>()
        observe(
                itemClass, uniqueId,
                { cancelable.set(it) },
                { change -> sendBlocking(change) },
                { failure -> close(failure) },
                { close() }
        )
        awaitClose { cancelable.get()?.cancel() }
    }
}

@ExperimentalCoroutinesApi
@Throws(DataStoreException::class)
fun <T : Model> DataStoreCategory.observe(
        itemClass: Class<T>, selectionCriteria: QueryPredicate): Flow<DataStoreItemChange<T>> {
    return callbackFlow {
        val cancelable = java.util.concurrent.atomic.AtomicReference<Cancelable?>()
        observe(
                itemClass, selectionCriteria,
                { cancelable.set(it) },
                { change -> sendBlocking(change) },
                { failure -> close(failure) },
                { close() }
        )
        awaitClose { cancelable.get()?.cancel() }
    }
}

@Throws(DataStoreException::class)
suspend fun DataStoreCategory.start() {
    return suspendCoroutine { continuation ->
        start(
                { continuation.resume(Unit) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(DataStoreException::class)
suspend fun DataStoreCategory.stop() {
    return suspendCoroutine { continuation ->
        stop(
                { continuation.resume(Unit) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(DataStoreException::class)
suspend fun DataStoreCategory.clear() {
    return suspendCoroutine { continuation ->
        clear(
                { continuation.resume(Unit) },
                { continuation.resumeWithException(it) }
        )
    }
}

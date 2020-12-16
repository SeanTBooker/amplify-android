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

package com.amplifyframework.storage

import com.amplifyframework.core.async.Cancelable
import com.amplifyframework.storage.options.StorageDownloadFileOptions
import com.amplifyframework.storage.options.StorageGetUrlOptions
import com.amplifyframework.storage.options.StorageListOptions
import com.amplifyframework.storage.options.StorageRemoveOptions
import com.amplifyframework.storage.options.StorageUploadFileOptions
import com.amplifyframework.storage.options.StorageUploadInputStreamOptions
import com.amplifyframework.storage.result.StorageDownloadFileResult
import com.amplifyframework.storage.result.StorageGetUrlResult
import com.amplifyframework.storage.result.StorageListResult
import com.amplifyframework.storage.result.StorageRemoveResult
import com.amplifyframework.storage.result.StorageTransferProgress
import com.amplifyframework.storage.result.StorageUploadFileResult
import com.amplifyframework.storage.result.StorageUploadInputStreamResult

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.single

import java.io.File
import java.io.InputStream

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Throws(StorageException::class)
suspend fun StorageCategory.getUrl(
        key: String,
        options: StorageGetUrlOptions = StorageGetUrlOptions.defaultInstance()
): StorageGetUrlResult {
    return suspendCoroutine { continuation ->
        getUrl(key, options,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@ExperimentalCoroutinesApi
@FlowPreview
fun StorageCategory.downloadFile(
        key: String,
        local: File,
        options: StorageDownloadFileOptions = StorageDownloadFileOptions.defaultInstance()
): InProgressStorageOperation<StorageDownloadFileResult> {
    val progress = ConflatedBroadcastChannel<StorageTransferProgress>()
    val result = ConflatedBroadcastChannel<StorageDownloadFileResult>()
    val delegate = downloadFile(key, local, options,
            { progress.sendBlocking(it) },
            {
                result.sendBlocking(it)
                result.close()
            },
            {
                progress.close(it)
            }
    )
    return InProgressStorageOperation(result.asFlow(), progress.asFlow(), delegate as Cancelable)
}

@ExperimentalCoroutinesApi
@FlowPreview
fun StorageCategory.uploadFile(
        key: String,
        local: File,
        options: StorageUploadFileOptions = StorageUploadFileOptions.defaultInstance()
): InProgressStorageOperation<StorageUploadFileResult> {
    val progress = ConflatedBroadcastChannel<StorageTransferProgress>()
    val result = ConflatedBroadcastChannel<StorageUploadFileResult>()
    val delegate = uploadFile(key, local, options,
            { progress.sendBlocking(it) },
            {
                result.sendBlocking(it)
                result.close()
            },
            {
                progress.close(it)
            }
    )
    return InProgressStorageOperation(result.asFlow(), progress.asFlow(), delegate as Cancelable)
}

@FlowPreview
@ExperimentalCoroutinesApi
fun StorageCategory.uploadInputStream(
        key: String,
        local: InputStream,
        options: StorageUploadInputStreamOptions = StorageUploadInputStreamOptions.defaultInstance()
): InProgressStorageOperation<StorageUploadInputStreamResult> {
    val progress = ConflatedBroadcastChannel<StorageTransferProgress>()
    val result = ConflatedBroadcastChannel<StorageUploadInputStreamResult>()
    val delegate = uploadInputStream(key, local, options,
            { progress.sendBlocking(it) },
            {
                result.sendBlocking(it)
                result.close()
            },
            {
                progress.close(it)
            }
    )
    return InProgressStorageOperation(result.asFlow(), progress.asFlow(), delegate as Cancelable)
}

data class InProgressStorageOperation<T>(
        private val result: Flow<T>,
        val progress: Flow<StorageTransferProgress>,
        private val delegate: Cancelable?): Cancelable {

    override fun cancel() {
        delegate?.cancel()
    }

    suspend fun result(): T {
        return result.single()
    }
}

@Throws(StorageException::class)
suspend fun StorageCategory.remove(
        key: String,
        options: StorageRemoveOptions = StorageRemoveOptions.defaultInstance()
): StorageRemoveResult {
    return suspendCoroutine { continuation ->
        remove(key, options,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(StorageException::class)
suspend fun StorageCategory.list(
        path: String,
        options: StorageListOptions = StorageListOptions.defaultInstance()
): StorageListResult {
    return suspendCoroutine { continuation ->
        list(path, options,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}
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

package com.amplifyframework.predictions

import android.graphics.Bitmap

import com.amplifyframework.predictions.models.IdentifyAction
import com.amplifyframework.predictions.models.LanguageType
import com.amplifyframework.predictions.options.IdentifyOptions
import com.amplifyframework.predictions.options.InterpretOptions
import com.amplifyframework.predictions.options.TextToSpeechOptions
import com.amplifyframework.predictions.options.TranslateTextOptions
import com.amplifyframework.predictions.result.IdentifyResult
import com.amplifyframework.predictions.result.InterpretResult
import com.amplifyframework.predictions.result.TextToSpeechResult
import com.amplifyframework.predictions.result.TranslateTextResult

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Throws(PredictionsException::class)
suspend fun PredictionsCategory.convertTextToSpeech(
        text: String, options: TextToSpeechOptions = TextToSpeechOptions.defaults()): TextToSpeechResult {
    return suspendCoroutine { continuation ->
        convertTextToSpeech(text, options,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(PredictionsException::class)
suspend fun PredictionsCategory.translateText(
        text: String, options: TranslateTextOptions = TranslateTextOptions.defaults()): TranslateTextResult {
    return suspendCoroutine { continuation ->
        translateText(text, options,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(PredictionsException::class)
suspend fun PredictionsCategory.translateText(
        text: String,
        fromLanguage: LanguageType,
        toLanguage: LanguageType,
        options: TranslateTextOptions = TranslateTextOptions.defaults()): TranslateTextResult {
    return suspendCoroutine { continuation ->
        translateText(text, fromLanguage, toLanguage, options,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(PredictionsException::class)
suspend fun PredictionsCategory.identify(
        actionType: IdentifyAction,
        image: Bitmap,
        options: IdentifyOptions = IdentifyOptions.defaults()): IdentifyResult {
    return suspendCoroutine { continuation ->
        identify(actionType, image, options,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(PredictionsException::class)
suspend fun PredictionsCategory.interpret(
        text: String,
        options: InterpretOptions = InterpretOptions.defaults()): InterpretResult {
    return suspendCoroutine { continuation ->
        interpret(text, options,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

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

package com.amplifyframework.auth

import android.app.Activity

import com.amplifyframework.auth.options.AuthSignInOptions
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.options.AuthWebUISignInOptions
import com.amplifyframework.auth.result.AuthResetPasswordResult
import com.amplifyframework.auth.result.AuthSignInResult
import com.amplifyframework.auth.result.AuthSignUpResult
import com.amplifyframework.auth.result.AuthUpdateAttributeResult

import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

@Throws(AuthException::class)
suspend fun AuthCategory.signUp(
        username: String, password: String, options: AuthSignUpOptions): AuthSignUpResult {
    return suspendCoroutine { continuation ->
        signUp(username, password, options,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.confirmSignUp(username: String, confirmationCode: String): AuthSignUpResult {
    return suspendCoroutine { continuation ->
        confirmSignUp(username, confirmationCode,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.resendSignUpCode(username: String): AuthSignUpResult {
    return suspendCoroutine { continuation ->
        resendSignUpCode(username,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.signIn(username: String?, password: String?, options: AuthSignInOptions): AuthSignInResult {
    return suspendCoroutine { continuation ->
        signIn(username, password, options,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.signIn(username: String?, password: String?): AuthSignInResult {
    return suspendCoroutine { continuation ->
        signIn(username, password,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.confirmSignIn(confirmationCode: String): AuthSignInResult {
    return suspendCoroutine { continuation ->
        confirmSignIn(confirmationCode,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.signInWithSocialWebUI(
        provider: AuthProvider, callingActivity: Activity): AuthSignInResult {
    return suspendCoroutine { continuation ->
        signInWithSocialWebUI(provider, callingActivity,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.signInWithSocialWebUI(
        provider: AuthProvider,
        callingActivity: Activity,
        options: AuthWebUISignInOptions): AuthSignInResult {
    return suspendCoroutine { continuation ->
        signInWithSocialWebUI(provider, callingActivity, options,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.signInWithWebUI(callingActivity: Activity): AuthSignInResult {
    return suspendCoroutine { continuation ->
        signInWithWebUI(callingActivity,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.signInWithWebUI(
        callingActivity: Activity, options: AuthWebUISignInOptions): AuthSignInResult {
    return suspendCoroutine { continuation ->
        signInWithWebUI(callingActivity, options,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.fetchAuthSession(): AuthSession {
    return suspendCoroutine { continuation ->
        fetchAuthSession(
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.rememberDevice() {
    return suspendCoroutine { continuation ->
        rememberDevice(
                { continuation.resume(Unit) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.forgetDevice() {
    return suspendCoroutine { continuation ->
        forgetDevice(
                { continuation.resume(Unit) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.forgetDevice(device: AuthDevice) {
    return suspendCoroutine { continuation ->
        forgetDevice(device,
                { continuation.resume(Unit) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.fetchDevices(): List<AuthDevice> {
    return suspendCoroutine { continuation ->
        fetchDevices(
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.resetPassword(username: String): AuthResetPasswordResult {
    return suspendCoroutine { continuation ->
        resetPassword(username,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.confirmResetPassword(newPassword: String, confirmationCode: String) {
    return suspendCoroutine { continuation ->
        confirmResetPassword(newPassword, confirmationCode,
                { continuation.resume(Unit) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.updatePassword(oldPassword: String, newPassword: String) {
    return suspendCoroutine { continuation ->
        updatePassword(oldPassword, newPassword,
                { continuation.resume(Unit) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.fetchUserAttributes(): List<AuthUserAttribute> {
    return suspendCoroutine { continuation ->
        fetchUserAttributes(
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.updateUserAttribute(attribute: AuthUserAttribute): AuthUpdateAttributeResult {
    return suspendCoroutine { continuation ->
        updateUserAttribute(attribute,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.updateUserAttributes(attributes: List<AuthUserAttribute>):
        Map<AuthUserAttributeKey, AuthUpdateAttributeResult> {
    return suspendCoroutine { continuation ->
        updateUserAttributes(attributes,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.resendUserAttributeConfirmationCode(
        attributeKey: AuthUserAttributeKey): AuthCodeDeliveryDetails {
    return suspendCoroutine { continuation ->
        resendUserAttributeConfirmationCode(attributeKey,
                { continuation.resume(it) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.confirmUserAttribute(
        attributeKey: AuthUserAttributeKey, confirmationCode: String) {
    return suspendCoroutine { continuation ->
        confirmUserAttribute(attributeKey, confirmationCode,
                { continuation.resume(Unit) },
                { continuation.resumeWithException(it) }
        )
    }
}

@Throws(AuthException::class)
suspend fun AuthCategory.signOut() {
    return suspendCoroutine { continuation ->
        signOut(
                { continuation.resume(Unit) },
                { continuation.resumeWithException(it) }
        )
    }}

@Throws(AuthException::class)
suspend fun AuthCategory.signOut(options: AuthSignOutOptions) {
    return suspendCoroutine { continuation ->
        signOut(options,
                { continuation.resume(Unit) },
                { continuation.resumeWithException(it) }
        )
    }
}

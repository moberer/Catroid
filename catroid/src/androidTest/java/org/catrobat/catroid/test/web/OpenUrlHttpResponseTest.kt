/*
 * Catroid: An on-device visual programming system for Android devices
 * Copyright (C) 2010-2022 The Catrobat Team
 * (<http://developer.catrobat.org/credits>)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * An additional term exception under section 7 of the GNU Affero
 * General Public License, version 3, is available at
 * http://developer.catrobat.org/license_additional_term
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.catrobat.catroid.test.web

import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.Assert.assertTrue
import okhttp3.OkHttpClient
import okhttp3.Response
import org.catrobat.catroid.common.Constants.MAIN_URL_HTTPS
import org.catrobat.catroid.testsuites.annotations.Cat.OutgoingNetworkTests
import org.catrobat.catroid.web.WebConnection
import org.junit.Before
import org.junit.Test
import org.junit.experimental.categories.Category
import org.junit.runner.RunWith
import java.util.concurrent.CompletableFuture

@Category(OutgoingNetworkTests::class)
@RunWith(AndroidJUnit4::class)
class OpenUrlHttpResponseTest : WebConnection.WebRequestListener {
    private val okHttpClient = OkHttpClient()
    private lateinit var response: CompletableFuture<String>
    private val url = MAIN_URL_HTTPS

    companion object {
        private const val HTML_RESPONSE_START = "<!DOCTYPE html>"
        private const val HTML_RESPONSE_END = "</html>"
    }

    @Before
    fun setUp() {
        response = CompletableFuture()
    }

    @Test
    fun testHttp() {
        WebConnection(okHttpClient, this, url).sendWebRequest()
        val res: String = response.get().trim()
        assertTrue(res.startsWith(HTML_RESPONSE_START))
        assertTrue(res.endsWith(HTML_RESPONSE_END))
    }

    override fun onRequestSuccess(httpResponse: Response) {
        response.complete(httpResponse.body()?.string() ?: "")
    }

    override fun onRequestError(httpError: String) {
        response.complete(httpError)
    }

    override fun onCancelledCall() {
        response.complete("")
    }
}

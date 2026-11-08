package com.example.helpdeskpro

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.helpdeskpro.data.remote.AndroidKeystoreTokenCipher
import com.example.helpdeskpro.data.remote.SessionTokenStore
import kotlinx.coroutines.runBlocking
import java.io.File
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @org.junit.Rule
    @JvmField
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.helpdeskpro", appContext.packageName)
    }

    @Test
    fun appStartsOnLogin() {
        composeRule.onNodeWithText("HelpDesk Pro").fetchSemanticsNode()
    }

    @Test
    fun sessionTokenIsPersistedAndCleared() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val tokenStore = SessionTokenStore(context)
        val token = "instrumented-test-token"

        tokenStore.save(token)
        val dataStoreFile = File(context.filesDir, "datastore/helpdesk_session.preferences_pb")
        assertTrue(dataStoreFile.exists())
        assertFalse(String(dataStoreFile.readBytes(), Charsets.ISO_8859_1).contains(token))
        assertEquals(token, tokenStore.get())

        tokenStore.clear()
        assertNull(tokenStore.get())
    }

    @Test
    fun tokenCipherEncryptsAndAuthenticatesData() {
        val cipher = AndroidKeystoreTokenCipher()
        val token = "sensitive-session-token"

        val encrypted = cipher.encrypt(token)
        assertNotEquals(token, encrypted)
        assertTrue(encrypted.startsWith("enc:v1:"))
        assertEquals(token, cipher.decrypt(encrypted))
        assertNotEquals(encrypted, cipher.encrypt(token))
        assertThrows(IllegalStateException::class.java) {
            cipher.decrypt(encrypted.dropLast(2) + "AA")
        }
    }
}
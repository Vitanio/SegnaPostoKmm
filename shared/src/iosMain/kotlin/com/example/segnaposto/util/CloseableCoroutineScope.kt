package com.example.segnaposto.util
import app.cash.sqldelight.db.Closeable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlin.coroutines.CoroutineContext
class CloseableCoroutineScope(context: CoroutineContext) : Closeable, CoroutineScope {
    override val coroutineContext: CoroutineContext = context
    override fun close() = coroutineContext.cancel()
}

fun CoroutineScope.close() {
    if (this is Closeable) close()
}
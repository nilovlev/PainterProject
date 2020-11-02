package client.network

import client.events.emit
import client.events.LineEvent
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread

class PainterClient(address: String, port: Int) {

    private val connection: Socket = Socket(address, port)
//    private val messageHandler: MessageHandler = MessageHandler()
    private var connected: Boolean = true

    private val reader: Scanner = Scanner(connection.getInputStream())
    private val writer: OutputStream = connection.getOutputStream()

    init {
        println("Connected to server at $address on port $port")
    }

    fun run() {
        thread { read() }
        while (connected) {
            val input = readLine() ?: ""
            if ("exit" in input) {
                shutdown()
            } else {
                write(input)
            }
        }
    }

    fun writeLine(line: Line) {
        writer.write((Json.encodeToString(line) + '\n').toByteArray(Charset.defaultCharset()))
    }

    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }

    private fun read() {
        while (connected) {
            val message = reader.nextLine()
            emit(LineEvent(Json.decodeFromString(message)))
        }
    }

    private fun shutdown() {
        connected = false
        reader.close()
        connection.close()
    }
}

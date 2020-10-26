package client

import java.io.OutputStream
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread

class PainterClient(address: String, port: Int) {

    private val connection: Socket = Socket(address, port)
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

    private fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }

    private fun read() {
        while (connected) {
            println(reader.nextLine())
        }
    }

    private fun shutdown() {
        connected = false
        reader.close()
        connection.close()
    }
}
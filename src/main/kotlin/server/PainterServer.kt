package server

import java.io.OutputStream
import java.net.ServerSocket
import java.net.Socket
import java.nio.charset.Charset
import java.util.*
import kotlin.concurrent.thread

private var connections: ArrayList<ClientHandler> = ArrayList()

fun main() {
    val server = ServerSocket(9999)
    println("Server is running on port ${server.localPort}")

    while (true) {
        val client = server.accept()
        println("Client connected: ${client.inetAddress.hostAddress}")

        thread {
            val cl = ClientHandler(client)
            connections.add(cl)
            cl.run()
        }
    }
}

class ClientHandler(private val client: Socket) {

    private val reader: Scanner = Scanner(client.getInputStream())
    private val writer: OutputStream = client.getOutputStream()
    private val messageHandler: MessageHandler = MessageHandler()
    private var running: Boolean = false

    fun run() {
        running = true

        while (running) {
            try {
                val text = reader.nextLine()
                if (text == "EXIT") shutdown()

                val result = messageHandler.handle(text)
                for (cl in connections) {
                    if (cl != this) cl.write(result)
                }
            } catch (e: Exception) {
                shutdown()
            }
        }
    }

    fun write(message: String) {
        writer.write((message + '\n').toByteArray(Charset.defaultCharset()))
    }

    private fun shutdown() {
        running = false
        client.close()
        connections.remove(this)
        println("${client.inetAddress.hostAddress} closed the connection")
    }

}
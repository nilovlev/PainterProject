package server

class MessageHandler {

    fun handle(message: String): String {
        return "Server successfully received the message: $message"
    }

}
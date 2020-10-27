package client

import java.util.ArrayList

class MessageHandler {

    fun handle(message: String): ArrayList<ArrayList<Double>> {

        var list: ArrayList<ArrayList<Double>> = ArrayList(ArrayList())

        for (n in message.drop(1).dropLast(1).split('[')) {
            val s = (n.dropLast(1).replace("]", "") + ",").split(',')
            try {
                list.add(arrayListOf(s[0].toDouble(), s[1].toDouble()))
            } catch (e: Exception) { }
        }
        return list
    }

}
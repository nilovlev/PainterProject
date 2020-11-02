package client.network

import kotlinx.serialization.*

@Serializable
data class Line(val list: ArrayList<Pair<Double, Double>>, val color: String, val width: Double)
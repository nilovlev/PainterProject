package client.view

import client.events.LineEvent
import client.events.register
import client.network.Line
import client.network.PainterClient
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.control.ColorPicker
import javafx.scene.layout.BorderPane
import tornadofx.*

class PainterView: View() {

    override val root: BorderPane by fxml("/Painter/Painter.fxml")

    @FXML
    lateinit var mainCanvas: Canvas

    @FXML
    lateinit var colorPicker: ColorPicker

    private val packageSize = 10

    fun initialize() {
        val address = "localhost"
        val port = 9999
        var client: PainterClient =  PainterClient(address, port)
        runAsync {
            client.run()
        }

        var list: ArrayList<Pair<Double, Double>> = ArrayList()

        val gc: GraphicsContext = mainCanvas.graphicsContext2D
        gc.lineWidth = 1.0

        var last = Pair(0.0, 0.0)

        mainCanvas.setOnMousePressed { e->
            gc.stroke = colorPicker.value
            gc.beginPath()
            last = Pair(e.x + 0.5, e.y + 0.5)
        }

        mainCanvas.setOnMouseDragged { e->
            val coord = Pair(e.x + 0.5, e.y + 0.5)
            gc.beginPath()
            gc.moveTo(last.first, last.second)
            gc.lineTo(coord.first, coord.second)
            last = coord
            gc.stroke()
            gc.closePath()
            if (list.size < packageSize) {
                list.add(coord)
            } else {
                list.add(coord)
                client.writeLine(Line(list, colorPicker.value.toString(), gc.lineWidth))
                list.clear()
                list.add(coord)
            }
        }

        mainCanvas.setOnMouseReleased { e->
            val coord = Pair(e.x + 0.5, e.y + 0.5)
            gc.lineTo(coord.first, coord.second)
            gc.stroke()
            gc.closePath()
            list.add(coord)
            client.writeLine(Line(list, colorPicker.value.toString(), gc.lineWidth))
            list.clear()
        }

        register<LineEvent> {
            drawLine(it.line)
        }
    }

    fun drawLine(line: Line) {
        val list = line.list
        val color = c(line.color)
        val width = line.width
        val gc2 = mainCanvas.graphicsContext2D
        gc2.lineWidth = width
        gc2.stroke = color

        var counter = 0
        for (coord in list) {
            counter++
            if (counter < list.size) {
                gc2.beginPath()
                gc2.moveTo(coord.first, coord.second)
                gc2.lineTo(list[counter].first, list[counter].second)
                gc2.stroke()
                gc2.closePath()
            }
        }
    }

}
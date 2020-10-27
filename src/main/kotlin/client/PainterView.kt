package client

import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import tornadofx.*

class PainterView: View() {

    override val root: AnchorPane by fxml("/Painter/Painter.fxml")

    @FXML
    lateinit var mainCanvas: Canvas

    val packageSize = 10

    fun initialize() {
        val address = "localhost"
        val port = 9999
        var client: PainterClient =  PainterClient(address, port, this)
        runAsync {
            client.run()
        }

        var list: ArrayList<ArrayList<Double>> = ArrayList(ArrayList())

        val gc: GraphicsContext = mainCanvas.graphicsContext2D
        gc.lineWidth = 1.0

        mainCanvas.setOnMousePressed { e->
            gc.stroke = Color.BLACK
            gc.beginPath()
            gc.lineTo(e.x, e.y)
        }

        mainCanvas.setOnMouseDragged { e->
            gc.lineTo(e.x, e.y)
            gc.stroke()
            if (list.size < packageSize) {
                list.add(arrayListOf(e.x, e.y))
            } else {
                list.add(arrayListOf(e.x, e.y))
                client.writeList(list)
                list.clear()
                list.add(arrayListOf(e.x, e.y))
            }
        }

        mainCanvas.setOnMouseReleased { e->
            gc.lineTo(e.x, e.y)
            gc.stroke()
            gc.closePath()
            list.add(arrayListOf(e.x, e.y))
            client.writeList(list)
            list.clear()
        }
    }

    fun draw(list: ArrayList<ArrayList<Double>>) {
        val gc = mainCanvas.graphicsContext2D
        gc.lineWidth = 1.0
        gc.stroke = Color.BLACK

        var counter = 0
        for (coord in list) {
            counter++
            if (counter < list.size) gc.strokeLine(coord[0], coord[1], list[counter][0], list[counter][1])
        }
    }

}
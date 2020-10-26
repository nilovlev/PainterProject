package client

import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import tornadofx.*

class PainterView: View() {
    override val root: AnchorPane by fxml("/Painter/Painter.fxml")

    @FXML
    lateinit var mainCanvas: Canvas

    fun initialize() {
        var list: ArrayList<ArrayList<Double>> = ArrayList(ArrayList())

        val gc = mainCanvas.graphicsContext2D
        gc.lineWidth = 1.0

        mainCanvas.setOnMousePressed { e->
            gc.stroke = Color.BLACK
            gc.beginPath()
            gc.lineTo(e.x, e.y)
        }

        mainCanvas.setOnMouseDragged { e->
            gc.lineTo(e.x, e.y)
            gc.stroke()
            if (list.size < 50) {
                list.add(arrayListOf(e.x, e.y))
            } else {
//                println(list)
                list.clear()
            }
        }

        mainCanvas.setOnMouseReleased { e->
            gc.lineTo(e.x, e.y)
            gc.stroke()
            gc.closePath()
        }

        val address = "localhost"
        val port = 9999
        runAsync {
            val client = PainterClient(address, port)
            client.run()
        }
    }
}
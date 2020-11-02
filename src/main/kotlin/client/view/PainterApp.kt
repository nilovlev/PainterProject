package client.view

import tornadofx.*
import kotlin.system.exitProcess

class PainterApp: App(PainterView::class) {

    override fun stop() {
        super.stop()
        exitProcess(0)
    }

}
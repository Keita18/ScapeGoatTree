import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import javax.swing.JPanel


class DrawSGT(private var binaryTree: ScapeGoatTree<Int>?) : JPanel() {

    private val padding = 10
    private val maxStr = "999"
    var isSearch: Boolean = false
    var toSearch: String? = null

    override fun paintComponent(g: Graphics) {
        super.paintComponent(g)
        if (binaryTree?.height() != 0)
            drawTree(g, 0, width, 0, height / binaryTree!!.height(), 0, 0, binaryTree!!.root)
        else this.repaint()
    }

    private fun drawTree(
            g: Graphics,
            StartWidth: Int,
            EndWidth: Int,
            StartHeight: Int,
            Level: Int,
            x: Int,
            y: Int,
            node: Node<Int>?
    ) {
        val data = node!!.value.toString()
        g.font = Font("Arial", Font.BOLD, 20)
        if (isSearch && data.equals(toSearch!!, ignoreCase = true)) { // for finding
            g.color = Color.RED
        } else {
            g.color = Color.BLACK
        }
        val fm = g.fontMetrics
        val dataWidth = fm.stringWidth(data)
        val ovalWidht = fm.stringWidth(maxStr) + padding

        g.drawOval(
                (StartWidth + EndWidth) / 2 - ovalWidht / 2,
                StartHeight + (Level - (ovalWidht + 10)) / 2,
                ovalWidht,
                ovalWidht
        )
        g.drawString(data, (StartWidth + EndWidth) / 2 - dataWidth / 2, StartHeight + Level / 2)

        val nextX = (StartWidth + EndWidth) / 2
        val nextY = StartHeight + Level / 2

        if (x > 0 && y > 0) {
            g.drawLine(x, y, nextX, nextY)
        }

        if (node.left != null) {
            drawTree(
                    g,
                    StartWidth,
                    (StartWidth + EndWidth) / 2,
                    StartHeight + Level,
                    Level,
                    nextX,
                    nextY,
                    node.left
            )
        }

        if (node.right != null) {
            drawTree(
                    g,
                    (StartWidth + EndWidth) / 2,
                    EndWidth,
                    StartHeight + Level,
                    Level,
                    nextX,
                    nextY,
                    node.right
            )
        }
    }
}

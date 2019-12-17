import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import javax.swing.*


class MainFrame : JFrame() {
    private var tree: ScapeGoatTree<Int>? = null
    private var drawTree: DrawSGT? = null

    private var contentTree: JPanel? = null
    private var insertValue = JTextField()
    private var findValue = JTextField()
    private var removeValue = JTextField()

    init {
        try {
            initComponents()
            tree = ScapeGoatTree()
            this.addComponentListener(object : ComponentAdapter() {

                override fun componentResized(e: ComponentEvent?) {
                    super.componentResized(e)
                    if (drawTree != null) {
                        drawTree!!.size = Dimension(contentTree!!.width, contentTree!!.height)
                        drawTree!!.repaint()
                    }
                }
            })
        } catch (e: Exception) {
        }
    }

    private fun initComponents() {
        contentTree = JPanel()
        val contentOptions = JPanel()

        val buttonInsert = JButton()
        val titleInsertion = JLabel()

        val buttonFind = JButton()
        val titleResearch: JLabel? = JLabel()

        val buttonRemove = JButton()
        val titleRemoval = JLabel()

        defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
        title = "ScapeGoat Tree"
        isLocationByPlatform = true
        preferredSize = Dimension(800, 600)
        size = preferredSize

        contentTree!!.background = Color.WHITE
        val contentTreeLayout = GroupLayout(contentTree)
        contentTree!!.layout = contentTreeLayout

        contentTreeLayout.setHorizontalGroup(
                contentTreeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 578, Short.MAX_VALUE.toInt())
        )
        contentTreeLayout.setVerticalGroup(
                contentTreeLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 578, Short.MAX_VALUE.toInt())
        )

        contentOptions.background = Color.GREEN

        titleInsertion.labelFor = titleInsertion
        titleInsertion.text = "INSERTION"
        titleInsertion.name = "titleInsertion"

        buttonInsert.text = "INSERT"
        buttonInsert.name = "buttonInsert"
        buttonInsert.addActionListener {
            buttonInsertActionPerformed()
        }

        titleResearch!!.labelFor = titleResearch
        titleResearch.text = "RESEARCH"
        titleResearch.name = "titleResearch"

        buttonFind.text = "FIND"
        buttonFind.name = "buttonFind"
        buttonFind.addActionListener {
            buttonFindActionPerformed()
        }

        titleRemoval.labelFor = titleRemoval
        titleRemoval.text = "REMOVAL"
        titleRemoval.name = "titleRemoval"

        buttonRemove.text = "REMOVE"
        buttonRemove.background = Color(255, 90, 90)
        buttonRemove.foreground = Color(255, 0, 0)
        buttonRemove.addActionListener { buttonRemoveActionPerformed() }

        insertValue.name = "insertValue"
        findValue.name = "findValue"
        removeValue.name = "removeValue"

        val contentOptionsLayout = GroupLayout(contentOptions)
        contentOptions.layout = contentOptionsLayout
        contentOptionsLayout.setHorizontalGroup(
                contentOptionsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                contentOptionsLayout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                contentOptionsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(
                                                                titleInsertion,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                200,
                                                                Short.MAX_VALUE.toInt()
                                                        )
                                                        .addComponent(insertValue)
                                                        .addComponent(
                                                                buttonInsert,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE.toInt()
                                                        )
                                                        .addComponent(
                                                                titleResearch,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE.toInt()
                                                        )
                                                        .addComponent(findValue)
                                                        .addComponent(
                                                                buttonFind,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE.toInt()
                                                        )
                                                        .addComponent(
                                                                titleRemoval,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE.toInt()
                                                        )
                                                        .addComponent(removeValue)
                                                        .addComponent(
                                                                buttonRemove,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE.toInt()
                                                        )
                                        )
                                        .addContainerGap()
                        )
        )
        contentOptionsLayout.setVerticalGroup(
                contentOptionsLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                contentOptionsLayout.createSequentialGroup()
                                        .addGap(28, 28, 28)
                                        .addComponent(titleInsertion)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                insertValue,
                                                GroupLayout.PREFERRED_SIZE,
                                                31,
                                                GroupLayout.PREFERRED_SIZE
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                buttonInsert,
                                                GroupLayout.PREFERRED_SIZE,
                                                43,
                                                GroupLayout.PREFERRED_SIZE
                                        )
                                        .addGap(23, 23, 23)
                                        .addComponent(titleResearch)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                findValue,
                                                GroupLayout.PREFERRED_SIZE,
                                                31,
                                                GroupLayout.PREFERRED_SIZE
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                buttonFind,
                                                GroupLayout.PREFERRED_SIZE,
                                                42,
                                                GroupLayout.PREFERRED_SIZE
                                        )
                                        .addGap(64, 64, 64)
                                        .addComponent(titleRemoval)
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                removeValue,
                                                GroupLayout.PREFERRED_SIZE,
                                                31,
                                                GroupLayout.PREFERRED_SIZE
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                buttonRemove,
                                                GroupLayout.PREFERRED_SIZE,
                                                42,
                                                GroupLayout.PREFERRED_SIZE
                                        )
                                        .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE.toInt())
                        )
        )

        val layout = GroupLayout(contentPane)
        contentPane.layout = layout
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(
                                                contentTree!!,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE.toInt()
                                        )
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(
                                                contentOptions,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.DEFAULT_SIZE,
                                                GroupLayout.PREFERRED_SIZE
                                        )
                                        .addContainerGap()
                        )
        )
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(
                                layout.createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(
                                                                contentTree!!,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE.toInt()
                                                        )
                                                        .addComponent(
                                                                contentOptions,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE.toInt()
                                                        )
                                        )
                                        .addContainerGap()
                        )
        )

        pack()
    }

    private fun buttonInsertActionPerformed() {
        try {
            val value = insertValue.text
            tree!!.add(value.toInt())
            insertValue.text = ""

            if (drawTree == null) {
                drawTree = DrawSGT(tree)
                drawTree!!.size = Dimension(contentTree!!.width, contentTree!!.height)
                drawTree!!.background = Color.WHITE
                contentTree!!.add(drawTree!!, BorderLayout.CENTER)
            }

            drawTree!!.isSearch = false
            drawTree!!.toSearch = null
            drawTree!!.repaint()

        } catch (ex: NumberFormatException) {
            JOptionPane.showMessageDialog(this, "Invalid Value", "WARNING",
                    JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun buttonFindActionPerformed() {
        try {
            val value = findValue.text
            val test = tree!!.contains(value.toInt())
            findValue.text = ""

            if (!test) {
                JOptionPane.showMessageDialog(this, "This value doesn't exist in tree",
                        "INFORMATION", JOptionPane.INFORMATION_MESSAGE)
            } else {
                drawTree!!.isSearch = true
                drawTree!!.toSearch = value
                drawTree!!.repaint()
                JOptionPane.showMessageDialog(this, "Value Found", "INFORMATION",
                        JOptionPane.INFORMATION_MESSAGE)
            }
        } catch (ex: NumberFormatException) {
            JOptionPane.showMessageDialog(this, "Invalid Value", "WARNING",
                    JOptionPane.WARNING_MESSAGE)
        }
    }

    private fun buttonRemoveActionPerformed() {
        try {
            val value = removeValue.text
            tree!!.remove(Integer.valueOf(value))
            removeValue.text = ""

            drawTree!!.isSearch = false
            drawTree!!.toSearch = null
            drawTree!!.repaint()

        } catch (ex: NumberFormatException) {
            JOptionPane.showMessageDialog(this, "VALOR INVALIDO", "WARNING",
                    JOptionPane.WARNING_MESSAGE)
        }
    }
}
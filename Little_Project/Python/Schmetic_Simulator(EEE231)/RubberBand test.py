import sys

from sys import exit

from PySide6.QtGui import QAction, QMouseEvent
from PySide6.QtWidgets import QApplication, QMainWindow, QHBoxLayout, QVBoxLayout, QWidget, QScrollBar, QRubberBand
from PySide6.QtWidgets import QMenuBar, QMenu
from PySide6.QtWidgets import QToolBar, QStatusBar
from PySide6.QtWidgets import QFileDialog, QMessageBox

from PySide6.QtCore import Qt, QPointF, QRectF, QRect, QSize
from PySide6.QtGui import QColor, QPainter, QPainterPath, QPen, QBrush, QIcon
from PySide6.QtWidgets import QGraphicsScene, QGraphicsView, QGraphicsEllipseItem, QGraphicsPathItem, \
    QGraphicsSimpleTextItem, QGraphicsRectItem, QGraphicsItemGroup

from PySide6.QtGui import QFontMetrics


# ******************************************************************************
class QmouseEvent:
    pass


class rbWindow(QWidget):
    def __init__(self):
        super().__init__()

        self.setWindowTitle("Johnny's Test Box")
        self.resize(500, 500)
        self.initUI()

    def initUI(self):
        # 1.create a RubberBand-pick controller
        self.rubberBand = QRubberBand(QRubberBand.Rectangle, self)
        pass

    def mousePressEvent(self, event):
        # QMouseEvent

        # 2.size: mouseClickPosition
        self.origin_pos = event.pos()
        self.rubberBand.setGeometry(QRect(self.origin_pos, QSize()))
        # 3.show RubberBand
        self.rubberBand.show()
        pass

    def mouseMoveEvent(self, event):
        # adjust picking area
        self.rubberBand.setGeometry(QRect(self.origin_pos, event.pos()).normalized())
        pass

    def mouseReleaseEvent(self, event):
        # 1.get area of RubberBand
        rect = self.rubberBand.geometry()
        # 2.check all child controllers in this area
        # Display the element in the marquee
        for child in self.children():
            if rect.contains(child.geometry()):
                print(child)
        self.rubberBand.hide()
        pass


if __name__ == "__main__":
    application = QApplication(sys.argv)
    window = rbWindow()
    window.show()
    exit(application.exec_())

#! /usr/bin/env python3

# selection rectangle is unviewable(strange spelling) when releaseMouseEven is used
# we can change node radius by using nodeRadius function now
# node bias have been solved
# node creating module added and it will connect to ground node automatically
# arc folling module added(but dont know how to fresh the whole painting page)
#double click connection added

<<<<<<< Updated upstream
# v1 25/04/21
=======
#v1 07/05/21
>>>>>>> Stashed changes

# ******************************************************************************

from sys import exit

import _ctypes

<<<<<<< Updated upstream
from PySide6.QtGui import QAction, QPixmap, QMouseEvent
from PySide6.QtWidgets import QApplication, QMainWindow, QHBoxLayout, QVBoxLayout, QWidget, QRubberBand
=======
from PySide6.QtGui import QAction
from PySide6.QtWidgets import QApplication, QMainWindow, QHBoxLayout, QVBoxLayout, QWidget
>>>>>>> Stashed changes
from PySide6.QtWidgets import QMenuBar, QMenu
from PySide6.QtWidgets import QToolBar, QStatusBar
from PySide6.QtWidgets import QFileDialog, QMessageBox

<<<<<<< Updated upstream
from PySide6.QtCore import Qt, QPointF, QRectF, QRect, QSize
from PySide6.QtGui import QColor, QPainter, QPainterPath, QPen, QBrush, QIcon
from PySide6.QtWidgets import QGraphicsScene, QGraphicsView, QGraphicsEllipseItem, QGraphicsPathItem, \
    QGraphicsSimpleTextItem, QGraphicsRectItem, QGraphicsItemGroup
=======
from PySide6.QtCore import Qt, QPointF, QRectF
from PySide6.QtGui import QColor, QPainter, QPainterPath, QPen, QBrush, QIcon
from PySide6.QtWidgets import QGraphicsScene, QGraphicsView, QGraphicsEllipseItem, QGraphicsPathItem, QGraphicsSimpleTextItem, QGraphicsRectItem, QGraphicsItemGroup
>>>>>>> Stashed changes

from PySide6.QtGui import QFontMetrics

import xml.dom.minidom as minidom
import os




# ******************************************************************************
selectedNodeId=-1

class NodeItem(QGraphicsEllipseItem):
    """
    Node class
    """
    # Global node colours, brushes, etc. fro all nodes
    nodeRadius = 25.0
    original_x=0
    original_y=0
    red = 153
    green = 225
    blue = 153
    nodeFillColour = QColor(red, green, blue)
    #nodeFillColour = QColor(226, 170, 243) # Naughty! Should not embed magic constants
    nodeFillBrush = QBrush(Qt.black, Qt.SolidPattern)
    nodeFillBrush.setColor(nodeFillColour)
    # Add node text
    nodeText = ""

    def __init__(self, x, y):
        self.x = x
        self.y = y
        self.nodeId=nodeItemlist_dict.lastId+1
        self.original_x=x
        self.original_y=y
        boundingRect = QRectF(x - self.nodeRadius, y - self.nodeRadius, 2.0 * self.nodeRadius, 2.0 * self.nodeRadius)   # Bounding rectangle of node 'ellipse' i.e. circle
        super().__init__(boundingRect)  # Invoke __init__ of base class
        self.setZValue(0)
        self.setBrush(self.nodeFillBrush)
        self.toolTip =  "node "+str(self.nodeId)
        self.setToolTip(self.toolTip)

        # Set node attributes
        self.ItemIsSelectable = True
        self.ItemIsMovable = True
        self.ItemSendsGeometryChanges = True

        # Create selection rectangle shown when node is selected
        self.selectionRectangle = QGraphicsRectItem(self.boundingRect())
        self.selectionRectangle.setVisible(False)

        return

    #-------------------------------------------------------------------

    def setNodeRadius(self, radius):
        # Set global node drawing radius
        self.prepareGeometryChange()
        self.nodeRadius = radius
        print("radius", self.nodeRadius)
        return

    #-------------------------------------------------------------------

    def setNodeText(self, new_text):
        # Set node text
        self.prepareGeometryChange()
        self.nodeText = new_text

        if (new_text=='Ground'):
            self.nodeId=0
            self.setToolTip="node 0"

        return

    #-------------------------------------------------------------------

    def paint(self, painter, option, parent):
        # Paint the node instance - called by QGraphicView instance
        #boundingRect = self.boundingRect()
        boundingRect = QRectF(self.original_x - self.nodeRadius, self.original_y - self.nodeRadius, 2.0 * self.nodeRadius, 2.0 * self.nodeRadius)

        if self.selectionRectangle.isVisible():
            # Paint selection rectangle
            painter.setPen(Qt.SolidLine)
            painter.setBrush(Qt.NoBrush)
            self.selectionRectangle.setRect(boundingRect)
            painter.drawRect(boundingRect)

        # Paint node circle
        painter.setBrush(self.nodeFillBrush)
        painter.drawEllipse(boundingRect)

        # Paint node text
        painter.setPen(Qt.black)
        painter.drawText(boundingRect, Qt.AlignCenter, self.nodeText)   # Clips nodeText. TODO - generate more accurate bounding retangle for text

        #print("paint called @ ", self.x, ", ", self.y,)

        return

    #-------------------------------------------------------------------

    def itemChange(self, change, value):
        # Called by scene when item changes
        print("change", self.nodeText, value)
        #if change == self.ItemPositionChange:
            # Redraw all  arcs  connected to node


        self.update()

        return super().itemChange(change, value)

     #-------------------------------------------------------------------

    def mousePressEvent(self, event):
        # Handler for mouse press event
        mousePos = event.pos()
        print("mouse press event at", mousePos.x(), ", ", mousePos.y())

        self.selectionRectangle.setVisible(True)
        self.update()

        return

    #-------------------------------------------------------------------

    def mouseReleaseEvent(self, event):
        # Handler for mouse release event
        mousePos = event.pos()
        self.selectionRectangle.setVisible(False)
        print("mouse release event at ", mousePos.x(), ", ", mousePos.y())

        print("-----------------------------")
        self.update()

        return

    #-------------------------------------------------------------------

    def mouseMoveEvent(self, event):
        # Handler for mouse move event
        #if self.nodeText!="ground":
        scenePosition = event.scenePos()
        self.x = scenePosition.x()-self.original_x
        self.y = scenePosition.y()-self.original_y

        self.prepareGeometryChange()
        self.setPos(QPointF(self.x,self.y))
        print("node ",self.nodeId," move to (", scenePosition.x(), scenePosition.y(),")")
        #self.arc.arcChange(QPointF(self.x,self.y))

        nodeItemlist_dict.AddNodeList(self.nodeId,QPointF(self.x+self.original_x,self.y+self.original_y),self.nodeText,self.nodeRadius)
        print("mouse release event at ", scenePosition.x(), ", ", scenePosition.y())

        list = filter(lambda x:self.nodeId== x[1], arcItemlist_dict.list_dict.items())
        for key in list:
            spacePos=key[0].find(' ')
            arcId=key[0][spacePos-1]

            arcAddress=arcItemlist_dict.list_dict['arc'+str(arcId)+' add']
            _ctypes.PyObj_FromPtr(arcAddress).arcChange()

        self.update()

        # else:
        #     print("gorund node, can't move")

        return

    #-------------------------------------------------------------------

    def mouseDoubleClickEvent(self, event):
        # Handler for mouse double click event
        global selectedNodeId

        if selectedNodeId==-1:
            selectedNodeId=self.nodeId
        else:
            add=self.arcConnection(selectedNodeId,self.nodeId)
            arcItemlist_dict.AddArcList(arcItemlist_dict.lastId+1,selectedNodeId,self.nodeId,add)
            arcItemlist_dict.lastId=arcItemlist_dict.lastId+1

            selectedNodeId=-1

        print("mouse double click event at node",self.nodeId)
        self.update()

        return

    #-------------------------------------------------------------------

    def arcConnection(self,startId,endId):
        startPosition=QPointF(nodeItemlist_dict.list_dict['node'+str(startId)+' x'],nodeItemlist_dict.list_dict['node'+str(startId)+' y'])
        endPosition=QPointF(nodeItemlist_dict.list_dict['node'+str(endId)+' x'],nodeItemlist_dict.list_dict['node'+str(endId)+' y'])

        arc=ArcItem(arcItemlist_dict.lastId+1,startPosition,endPosition)
        mainWindow.graphicsScene.addItem(arc)
        add=id(arc)
        #print(_ctypes.PyObj_FromPtr(add))

        print("arc connection added between node",startId,"and node",endId)

        return add

#******************************************************************************

class ArcItem (QGraphicsPathItem):
    C = QPointF(0,0)

    def __init__(self,arcId,nodeCentre_A,nodeCentre_B):
        self.A=nodeCentre_A
        self.B=nodeCentre_B
        self.arcId=arcId

        self.startPosition=self.B
        self.midPosition=QPointF((self.A.x()+self.B.x())/2,(self.A.y()+self.B.y())/2)
        self.endPosition=self.A

        path=QPainterPath(self.startPosition)
        path.quadTo(self.midPosition.x(),self.midPosition.y(),self.endPosition.x(),self.endPosition.y())
        super().__init__(path)

        #print("path added", path)
        self.setZValue(-1)
        self.toolTip= "this is an arc"
        self.setToolTip(self.toolTip)

        return

    def arcChange(self):
        startPointId=arcItemlist_dict.list_dict["arc"+str(self.arcId)+" startID"]
        endPointId=arcItemlist_dict.list_dict["arc"+str(self.arcId)+" endID"]
        self.A=QPointF(nodeItemlist_dict.list_dict["node"+str(startPointId)+" x"],nodeItemlist_dict.list_dict["node"+str(startPointId)+" y"])
        self.B=QPointF(nodeItemlist_dict.list_dict["node"+str(endPointId)+" x"],nodeItemlist_dict.list_dict["node"+str(endPointId)+" y"])

        #self.C = nodeCentre_C
        self.prepareGeometryChange()
        #self.setPos(QPointF(nodeCentre_C.x(),nodeCentre_C.y()))

        self.startPosition=self.B
        self.endPosition=self.A
        self.midPosition=QPointF((self.A.x()+self.B.x())/2,(self.A.y()+self.B.y())/2)
        print ("arc",self.arcId,"change")
        #self.update(0,0,100,100)

        return

    def paint(self, painter, option, parent):
        painter.setPen(Qt.black)
        painter.setBrush(Qt.NoBrush)

        path=QPainterPath(self.startPosition)
        path.quadTo(self.midPosition.x(),self.midPosition.y(),self.endPosition.x(),self.endPosition.y())

        painter.drawPath(path)
        print("arc move to (",self.startPosition.x(),self.startPosition.y(),") (",self.midPosition.x(),self.midPosition.y(),") (",self.endPosition.x(),self.endPosition.x(),")")

        return

#******************************************************************************

class ItemList(object):
    lastId=0
    #maximumArcId=0
    list_dict={'node0 x':300,'node0 y':300,'node0 text':'ground','node0 radius':25}

    def AddNodeList(self,ID,position,text,radius):
        nodeId='node'+str(ID)
        self.list_dict[nodeId+' x']=position.x()
        self.list_dict[nodeId+' y']=position.y()
        self.list_dict[nodeId+' text']=text
        self.list_dict[nodeId+' radius']=radius
        print("node list_dictoinary (",self.list_dict[nodeId+' x'],self.list_dict[nodeId+' y'],self.list_dict[nodeId+' text'],self.list_dict[nodeId+' radius'],")")
        print(self.list_dict)

    def AddArcList(self,ID,startNode,endNode,add):
        arcId='arc'+str(ID)
        self.list_dict[arcId+' startID']=startNode
        self.list_dict[arcId+' endID']=endNode
        self.list_dict[arcId+' add']=add
        print("arc list_dictionary (",self.list_dict[arcId+' startID'],self.list_dict[arcId+' endID'],self.list_dict[arcId+' add'],")")

#******************************************************************************

selectedNodeId=-1

class NodeItem(QGraphicsEllipseItem):
    """
    Node class 
    """
    # Global node colours, brushes, etc. fro all nodes
    nodeRadius = 25.0
    original_x=0
    original_y=0
    nodeFillColour = QColor(226, 170, 243) # Naughty! Should not embed magic constants
    nodeFillBrush = QBrush(Qt.black, Qt.SolidPattern)   
    nodeFillBrush.setColor(nodeFillColour)
    # Add node text 
    nodeText = ""

    def __init__(self, x, y):
        self.x = x
        self.y = y
        self.nodeId=nodeItemlist_dict.lastId+1
        self.original_x=x
        self.original_y=y
        boundingRect = QRectF(x - self.nodeRadius, y - self.nodeRadius, 2.0 * self.nodeRadius, 2.0 * self.nodeRadius)   # Bounding rectangle of node 'ellipse' i.e. circle
        super().__init__(boundingRect)  # Invoke __init__ of base class
        self.setZValue(0)
        self.setBrush(self.nodeFillBrush)
        self.toolTip =  "node "+str(self.nodeId)
        self.setToolTip(self.toolTip)
        
        # Set node attributes
        self.ItemIsSelectable = True
        self.ItemIsMovable = True
        self.ItemSendsGeometryChanges = True
        
        # Create selection rectangle shown when node is selected
        self.selectionRectangle = QGraphicsRectItem(self.boundingRect())
        self.selectionRectangle.setVisible(False)
         
        return
        
    #-------------------------------------------------------------------
            
    def setNodeRadius(self, radius):
        # Set global node drawing radius
        self.prepareGeometryChange()
        self.nodeRadius = radius
        print("radius", self.nodeRadius)
        return
                
    #-------------------------------------------------------------------

    def setNodeText(self, new_text):
        # Set node text
        self.prepareGeometryChange()
        self.nodeText = new_text

        if (new_text=='ground'):
            self.nodeId=0
            self.setToolTip="node 0"

        return

    #-------------------------------------------------------------------
      
    def paint(self, painter, option, parent):
        # Paint the node instance - called by QGraphicView instance
        #boundingRect = self.boundingRect()
        boundingRect = QRectF(self.original_x - self.nodeRadius, self.original_y - self.nodeRadius, 2.0 * self.nodeRadius, 2.0 * self.nodeRadius)
        
        if self.selectionRectangle.isVisible():
            # Paint selection rectangle
            painter.setPen(Qt.SolidLine)
            painter.setBrush(Qt.NoBrush)
            self.selectionRectangle.setRect(boundingRect)
            painter.drawRect(boundingRect)

        # Paint node circle
        painter.setBrush(self.nodeFillBrush)
        painter.drawEllipse(boundingRect)
        
        # Paint node text
        painter.setPen(Qt.black)
        painter.drawText(boundingRect, Qt.AlignCenter, self.nodeText)   # Clips nodeText. TODO - generate more accurate bounding retangle for text

        #print("paint called @ ", self.x, ", ", self.y,)
        
        return
        
    #-------------------------------------------------------------------
    
    def itemChange(self, change, value):
        # Called by scene when item changes
        print("change", self.nodeText, value)
        #if change == self.ItemPositionChange:
            # Redraw all  arcs  connected to node
            
        
        self.update()
        
        return super().itemChange(change, value)
        
     #-------------------------------------------------------------------
     
    def mousePressEvent(self, event):
        # Handler for mouse press event
        mousePos = event.pos()
        print("mouse press event at", mousePos.x(), ", ", mousePos.y())
        
        self.selectionRectangle.setVisible(True)
        self.update()
        
        return

    #-------------------------------------------------------------------
 
    def mouseReleaseEvent(self, event):
        # Handler for mouse release event
        mousePos = event.pos()
        self.selectionRectangle.setVisible(False)
        print("mouse release event at ", mousePos.x(), ", ", mousePos.y())

        print("-----------------------------")
        self.update()
              
        return
        
    #-------------------------------------------------------------------
    
    def mouseMoveEvent(self, event):
        # Handler for mouse move event
        #if self.nodeText!="ground":
        scenePosition = event.scenePos()
        self.x = scenePosition.x()-self.original_x
        self.y = scenePosition.y()-self.original_y
            
        self.prepareGeometryChange()
        self.setPos(QPointF(self.x,self.y))
        print("node ",self.nodeId," move to (", scenePosition.x(), scenePosition.y(),")")
        #self.arc.arcChange(QPointF(self.x,self.y))

        nodeItemlist_dict.AddNodeList(self.nodeId,QPointF(self.x+self.original_x,self.y+self.original_y),self.nodeText,self.nodeRadius)
        print("mouse release event at ", scenePosition.x(), ", ", scenePosition.y())

        list = filter(lambda x:self.nodeId== x[1], arcItemlist_dict.list_dict.items())
        for key in list:
            spacePos=key[0].find(' ')
            arcId=key[0][spacePos-1]

            arcAddress=arcItemlist_dict.list_dict['arc'+str(arcId)+' add']
            _ctypes.PyObj_FromPtr(arcAddress).arcChange()

        self.update()

        # else:
        #     print("gorund node, can't move")

        return

    #-------------------------------------------------------------------        
    
    def mouseDoubleClickEvent(self, event):
        # Handler for mouse double click event
        global selectedNodeId

        if selectedNodeId==-1:
            selectedNodeId=self.nodeId
        else:
            add=self.arcConnection(selectedNodeId,self.nodeId)
            arcItemlist_dict.AddArcList(arcItemlist_dict.lastId+1,selectedNodeId,self.nodeId,add)
            arcItemlist_dict.lastId=arcItemlist_dict.lastId+1

            selectedNodeId=-1

        print("mouse double click event at node",self.nodeId)
        self.update()
        
        return
    
    #-------------------------------------------------------------------        
    
    def arcConnection(self,startId,endId):
        startPosition=QPointF(nodeItemlist_dict.list_dict['node'+str(startId)+' x'],nodeItemlist_dict.list_dict['node'+str(startId)+' y'])
        endPosition=QPointF(nodeItemlist_dict.list_dict['node'+str(endId)+' x'],nodeItemlist_dict.list_dict['node'+str(endId)+' y'])

        arc=ArcItem(arcItemlist_dict.lastId+1,startPosition,endPosition)
        mainWindow.graphicsScene.addItem(arc)
        add=id(arc)
        #print(_ctypes.PyObj_FromPtr(add))

        print("arc connection added between node",startId,"and node",endId)

        return add

#******************************************************************************

class ArcItem (QGraphicsPathItem):
    C = QPointF(0,0)

    def __init__(self,arcId,nodeCentre_A,nodeCentre_B):
        self.A=nodeCentre_A
        self.B=nodeCentre_B
        self.arcId=arcId

        self.startPosition=self.B
        self.midPosition=QPointF((self.A.x()+self.B.x())/2,(self.A.y()+self.B.y())/2)
        self.endPosition=self.A

        path=QPainterPath(self.startPosition)
        path.quadTo(self.midPosition.x(),self.midPosition.y(),self.endPosition.x(),self.endPosition.y())
        super().__init__(path)

        #print("path added", path)
        self.setZValue(-1)
        self.toolTip= "this is an arc"
        self.setToolTip(self.toolTip)

        return 
    
    def arcChange(self):
        startPointId=arcItemlist_dict.list_dict["arc"+str(self.arcId)+" startID"]
        endPointId=arcItemlist_dict.list_dict["arc"+str(self.arcId)+" endID"]
        self.A=QPointF(nodeItemlist_dict.list_dict["node"+str(startPointId)+" x"],nodeItemlist_dict.list_dict["node"+str(startPointId)+" y"])
        self.B=QPointF(nodeItemlist_dict.list_dict["node"+str(endPointId)+" x"],nodeItemlist_dict.list_dict["node"+str(endPointId)+" y"])

        #self.C = nodeCentre_C
        self.prepareGeometryChange()
        #self.setPos(QPointF(nodeCentre_C.x(),nodeCentre_C.y()))

        self.startPosition=self.B
        self.endPosition=self.A
        self.midPosition=QPointF((self.A.x()+self.B.x())/2,(self.A.y()+self.B.y())/2)
        print ("arc",self.arcId,"change")
        #self.update(0,0,100,100)

        return

    def paint(self, painter, option, parent):
        painter.setPen(Qt.black)
        painter.setBrush(Qt.NoBrush)

        path=QPainterPath(self.startPosition)
        path.quadTo(self.midPosition.x(),self.midPosition.y(),self.endPosition.x(),self.endPosition.y())

        painter.drawPath(path)
        print("arc move to (",self.startPosition.x(),self.startPosition.y(),") (",self.midPosition.x(),self.midPosition.y(),") (",self.endPosition.x(),self.endPosition.x(),")")

        return

#******************************************************************************
  
class ItemList(object):
    lastId=0
    #maximumArcId=0
    list_dict={'node0 x':300,'node0 y':300,'node0 text':'ground','node0 radius':25}

    def AddNodeList(self,ID,position,text,radius):
        nodeId='node'+str(ID)
        self.list_dict[nodeId+' x']=position.x()
        self.list_dict[nodeId+' y']=position.y()
        self.list_dict[nodeId+' text']=text
        self.list_dict[nodeId+' radius']=radius
        print("node list_dictoinary (",self.list_dict[nodeId+' x'],self.list_dict[nodeId+' y'],self.list_dict[nodeId+' text'],self.list_dict[nodeId+' radius'],")")
        print(self.list_dict)

    def AddArcList(self,ID,startNode,endNode,add):
        arcId='arc'+str(ID)
        self.list_dict[arcId+' startID']=startNode
        self.list_dict[arcId+' endID']=endNode
        self.list_dict[arcId+' add']=add
        print("arc list_dictionary (",self.list_dict[arcId+' startID'],self.list_dict[arcId+' endID'],self.list_dict[arcId+' add'],")")

#******************************************************************************

class MainWindow(QMainWindow):
    def __init__(self):
        super().__init__()

        self.setWindowTitle("EEE231 Group A")
        self.setWindowIcon(QIcon("./Icons/iconCircuit-50px.png"))
        self.setMinimumSize(300, 300)
        self.mainLayout = QVBoxLayout()

<<<<<<< Updated upstream
        # Create actions
        self._createActions()

=======
>>>>>>> Stashed changes
        # Create ToolBar
        self.createToolBar()

        # Create MenuBar
        self._createMenuBar()

        # Set mainLayout as the central widget
        self.mainWidget = QWidget()
        self.mainWidget.setLayout(self.mainLayout)
        self.setCentralWidget(self.mainWidget)
<<<<<<< Updated upstream

        # Create graphics scene
        graphicsHeight = 500
        graphicsWidth = 500
        self.graphicsScene = QGraphicsScene(0, 0, graphicsWidth, graphicsHeight, self)

        # -----------------------------(temp)
        ground = NodeItem(300, 300)
        self.graphicsScene.addItem(ground)
        ground.setNodeText("Ground")
        nodeCentre_G = QPointF(ground.x, ground.y)
=======
        
        # Create graphics scene
        self.graphicsScene = QGraphicsScene(0, 0, 511, 511, self)   # Naughty! Should not be embedding 'magic' constants in a program!!!
        
        #-----------------------------(temp)
        ground = NodeItem(300,300)
        self.graphicsScene.addItem(ground)
        ground.setNodeText("ground")
        ground.nodeId=0
        nodeCentre_G=QPointF(ground.x,ground.y)
>>>>>>> Stashed changes

        #test = NodeItem(150, 100)
        #self.graphicsScene.addItem(test)
        #test.setNodeText("test")
<<<<<<< Updated upstream
        #test.setNodeRadius(15)
        #nodeCentre_T = QPointF(test.x, test.y)

        #arc1 = ArcItem(nodeCentre_G, nodeCentre_T)
        #self.graphicsScene.addItem(arc1)
        # ---------------------------------

        # Create graphics view
        self.createRubberBand()
        self.graphicsView = QGraphicsView(self.graphicsScene)
        self.graphicsView.setDragMode(QGraphicsView.RubberBandDrag)
        self.graphicsView.setRubberBandSelectionMode(Qt.ContainsItemBoundingRect)
=======
        #test.setNodeRadius(25)
        #nodeCentre_T=QPointF(test.x,test.y)
        
        #arc1=ArcItem(nodeCentre_G,nodeCentre_T)
        #self.graphicsScene.addItem(arc1)
        #test.arcConnection(arc1)
        #---------------------------------
        
        # Create graphics view
        self.graphicsView = QGraphicsView(self.graphicsScene)
        #self.graphicsView.setDragMode(QGraphicsView.RubberBandDrag)
        #self.graphicsView.setRubberBandSelectionMode(Qt.ContainsItemBoundingRect)
>>>>>>> Stashed changes
        self.graphicsView.setRenderHints(QPainter.Antialiasing)
        self.mainLayout.addWidget(self.graphicsView)

        # Is a status bar needed in this application?
        self.statusBar = QStatusBar()
        self.mainLayout.addWidget(self.statusBar)
<<<<<<< Updated upstream
        self.statusBar.showMessage("Created a new file")

    # -------------------------------------------------------------------

    def createRubberBand(self):
            # 1.create a RubberBand-pick controller
            self.rubberBand = QRubberBand(QRubberBand.Rectangle, self)
            pass

    # -------------------------------------------------------------------

    def mouseReleaseEvent(self, event):
        # 1.get area of RubberBand
        rect = self.rubberBand.geometry()
        # 2.check all child controllers in this area
        for child in self.children():
            if rect.contains(child.geometry()):
                print(child)
        # self.rubberBand.hide()
        pass

    # -------------------------------------------------------------------

    def _createActions(self):
        # creating action

        self.newAction = QAction("&New", self)
        self.openAction = QAction("&Open", self)
        self.openAction.triggered.connect(self.open)
        self.exitAction = QAction("&Exit", self)
        self.saveAction = QAction("&Save", self)
        self.saveAction.triggered.connect(self.save)
        self.helpAction = QAction("&Help", self)
        self.aboutAction = QAction("&About", self)

        # connect actions to functions

    # -------------------------------------------------------------------
    def _createMenuBar(self):
        menuBar = self.menuBar()

        # File tab
        fileMenu = QMenu("&File", self)
        menuBar.addMenu(fileMenu)
        fileMenu.addAction(self.newAction)
        fileMenu.addAction(self.openAction)
        fileMenu.addAction(self.saveAction)
        fileMenu.addAction(self.exitAction)

        # help tab
        helpMenu = menuBar.addMenu("&Help")
        helpMenu.addAction(self.aboutAction)
        helpMenu.addAction(self.helpAction)
        self.helpAction.triggered.connect(self.helpButton)
        self.aboutAction.triggered.connect(self.aboutButton)

    # About Button
    def aboutButton(self):
        aboutMessageBox = QMessageBox(self)
        aboutMessageBox.setWindowModality(Qt.NonModal)
        aboutMessageBox.setWindowTitle("About this program")
        aboutMessageBox.setText("<p><h4>This program is developed by engineers from EEE231 Group A. "
                                "The program is written in Python and most of the UI are written using Pyside.<br /> "
                                "</h4></p> "
                                "<p><h4>Acknowledgements to the programmers who have contributed to this program (in "
                                "no particular order) </h4>"
                                "Joshua Adewoye <br />"
                                "Pengyu Shao <br />"
                                "Yang Cen <br />"
                                "Zhengyi Huang <br />"
                                "Iyad Khairallah <br />"
                                "Yufei Liu <br />"
                                "Chi Hei Tan <br />"
                                "Brian Wu <br />"
                                "Qidong Ye <br />"
                                "Zimu Bao <br /></p>"
                                "<p><h4>Special thanks to (in no particular order) </h4>"
                                "Dr Peter Rockett<br />"
                                "Dr Charith Abhayaratne<br />"
                                "Mr Prathamesh Khatavkar</p>")
        aboutMessageBox.setIconPixmap(QPixmap("./Icons/iconInformation-80px.png").scaled(80, 80))
        aboutMessageBox.open()

    # Help Button
    def helpButton(self):
        helpMessageBox = QMessageBox(self)
        helpMessageBox.setWindowModality(Qt.NonModal)
        helpMessageBox.setWindowTitle("Help")
        helpMessageBox.setText('<h4>Please open this link to get help</h4>'
                               '<a href="https://github.com/pirlite2/EEE231-group-A/blob/main/README.md">'
                               'click here')
        helpMessageBox.setIconPixmap(QPixmap("./Icons/iconHelp-80px.png").scaled(80, 80))
        helpMessageBox.open()

    # --------------------------------------------------------------------

    def createToolBar(self):
=======

        # Set mainLayout as the central widget
        self.mainWidget = QWidget()
        self.mainWidget.setLayout(self.mainLayout)
        self.setCentralWidget(self.mainWidget)
        
    #-------------------------------------------------------------------

    def _createMenuBar(self):
        menuBar = self.menuBar()

        fileMenu = QMenu("&file", self)
        menuBar.addMenu(fileMenu)

        editMenu = menuBar.addMenu("&Edit")
        helpMenu = menuBar.addMenu("&help")
        

    #--------------------------------------------------------------------

    def createToolBar(self):

>>>>>>> Stashed changes
        # Create toolbar buttons, doesn't seem to work outside main window class
        self.mainToolBar = QToolBar()
        self.mainToolBar.setMovable(False)

<<<<<<< Updated upstream
        # Add Current source node button
        self.addCurrentNode = self.mainToolBar.addAction("+ Current Source Node")
        self.addCurrentNode.triggered.connect(self.addCNode)
        self.addCurrentNode.setIcon(QIcon("./Icons/iconAddCurrentSourceNode.png"))

        # Add Internal node button
        self.addInternalNode = self.mainToolBar.addAction("+ Internal Node")
        self.addInternalNode.triggered.connect(self.addINode)
        self.addInternalNode.setIcon(QIcon("./Icons/iconAddInternalNode.png"))

        # Add arc button
        self.addArcB = self.mainToolBar.addAction("+ Arc")
        self.addArcB.triggered.connect(self.addArc)
        self.addArcB.setIcon(QIcon("./Icons/iconAddArcs.png"))
=======
        # Add Node button
        self.addNodeB = self.mainToolBar.addAction("+ Node") 
        #QIcon addNodeIcon = QIcon.fromTheme("document new", QIcon(":images/new.png")) # Not sure how the icons work exactly
        self.addNodeB.triggered.connect(self.addNode)

        # Add arc button
        self.addArcB = self.mainToolBar.addAction("+ Arc")
        self.addArcB.triggered.connect(self.addArc) 
>>>>>>> Stashed changes

        # Delete button
        self.deleteB = self.mainToolBar.addAction("Delete")
        self.deleteB.triggered.connect(self.delete)
<<<<<<< Updated upstream
        self.deleteB.setIcon(QIcon("./Icons/iconDelete_2.png"))

        # Save button
        self.saveB = self.mainToolBar.addAction("Save")
        self.saveB.triggered.connect(self.save)
        self.saveB.setIcon(QIcon("./Icons/iconSave.png"))

        self.addToolBar(self.mainToolBar)  # Setting mainToolBar as a Toolbar
        self.mainLayout.addWidget(self.mainToolBar)  # Adding mainToolBar to layout widget

    # -------------------------------------------------------------------

    def addCNode(self):
        """Handler for add Current source node action"""
         #tempId=ItemList.maximumNodeId+1
        node = NodeItem(200,200)
        self.graphicsScene.addItem(node)
        node.setNodeText("Current")

        nodeItemlist_dict.lastId=nodeItemlist_dict.lastId+1

        nodeItemlist_dict.AddNodeList(node.nodeId,QPointF(node.x,node.y),node.nodeText,node.nodeRadius)

        self.statusBar.showMessage("Added a Current Source Node")

        #arc=ArcItem(QPointF(300,300),QPointF(200,200))
        #self.graphicsScene.addItem(arc)
        #node.arcConnection(arc)
        return

    # -------------------------------------------------------------------

    def addINode(self):
        """Handler for add Current source node action"""
         #tempId=ItemList.maximumNodeId+1
        node = NodeItem(200,200)
        self.graphicsScene.addItem(node)
        node.setNodeText("Internal")

        nodeItemlist_dict.lastId=nodeItemlist_dict.lastId+1

        nodeItemlist_dict.AddNodeList(node.nodeId,QPointF(node.x,node.y),node.nodeText,node.nodeRadius)

        self.statusBar.showMessage("Added an Internal Node")

=======

        # Save button
        self.saveB = self.mainToolBar.addAction("Save")
        self.saveB.triggered.connect(self.save)   

        # Save as button
        self.saveAsB = self.mainToolBar.addAction("Save As") 
        self.saveAsB.triggered.connect(self.saveAs)

        self.addToolBar(self.mainToolBar) # Setting mainToolBar as a Toolbar
        self.mainLayout.addWidget(self.mainToolBar) # Adding mainToolBar to layout widget

    #-------------------------------------------------------------------

    def addNode(self):
        """Handler for addNode action"""
        #tempId=ItemList.maximumNodeId+1
        node = NodeItem(200,200)
        self.graphicsScene.addItem(node)
        node.setNodeText("new node")

        nodeItemlist_dict.lastId=nodeItemlist_dict.lastId+1 

        nodeItemlist_dict.AddNodeList(node.nodeId,QPointF(node.x,node.y),node.nodeText,node.nodeRadius)

>>>>>>> Stashed changes
        #arc=ArcItem(QPointF(300,300),QPointF(200,200))
        #self.graphicsScene.addItem(arc)
        #node.arcConnection(arc)
        return

<<<<<<< Updated upstream
    # -------------------------------------------------------------------

    def addArc(self):
        """Handler for addArc action"""
        addArcMessageBox = QMessageBox(self)
        addArcMessageBox.setWindowModality(Qt.NonModal)
        addArcMessageBox.setWindowTitle("Add Arc")
        addArcMessageBox.setText('Please <b>Double-Click</b> on the two nodes you want to connect to generate'
                                 'an Arc to connect them!')
        addArcMessageBox.setIconPixmap(QPixmap("./Icons/iconAddArcs-80px.png").scaled(80, 80))
        addArcMessageBox.open()
        return

    # -------------------------------------------------------------------

    def delete(self):
        """Handler for delete action"""
        return

    # -------------------------------------------------------------------

    def save(self):
        """Handler for save action"""
        # Status bar display
        self.statusBar.showMessage("File saved")

        # set the root
        root = minidom.Document()
        # ceate class
        Class = root.createElement('Class')
        globalData = root.createElement('globalData')
        nodeClass = root.createElement('nodeClass')
        arcClass = root.createElement('arcClass')
        # ----------------------------add new data below this comment----------------------------
        # Global data
        lastNode = root.createElement('lastNodeID')
        lastArc = root.createElement('lastArcID')

        lastNode.setAttribute('lastNodeID', str(lastNodeID))
        lastArc.setAttribute('lastArcID', str(lastArcID))

        globalData.appendChild(lastNode)
        globalData.appendChild(lastArc)
        #read from gui
        #read arc from dict
        for n in range(6):
            for saving in DictOfArc:
                if n == 0:
                    arcID.append(saving[n])
                elif n == 1:
                    StartNodeID.append(saving[n])
                elif n == 2:
                    add.append(saving[n])
                elif n == 3:
                 EndNodeID.append(saving[n])
                elif n == 4:
                    arcUnit.append(saving[n])
                elif n == 5:
                    Resis.append(saving[n])
        #read node from dict
        for n in range(6):
            for saving in DictOfNode:
                if n == 0:
                    nodeID.append(saving[n])
                elif n == 1:
                    X.append(saving[n])
                elif n == 2:
                    Y.append(saving[n])
                elif n == 3:
                    Radius.append(saving[n])
                elif n == 4:
                    nodeUnit.append(saving[n])
                elif n == 5:
                    Value.append(saving[n])
        #node data:
        for i in range(0,len(nodeID)):
            #create a node (create in format:<node ID="1"> </node> )
            node = root.createElement('node')
            node.setAttribute('ID',str(nodeID[i]))
            #def name of element
            nX      = root.createElement('nX')
            nY      = root.createElement('nY')
            nRadius = root.createElement('nRadius')
            nUnit   = root.createElement('nUnit')
            nValue  = root.createElement('nValue')
            #save the data in the format: <nX>25</nX>
            nX      .appendChild(root.createTextNode(str(X[i])))
            nY      .appendChild(root.createTextNode(str(Y[i])))
            nRadius .appendChild(root.createTextNode(str(Radius[i])))
            nUnit   .appendChild(root.createTextNode(str(nodeUnit[i])))
            nValue  .appendChild(root.createTextNode(str(Value[i])))
            #append the elements to node
            node.appendChild(nX)
            node.appendChild(nY)
            node.appendChild(nRadius)
            node.appendChild(nUnit)
            node.appendChild(nValue)
            #append the node to nodeClass
            nodeClass.appendChild(node)
        #arc data, similar to node.
        for i in range(0,len(arcID)):
            arc=root.createElement('arc')
            arc.setAttribute('ID',str(arcID[i]))

            aStartNodeID    = root.createElement('aStartNodeID')
            aAdd            = root.createElement('aAdd')
            aEndNodeID      = root.createElement('aEndNodeID')
            aUnit           = root.createElement('aUnit')
            aResis          = root.createElement('aResis')

            aStartNodeID.appendChild(root.createTextNode(str(StartNodeID[i])))
            aAdd       .appendChild(root.createTextNode(str(add[i])))
            aEndNodeID  .appendChild(root.createTextNode(str(EndNodeID[i])))
            aUnit       .appendChild(root.createTextNode(str(arcUnit[i])))
            aResis      .appendChild(root.createTextNode(str(Resis[i])))

            arc.appendChild(aStartNodeID)
            arc.appendChild(aAdd)
            arc.appendChild(aEndNodeID)
            arc.appendChild(aUnit)
            arc.appendChild(aResis)

            arcClass.appendChild(arc)

        Class.appendChild(globalData)
        Class.appendChild(nodeClass)
        Class.appendChild(arcClass)

        root.appendChild(Class)
        # save the file
        name = QFileDialog.getSaveFileName(self, 'Save File', "data.xml", ("*.xml"))
        with open(name[0], "w") as f:  # path and name can be change
            root.writexml(f, indent="\t", newl="\n", encoding='UTF-8')

        self.statusBar.showMessage("Saved the file")

        return

    # -------------------------------------------------------------------

    def open(self):
        """Handler for open action, changed from saveAs"""
        fileName = QFileDialog.getOpenFileName(self, "Open File", ".", ("*.xml"))
        print("opening ", fileName[0])
        path = fileName[0]  # need to change the path to run
        doc = minidom.parse(path)
        Class = doc.documentElement

        nodes = Class.getElementsByTagName('node')
        arcs = Class.getElementsByTagName('arc')

        LNID = Class.getElementsByTagName('lastNodeID')
        LAID = Class.getElementsByTagName('lastArcID')
        for data in LNID:
            lastNodeID = (data.getAttribute('lastNodeID'))
        for data in LAID:
            lastArcID = (data.getAttribute('lastArcID'))
        for node in nodes:
            nodeID  .append(int(node.getAttribute('ID')))
            X       .append(int(node.getElementsByTagName('nX')[0]      .childNodes[0].data))
            Y       .append(int(node.getElementsByTagName('nY')[0]      .childNodes[0].data))
            Radius  .append(int(node.getElementsByTagName('nRadius')[0] .childNodes[0].data))
            nodeUnit.append(    node.getElementsByTagName('nUnit')[0]   .childNodes[0].data)
            Value   .append(int(node.getElementsByTagName('nValue')[0]  .childNodes[0].data))
        for arc in arcs:
            arcID       .append(int(arc.getAttribute('ID')))
            StartNodeID .append(int(arc.getElementsByTagName('aStartNodeID')[0] .childNodes[0].data))
            add         .append(int(arc.getElementsByTagName('aAdd')[0]         .childNodes[0].data))
            EndNodeID   .append(int(arc.getElementsByTagName('aEndNodeID')[0]   .childNodes[0].data))
            arcUnit     .append(    arc.getElementsByTagName('aUnit')[0]        .childNodes[0].data)
            Resis       .append(int(arc.getElementsByTagName('aResis')[0]       .childNodes[0].data))
        #read the lists from xml
        n = 0
        for nodeID_item in nodeID:
            ItemList.dict['node' + str(nodeID_item)] = [nodeID[n], X[n], Y[n], Radius[n], nodeUnit[n], Value[n]]
            n = n + 1
        n = 0
        for arcID_item in arcID:
            ItemList.dict['arc' + str(arcID_item)] = [arcID[n], StartNodeID[n], add[n], EndNodeID[n], arcUnit[n], Resis[n]]
            n = n + 1
        self.statusBar.showMessage("Opened a file")

        return

#the list below tesing in save/load function, please remmeber to remove it.
#golbal
lastNodeID  = 0
lastArcID   = 0
#Radius     = 0
#qcolourR   = 0
#qcolourG   = 0
#qcolourB   = 0
#node
nodeID      = []
X           = []
Y           = []
Radius      = []
nodeUnit    = []
Value       = []
#arc
arcID       = []
StartNodeID = []
add         = []
EndNodeID   = []
arcUnit     = []
Resis       = []
#test end -----------------------------
# ******************************************************************************
=======
    #-------------------------------------------------------------------

    def addArc(self):
        """Handler for addArc action"""
        return
    
    #-------------------------------------------------------------------

    def delete(self):
        """Handler for delete action"""
        self.destroy()
        return

    #-------------------------------------------------------------------

    def save(self):
        """Handler for save action"""
        return
    
    #-------------------------------------------------------------------

    def saveAs(self):
        """Handler for saveAs action"""
        return

#******************************************************************************
>>>>>>> Stashed changes

if __name__ == "__main__":
    application = QApplication([])

    nodeItemlist_dict=ItemList()
    arcItemlist_dict=ItemList()

    mainWindow = MainWindow()
    mainWindow.show()

<<<<<<< Updated upstream
    exit(application.exec_())
=======
    exit(application.exec_())
>>>>>>> Stashed changes

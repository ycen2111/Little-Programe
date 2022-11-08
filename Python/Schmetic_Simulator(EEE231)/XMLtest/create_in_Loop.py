#! /usr/bin/env python3
import xml.dom.minidom as minidom
#test data
#golbal 
lastNodeID  = 2
lastArcID   = 2
'''
Radius = 25
qcolourR =100
qcolourG =100
qcolourB =100
'''
#node "node"+ID:[ID,x,y,radius,unit,value]
gnx=150
gny=200
nodeID      = [1,2] 
X           = [0,10 ]
Y           = [0,20]
Radius      = [25,25] 
nodeUnit    = ['A','V']
Value       = [2,5]
#arc  #"arc"+ID:[ID,startNodeID,add,endNodeID,unit,resistance]
arcID       = [1,2]
StartNodeID = [1,2]  
add         = [0,50]  
EndNodeID   = [2,1] 
arcUnit     = ['A','mA']
Resis       = [1,2] 
#start of create code
#set the root
root = minidom.Document()
#ceate class
Class       = root.createElement('Class')
globalData  = root.createElement('globalData')
nodeClass   = root.createElement('nodeClass')
arcClass    = root.createElement('arcClass')
#----------------------------add new data below this comment----------------------------
#Global data
lastNode    = root.createElement('lastNodeID')
lastArc     = root.createElement('lastArcID')
'''
Radius     = root.createElement('Radius')
qcolourR   = root.createElement('qcolourR')
qcolourG   = root.createElement('qcolourG')
qcolourB   = root.createElement('qcolourB')
'''
lastNode.setAttribute('lastNodeID', str(lastNodeID)) 
lastArc .setAttribute('lastArcID',  str(lastArcID)) 
'''
Radius .setAttribute('Radius',  str(Radius)) 
qcolourR .setAttribute('qcolourR',  str(qcolourR)) 
qcolourG .setAttribute('qcolourG',  str(qcolourG)) 
qcolourB .setAttribute('qcolourB',  str(qcolourB)) 
'''

globalData.appendChild(lastNode)
globalData.appendChild(lastArc)
'''
globalData.appendChild(Radius)
globalData.appendChild(qcolourR)
globalData.appendChild(qcolourG)
globalData.appendChild(qcolourB)
'''
ground= root.createElement('groundNode')
groundx=root.createElement('gnx')
groundy=root.createElement('gny')
groundx.appendChild(root.createTextNode(str(gnx)))
groundy.appendChild(root.createTextNode(str(gny)))
ground.appendChild(groundx)
ground.appendChild(groundy)
nodeClass.appendChild(ground)
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
#save the file
with open("test.xml", "w") as f:
    root.writexml(f, indent ="\t", newl="\n",encoding='UTF-8') 
'''
name=QFileDialog.getSaveFileName(self,'Save File',"data.xml", ("*.xml"))
with open(name[0], "w") as f:#path and name can be change
    root.writexml(f, indent ="\t", newl="\n",encoding='UTF-8') 
'''



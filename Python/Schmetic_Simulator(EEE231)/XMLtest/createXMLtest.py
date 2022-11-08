#! /usr/bin/env python3
'''
there are 2 format of the node in xml:
    A = root.createElement('Beta')
    A.setAttribute('C','1')
it create: <Beta C=1> </Beta>

    A = root.createElement('Beta')
    A.appendChild(root.createTextNode('1'))  
it create: <Beta>25</Beta>
* the number '1' is in str, when input x in int, then need to use str(x) 
'''
import xml.dom.minidom as minidom
#data
nodeRadius= 25
nodeXLocation =100
nodeYLocation =250
setZValue =5
#create the root
root = minidom.Document()
#ceate class
Class = root.createElement('Class')
nodeClass=root.createElement('nodeClass')
arcClass=root.createElement('arcClass')
#node data
#node1
node = root.createElement('node')
node.setAttribute('ID','1') #create informat: <node ID="1"> </node> 

radius = root.createElement('radius')
radius.appendChild(root.createTextNode(str(nodeRadius))) #create informat: <radius>25</radius>

xLocation = root.createElement('xLocation')
xLocation.appendChild(root.createTextNode(str(nodeXLocation)))

yLocation = root.createElement('yLocation')
yLocation.appendChild(root.createTextNode(str(nodeYLocation)))

Zvalue = root.createElement('setZValue')
Zvalue.appendChild(root.createTextNode(str(setZValue)))

node.appendChild(radius)
node.appendChild(xLocation)
node.appendChild(yLocation)
node.appendChild(Zvalue)

#node2
node1 = root.createElement('node')
node1.setAttribute('ID','2') 

radius1 = root.createElement('radius')
radius1.appendChild(root.createTextNode('5')) 

xLocation1 = root.createElement('xLocation')
xLocation1.appendChild(root.createTextNode('2'))

yLocation1 = root.createElement('yLocation')
yLocation1.appendChild(root.createTextNode('2'))

Zvalue1 = root.createElement('setZValue')
Zvalue1.appendChild(root.createTextNode('2'))

node1.appendChild(radius1)
node1.appendChild(xLocation1)
node1.appendChild(yLocation1)
node1.appendChild(Zvalue1)
#arc Class
#arc data
arc=root.createElement('arc')
arc.setAttribute('ID','1')

x1 = root.createElement('x1')
x2 = root.createElement('x2')
y1 = root.createElement('y1')
y2 = root.createElement('y2')

x1.appendChild(root.createTextNode('0'))
x2.appendChild(root.createTextNode('0'))
y1.appendChild(root.createTextNode('20'))
y2.appendChild(root.createTextNode('20'))

arc.appendChild(x1)
arc.appendChild(x2)
arc.appendChild(y1)
arc.appendChild(y2)

#appending the childnode
nodeClass.appendChild(node)
nodeClass.appendChild(node1)
arcClass.appendChild(arc)

Class.appendChild(nodeClass)
Class.appendChild(arcClass)
root.appendChild(Class)


with open("tempf1.xml", "w") as f:
    root.writexml(f, indent ="\t", newl="\n",encoding='UTF-8') 



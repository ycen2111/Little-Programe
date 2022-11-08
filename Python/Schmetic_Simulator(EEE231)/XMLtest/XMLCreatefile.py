#! /usr/bin/env python3
import xml.dom.minidom as minidom
  
NodeIDcount = 44 #incrimented by creating a new node
ArcIDcount = 21 #incremented by creating a new arc
root = minidom.Document()
  
gui = root.createElement('GUI') 
root.appendChild(gui)

node = root.createElement('Node')
node.setAttribute('NodeID', str(NodeIDcount))
gui.appendChild(node)

arc = root.createElement('Arc')
arc.setAttribute('ArcID', str(ArcIDcount))
gui.appendChild(arc)




  
  
with open("tempf.xml", "w") as f:
    root.writexml(f, indent ="\t", newl="\n",encoding='UTF-8') 
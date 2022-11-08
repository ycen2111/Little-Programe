#! /usr/bin/env python3
'''
fileName = QFileDialog.getOpenFileName(self, "Open File", ".", ("*.md"))
fileName[0] will be the path of the opened file

Please run the createXMLtest.py first.
'''
# use the parse() function to load and parse an XML file
    # *THE PATH IS DEPEND ON THE USER, we need to let the user import the path.
import xml.dom.minidom
import os 

def main():
    #data:
    nodeID=[]
    nx=[]
    ny=[]
    nr=[]
    nz=[]

    arcID =[]
    ax1=[]
    ax2=[]
    ay1=[]
    ay2=[]
    #start loading XML
    path="tempf1.xml" #change to fileName
    doc = xml.dom.minidom.parse(path)
    Class = doc.documentElement
    nodes= Class.getElementsByTagName('node')
    arcs= Class.getElementsByTagName('arc')
    for node in nodes:
        print("---node---")
        if node.hasAttribute('ID'):
            #create the new node
            nodeID.append(int(node.getAttribute('ID')))
            print("ID: {}".format(node.getAttribute('ID')))#getting ID
        nx.append(int(node.getElementsByTagName('xLocation')[0].childNodes[0].data))
        ny.append(int(node.getElementsByTagName('yLocation')[0].childNodes[0].data))
        nr.append(int(node.getElementsByTagName('radius')[0].childNodes[0].data))
        nz.append(int(node.getElementsByTagName('setZValue')[0].childNodes[0].data))
        print("x: {}".format(node.getElementsByTagName('xLocation')[0].childNodes[0].data))
        print("y: {}".format(node.getElementsByTagName('yLocation')[0].childNodes[0].data))
        print("R: {}".format(node.getElementsByTagName('radius')[0].childNodes[0].data))# data in string
        print("Z: {}".format(node.getElementsByTagName('setZValue')[0].childNodes[0].data))# data in string
        x=int(node.getElementsByTagName('xLocation')[0].childNodes[0].data) #transfer the data to int
        print('Test for changing to int')
        print('x =',x)
        z=x+10 #test for int data
        print('x+10=',z)
    print("------------------------")
    for arc in arcs:
        print("---arc---")
        if arc.hasAttribute('ID'):
            #create the new node
            arcID.append(int(arc.getAttribute('ID')))
            print("ID: {}".format(arc.getAttribute('ID')))#getting ID

        ax1.append(int(arc.getElementsByTagName('x1')[0].childNodes[0].data))
        ax2.append(int(arc.getElementsByTagName('x2')[0].childNodes[0].data))
        ay1.append(int(arc.getElementsByTagName('y1')[0].childNodes[0].data))
        ay2.append(int(arc.getElementsByTagName('y2')[0].childNodes[0].data))
        print("x1: {}".format(arc.getElementsByTagName('x1')[0].childNodes[0].data))
        print("x2: {}".format(arc.getElementsByTagName('x2')[0].childNodes[0].data))
        print("y1: {}".format(arc.getElementsByTagName('y1')[0].childNodes[0].data))
        print("y2: {}".format(arc.getElementsByTagName('y2')[0].childNodes[0].data))
    print("------data-------")
    print(nodeID)
    print(nx)
    print(ny)
    print(nr)
    print(nz)
    print(nt)
    print("------------------")
    print(arcID)
    print(ax1)
    print(ax2)
    print(ay1)
    print(ay2)
'''
loading format: <Beta>123</Beta>
    A.getElementsByTagName('Beta')[0].childNodes[0].data
from the node named B in class A, the the data inside

loading format: <Beta C=1> </Beta>
    Beta.getAttribute('C')
In class(or node?) Beta, get the value that C=
*these get the value in string, need to convert them by int()

    '''

if __name__ == "__main__":
    main()
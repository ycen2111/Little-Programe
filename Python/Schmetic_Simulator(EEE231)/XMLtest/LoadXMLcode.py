'''
Please run the create_in_Loop.py first.
'''
import xml.dom.minidom as minidom
import os 
#golbal data
lastNodeID=0
lastArcID =0
#Radius=0
#qcolourR=0
#qcolourG=0
#qcolourB=0
#node para
nodeID      = [] 
X           = []
Y           = []
Radius      = [] 
Text        = []
nodeUnit    = []
Value       = []

#arc para
arcID       = []
StartNodeID = []  
add        = []  
EndNodeID   = [] 
arcUnit     = []
Resis       = [] 
'''
groundX =0
groundY =0
'''
#function of loading XML
def main():
    '''
    fileName = QFileDialog.getOpenFileName(self, "Open File", ".", ("*.xml"))
    print("opening ", fileName[0])
    path    =fileName[0] #need to change the path to run
    '''
    path    ="/home/brianwu/GIT/EEE231-group-A/test.xml" #need to change the path to run
    doc     = minidom.parse(path)
    Class   = doc.documentElement

    nodes   = Class.getElementsByTagName('node')
    arcs    = Class.getElementsByTagName('arc')
        #code between the comment works, but could be improve.
    LNID    = Class.getElementsByTagName('lastNodeID')
    LAID    = Class.getElementsByTagName('lastArcID')
    '''
    ground  =Class.getElementsByTagName('groundNode')
    for items in ground:
        groundX=int(items.getElementsByTagName('gnx')[0]      .childNodes[0].data)
        groundY=int(items.getElementsByTagName('gny')[0]      .childNodes[0].data)
    Rad  = Class.getElementsByTagName('Radius')
    QCR= Class.getElementsByTagName('qcolourR')
    QCG= Class.getElementsByTagName('qcolourG')
    QCB= Class.getElementsByTagName('qcolourB')
    '''
    for data in LNID:
        lastNodeID  = (data.getAttribute('lastNodeID'))
    for data in LAID:
        lastArcID   = (data.getAttribute('lastArcID'))
        '''
    for data in Rad:
        Radius   = (data.getAttribute('Radius'))
    for data in QCR:
        qcolourR   = (data.getAttribute('qcolourR'))
    for data in QCG:
        qcolourG   = (data.getAttribute('qcolourG'))
    for data in QCB:
        qcolourB   = (data.getAttribute('qcolourB'))
        '''
        #code between the comment works, but could be improve
    #append the node data to their list
    for node in nodes:
        nodeID  .append(int(node.getAttribute('ID')))
        X       .append(int(node.getElementsByTagName('nX')[0]      .childNodes[0].data))
        Y       .append(int(node.getElementsByTagName('nY')[0]      .childNodes[0].data))
        Radius  .append(int(node.getElementsByTagName('nRadius')[0] .childNodes[0].data))
        nodeUnit.append(    node.getElementsByTagName('nUnit')[0]   .childNodes[0].data)
        Value   .append(int(node.getElementsByTagName('nValue')[0]  .childNodes[0].data))
    #append the arc data to their list
    for arc in arcs:
        arcID       .append(int(arc.getAttribute('ID')))
        StartNodeID .append(int(arc.getElementsByTagName('aStartNodeID')[0] .childNodes[0].data))
        add         .append(int(arc.getElementsByTagName('aAdd')[0]         .childNodes[0].data))
        EndNodeID   .append(int(arc.getElementsByTagName('aEndNodeID')[0]   .childNodes[0].data))
        arcUnit     .append(    arc.getElementsByTagName('aUnit')[0]        .childNodes[0].data)
        Resis       .append(int(arc.getElementsByTagName('aResis')[0]       .childNodes[0].data))
    #beetween the ''' are for testing, no need to copy to the main program
    #remove the ''' to test.
    '''
    print('groundX = ',groundX)
    print('groundY = ',groundY)
    
    print("------data-------")
    print("------global-------")
    print('lastNodeID = ',lastNodeID)
    print('lastArcID  = ', lastArcID)
    print("------node-------")
    print('nodeID   =',nodeID)
    print('X        =',X)
    print('Y        =',Y)
    print('Radius   =',Radius)
    print('Text     =',Text)
    print('nodeUnit =',nodeUnit)
    print('Value    =',Value)
    print("-------ARC-------")
    print('arcID        =',arcID)
    print('StartNodeID  =',StartNodeID)
    print('MidX         =',MidX)
    print('MidY         =',MidY)
    print('EndNodeID    =',EndNodeID)
    print('arcUnit      =',arcUnit)
    print('Resis        =',Resis)
    '''
if __name__ == "__main__":
    main()
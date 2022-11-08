###Only the contents of functions are useful, change the node dictionary name and the arc dictionary name to
###the corresponding names when using them

#"node"+ID:[ID,x,y,radius,unit,value]
#"arc"+ID:[ID,startNodeID,add,endNodeID,unit,resistance]
DictOfArc = [[0, 25, 30, 2, 3, 5], [3, 70, 40, 20, 40, 50]] #anything, the arc dictionary
DictOfNode = [[0, 25, 30, 2, 3, 5], [3, 70, 40, 20, 40, 50]] #anything, the node dictionary
#list of node        Change according to demand
nodeID = []
X = []
Y = []
Radius = []
nodeUnit = []
Value = []

#list of arc         Change according to demand
arcID = []
StartNodeID = []
add = []
EndNodeID = []
arcUnit = []
Resis = []

#################################function#################################
def read_dict_from_gui():
    saving = [] #list used to read the dict
    #name_of_arc_dist = DictOfArc  ###Delete # on official use###
    #read arc from dict
    for n in range(6):
        for item in DictOfArc:
            #saving = name_of_arc_dict.get(item)
            saving = item
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
    # name_of_node_dist = DictOfNode  ###Delete # on official use###
    for n in range(6):
        for item in DictOfNode:
            #print(scores.get(item)) #test
            #saving=name_of_node_dict.get(item)
            saving = item
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
    return
#################################function#################################
read_dict_from_gui()
print(nodeID)
print(Radius)
print(Value)
print(arcID)
print(EndNodeID)

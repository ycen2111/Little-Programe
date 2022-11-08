###Only the contents of functions are useful, change the node dictionary name and the arc dictionary name to
###the corresponding names when using them
#"node"+ID:[ID,x,y,radius,unit,value]
#"arc"+ID:[ID,startNodeID,add,endNodeID,unit,resistance]
DictOfNode = [[0, 1, 5, 331, 50, 10, 1000], [1, 2, 10, 332, 100, 20, 2000]]
DictOfArc = [[0, 1, 5, 331, 50, 10, 1000], [1, 2, 10, 332, 100, 20, 2000]]
ItemList.dict = {}
#list of node        Change according to demand
nodeID = [0, 3]
X = [5, 6]
Y = [5, 6]
Radius = [5, 6]
nodeUnit = [5, 6]
Value = [5, 6]
#list of arc         Change according to demand
arcID = [0, 1]
StartNodeID = [5, 6]
add = [5, 6]
EndNodeID = [5, 6]
arcUnit = [5, 6]
Resis = [5, 6]
########################### function ##########################################
def read_lists_from_xml():
    n = 0
    for nodeID_item in nodeID:
        ItemList.dict['node' + str(nodeID_item)] = [nodeID[n], X[n], Y[n], Radius[n], nodeUnit[n], Value[n]]
        n = n + 1
    n = 0
    for arcID_item in arcID:
        ItemList.dict['arc' + str(arcID_item)] = [arcID[n], StartNodeID[n], add[n], EndNodeID[n], arcUnit[n], Resis[n]]
        n = n + 1
    return
############################ function ##########################################
read_lists_from_xml()

print(ItemList.dict)

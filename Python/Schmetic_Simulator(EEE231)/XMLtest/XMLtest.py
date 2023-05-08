#! /usr/bin/env python3
from xml.dom import minidom

def main():
    # use the parse() function to load and parse an XML file
    # *THE PATH IS DEPEND ON THE USER, we need to let the user import the path.
    path="/home/brianwu/GIT/EEE231-group-A/XMLtest/Myxml.xml"
    doc = minidom.parse(path)
    
    # print out the document node and the name of the first child tag
    print (doc.nodeName)
    print (doc.firstChild.tagName)
     # get a list of XML tags from the document and print each one
    expertise = doc.getElementsByTagName("expertise")
    print ("%d expertise:" % expertise.length)
    for skill in expertise:
        print (skill.getAttribute("name"))

    # create a new XML tag and add it into the document
    newexpertise = doc.createElement("expertise")
    newexpertise.setAttribute("name", "BigData")
    doc.firstChild.appendChild(newexpertise)
    print (" ")

    expertise = doc.getElementsByTagName("expertise")
    print ("%d expertise:" % expertise.length)
    for skill in expertise:
        print (skill.getAttribute("name"))
 
if __name__ == "__main__":
    main()
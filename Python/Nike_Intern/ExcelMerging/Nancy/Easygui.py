import easygui as gui #import gui lib

gui.msgbox("Hello, world!")
gui.msgbox(msg="Message",title="Title",ok_button="Yes Button") #open a message box

cc=gui.ccbox(msg="Message",title="Title") #continue-cancle box return True False
if cc: pass
else: sys.exit(0)

yn=gui.ynbox(msg="Message",title="Title") #yes-no box return True False

choice=gui.choicebox(msg="Message",title="Title",choices=["option A","option B","option C"]) #choice box return string

button=gui.buttonbox(msg="Message",title="Title",choices=["Button A","Butotn B","Button C"])#button box return string

index=gui.indexbox(msg="Message",title="Title",choices=["Button A","Butotn B","Button C"])

#file_address = gui.fileopenbox('open file', 'C:/User/Administrator/Desktop/__pycache__')
#print(file_address)
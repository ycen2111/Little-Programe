#保存鼠标位置和事件状态
mouse_x = 0
mouse_y = 0
button_right = False
button_left = False
last_mouse_x = 0
last_mouse_y = 0

def save_coordinary(coordinary):
    global last_mouse_x, last_mouse_y, mouse_x, mouse_y
    last_mouse_x = mouse_x
    last_mouse_y = mouse_y
    mouse_x = coordinary[0]
    mouse_y = coordinary[1]

def get_moving_distance():
    global last_mouse_x, last_mouse_y, mouse_x, mouse_y
    return [mouse_x - last_mouse_x, mouse_y - last_mouse_y]

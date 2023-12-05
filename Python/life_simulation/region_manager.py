import parameter as data
#记录元素位置和大小，判断当前鼠标所在的是哪个区域

WIDTH = data.ROWS * (data.GRID_SIZE + data.GRID_GAP) + data.GRID_GAP
GRID_HEIGHT = data.COLS * (data.GRID_SIZE + data.GRID_GAP) + data.GRID_GAP
MENU_HEIGHT = 30
HEIGHT = GRID_HEIGHT + MENU_HEIGHT
#(start x, start y, stop x, stop y)
MENU_AREA = (0,0,WIDTH,MENU_HEIGHT)
GRID_AREA = (0,MENU_HEIGHT,WIDTH,HEIGHT)

def get_menu_start_point():
    return (0,0)

def get_menu_size():
    return (WIDTH,MENU_HEIGHT)

def get_grid_start_point():
    return (0,MENU_HEIGHT)

def get_grid_size():
    return (WIDTH,GRID_HEIGHT)

def in_grid_region(position):
    x = position[0]
    y = position[1]

    if (x > 0 and x < WIDTH and y > MENU_HEIGHT and y < HEIGHT):
        return True
    else:
        return False
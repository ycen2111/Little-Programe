#初始界面参数
GRID_SIZE = 5
GRID_GAP = 1
ROWS = 80
COLS = 80

#初始资源参数
MATERIAL_NUM = 10
ENERGY = 1

#颜色定义
BLACK = (0,0,0)
WHITE = (255,255,255)
GREY = (100,100,100)
DELIGHT_GREY = (150,150,150)
LIGHT_GREY = (200,200,200)
RED = (255,0,0)
GREEN = (0,255,0)
BLUE = (0,0,255)

def set_grid_size(new_size):
    global GRID_SIZE
    GRID_SIZE = new_size

def set_grid_gap(new_gap):
    global GRID_GAP
    GRID_GAP = new_gap
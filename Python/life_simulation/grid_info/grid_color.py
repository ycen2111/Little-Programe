#控制方格的颜色

#颜色定义
BLACK = (0,0,0)
WHITE = (255,255,255)
GREY = (100,100,100)
LIGHT_GREY = (200,200,200)
RED = (255,0,0)
GREEN = (0,255,0)
BLUE = (0,0,255)

#记录方格颜色
color = []

#初始化方格颜色
def init(cols, rows):
    global color
    color = [[GREY for _ in range(cols)] for _ in range(rows)]

#根据矩阵坐标来回切换颜色
def switch_color(grid_y, grid_x, switched_color):
    if (color[grid_y][grid_x] == switched_color):
        color[grid_y][grid_x] = GREY
    else:
        color[grid_y][grid_x] = switched_color
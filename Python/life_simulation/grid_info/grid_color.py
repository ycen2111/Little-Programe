import config
#控制方格的颜色

#记录方格颜色
color = []

#初始化方格颜色
def init(cols, rows):
    global color
    color = [[config.GREY for _ in range(cols)] for _ in range(rows)]

#根据矩阵坐标来回切换颜色
def switch_color(grid_y, grid_x, switched_color):
    if (color[grid_y][grid_x] == switched_color):
        color[grid_y][grid_x] = config.GREY
    else:
        color[grid_y][grid_x] = switched_color
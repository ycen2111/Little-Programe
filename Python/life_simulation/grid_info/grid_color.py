import config
#控制方格的颜色

#记录方格颜色
color = []

#初始化方格颜色
def init(cols, rows):
    global color
    color = [[config.GREY for _ in range(cols)] for _ in range(rows)]

#更改颜色
def change_color(grid_x, grid_y, switched_color):
    color[grid_x][grid_y] = switched_color
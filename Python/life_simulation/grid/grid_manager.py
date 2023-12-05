import pygame as game

#方格初始设置
GRID_SIZE = 5
GRID_GAP = 1
ROWS = 80
COLS = 80

BLACK = (0,0,0)
WHITE = (255,255,255)
GREY = (100,100,100)
RED = (255,0,0)
GREEN = (0,255,0)
BLUE = (0,0,255)

#记录方格颜色
grid_color = []
#记录方格位置和尺寸
grid_rects = []

#初始化矩阵，定义方格颜色和位置
def init():
    global grid_color, grid_rects
    #初始化方格颜色
    grid_color = [[GREY for _ in range(COLS)] for _ in range(ROWS)]

    #方格的位置和尺寸定义
    for y in range(ROWS):
        row = []
        for x in range(COLS):
            #pygame.Rect(x_position, y_position, width, length)
            rect = game.Rect(x * (GRID_SIZE + GRID_GAP) + GRID_GAP, y * (GRID_SIZE + GRID_GAP) + GRID_GAP, GRID_SIZE, GRID_SIZE)
            row.append(rect)
        grid_rects.append(row)

#平移矩阵
def move(shift_x, shift_y):
    for row in grid_rects:
        for rect in row:
            rect.x += shift_x
            rect.y += shift_y

#点击方格变色
def click_grid():
    #读取鼠标位置
    coordinary = game.mouse.get_pos()
    #计算方格坐标
    grid_y = int (abs(coordinary[1] - grid_rects[0][0].y) / (GRID_SIZE + GRID_GAP))
    grid_x = int (abs(coordinary[0] - grid_rects[0][0].x) / (GRID_SIZE + GRID_GAP))
    #检查是否点击了方格
    if (grid_x < ROWS and grid_y < COLS):
        #切换颜色
        if (grid_color[grid_y][grid_x] == GREY):
            grid_color[grid_y][grid_x] = GREEN
        else:
            grid_color[grid_y][grid_x] = GREY

#以鼠标坐标为中心放大方格
def zoom_in(zoom_size):
    global GRID_SIZE, GRID_GAP
    #加边长
    GRID_SIZE += zoom_size
    #计算新边距
    new_grid_gap = int (GRID_SIZE/5)
    gap_diff = new_grid_gap - GRID_GAP
    GRID_GAP = new_grid_gap

    total_diff = zoom_size + gap_diff
    #读取鼠标位置，计算偏移量
    coordinary = game.mouse.get_pos()
    dx = int (abs(coordinary[1] - grid_rects[0][0].y) / (GRID_SIZE + GRID_GAP)) * total_diff #计算鼠标当前坐标离最左上角的方格隔了多少行列，这些行列增加的距离就是偏移量
    dy = int (abs(coordinary[0] - grid_rects[0][0].x)/ (GRID_SIZE + GRID_GAP)) * total_diff
    #移动方格位置
    for x, row in enumerate(grid_rects):
        for y, rect in enumerate(row):
            rect.width = GRID_SIZE
            rect.height = GRID_SIZE
            rect.x += y * total_diff - dx
            rect.y += x * total_diff - dy

#以鼠标坐标为中心缩小方格
def zoom_out(zoom_size):
    global GRID_SIZE, GRID_GAP
    #只有 GRID_SIZE - zoom_size > 0才能继续
    if (GRID_SIZE > zoom_size):
        #减边长
        GRID_SIZE -= zoom_size
        #计算新边距
        new_grid_gap = int (GRID_SIZE/5)
        gap_diff = GRID_GAP - new_grid_gap
        GRID_GAP = new_grid_gap

        total_diff = zoom_size + gap_diff
        #读取鼠标位置，计算偏移量
        coordinary = game.mouse.get_pos()
        dx = int (abs(coordinary[1] - grid_rects[0][0].x) / (GRID_SIZE + GRID_GAP)) * total_diff #计算鼠标当前坐标离最左上角的方格隔了多少行列，这些行列增加的距离就是偏移量
        dy = int (abs(coordinary[0] - grid_rects[0][0].y)/ (GRID_SIZE + GRID_GAP)) * total_diff
        #移动方格位置
        for x, row in enumerate(grid_rects):
            for y, rect in enumerate(row):
                rect.width = GRID_SIZE
                rect.height = GRID_SIZE
                rect.x -= y * total_diff - int (coordinary[1] * 0) - dx
                rect.y -= x * total_diff - int (coordinary[0] * 0) - dy
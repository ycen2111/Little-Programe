import pygame as game
import config
import region_manager as region
#控制方格位置坐标

#记录方格位置和尺寸
grid_rects = []

#方格的位置和尺寸定义
def init(cols, rows):
    for y in range(rows):
        row = []
        for x in range(cols):
            #pygame.Rect(x_position, y_position, width, length)
            rect = game.Rect(x * (config.GRID_SIZE + config.GRID_GAP) + config.GRID_GAP, y * (config.GRID_SIZE + config.GRID_GAP) + config.GRID_GAP, config.GRID_SIZE, config.GRID_SIZE)
            row.append(rect)
        grid_rects.append(row)

#平移矩阵
def move(shift_x, shift_y):
    for row in grid_rects:
        for rect in row:
            rect.x += shift_x
            rect.y += shift_y

#查找鼠标位置的方格的坐标
def find_grid():
    #读取鼠标位置
    coordinary = game.mouse.get_pos()
    #消除鼠标坐标和方格矩阵坐标之间的偏移量
    grid_start_point = region.get_grid_start_point()
    mouse_y = coordinary[1] - grid_start_point[1]
    mouse_x = coordinary[0] - grid_start_point[0]
    #计算方格坐标
    cell_unit = config.GRID_SIZE + config.GRID_GAP
    grid_y = int (abs(mouse_y - grid_rects[0][0].y) / cell_unit)
    grid_x = int (abs(mouse_x - grid_rects[0][0].x) / cell_unit)
    
    return grid_x, grid_y

#以鼠标坐标为中心放大方格
def zoom_in(zoom_size):
    #加边长
    config.set_grid_size(config.GRID_SIZE + zoom_size)
    #计算新边距
    new_grid_gap = int (config.GRID_SIZE/5)
    gap_diff = new_grid_gap - config.GRID_GAP
    config.set_grid_gap(new_grid_gap)

    total_diff = zoom_size + gap_diff
    #读取鼠标位置，计算偏移量
    coordinary = game.mouse.get_pos()
    dx = int (abs(coordinary[1] - grid_rects[0][0].y) / (config.GRID_SIZE + config.GRID_GAP)) * total_diff #计算鼠标当前坐标离最左上角的方格隔了多少行列，这些行列增加的距离就是偏移量
    dy = int (abs(coordinary[0] - grid_rects[0][0].x)/ (config.GRID_SIZE + config.GRID_GAP)) * total_diff
    #移动方格位置
    for x, row in enumerate(grid_rects):
        for y, rect in enumerate(row):
            rect.width = config.GRID_SIZE
            rect.height = config.GRID_SIZE
            rect.x += y * total_diff - dx
            rect.y += x * total_diff - dy

#以鼠标坐标为中心缩小方格
def zoom_out(zoom_size):
    #只有 GRID_SIZE - zoom_size > 0才能继续
    if (config.GRID_SIZE > zoom_size):
        #减边长
        config.set_grid_size(config.GRID_SIZE - zoom_size)
        #计算新边距
        new_grid_gap = int (config.GRID_SIZE/5)
        gap_diff = config.GRID_GAP - new_grid_gap
        config.set_grid_gap(new_grid_gap)

        total_diff = zoom_size + gap_diff
        #读取鼠标位置，计算偏移量
        coordinary = game.mouse.get_pos()
        dx = int (abs(coordinary[1] - grid_rects[0][0].x) / (config.GRID_SIZE + config.GRID_GAP)) * total_diff #计算鼠标当前坐标离最左上角的方格隔了多少行列，这些行列增加的距离就是偏移量
        dy = int (abs(coordinary[0] - grid_rects[0][0].y)/ (config.GRID_SIZE + config.GRID_GAP)) * total_diff
        #移动方格位置
        for x, row in enumerate(grid_rects):
            for y, rect in enumerate(row):
                rect.width = config.GRID_SIZE
                rect.height = config.GRID_SIZE
                rect.x -= y * total_diff - int (coordinary[1] * 0) - dx
                rect.y -= x * total_diff - int (coordinary[0] * 0) - dy
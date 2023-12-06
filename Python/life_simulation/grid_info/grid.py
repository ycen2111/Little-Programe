import config
import grid_info.grid_color as grid_color
import grid_info.grid_coordinary as grid_coordinary

import game_data as data

#初始化所有方格数据
def init():
    #初始化方格颜色
    grid_color.init(config.COLS, config.ROWS)
    #方格的位置和尺寸定义
    grid_coordinary.init(config.COLS, config.ROWS)

#点击方格变色设置生命
def click_grid():
    #查找方格坐标
    grid_y, grid_x = grid_coordinary.find_grid()
    #检查坐标是否有效
    if (grid_x < config.ROWS and grid_y < config.COLS):
        if (not data.is_cell_alive(grid_x, grid_y)): #如果坐标没有存活cell
            #记录这个坐标
            data.add_lived_cell(grid_x, grid_y)
            print("1 cell added in (" + str(grid_x) + ", " + str(grid_y) + "), " + str(data.count_alive_cell()) + " total alive")
        else:
            #去除这个坐标
            data.remove_lived_cell(grid_x, grid_y)
            print("1 cell removed in (" + str(grid_x) + ", " + str(grid_y) + "), " + str(data.count_alive_cell()) + " total alive")

#返回单个方格颜色
def get_grid_color(x, y):
    return grid_color.color[x][y]

#返回所有方格坐标数据
def get_grid_coordinary():
    return grid_coordinary.grid_rects
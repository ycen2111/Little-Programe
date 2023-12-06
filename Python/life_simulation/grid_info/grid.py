import config
import grid_info.grid_color as grid_color
import grid_info.grid_coordinary as grid_coordinary

#初始化所有方格数据
def init():
    #初始化方格颜色
    grid_color.init(config.COLS, config.ROWS)
    #方格的位置和尺寸定义
    grid_coordinary.init(config.COLS, config.ROWS)

#点击方格变色
def click_grid():
    #查找方格坐标
    grid_y, grid_x = grid_coordinary.find_grid()
    #检查坐标是否有效
    if (grid_x < config.ROWS and grid_y < config.COLS):
        #切换颜色
        grid_color.switch_color(grid_y, grid_x, config.GREEN)

#返回单个方格颜色
def get_grid_color(y, x):
    return grid_color.color[y][x]

#返回所有方格坐标数据
def get_grid_coordinary():
    return grid_coordinary.grid_rects
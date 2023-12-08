import config
import grid_info.grid_color as color
import my_class.cell as cell
#用字典记录所有游戏数据

#资源dict
material = {}
#存活方格cell列表
lived_cell = {}

#初始化游戏资源
def init():
    global material
    #设置初始资源数量
    for y in range(config.COLS):
        for x in range(config.ROWS):
            material[x, y] = config.MATERIAL_NUM

#查询方格是否存活
def is_cell_alive(x, y):
    global lived_cell
    return (x, y) in lived_cell

#查询有多少方格存活
def count_alive_cell():
    global lived_cell
    return len(lived_cell)

#增加存活方格
def add_lived_cell(x, y, code_id, start_step):
    global lived_cell
    lived_cell[x, y] = cell.Cell(x, y, code_id, start_step)
    color.change_color(x, y, config.GREEN)

#去除存活方格
def remove_lived_cell(x, y):
    global lived_cell
    del lived_cell[x, y]
    color.change_color(x, y, config.GREY)

#返回String格式信息
def get_string_info(x, y):
    global material, lived_cell
    info = []
    info.append("X: " + str(x) + "  Y: " + str(y))
    info.append("Env_mate: " + str(material[x, y]))
    info.append("Code_id: " + str(lived_cell[x, y].code_id))
    info.append("Curr_step: " + str(lived_cell[x, y].curr_step))
    info.append("Age: " + str(lived_cell[x, y].age))
    info.append("Own_mate: " + str(lived_cell[x, y].material))
    info.append("Own_ener: " + str(lived_cell[x, y].energy))
    return info
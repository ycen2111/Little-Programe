import sys
import pygame as game
import config
import region_manager as region
import mouse_condition as mouse
import my_class.button as button
import my_class.text as text
import my_class.timer as timer
import grid_info.grid as grid

import my_data.cell_data as data
import my_logic.main as sim

#游戏初始化
game.init()
is_running = True

#界面大小设置
TITLE = "Test"
game.display.set_caption(TITLE)
screen = game.display.set_mode((region.WIDTH,region.HEIGHT))
#目录区域设置
menu_surface = game.Surface(region.get_menu_size())
menu_surface.fill(config.LIGHT_GREY)
#用button类设置界面按钮
start_button = button.Button(region.get_menu_start_point(), (100, region.MENU_HEIGHT), "START", config.DELIGHT_GREY, menu_surface)
start_button.draw()
#方格区域设置
grid_surface = game.Surface(region.get_grid_size())
grid_surface.fill(config.LIGHT_GREY)
#鼠标信息区域设置
is_info_show = False
info_surface = game.Surface(region.get_info_size())
info_surface.fill(config.LIGHT_GREY)
info_name = ["coordinary", "env_mate", "code_id", "curr_step", "age", "own_mate", "own_ener"] #显示列表
info_list = []
info_coordinary = [0, 0] #
for i, name in enumerate(info_name):
    info_list.append(text.Text(name, config.BLACK, 16, (region.INFO_WIDTH/2, 10 * i + 8)))

#初始化方格颜色和数量
grid.init()
#初始化存储单元
data.init()

#实例化计时器
stay_timer = timer.Timer(0.5) #0.5s后完成

#开始
while is_running:
    #设置界面底色
    grid_surface.fill(config.LIGHT_GREY)
    #记录鼠标位置
    mouse_pos = game.mouse.get_pos()
    #计算鼠标指向的方格位置
    coordinary_x, coordinary_y = grid.find_grid()
    #更新计时器数据
    stay_timer.update()

    #监控用户事件
    for event in game.event.get():
        #关闭按钮
        if event.type == game.QUIT:
            is_running = False
        
        #其他事件
        else:
            is_info_show = False
            #检测到鼠标按下或滚轮变化
            if (event.type == game.MOUSEBUTTONDOWN):
                #左键
                if (event.button == 1):
                    mouse.button_left = True
                    #点击在grid_region区域
                    if (region.in_grid_region(mouse_pos)):
                        grid.click_grid()
                    #点击在start按钮
                    elif (start_button.on_button()):
                        if (start_button.get_text() == "START"):
                            #结束模拟
                            start_button.change_text("STOP")
                            sim.stop_sim()
                        else:
                            #开始模拟
                            start_button.change_text("START")
                            sim.start_sim()
                #右键
                elif (event.button == 3):
                    mouse.button_right = True
                #滚轮向上
                elif (event.button == 4 and region.in_grid_region(mouse_pos)):
                    #放大
                    grid.grid_coordinary.zoom_in(2)
                #滚轮向下
                elif (event.button == 5 and region.in_grid_region(mouse_pos)):
                    #缩小
                    grid.grid_coordinary.zoom_out(2)

            #检测到鼠标松开
            elif (event.type == game.MOUSEBUTTONUP and region.in_grid_region(mouse_pos)):
                mouse.button_left = False
                mouse.button_right = False

            #检测到鼠标移动
            if (event.type == game.MOUSEMOTION):
                stay_timer.reset_start_time()
                mouse.save_coordinary(mouse_pos)
                #鼠标在grid范围内
                if (region.in_grid_region(mouse_pos)):
                    #鼠标右击
                    if (mouse.button_right):
                        #得到鼠标移动距离
                        distance = mouse.get_moving_distance()
                        #移动方格位置
                        grid.grid_coordinary.move(distance[0], distance[1])

                #鼠标在start按钮上
                if (start_button.on_button()):
                    start_button.change_color(config.WHITE)
                else:
                    start_button.change_color(config.DELIGHT_GREY)

    #鼠标静止1s，并且鼠标指向存活的cell
    if (stay_timer.is_finish and not is_info_show):
        #查询当前方格信息
        if (data.is_cell_alive(coordinary_x, coordinary_y)):
            is_info_show = True
            info_coordinary = [coordinary_x, coordinary_y]
    
    #将方格画到界面上
    for y, row in enumerate(grid.get_grid_coordinary()):
        for x, rect in enumerate(row):
            game.draw.rect(grid_surface, grid.get_grid_color(x, y), rect)
    screen.blit(menu_surface, region.get_menu_start_point())
    screen.blit(grid_surface, region.get_grid_start_point())
    if (is_info_show): #方格信息栏显示
        screen.blit(info_surface, mouse_pos) #绘画底色
        cell_info_list = data.get_string_info(info_coordinary[0], info_coordinary[1])
        for i, info_text in enumerate(info_list):
            info_text.change_text(cell_info_list[i])
            info_text.change_pos(mouse_pos)
            info_text.draw(screen)

    #刷新屏幕
    game.display.flip()

#卸载模块
game.quit()
#结束程序
sys.exit()
import sys
import pygame as game
import config
import region_manager as region
import mouse_condition as mouse
import my_class.button as button
import grid_info.grid as grid

import game_data as data
import my_logic.main as sim

#游戏初始化
game.init()
is_running = True

#界面大小设置
TITLE = "Test"
game.display.set_caption(TITLE)
screen = game.display.set_mode((region.WIDTH,region.HEIGHT))
#界面区域设置
menu_surface = game.Surface(region.get_menu_size())
menu_surface.fill(config.LIGHT_GREY)
grid_surface = game.Surface(region.get_grid_size())
grid_surface.fill(config.LIGHT_GREY)
#用button类设置界面按钮
start_button = button.Button(region.get_menu_start_point(), (100, region.MENU_HEIGHT), "START", config.DELIGHT_GREY, menu_surface)
start_button.draw()

#初始化方格颜色和数量
grid.init()
#初始化存储单元
data.init()

#开始
while is_running:
    #设置界面底色
    grid_surface.fill(config.LIGHT_GREY)

    #监控用户事件
    for event in game.event.get():
        #关闭按钮
        if event.type == game.QUIT:
            is_running = False
        
        #其他事件
        else:
            #检测到鼠标按下或滚轮变化
            if (event.type == game.MOUSEBUTTONDOWN):
                #左键
                if (event.button == 1):
                    mouse.button_left = True
                    #点击在grid_region区域
                    if (region.in_grid_region(game.mouse.get_pos())):
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
                elif (event.button == 4 and region.in_grid_region(game.mouse.get_pos())):
                    #放大
                    grid.grid_coordinary.zoom_in(2)
                #滚轮向下
                elif (event.button == 5 and region.in_grid_region(game.mouse.get_pos())):
                    #缩小
                    grid.grid_coordinary.zoom_out(2)

            #检测到鼠标松开
            elif (event.type == game.MOUSEBUTTONUP and region.in_grid_region(game.mouse.get_pos())):
                mouse.button_left = False
                mouse.button_right = False

            #检测到鼠标移动
            if (event.type == game.MOUSEMOTION):
                mouse.save_coordinary(game.mouse.get_pos())
                #右键按住移动
                if (mouse.button_right and region.in_grid_region(game.mouse.get_pos())):
                    #得到鼠标移动距离
                    distance = mouse.get_moving_distance()
                    #移动方格位置
                    grid.grid_coordinary.move(distance[0], distance[1])
                #鼠标在start按钮上
                if (start_button.on_button()):
                    start_button.change_color(config.WHITE)
                else:
                    start_button.change_color(config.DELIGHT_GREY)
    
    #将方格画到界面上
    for x, row in enumerate(grid.get_grid_coordinary()):
        for y, rect in enumerate(row):
            game.draw.rect(grid_surface, grid.get_grid_color(x, y), rect)
    screen.blit(menu_surface, region.get_menu_start_point())
    screen.blit(grid_surface, region.get_grid_start_point())

    #刷新屏幕
    game.display.flip()

#卸载模块
game.quit()
#结束程序
sys.exit()
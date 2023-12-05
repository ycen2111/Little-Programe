import sys
import pygame as game
import mouse_conition as mouse
import grid_info.grid as grid

#全局变量
WIDTH = grid.ROWS * (grid.grid_coordinary.GRID_SIZE + grid.grid_coordinary.GRID_GAP) + grid.grid_coordinary.GRID_GAP
GRID_HEIGHT = grid.COLS * (grid.grid_coordinary.GRID_SIZE + grid.grid_coordinary.GRID_GAP) + grid.grid_coordinary.GRID_GAP
MENU_HEIGHT = 30
TITLE = "Test"


#游戏初始化
game.init()
is_running = True

#界面大小设置
game.display.set_caption(TITLE)
screen = game.display.set_mode((WIDTH,GRID_HEIGHT + MENU_HEIGHT))
#方格界面设置
menu_surface = game.Surface((WIDTH,MENU_HEIGHT))
menu_surface.fill(grid.grid_color.LIGHT_GREY)
screen.blit(menu_surface, (0,0))
grid_surface = game.Surface((WIDTH,GRID_HEIGHT))
grid_surface.fill(grid.grid_color.LIGHT_GREY)

#初始化方格颜色和数量
grid.init()

#开始
while is_running:
    #设置界面底色
    grid_surface.fill(grid.grid_color.LIGHT_GREY)

    #监控用户事件
    for event in game.event.get():
        #关闭按钮
        if event.type == game.QUIT:
            is_running = False
        
        #其他事件
        else:
            #检测到鼠标按下或滚轮变化
            if event.type == game.MOUSEBUTTONDOWN:
                #左键
                if (event.button == 1):
                    mouse.button_left = True
                    grid.click_grid()
                #右键
                elif (event.button == 3):
                    mouse.button_right = True
                #滚轮向上
                elif (event.button == 4):
                    #放大
                    grid.grid_coordinary.zoom_in(2)
                #滚轮向下
                elif (event.button == 5):
                    #缩小
                    grid.grid_coordinary.zoom_out(2)

            #检测到鼠标松开
            elif event.type == game.MOUSEBUTTONUP:
                mouse.button_left = False
                mouse.button_right = False

            #检测到鼠标移动
            if event.type == game.MOUSEMOTION:
                mouse.save_coordinary(game.mouse.get_pos())
                if (mouse.button_right):
                    #得到鼠标移动距离
                    distance = mouse.get_moving_distance()
                    #移动方格位置
                    grid.grid_coordinary.move(distance[0], distance[1])
    
    #将方格画到界面上
    for y, row in enumerate(grid.get_grid_coordinary()):
        for x, rect in enumerate(row):
            game.draw.rect(grid_surface, grid.get_grid_color(y, x), rect)
    screen.blit(grid_surface, (0,MENU_HEIGHT))

    #刷新屏幕
    game.display.flip()

#卸载模块
game.quit()
#结束程序
sys.exit()
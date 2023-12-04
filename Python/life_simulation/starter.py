import sys
import pygame as game
import mouse_saver as mouse

#全局变量
GRID_SIZE = 5
GRID_GAP = int (GRID_SIZE/5)
ROWS = 80
COLS = 80

WIDTH = ROWS * (GRID_SIZE + GRID_GAP) + GRID_GAP
HEIGHT = COLS * (GRID_SIZE + GRID_GAP) + GRID_GAP
TITLE = "Test"
BLACK = (0,0,0)
WHITE = (255,255,255)
GREY = (100,100,100)

#游戏初始化
game.init()
is_running = True

#界面设置
game.display.set_caption(TITLE)
screen = game.display.set_mode((WIDTH,HEIGHT))

#初始化方格颜色和数量
grid = [[GREY for _ in range(COLS)] for _ in range(ROWS)]

#方格的位置和尺寸定义
grid_rects = []
for y in range(ROWS):
    row = []
    for x in range(COLS):
        #pygame.Rect(x_position, y_position, width, length)
        rect = game.Rect(x * (GRID_SIZE + GRID_GAP) + GRID_GAP, y * (GRID_SIZE + GRID_GAP) + GRID_GAP, GRID_SIZE, GRID_SIZE)
        row.append(rect)
    grid_rects.append(row)

#开始
while is_running:
    #设置界面底色
    screen.fill(WHITE)

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
                #右键
                elif (event.button == 3):
                    mouse.button_right = True
                #滚轮向上
                if (event.button == 4):
                    #加边长
                    size_diff = int (GRID_SIZE * 0.1) + 1 # +1防止GRID_SIZE太小时size_diff无法变化
                    GRID_SIZE += size_diff
                    #计算新边距
                    new_grid_gap = int (GRID_SIZE/5)
                    gap_diff = new_grid_gap - GRID_GAP
                    GRID_GAP = new_grid_gap

                    total_diff = size_diff + gap_diff
                    #读取鼠标位置
                    coordinary = game.mouse.get_pos()
                    #移动方格位置
                    for x, row in enumerate(grid_rects):
                        for y, rect in enumerate(row):
                            rect.width = GRID_SIZE
                            rect.height = GRID_SIZE
                            rect.x += y * total_diff - int (coordinary[1] * 0.2)
                            rect.y += x * total_diff - int (coordinary[0] * 0.2)
                #滚轮向下
                elif (event.button == 5 and GRID_SIZE != 1):
                    #减边长
                    size_diff = int (GRID_SIZE * 0.1) + 1 # +1防止GRID_SIZE太小时size_diff无法变化
                    GRID_SIZE -= size_diff
                    #计算新边距
                    new_grid_gap = int (GRID_SIZE/5)
                    gap_diff = GRID_GAP - new_grid_gap
                    GRID_GAP = new_grid_gap

                    total_diff = size_diff + gap_diff
                    #读取鼠标位置
                    coordinary = game.mouse.get_pos()
                    #移动方格位置
                    for x, row in enumerate(grid_rects):
                        for y, rect in enumerate(row):
                            rect.width = GRID_SIZE
                            rect.height = GRID_SIZE
                            rect.x -= y * total_diff - int (coordinary[1] * 0.2)
                            rect.y -= x * total_diff - int (coordinary[0] * 0.2)
                    
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
                    for row in grid_rects:
                        for rect in row:
                            rect.x += distance[0]
                            rect.y += distance[1]
    
    #将方格画到界面上
    for y, row in enumerate(grid_rects):
        for x, rect in enumerate(row):
            game.draw.rect(screen, grid[y][x], rect)

    #刷新屏幕
    game.display.flip()

#卸载模块
game.quit()
#推出程序
sys.exit()
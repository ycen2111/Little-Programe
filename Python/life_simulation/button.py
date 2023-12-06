import pygame as game
import config

#设置目录按钮
class Button:
    #初始设置
    def __init__(self, start_point, size, text, color, surface):
        self.x = start_point[0]
        self.y = start_point[1]
        self.width = size[0]
        self.height = size[1]
        self.text = text
        self.color = color
        self.surface = surface
        self.rect = game.Rect(self.x, self.y, self.width, self.height)
        font = game.font.Font(None, 36)
        self.text_surface = font.render(self.text, True, config.BLACK)
        self.text_rect = self.text_surface.get_rect(center=self.rect.center)

    #绘制按钮
    def draw(self):
        #绘制按钮主体
        game.draw.rect(self.surface, config.BLACK, self.rect, border_radius=10) #边框
        game.draw.rect(self.surface, self.color, self.rect) #按钮主体
        #绘制按钮上的文字
        self.surface.blit(self.text_surface, self.text_rect)
    
    #更改颜色
    def change_color(self, color):
        self.color = color
        self.draw()
    
    def on_button(self):
        #判断鼠标在按钮上
        if self.rect.collidepoint(game.mouse.get_pos()):
            return True
        else:
            return False
import pygame as game
import config
import my_class.text as text_rect

#设置按钮
class Button:
    #初始设置
    def __init__(self, start_point, rect_size, text, color, screen):
        self.x = start_point[0]
        self.y = start_point[1]
        self.width = rect_size[0]
        self.height = rect_size[1]
        self.text = text
        self.color = color
        self.screen = screen
        self.rect = game.Rect(self.x, self.y, self.width, self.height)
        self.text_rect = text_rect.Text(self.text, config.BLACK, 36, self.rect.center)

    #绘制按钮
    def draw(self):
        #绘制按钮主体
        game.draw.rect(self.screen, config.BLACK, self.rect, border_radius=10) #边框
        game.draw.rect(self.screen, self.color, self.rect) #按钮主体
        #绘制按钮上的文字
        self.text_rect.draw(self.screen)
    
    #更改颜色
    def change_color(self, color):
        self.color = color
        self.draw()
    
    #更改文字
    def change_text(self, text):
        self.text_rect.change_text(text)
        self.draw()
    
    #返回文字
    def get_text(self):
        return self.text_rect.text
    
    #判断鼠标在按钮上
    def on_button(self):
        if self.rect.collidepoint(game.mouse.get_pos()):
            return True
        else:
            return False
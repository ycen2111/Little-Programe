import pygame as game
#设置文字

class Text:
    def __init__(self, text, color, size, center):
        self.text = text
        self.color = color
        self.x = center[0]
        self.y = center[1]
        self.font = game.font.Font(None, size)
        self.text_surface = self.font.render(self.text, True, self.color)
        self.text_rect = self.text_surface.get_rect(center= (self.x, self.y))
    
    def draw(self, screen):
        screen.blit(self.text_surface, self.text_rect)

    def change_text(self, text):
        self.text = text
        self.text_surface = self.font.render(self.text, True, self.color)
        self.text_rect = self.text_surface.get_rect(center= (self.x, self.y))

    def change_pos(self, pos):
        self.text_rect = self.text_surface.get_rect(center= (self.x + pos[0], self.y + pos[1]))

    def change_color(self, color):
        self.color = color
        self.text_surface = self.font.render(self.text, True, self.color)
        self.text_rect = self.text_surface.get_rect(center= (self.x, self.y))

    def change_size(self, size):
        self.font = game.font.Font(None, size)
        self.text_surface = self.font.render(self.text, True, self.color)
        self.text_rect = self.text_surface.get_rect(center= (self.x, self.y))
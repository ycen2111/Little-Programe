#方格上出现的活细胞

class Cell:
    def __init__(self, x, y, code_id, start_step):
        self.x = x
        self.y = y
        self.code_id = code_id
        self.curr_step = start_step
        self.energy = 0
        self.material = 0
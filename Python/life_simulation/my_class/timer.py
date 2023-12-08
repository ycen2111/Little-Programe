import time
#计算间隔时长

class Timer:
    def __init__(self, limit):
        self.start_time = time.time()
        self.end_time = 0
        self.limit = limit
        self.is_finish = False

    def reset_start_time(self):
        self.start_time = time.time()

    def update(self):
        if (time.time() - self.start_time > self.limit):
            self.is_finish = True
        else:
            self.is_finish = False
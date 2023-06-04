import json


class Config:
    def __init__(self, config_path="C:/Users/mduc0/Documents/CODE/Java/dbms-project/cinema-management-system/cinema"
                                   "-management-system-main/src/main/python/configs/development-tavico-ubuntu.json"):
        self._config = json.load(open(config_path, encoding='utf-8'))

    def get(self, key):

        value = self._config.copy()
        for k in key.split('.'):
            value = value.get(k)

        return value

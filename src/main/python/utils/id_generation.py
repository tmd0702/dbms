import sys
sys.path.insert(0, "C:/Users/mduc0/Documents/CODE/Java/dbms-project/cinema-management-system/cinema-management-system-main/src/main/python/database/processor")
from database import *


def generate_id_format(table):
    id_format = ""
    if "_" in table:
        for table_name_element in table.split("_"):
            id_format += table_name_element[0]
    else:
        id_format = table[0:3]
    return id_format


class IdGeneration:
    def __init__(self, database):
        self._database = database
        self._sequence = dict()

    def generate_id(self, table):
        table = table.upper()
        query = f"SELECT MAX(ID) AS ID FROM {table}"
        try:
            self._database.get_cursor().execute(query)
            result = self._database.get_cursor().fetchone()
            max_index = int(result[0].split("_")[1]) if "_" in result[0] else int(result[0])
            self._sequence[table] = max_index
        except Exception as e:
            print(e)
        self._sequence[table] = self._sequence.get(table, 0) + 1
        id = generate_id_format(table) + "_" + str(self._sequence.get(table)).zfill(5)
        return id

    


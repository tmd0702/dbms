import numpy as np


def get_data_row_value_by_column_name(data: dict(), column_name: str, row_index: int):
    ret_value = None
    for column in data["column_names"]:
        if column == column_name:
            try:
                ret_value = data["values"][row_index][data["column_names"].index(column)]
            except Exception as e:
                print(e)
            break
    return ret_value

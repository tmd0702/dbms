import mysql.connector
import sys


class Database:
    def __init__(self, config):
        try:
            self.mydb = mysql.connector.connect(
                host=config.get("DATABASE.DB_HOST"),
                user=config.get("DATABASE.DB_USER"),
                password=config.get("DATABASE.DB_PASS")
            )
            self.cursor = self.mydb.cursor()
            self.cursor.execute("USE MOVIE")
            print("Connected")
        except Exception as e:
            print('Error occurred:', e)

    def commit(self):
        self.mydb.commit()

    def rollback(self):
        self.mydb.rollback()

    def get_cursor(self):
        return self.cursor

import sys
from http import HTTPStatus
sys.path.insert(0, "C:/Users/mduc0/Documents/CODE/Java/dbms-project/cinema-management-system/cinema-management-system-main/src/main/python/utils/")

from datatype import *


class Processor:
    def __init__(self, default_database_table, database, id_generation):
        self._database = database
        self._id_generation = id_generation
        self._default_database_table = default_database_table


    def get_id_generation(self):
        return self._id_generation

    def get_database(self):
        return self._database

    def set_default_database_table(self, default_database_table):
        self._default_database_table = default_database_table

    def get_default_database_table(self):
        return self._default_database_table

    def _select(self, values, start_index, quantity, query_condition, sort_query, table):
        query = f"SELECT {values} FROM {table}"
        query = query + " WHERE " + query_condition if len(query_condition) > 0 else query
        query = query + " ORDER BY " + sort_query if len(sort_query) > 0 else query
        query = query + f" LIMIT {start_index}, {quantity}" if quantity > -1 else query
        print(query)
        def _get_column_names_map(x):
            return x[0]

        def _get_datatype_ids_map(x):
            return Datatype(x[1]).name

        response = {
            "data": None,
            "status_code": HTTPStatus.OK.value,
            "error_message": None
        }

        try:
            self._database.get_cursor().execute(query)
            result = self._database.get_cursor().fetchall()
            column_names, column_datatypes = list(map(_get_column_names_map, self._database.get_cursor().description)), list(map(_get_datatype_ids_map, self._database.get_cursor().description))

            def _parse_value_into_string(value):
                return str(value)

            data = {
                "column_names": column_names,
                "column_datatypes": column_datatypes,
                "values": [list(map(_parse_value_into_string, row)) for row in result],
            }
            response["data"] = data

        except Exception as e:
            response["error_message"] = str(e)
            response["status_code"] = HTTPStatus.BAD_REQUEST.value

        print(response)
        return response

    def _insert(self, column_value_dict, table, is_generate_id_allow=True, is_commit=True):

        if is_generate_id_allow:
            column_value_dict["ID"] = self.get_id_generation().generate_id(self.get_default_database_table())

        def norm_sql_val_map_func(x):
            return "'" + x + "'"
        response = {
            "status_code": HTTPStatus.OK.value,
            "error_message": None
        }

        query = f'INSERT INTO {table} ({", ".join(list(column_value_dict.keys()))}) VALUES ({", ".join(list(map(norm_sql_val_map_func, column_value_dict.values())))})'
        try:
            self._database.get_cursor().execute(query)
            if is_commit:
                self._database.commit()
        except Exception as e:
            response["error_message"] = str(e)
            response["status_code"] = HTTPStatus.BAD_REQUEST.value
        return response

    def _update(self, column_value_dict, query_condition, table, is_commit):
        response = {
            "status_code": HTTPStatus.OK.value,
            "error_message": None
        }

        def construct_update_sql_set_statement(columns, values):
            set_statement = ""
            for i in range(len(columns)):
                set_statement += f"{columns[i]} = '{values[i]}'"
                if i < len(columns) - 1:
                    set_statement += ", "
            return set_statement

        columns, values = list(column_value_dict.keys()), list(column_value_dict.values())
        query = f"UPDATE {table} SET {construct_update_sql_set_statement(columns, values)} WHERE {query_condition}"
        try:
            self._database.get_cursor().execute(query)
            if is_commit:
                self._database.commit()
        except Exception as e:
            response["error_message"] = str(e)
            response["status_code"] = HTTPStatus.BAD_REQUEST.value
        return response

    def _delete(self, query_condition, table, is_commit):
        response = {
            "status_code": HTTPStatus.OK.value,
            "error_message": None
        }

        query = f"DELETE FROM {table} WHERE {query_condition}"
        try:
            self._database.get_cursor().execute(query)
            if is_commit:
                self._database.commit()
        except Exception as e:
            response["error_message"] = str(e)
            response["status_code"] = HTTPStatus.BAD_REQUEST.value
        return response

    def select_data(self, start_index, quantity, query_condition, sort_query, count=False):
        return self._select("*" if not count else "COUNT(*) AS COUNT", start_index, quantity, query_condition, sort_query, self._default_database_table)

    def insert_data(self, column_value_dict, is_commit=True):
        return self._insert(column_value_dict, self.get_default_database_table(), True, is_commit)

    def update_data(self, column_value_dict, query_condition, is_commit=True):
        return self._update(column_value_dict, query_condition, self._default_database_table, is_commit)

    def delete_data(self, query_condition, is_commit):
        return self._delete(query_condition, self._default_database_table, is_commit)


class PromotionManagementProcessor(Processor):
    def __init__(self, database, id_generation):
        super().__init__("PROMOTIONS", database, id_generation)

    def select_data(self, start_index, quantity, query_condition, sort_query, count=False):
        final_query_condition = "USER_CATEGORY.ID = PROMOTIONS.USER_CATEGORY_ID"
        if len(query_condition):
            final_query_condition += f" AND {query_condition}"
        return self._select(
            "PROMOTIONS.ID AS 'PROMOTIONS.ID', USER_CATEGORY.POINT_LOWERBOUND AS 'USER_CATEGORY.POINT_LOWERBOUND', "
            "PROMOTIONS.NAME AS 'PROMOTIONS.NAME', PROMOTIONS.START_DATE AS 'PROMOTIONS.START_DATE', "
            "PROMOTIONS.END_DATE AS 'PROMOTIONS.END_DATE', PROMOTIONS.DESCRIPTION AS 'PROMOTIONS.DESCRIPTION', "
            "PROMOTIONS.DISCOUNT AS 'PROMOTIONS.DISCOUNT', USER_CATEGORY.CATEGORY AS 'USER_CATEGORY.CATEGORY'" if not count else "COUNT(*) AS 'COUNT'",
            start_index, quantity, final_query_condition, sort_query, "PROMOTIONS, USER_CATEGORY")


class MovieManagementProcessor(Processor):
    def __init__(self, database, id_generation):
        super().__init__("MOVIES", database, id_generation)


class AccountManagementProcessor(Processor):
    def __init__(self, database, id_generation):
        super().__init__("USERS", database, id_generation)

    def select_data(self, start_index, quantity, query_condition, sort_query, count=False):
        final_query_condition = "USERS.USER_CATEGORY_ID = USER_CATEGORY.ID"
        if len(query_condition):
            final_query_condition += f" AND {query_condition}"
        return self._select(
            "USERS.ID AS 'USERS.ID', USERS.USERNAME AS 'USERS.USERNAME', USERS.FIRST_NAME AS 'USERS.FIRST_NAME', "
            "USERS.LAST_NAME AS 'USERS.LAST_NAME', USERS.DOB AS 'USERS.DOB', USERS.GENDER AS 'USERS.GENDER', "
            "USERS.ADDRESS AS 'USERS.ADDRESS', USERS.PHONE AS 'USERS.PHONE', USERS.EMAIL AS 'USERS.EMAIL', "
            "USERS.USER_ROLE AS 'USERS.USER_ROLE', USERS.SCORE AS 'USERS.SCORE', USER_CATEGORY.CATEGORY AS "
            "'USER_CATEGORY.CATEGORY'" if not count else "COUNT(*) AS COUNT",
            start_index, quantity, final_query_condition, sort_query, "USERS, USER_CATEGORY")


class AuthenticationManagementProcessor(Processor):
    def __init__(self, database, id_generation):
        super().__init__("AUTHENTICATION", database, id_generation)

    def select_data(self, start_index, quantity, query_condition, sort_query, count=False):
        final_query_condition = "A.USER_ID = U.ID"
        if len(query_condition) > 0:
            final_query_condition += f" AND {query_condition}"
        return self._select("A.PASS AS PASSWORD" if not count else "COUNT(*) AS COUNT", start_index, quantity, final_query_condition, "", "USERS U, AUTHENTICATION A")


class BookingItemManagementProcessor(Processor):
    def __init__(self, database, id_generation):
        super().__init__("BOOKING_ITEMS", database, id_generation)

    def insert_data(self, column_value_dict, is_commit=True):
        return self._insert(column_value_dict, self.get_default_database_table(), False, is_commit)

    def select_data(self, start_index, quantity, query_condition, sort_query, count=False):
        final_query_condition = "BOOKING_ITEMS.ITEM_ID = ITEMS.ID AND BOOKING_ITEMS.PAYMENT_ID = PAYMENTS.ID AND " \
                                "PAYMENTS.USER_ID = USERS.ID AND ITEMS.ITEM_CATEGORY_ID = PRICES.COMPONENT_ID AND " \
                                "ITEM_CATEGORY.ID = ITEMS.ITEM_CATEGORY_ID "
        if len(query_condition):
            final_query_condition += f" AND {query_condition}"
        return self._select("ITEMS.ID AS 'ITEMS.ID', ITEMS.NAME AS 'ITEMS.NAME', ITEM_CATEGORY.CATEGORY AS "
                            "'ITEM_CATEGORY.CATEGORY', PRICES.PRICE AS 'PRICES.PRICE', BOOKING_ITEMS.QUANTITY AS "
                            "'BOOKING_ITEMS.QUANTITY', PAYMENTS.ID AS 'PAYMENTS.ID', USERS.USERNAME AS "
                            "'USERS.USERNAME'" if not count else "COUNT(*) AS COUNT", start_index,
                            quantity, final_query_condition, sort_query,
                            "BOOKING_ITEMS, ITEMS, PAYMENTS, USERS, PRICES, ITEM_CATEGORY")


class BookingTicketManagementProcessor(Processor):
    def __init__(self, database, id_generation):
        super().__init__("BOOKING_TICKETS", database, id_generation)


    def insert_data(self, column_value_dict, is_commit=True):
        return self._insert(column_value_dict, self.get_default_database_table(), False, is_commit)


    def select_data(self, start_index, quantity, query_condition, sort_query, count=False):
        final_query_condition = "SCHEDULES.SHOW_TIME_ID = SHOW_TIMES.ID AND SCHEDULES.ID = TICKETS.SCHEDULE_ID AND " \
                                "TICKETS.SEAT_ID = SEATS.ID AND CINEMAS.ID = SCREEN_ROOMS.CINEMA_ID AND " \
                                "SCREEN_ROOMS.ID = SCHEDULES.SCREEN_ROOM_ID AND MOVIES.ID = SCHEDULES.MOVIE_ID AND " \
                                "BOOKING_TICKETS.TICKET_ID = TICKETS.ID AND BOOKING_TICKETS.PAYMENT_ID = PAYMENTS.ID " \
                                "AND USERS.ID = PAYMENTS.USER_ID AND SEATS.SEAT_CATEGORY_ID = PRICES.COMPONENT_ID"
        if len(query_condition):
            final_query_condition += f" AND {query_condition}"
        return self._select("TICKETS.ID AS 'TICKETS.ID', PRICES.PRICE AS 'PRICES.PRICE', SCHEDULES.SHOW_DATE AS "
                            "'SCHEDULES.SHOW_DATE', SHOW_TIMES.START_TIME AS 'SHOW_TIMES.START_TIME', SEATS.NAME AS "
                            "'SEATS.NAME', CINEMAS.NAME AS 'CINEMAS.NAME', SCREEN_ROOMS.NAME AS 'SCREEN_ROOMS.NAME', "
                            "MOVIES.TITLE AS 'MOVIES.TITLE', PAYMENTS.ID AS 'PAYMENTS.ID', USERS.USERNAME AS "
                            "'USERS.USERNAME'" if not count else "COUNT(*) AS COUNT", start_index,
                            quantity, final_query_condition, sort_query, "TICKETS, SEATS, SCHEDULES, SHOW_TIMES, "
                                                                         "SCREEN_ROOMS, CINEMAS, MOVIES, PAYMENTS, "
                                                                         "BOOKING_TICKETS, USERS, PRICES")


class CinemaManagementProcessor(Processor):
    def __init__(self, database, id_generation):
        super().__init__("CINEMAS", database, id_generation)

    def select_data(self, start_index, quantity, query_condition, sort_query, count=False):
        return self._select("CINEMAS.ID AS 'CINEMAS.ID', CINEMAS.NAME AS 'CINEMAS.NAME', CINEMAS.ADDRESS AS "
                            "'CINEMAS.ADDRESS'" if not count else "COUNT(*) AS 'COUNT'", start_index, quantity, query_condition, sort_query, self.get_default_database_table())


class ItemManagementProcessor(Processor):
    def __init__(self, database, id_generation):
        super().__init__("ITEMS", database, id_generation)

    def select_data(self, start_index, quantity, query_condition, sort_query, count=False):
        final_query_condition = "ITEM_CATEGORY.ID = ITEMS.ITEM_CATEGORY_ID AND PRICES.COMPONENT_ID = ITEM_CATEGORY.ID";
        if len(query_condition) > 0:
            final_query_condition += f" AND {query_condition}"
        return self._select(
            "ITEMS.ID AS 'ITEMS.ID', ITEMS.NAME AS 'ITEMS.NAME', ITEM_CATEGORY.CATEGORY AS 'ITEM_CATEGORY.CATEGORY', "
            "PRICES.PRICE AS 'PRICES.PRICE', ITEMS.REVENUE AS 'ITEMS.REVENUE'" if not count else "COUNT(*) AS COUNT",
            start_index, quantity, query_condition, sort_query, "ITEMS, ITEM_CATEGORY, PRICES")


class PaymentManagementProcessor(Processor):
    def __init__(self, database, id_generation):
        super().__init__("PAYMENTS", database, id_generation)

    def select_data(self, start_index, quantity, query_condition, sort_query, count=False):
        final_query_condition = "USERS.ID = PAYMENTS.USER_ID AND PAYMENT_METHODS.ID = PAYMENTS.PAYMENT_METHOD_ID AND " \
                                "SCHEDULES.ID = PAYMENTS.SCHEDULE_ID AND SCREEN_ROOMS.ID = SCHEDULES.SCREEN_ROOM_ID " \
                                "AND CINEMAS.ID = SCREEN_ROOMS.CINEMA_ID AND SCHEDULES.MOVIE_ID = MOVIES.ID "
        if len(query_condition) > 0:
            final_query_condition += f" AND {query_condition}"
        return self._select(
            "PAYMENTS.ID AS 'PAYMENTS.ID', PAYMENTS.PAYMENT_DATE AS 'PAYMENTS.PAYMENT_DATE', PAYMENT_METHODS.NAME AS "
            "'PAYMENT_METHODS.NAME', PAYMENTS.TOTAL_AMOUNT AS 'PAYMENTS.TOTAL_AMOUNT', USERS.USERNAME AS "
            "'USERS.USERNAME', MOVIES.TITLE AS 'MOVIES.TITLE', CINEMAS.NAME AS 'CINEMAS.NAME'" if not count else "COUNT(*) AS COUNT",
            start_index, quantity, final_query_condition, sort_query,
            "PAYMENTS, USERS, PAYMENT_METHODS, SCHEDULES, MOVIES, "
            "SCREEN_ROOMS, CINEMAS")


class ReviewManagementProcessor(Processor):
    def __init__(self, database, id_generation):
        super().__init__("REVIEW", database, id_generation)

    def select_data(self, start_index, quantity, query_condition, sort_query, count=False):
        final_query_condition = "R.MOVIE_ID = M.ID AND R.USER_ID = U.ID AND U.USER_CATEGORY_ID = UC.ID"
        if len(query_condition) > 0:
            final_query_condition += f" AND {query_condition}"
        return self._select(
            "UC.CATEGORY, U.USERNAME, M.ID AS MOVIE_ID, R.RATING, R.COMMENT, R.DATE" if not count else "COUNT(*) AS COUNT"
            ,
            start_index, quantity, final_query_condition, sort_query, "REVIEW R, USERS U, MOVIES M, USER_CATEGORY UC")


class ScheduleManagementProcessor(Processor):
    def __init__(self, database, id_generation):
        super().__init__("SCHEDULES", database, id_generation)

    def select_data(self, start_index, quantity, query_condition, sort_query, count=False):
        final_query_condition = "SCHEDULES.MOVIE_ID = MOVIES.ID AND SCHEDULES.SCREEN_ROOM_ID = SCREEN_ROOMS.ID AND " \
                                "CINEMAS.ID = SCREEN_ROOMS.CINEMA_ID AND SCHEDULES.SHOW_TIME_ID = SHOW_TIMES.ID "
        if len(query_condition) > 0:
            final_query_condition += f" AND {query_condition}"
        return self._select(
            "SCHEDULES.ID AS 'SCHEDULES.ID', SCHEDULES.SHOW_DATE AS 'SCHEDULES.SHOW_DATE', SHOW_TIMES.START_TIME AS "
            "'SHOW_TIMES.START_TIME', MOVIES.TITLE AS 'MOVIES.TITLE', SCREEN_ROOMS.NAME AS 'SCREEN_ROOMS.NAME', "
            "CINEMAS.NAME AS 'CINEMAS.NAME'" if not count else "COUNT(*) AS COUNT",
            start_index, quantity, final_query_condition, sort_query, "SCHEDULES, MOVIES, SHOW_TIMES, SCREEN_ROOMS, "
                                                                      "CINEMAS")


class ScreenRoomManagementProcessor(Processor):
    def __init__(self, database, id_generation):
        super().__init__("SCREEN_ROOMS", database, id_generation)

    def select_data(self, start_index, quantity, query_condition, sort_query, count=False):
        final_query_condition = "SCREEN_ROOMS.CINEMA_ID = CINEMAS.ID"
        if len(query_condition) > 0:
            final_query_condition += f" AND {query_condition}"
        return self._select(
            "SCREEN_ROOMS.ID AS 'SCREEN_ROOMS.ID', SCREEN_ROOMS.NAME AS 'SCREEN_ROOMS.NAME', SCREEN_ROOMS.CAPACITY AS "
            "'SCREEN_ROOMS.CAPACITY', CINEMAS.NAME AS 'CINEMAS.NAME'" if not count else "COUNT(*) AS COUNT",
            start_index, quantity, final_query_condition, sort_query, "CINEMAS, SCREEN_ROOMS")


class ShowTimeManagementProcessor(Processor):
    def __init__(self, database, id_generation):
        super().__init__("SHOW_TIMES", database, id_generation)

    def select_data(self, start_index, quantity, query_condition, sort_query, count=False):
        final_query_condition = query_condition
        return self._select(
            "SHOW_TIMES.ID AS 'SHOW_TIMES.ID', SHOW_TIMES.START_TIME AS 'SHOW_TIMES.START_TIME'" if not count else "COUNT(*) AS COUNT",
            start_index, quantity, final_query_condition, sort_query, self.get_default_database_table())

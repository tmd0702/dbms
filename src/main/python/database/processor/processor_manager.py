from processor import *
from database import *
sys.path.insert(0, "C:/Users/mduc0/Documents/CODE/Java/dbms-project/cinema-management-system/cinema-management-system-main/src/main/python/utils/")
from id_generation import *

class ProcessorManager:
    def __init__(self, config):
        self._database = Database(config)
        self._id_generation = IdGeneration(self._database)
        self._account_management_processor = AccountManagementProcessor(self._database, self._id_generation)
        self._authentication_management_processor = AuthenticationManagementProcessor(self._database, self._id_generation)
        self._booking_item_management_processor = BookingItemManagementProcessor(self._database, self._id_generation)
        self._booking_ticket_management_processor = BookingTicketManagementProcessor(self._database, self._id_generation)
        self._cinema_management_processor = CinemaManagementProcessor(self._database, self._id_generation)
        self._item_management_processor = ItemManagementProcessor(self._database, self._id_generation)
        self._payment_management_processor = PaymentManagementProcessor(self._database, self._id_generation)
        self._review_management_processor = ReviewManagementProcessor(self._database, self._id_generation)
        self._schedule_management_processor = ScheduleManagementProcessor(self._database, self._id_generation)
        self._screen_room_management_processor = ScreenRoomManagementProcessor(self._database, self._id_generation)
        self._show_time_management_processor = ShowTimeManagementProcessor(self._database, self._id_generation)
        self._movie_management_processor = MovieManagementProcessor(self._database, self._id_generation)
        self._promotion_management_processor = PromotionManagementProcessor(self._database, self._id_generation)

    def get_id_generation(self):
        return self._id_generation

    def get_movie_management_processor(self):
        return self._movie_management_processor

    def get_promotion_management_processor(self):
        return self._promotion_management_processor

    def get_schedule_management_processor(self):
        return self._schedule_management_processor

    def get_screen_room_management_processor(self):
        return self._screen_room_management_processor

    def get_show_time_management_processor(self):
        return self._show_time_management_processor

    def get_item_management_processor(self):
        return self._item_management_processor

    def get_payment_management_processor(self):
        return self._payment_management_processor

    def get_review_management_processor(self):
        return self._review_management_processor

    def get_booking_ticket_management_processor(self):
        return self._booking_ticket_management_processor

    def get_cinema_management_processor(self):
        return self._cinema_management_processor

    def get_account_management_processor(self):
        return self._account_management_processor

    def get_authentication_management_processor(self):
        return self._authentication_management_processor

    def get_booking_item_management_processor(self):
        return self._booking_item_management_processor

    def get_database(self):
        return self._database

package Database;

import CinemaManager.Cinema;

public class ProcessorManager {
    private BookingProcessor bookingProcessor;
    private UserCategoryManagementProcessor userCategoryManagementProcessor;
    private FiltererProcessor filtererProcessor;
    private PaymentManagementProcessor paymentManagementProcessor;
    private MovieManagementProcessor movieManagementProcessor;
    private CinemaManagementProcessor cinemaManagementProcessor;
    private ShowTimeManagementProcessor showTimeManagementProcessor;
    private ScreenRoomManagementProcessor screenRoomManagementProcessor;
    private SeatManagementProcessor seatManagementProcessor;
    private ScheduleManagementProcessor scheduleManagementProcessor;
    private ReviewManagementProcessor reviewManagementProcessor;
    private AccountManagementProcessor accountManagementProcessor;
    private PromotionManagementProcessor promotionManagementProcessor;
    private ItemManagementProcessor itemManagementProcessor;
    private AuthenticationManagementProcessor authenticationManagementProcessor;
    private TicketManagementProcessor ticketManagementProcessor;
    private BookingTicketManagementProcessor bookingTicketManagementProcessor;
    private BookingItemManagementProcessor bookingItemManagementProccessor;

    public ProcessorManager() throws Exception {
        this.promotionManagementProcessor = new PromotionManagementProcessor();
        this.authenticationManagementProcessor = new AuthenticationManagementProcessor();
        this.userCategoryManagementProcessor = new UserCategoryManagementProcessor();
        this.paymentManagementProcessor = new PaymentManagementProcessor();
        this.accountManagementProcessor = new AccountManagementProcessor();
        this.reviewManagementProcessor = new ReviewManagementProcessor();
        this.scheduleManagementProcessor = new ScheduleManagementProcessor();
        this.screenRoomManagementProcessor = new ScreenRoomManagementProcessor();
        this.seatManagementProcessor = new SeatManagementProcessor();
        this.cinemaManagementProcessor = new CinemaManagementProcessor();
        this.showTimeManagementProcessor = new ShowTimeManagementProcessor();
        this.movieManagementProcessor = new MovieManagementProcessor();
        this.bookingProcessor = new BookingProcessor();
        this.filtererProcessor = new FiltererProcessor();
        this.itemManagementProcessor = new ItemManagementProcessor();
        this.ticketManagementProcessor = new TicketManagementProcessor();
        this.bookingTicketManagementProcessor = new BookingTicketManagementProcessor();
        this.bookingItemManagementProccessor = new BookingItemManagementProcessor();
    }
    public BookingItemManagementProcessor getBookingItemManagementProccessor() {
        return this.bookingItemManagementProccessor;
    }
    public ItemManagementProcessor getItemManagementProcessor() {
        return this.itemManagementProcessor;
    }

    public PromotionManagementProcessor getPromotionManagementProcessor() {
        return this.promotionManagementProcessor;
    }

    public AuthenticationManagementProcessor getAuthenticationManagementProcessor() {
        return this.authenticationManagementProcessor;
    }

    public UserCategoryManagementProcessor getUserCategoryManagementProcessor() {
        return this.userCategoryManagementProcessor;
    }

    public PaymentManagementProcessor getPaymentManagementProcessor() {
        return this.paymentManagementProcessor;
    }

    public BookingItemManagementProcessor getBookingItemManagementProcessor() {
        return this.bookingItemManagementProccessor;
    }

    public ReviewManagementProcessor getReviewManagementProcessor() {
        return this.reviewManagementProcessor;
    }

    public TicketManagementProcessor getTicketManagementProcessor() {
        return this.ticketManagementProcessor;
    }

    public AccountManagementProcessor getAccountManagementProcessor() {
        return this.accountManagementProcessor;
    }

    public BookingTicketManagementProcessor getBookingTicketManagementProcessor() {
        return this.bookingTicketManagementProcessor;
    }

    public ScheduleManagementProcessor getScheduleManagementProcessor() {
        return this.scheduleManagementProcessor;
    }

    public ScreenRoomManagementProcessor getScreenRoomManagementProcessor() {
        return this.screenRoomManagementProcessor;
    }

    public SeatManagementProcessor getSeatManagementProcessor() {
        return this.seatManagementProcessor;
    }

    public CinemaManagementProcessor getCinemaManagementProcessor() {
        return cinemaManagementProcessor;
    }

    public ShowTimeManagementProcessor getShowTimeManagementProcessor() {
        return showTimeManagementProcessor;
    }

    public BookingProcessor getBookingProcessor() {
        return this.bookingProcessor;
    }

    public FiltererProcessor getFiltererProcessor() {
        return this.filtererProcessor;
    }

    public MovieManagementProcessor getMovieManagementProcessor() {
        return this.movieManagementProcessor;
    }
}
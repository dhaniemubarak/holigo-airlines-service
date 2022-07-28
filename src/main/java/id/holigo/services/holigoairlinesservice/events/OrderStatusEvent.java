package id.holigo.services.holigoairlinesservice.events;

public enum OrderStatusEvent {
    BOOK_SUCCESS, BOOK_FAIL, PROCESS_ISSUED, ISSUED_SUCCESS, WAITING_ISSUED, RETRYING_ISSUED, ISSUED_FAIL, ORDER_CANCEL, ORDER_EXPIRE
}

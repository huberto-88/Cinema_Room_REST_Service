package cinema.controller;

import cinema.cinema.CinemaRoom;
import cinema.service.CinemaService;
import cinema.cinema.Seat;
import cinema.cinema.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
public class Controller {

    private final CinemaService cinemaService;

    @Autowired
    public Controller(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping("/seats")
    public CinemaRoom getCinemaRoom() {
        return cinemaService.getCinemaRoom();
    }

    class ErrorRequest {
        private final String error;

        public ErrorRequest(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }
    }

    @PostMapping("/purchase")
    public ResponseEntity<Object> bookASeat(@RequestBody Seat seat) {

        if (seat.getColumn() > cinemaService.getCinemaRoom().getTotalColumns()
                || seat.getRow() > cinemaService.getCinemaRoom().getTotalRows()
                || seat.getRow() < 1 || seat.getColumn() < 1) {
            return new ResponseEntity<>(new ErrorRequest("The number of a row or a column is out of bounds!"),
                    HttpStatus.BAD_REQUEST);
        }

        for (int i = 0; i < getCinemaRoom().getAvailableSeats().size(); i++) {
            Seat tempSeat = getCinemaRoom().getAvailableSeats().get(i);
            if (tempSeat.getRow() == seat.getRow()
                    && tempSeat.getColumn() == seat.getColumn()) {
                Ticket ticket = new Ticket(UUID.randomUUID().toString(), tempSeat);
                cinemaService.getTicketList().add(ticket);
                cinemaService.getSeatList().remove(i);
                cinemaService.getStatistics().sellTicket(ticket.getSeat().getPrice());
                return ResponseEntity.ok().body(ticket);
            }
        }
        return new ResponseEntity<>(new ErrorRequest("The ticket has been already purchased!"),
                HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/return")
    public ResponseEntity<Object> returnTicket(@RequestBody Ticket ticket) {

        for (int i = 0; i < cinemaService.getTicketList().size(); i++) {
            if (ticket.getToken().equals(cinemaService.getTicketList().get(i).getToken())) {
                Ticket tempTicket = cinemaService.getTicketList().get(i);
                cinemaService.getTicketList().remove(tempTicket);
                cinemaService.getSeatList().add(tempTicket.getSeat());
                cinemaService.getStatistics().returnTicket(tempTicket.getSeat().getPrice());
                return ResponseEntity.ok(Map.of("returned_ticket", tempTicket.getSeat()));
            }
        }
        return ResponseEntity.badRequest().body(new ErrorRequest("Wrong token!"));
    }

    @PostMapping("/stats")
    public ResponseEntity<Object> getStatistics
            (@RequestParam(value = "password", required = false) String password) {
        if (!cinemaService.getPassword().equals(password)) {
            return new ResponseEntity<>(new ErrorRequest("The password is wrong!"), HttpStatus.UNAUTHORIZED);
        }
        return ResponseEntity.ok(cinemaService.getStatistics());
    }
}
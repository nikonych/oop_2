package org.example.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.example.enums.BetStatus;
import org.example.enums.BetType;

@Entity
@Table(name = "bets")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class Bet {
    public Bet(User user, Integer cash, Double bet, Horse horse, BetType betType) {
        this.user = user;
        this.cash = cash;
        this.bet = bet;
        this.horse = horse;
        this.betType = betType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    User user;

    Integer cash;

    Double bet;

    @ManyToOne
    Horse horse;

    BetType betType;

    BetStatus betStatus = BetStatus.ACTIVE;

    public Bet() {

    }
}

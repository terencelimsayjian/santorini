package com.terence.santorini.game;

import com.terence.santorini.gamelogic.SantoriniBoard;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;

@Entity
@Data
public class Game {

  @Id
  @GeneratedValue(generator = "prefix-uuid-generator")
  @GenericGenerator(name ="prefix-uuid-generator", parameters = @Parameter(name = "prefix", value = "gme"), strategy = "com.terence.santorini.persistence.PrefixUUIDGenerator")
  private String id;

  private String gameBoard;

  private String player1;

  private String player2;

  private String currentPlayer;

  private String winner;

  private Instant createdAt;

  private Instant updatedAt;

}

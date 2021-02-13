package com.terence.santorini.game;

import com.terence.santorini.gamelogic.JsonGameRepresentation;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Instant;

@Entity
@Getter
@Setter
public class Game extends BaseEntity {

  @Id
  @GeneratedValue(generator = "prefix-uuid-generator")
  @GenericGenerator(name ="prefix-uuid-generator", parameters = @Parameter(name = "prefix", value = "gme"), strategy = "com.terence.santorini.persistence.PrefixUUIDGenerator")
  private String id;

  @Type(type = "jsonb")
  @Column(name = "game_board", columnDefinition = "jsonb")
//  @Convert(converter = GameBoardConverter.class)
  private JsonGameRepresentation gameBoard;

  @Column(name = "player_1")
  private String player1;

  @Column(name = "player_2")
  private String player2;

  @Column(name = "current_player")
  private String currentPlayer;

  @Column(name = "winner")
  private String winner;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

}

package com.terence.santorini.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class OutputMessage {
  private String from;
  private String text;
  private String time;
}

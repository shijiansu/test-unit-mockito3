package test.java.mockito3.first_try._3_doreturn_vs_thenreturn;

import lombok.Data;

@Data
public class Intent {
  String action;

  public Intent(String action) {
    this.action = action;
  }
}

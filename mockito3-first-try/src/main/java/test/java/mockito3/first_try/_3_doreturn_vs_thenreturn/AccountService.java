package test.java.mockito3.first_try._3_doreturn_vs_thenreturn;

public class AccountService extends IntentService {
  String account;

  public AccountService(String account) {
    this.account = account;
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    if ("DELETE".equals(intent.getAction())) {
      sendDeleteRequest();
    }
  }

  protected void sendDeleteRequest() {
    // Network access
    System.out.println("Require network access!!!");
  }
}

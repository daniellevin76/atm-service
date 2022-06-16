import java.io.IOException;

public class ATMService {

    private BankService bankService;

    private UserInput userInput;

    private CashService cashService;
    public boolean loggedIn, cardLocked;

    public boolean isCardLocked() {
        return cardLocked;
    }

    public ATMService(BankService bankService, UserInput userInput, CashService cashService



    ) {
        this.bankService = bankService;
        this.userInput = userInput;
        this.cashService = cashService;
    }
    public void insertCard(String cardId) throws IOException {
        cardLocked = bankService.checkIfCardLocked(cardId);
        if(cardLocked){
            displayCardStatus();
        }else{
            displayBankName(cardId);
            logIn(cardId);
        }
    }

    public String displayBankName(String cardId) {
        return BankService.retrieveBankName(cardId);
    }

    public void logIn(String cardId) throws IOException {
    int nrOfTries = 1;
        do {
           nrOfTries ++;
          //  nrOfTries = bankService.getNumberOfLoginTries();
           String pin = userInput.getPinFromUser();
            loggedIn = authenticateUser(cardId, pin);
           // nrOfTries += 1;
            bankService.sendNumberOfLoginTries(nrOfTries);
            System.out.println("nrOfTries " + nrOfTries);
            System.out.println(loggedIn);

        } while (!loggedIn && nrOfTries < 3);

        if (nrOfTries == 3) {
            displayMessage(nrOfTries);
            cardLocked = true;
            bankService.lockCard(cardId);
        }
        handleCash(loggedIn);
    }

    public void handleCash(boolean loggedIn){
        if (loggedIn) {
            System.out.println("In handle cash " + loggedIn);
            displayServiceOptions();
            String choice = userInput.getChoice();
            cashService.chooseServiceType(CashService.ServiceType.valueOf(choice));
        }
    }
    private String displayServiceOptions() {
        return "CASH withdrawal \n CASH deposit \n BALANCE check";
    }

    public BankService getBankService() {
        return bankService;
    }
    public void setBankService(BankService bankService) {
        this.bankService = bankService;
    }
    public User retrieveUser(String cardId) {
        return bankService.retrieveUserByCardId(cardId);

    }
    //Authenticate user by contacting the bank
    public boolean authenticateUser(String cardId, String pin) {
        return bankService.authenticateUser(cardId, pin);

    }


    public String displayMessage(int tries) {
        String message = "";
        if(tries<3){
             message = "Number of tries: " + tries;
        } else{
        message = "You're locked out";
    }
    return message;
}

    public String displayCardStatus() {
        return "Sorry, your card is blocked";
    }



    public String exit() {
        bankService.disConnect();
        loggedIn = false;
        ejectCard();
      return "Bye";
    }

    private void ejectCard() {
    }
}

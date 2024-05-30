import java.math.BigDecimal;
import java.math.RoundingMode;

// Abstract class Deposit
abstract class Deposit {
    private BigDecimal money;
    private int period;

    public Deposit(double depositAmount, int depositPeriod) {
        this.money = BigDecimal.valueOf(depositAmount);
        this.period = depositPeriod;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public int getPeriod() {
        return period;
    }

    public abstract BigDecimal income();
}

// BaseDeposit class
class BaseDeposit extends Deposit {
    public BaseDeposit(double depositAmount, int depositPeriod) {
        super(depositAmount, depositPeriod);
    }

    @Override
    public BigDecimal income() {
        BigDecimal amount = getMoney();
        for (int i = 0; i < getPeriod(); i++) {
            amount = amount.add(amount.multiply(BigDecimal.valueOf(0.05)).setScale(2, RoundingMode.HALF_UP));
        }
        return amount.subtract(getMoney()).setScale(2, RoundingMode.HALF_UP);
    }
}

// SpecialDeposit class
class SpecialDeposit extends Deposit {
    public SpecialDeposit(double depositAmount, int depositPeriod) {
        super(depositAmount, depositPeriod);
    }

    @Override
    public BigDecimal income() {
        BigDecimal amount = getMoney();
        for (int i = 1; i <= getPeriod(); i++) {
            amount = amount.add(amount.multiply(BigDecimal.valueOf(i / 100.0)).setScale(2, RoundingMode.HALF_UP));
        }
        return amount.subtract(getMoney()).setScale(2, RoundingMode.HALF_UP);
    }
}

// LongDeposit class
class LongDeposit extends Deposit {
    public LongDeposit(double depositAmount, int depositPeriod) {
        super(depositAmount, depositPeriod);
    }

    @Override
    public BigDecimal income() {
        BigDecimal amount = getMoney();
        if (getPeriod() <= 6) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }
        for (int i = 7; i <= getPeriod(); i++) {
            amount = amount.add(amount.multiply(BigDecimal.valueOf(0.15)).setScale(2, RoundingMode.HALF_UP));
        }
        return amount.subtract(getMoney()).setScale(2, RoundingMode.HALF_UP);
    }
}

// Client class
class Client {
    private Deposit[] deposits;
    private int depositCount;

    public Client() {
        this.deposits = new Deposit[10];
        this.depositCount = 0;
    }

    public boolean addDeposit(Deposit deposit) {
        if (depositCount < deposits.length) {
            deposits[depositCount++] = deposit;
            return true;
        }
        return false;
    }

    public BigDecimal totalIncome() {
        BigDecimal totalIncome = BigDecimal.ZERO;
        for (Deposit deposit : deposits) {
            if (deposit != null) {
                totalIncome = totalIncome.add(deposit.income());
            }
        }
        return totalIncome.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal maxIncome() {
        BigDecimal maxIncome = BigDecimal.ZERO;
        for (Deposit deposit : deposits) {
            if (deposit != null) {
                BigDecimal income = deposit.income();
                if (income.compareTo(maxIncome) > 0) {
                    maxIncome = income;
                }
            }
        }
        return maxIncome.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal getIncomeByNumber(int number) {
        if (number >= 0 && number < deposits.length && deposits[number] != null) {
            return deposits[number].income().setScale(2, RoundingMode.HALF_UP);
        }
        return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.addDeposit(new BaseDeposit(1000, 3));
        client.addDeposit(new SpecialDeposit(1000, 3));
        client.addDeposit(new LongDeposit(1000, 9));

        System.out.println("Total Income: " + client.totalIncome());
        System.out.println("Max Income: " + client.maxIncome());
        System.out.println("Income from deposit 1: " + client.getIncomeByNumber(1));
    }
}

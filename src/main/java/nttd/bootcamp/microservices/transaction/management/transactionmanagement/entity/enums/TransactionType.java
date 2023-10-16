package nttd.bootcamp.microservices.transaction.management.transactionmanagement.entity.enums;

public enum TransactionType {
    DEPOSIT("01","deposit"),
    WITHDRAWAL("02","withdrawal");

    private final String code;
    private final String name;

    TransactionType(String code, String name) {
        this.code = code;
        this.name = name;
    }

}

package nttd.bootcamp.microservices.transaction.management.transactionmanagement.dto;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransactionDto {
    private String id;
    private String transactionId;
    private String accountId;
    private String type;
    private String amount;
    private Date transactionDate;
}

package nttd.bootcamp.microservices.transaction.management.transactionmanagement.service;

import nttd.bootcamp.microservices.transaction.management.transactionmanagement.dto.TransactionOperationDto;
import nttd.bootcamp.microservices.transaction.management.transactionmanagement.entity.TransactionEntity;
import nttd.bootcamp.microservices.transaction.management.transactionmanagement.exceptions.InsufficientFundsException;
import nttd.bootcamp.microservices.transaction.management.transactionmanagement.exceptions.InvalidTransactionTypeException;
import nttd.bootcamp.microservices.transaction.management.transactionmanagement.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;


    @Transactional
    public Mono<TransactionEntity> performTransaction(String type, TransactionOperationDto transactionOperationDto){
        return accountService.findAccountById(transactionOperationDto.getAccountId())
                .flatMap(account -> {
                   if (type.equals("deposit")){
                       account.setBalance(account.getBalance() + transactionOperationDto.getAmount());
                   }else if (type.equals("withdrawal")){
                       if (account.getBalance()<transactionOperationDto.getAmount()){
                           return Mono.error(new InsufficientFundsException("Fondos insuficientes"));
                       }
                       account.setBalance(account.getBalance()-transactionOperationDto.getAmount());
                   }else {
                       return Mono.error(new InvalidTransactionTypeException("Tipo de transacción no válido"));
                   }

                   TransactionEntity transactionEntity = new TransactionEntity();
                   transactionEntity.setAccountId(transactionOperationDto.getAccountId());
                   transactionEntity.setType(type);
                   transactionEntity.setAmount(transactionOperationDto.getAmount());
                   transactionEntity.setTransactionDate(LocalDateTime.now());
                   return transactionRepository.save(transactionEntity)
                           .flatMap(savedTransaction -> accountService
                                   .modifyAccountBalance(transactionOperationDto.getAccountId(),account).thenReturn(savedTransaction));
                });
    }


}

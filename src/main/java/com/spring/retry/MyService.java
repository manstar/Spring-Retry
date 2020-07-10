package com.spring.retry;

import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLSyntaxErrorException;

@Service
public class MyService {

    static int retryCount = 0;
    static int retryCount2 = 0;
    static int retryCount3 = 0;

    @Retryable(
            value = {SQLException.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 2000))
    int countContents() throws SQLException {
        retryCount++;
        System.out.println("retryCount " + retryCount);
        if (retryCount == 2) {
            return 100;

        } else {
            throw new SQLException();
        }
    }

    @Retryable(
            value = {SQLSyntaxErrorException.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 2000))
    int insertContents() throws SQLSyntaxErrorException {
        retryCount2++;
        System.out.println("retryCount " + retryCount2);
        throw new SQLSyntaxErrorException();
    }

    @Retryable(
            value = {SQLIntegrityConstraintViolationException.class},
            maxAttempts = 4,
            backoff = @Backoff(delay = 2000))
    int deleteContents(String sql) throws SQLIntegrityConstraintViolationException {
        retryCount3++;
        System.out.println("retryCount " + retryCount3);
        throw new SQLIntegrityConstraintViolationException();
    }

    @Recover
    public int recover(SQLIntegrityConstraintViolationException e, String sql) {
        System.out.println("Recover called : message=" + e.getMessage() + ", sql=" + sql);
        return 50;
    }

    /// retryTemplate에서 사용하는 메서드
    int countContentsForRetryTemplate() {
        try {
            retryCount++;
            System.out.println("retryCount " + retryCount);
            if (retryCount == 2)
                return 100;
            else
                throw new ArithmeticException();
        } catch(Exception ex) {
            throw new ArithmeticException();
        }
    }

}

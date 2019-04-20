/**
 * @author: Yi
 * @className: OverdraftException.java
 * @packageName: banking.server
 * @date: 2018-10-09    15:40
 * @description: This file is an exception created by myself, which is throwable when can't afford the amount.
 */

package banking.server;

class OverdraftException extends Exception {
    private final double deficit;


    /**
     * @return the value of deficit
     */
    double getDeficit() {
        return deficit;
    }


    /**
     * @param message: the message stored in the exception
     * @param deficit: the value of deficit
     */
    OverdraftException(String message, double deficit) {
        super(message);
        this.deficit = deficit;
    }
}

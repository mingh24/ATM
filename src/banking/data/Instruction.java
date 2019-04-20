/**
 * @author: Yi
 * @className: Instruction.java
 * @packageName: banking.data
 * @date: 2018-11-24    14:50
 * @description: This interface contains the instructions pass by among server and clients.
 */

package banking.data;

public interface Instruction {
    String LOGIN = "LOGIN";
    String FORCE_EXIT = "FORCE_EXIT";
    String TRANSFER = "TRANSFER";
    String REFUND = "REFUND";
    String QUERY = "QUERY";
    String WITHDRAW = "WITHDRAW";
    String DEPOSIT = "DEPOSIT";
    String MODIFY_PSW = "MODIFY_PSW";

    String SUCCESS = "SUCCESS";
    String ERROR = "ERROR";

    String YES = "YES";
    String NO = "NO";
}

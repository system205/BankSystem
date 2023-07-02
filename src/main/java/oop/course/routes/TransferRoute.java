package oop.course.routes;

import oop.course.implementations.*;
import oop.course.interfaces.*;
import oop.course.tools.implementations.*;
import oop.course.tools.interfaces.*;

public class TransferRoute implements Route {
    @Override
    public Response act(Request request) {
        // take current account number and another account number
        // check with the token that a user is eligible to specify this current account
        // tell the database to perform a transaction to transfer money from one account to another
        // if success return response 200

//        // Another process
//        String token = request.headers().stream()
//                .filter(header -> header.startsWith("Authorization"))
//                .map(s -> s.substring("Authorization: Bearer ".length()))
//                .findFirst().orElseThrow(() -> new RuntimeException("There is not authorization when performing a transaction"));
//        if (!this.validator.verify(token, senderAccount)) {
//            return new IllegalAccessResponse("You do not have access to the account: " + senderAccount);
//        }

        // Probably another process
//        StringBuilder body = new StringBuilder();
//        for (String line : request.body()) {
//            body.append(line);
//        }
//        Form form = new JsonForm(body.toString());
//        String senderAccount = form.stringField("senderAccount");
//        String receiverAccount = form.stringField("receiverAccount");
        
//        Transaction transaction = new TransactionDB();
//        Response resp = transaction.transfer(senderAccount, receiverAccount);
        return new EmptyResponse();
    }

    @Override
    public boolean accept(String path) {
        return "/transfer".equals(path);
    }
}

package com.lp.akka.notes.remote;


import akka.actor.UntypedActor;
import akka.remote.AssociationErrorEvent;
import akka.remote.transport.Transport;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * @author li.pan
 * @title flink源码中使用Akka {@link https://www.codota.com/web/assistant/code/rs/5c7441cf49efcb0001ecfe13#L55}
 */
public class QuarantineMonitor extends UntypedActor {

    private static final Pattern pattern = Pattern.compile("^Invalid address:\\s+(.*)$");

    private static final String QUARANTINE_MSG = "The remote system has a UID that has been quarantined. Association aborted.";
    private static final String QUARANTINED_MSG = "The remote system has quarantined this system. No further associations to the remote system are possible until this system is restarted.";



    @Override
    public void preStart() {
        // 订阅,AssociationErrorEvent当连接错误会触发该事件事件信息除了包括本地、远程地址还有错误细腻下
        getContext().system().eventStream().subscribe(getSelf(), AssociationErrorEvent.class);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof AssociationErrorEvent) {
            AssociationErrorEvent associationErrorEvent = (AssociationErrorEvent) message;

            // IMPORTANT: The check for the quarantining event is highly specific to Akka 2.3.7
            // and can change with a different Akka version.
            // It assumes the following:
            // AssociationErrorEvent(InvalidAssociation(InvalidAssociationException(QUARANTINE(D)_MSG))
            if (associationErrorEvent.getCause() != null) {
                Throwable invalidAssociation = associationErrorEvent.getCause();
                Matcher matcher = pattern.matcher(invalidAssociation.getMessage());

                final String remoteSystem;

                if (matcher.find()) {
                    remoteSystem = matcher.group(1);
                } else {
                    remoteSystem = "Unknown";
                }

                if (invalidAssociation.getCause() instanceof Transport.InvalidAssociationException) {
                    Transport.InvalidAssociationException invalidAssociationException = (Transport.InvalidAssociationException) invalidAssociation.getCause();

                    // don't hate the player, hate the game! That's the only way to find out if we
                    // got quarantined or quarantined another actor system in Akka 2.3.7
                    if (QUARANTINE_MSG.equals(invalidAssociationException.getMessage())) {

                    } else if (QUARANTINED_MSG.equals(invalidAssociationException.getMessage())) {

                    } else {
                        System.out.println("The invalid association exception's message could not be matched.");
                    }
                } else {
                    System.out.println("The association error event's root cause is not of type {}.");
                }
            } else {
                System.out.println("Received association error event which did not contain a cause.");
            }
        }
    }
}
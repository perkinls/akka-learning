package com.lp.akka.notes;


/**
 * 消息
 * @author lipan
 */
public class Messages {
    /**
     * 用户加入聊天室
     */
    static public class JoinChatroom {
        public final UserRef userRef;

        public JoinChatroom(UserRef userRef) {
            this.userRef = userRef;
        }
    }

    /**
     * 发送消息到聊天室
     */
    static public class PostToChatroom{
        public final String line, username;

        public PostToChatroom(String line, String username) {
            this.line = line;
            this.username = username;
        }
    }
}

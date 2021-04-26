package com.lp.akka.notes.clientactor.statepo;

/**
* Disconnect msg在本例中未实现。因为我们不是直接处理w/套接字，所以我们可以实现一个偶然的ping/heartbeat，
 * 如果远程参与者没有响应，它会重新启动这个参与者。重启后，参与者将恢复到默认状态并存储消息
*/

class Disconnected {
}

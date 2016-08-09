package com.companyname.bank.demo2;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class SimpleChatServerHandler extends IoHandlerAdapter { // (1)

    private final Set<IoSession> sessions = Collections
            .synchronizedSet(new HashSet<IoSession>()); //  (2)

    @Override
    public void sessionCreated(IoSession session) throws Exception {// (3)
        sessions.add(session);
        broadcast(" has join the chat", session);
    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {// (4)
        sessions.remove(session);
        broadcast(" has left the chat", session);
    }

    @Override
    public void messageReceived(IoSession session, Object message)
            throws Exception {// (5)
        String str = message.toString();
        broadcast(str, session);
    }

    @Override
    public void sessionIdle(IoSession session, IdleStatus status)
            throws Exception {// (6)
        System.out.println("[Server] IDLE " + session.getRemoteAddress()
                + session.getIdleCount(status));
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) {
        cause.printStackTrace();// (7)
        System.out.println("[Server] Client:" + session.getRemoteAddress()
                + "异常");
        // 遇到未捕获的异常，则关闭连接
        session.close(true);
    }

    /**
     * 广播消息
     *
     * @param message
     */
    private void broadcast(String message, IoSession exceptSession) {// (8)
        synchronized (sessions) {
            for (IoSession session : sessions) {
                if (session.isConnected()) {
                    if (session.equals(exceptSession)) {
                        session.write("[You]" + message);
                    } else {
                        session.write("[Client" + session.getRemoteAddress()
                                + "] " + message);
                    }

                }
            }
        }
    }
}
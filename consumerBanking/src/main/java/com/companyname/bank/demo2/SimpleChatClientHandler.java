package com.companyname.bank.demo2;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

public class SimpleChatClientHandler extends IoHandlerAdapter {
    @Override
    public void messageReceived(IoSession session, Object message) {
        String str = message.toString();
        System.out.println(str);
    }
}
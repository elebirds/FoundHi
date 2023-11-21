package net.mcpes.foundhi.core.network.server.listener;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.ssl.SslContext;

import java.nio.charset.Charset;

/**
 * @author hhm @ SGameCore Project
 **/
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    private final SslContext sslCtx;
    private ServerHandler handler;

    ServerInitializer(SslContext sslCtx) {
        this.sslCtx = sslCtx;
    }

    public ServerHandler getHandler() {
        return handler;
    }

    @Override
    public void initChannel(SocketChannel ch) {
        ChannelPipeline pipeline = ch.pipeline();
        handler = new ServerHandler();
        // Add SSL handler first to encrypt and decrypt everything.
        // In this example, we use a bogus certificate in the server side
        // and accept any invalid certificates in the client side.
        // You will need something more complicated to identify both
        // and server in the real world.
        pipeline.addLast(sslCtx.newHandler(ch.alloc()));
        // On top of the SSL handler, add the text line codec.
        pipeline.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
        pipeline.addLast(new StringDecoder(Charset.forName("UTF-8")));
        pipeline.addLast(new StringEncoder(Charset.forName("UTF-8")));
        // and then business logic.
        pipeline.addLast(handler);
    }
}

/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.common;

import com.wgcloud.common.NettyServerInitializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

public class NettyServer {
    private static final AtomicBoolean STARTED = new AtomicBoolean(false);
    private static EventLoopGroup bossGroup;
    private static EventLoopGroup workerGroup;
    private static Channel serverChannel;

    public static void start(int port) throws InterruptedException {
        if (!STARTED.compareAndSet(false, true)) {
            return;
        }
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            ((ServerBootstrap)serverBootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)).childHandler((ChannelHandler)new NettyServerInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            serverChannel = channelFuture.channel();
            serverChannel.closeFuture().sync();
        }
        finally {
            NettyServer.stop();
        }
    }

    public static void startAsync(int port) {
        Thread nettyThread = new Thread(() -> {
            try {
                NettyServer.start(port);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "web-ssh-netty-server");
        nettyThread.setDaemon(true);
        nettyThread.start();
    }

    public static void stop() {
        STARTED.set(false);
        if (serverChannel != null) {
            serverChannel.close();
            serverChannel = null;
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
            bossGroup = null;
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
            workerGroup = null;
        }
    }
}


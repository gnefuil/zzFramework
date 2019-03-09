package com.javapex.zz.web.bootstrap;

import com.javapex.zz.web.bean.ZzBeanManager;
import com.javapex.zz.web.config.AppConfig;
import com.javapex.zz.web.configuration.ApplicationConfiguration;
import com.javapex.zz.web.constant.ZzConstant;
import com.javapex.zz.web.context.ZzContext;
import com.javapex.zz.web.init.ZzInitializer;
import com.javapex.zz.web.log.LoggerBuilder;
import com.javapex.zz.web.thread.ThreadLocalHolder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;

import static com.javapex.zz.web.configuration.ConfigurationHolder.getConfiguration;
import static com.javapex.zz.web.constant.ZzConstant.SystemProperties.APPLICATION_THREAD_SHUTDOWN_NAME;
import static com.javapex.zz.web.constant.ZzConstant.SystemProperties.APPLICATION_THREAD_WORK_NAME;

public class NettyBootStrap {

    private final static Logger LOGGER = LoggerBuilder.getLogger(NettyBootStrap.class);

    private static AppConfig appConfig = AppConfig.getInstance() ;
    private static EventLoopGroup boss = new NioEventLoopGroup(1,new DefaultThreadFactory("boss"));
    private static EventLoopGroup work = new NioEventLoopGroup(0,new DefaultThreadFactory(APPLICATION_THREAD_WORK_NAME));
    private static Channel channel ;

    /**
     * Start netty Server
     *
     * @throws Exception
     */
    public static void startZz() throws Exception {
        // start
        startServer();

        // register shutdown hook
        shutDownServer();

        // synchronized channel
        joinServer();
    }

    /**
     * start netty server
     * @throws InterruptedException
     */
    private static void startServer() throws InterruptedException {
        ServerBootstrap bootstrap = new ServerBootstrap()
                .group(boss, work)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ZzInitializer());

        ChannelFuture future = bootstrap.bind(AppConfig.getInstance().getPort()).sync();
        if (future.isSuccess()) {
            appLog();
        }
        channel = future.channel();
    }

    private static void joinServer() throws Exception {
        channel.closeFuture().sync();
    }

    private static void appLog() {
        Long start = ThreadLocalHolder.getLocalTime();
        ApplicationConfiguration applicationConfiguration = (ApplicationConfiguration) getConfiguration(ApplicationConfiguration.class);
        long end = System.currentTimeMillis();
        LOGGER.info("Zz started on port: {}.cost {}ms", applicationConfiguration.get(ZzConstant.ZZ_PORT), end - start);
        LOGGER.info(">> access http://{}:{}{} <<","127.0.0.1",appConfig.getPort(),appConfig.getRootPath());
    }

    /**
     * shutdown server
     */
    private static void shutDownServer() {
        ShutDownThread shutDownThread = new ShutDownThread();
        shutDownThread.setName(APPLICATION_THREAD_SHUTDOWN_NAME);
        Runtime.getRuntime().addShutdownHook(shutDownThread);
    }

    private static class ShutDownThread extends Thread {
        @Override
        public void run() {
            LOGGER.info("Zz server stop...");
            ZzContext.removeContext();

            ZzBeanManager.getInstance().releaseBean();

            boss.shutdownGracefully();
            work.shutdownGracefully();

            LOGGER.info("Zz server has been successfully stopped.");
        }

    }
}

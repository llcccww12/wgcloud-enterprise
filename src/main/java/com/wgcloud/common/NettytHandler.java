/*
 * Decompiled with CFR 0.152.
 */
package com.wgcloud.common;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.wgcloud.util.staticvar.StaticKeys;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.GlobalEventExecutor;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettytHandler
extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private static final Logger logger = LoggerFactory.getLogger(NettytHandler.class);
    public static final String HANDLE_OPERATE = "handle";
    public static final String HANDLE_VALUE = "value";
    public static final Integer CONNECTION_OUT = 30000;
    public static final String ENTER_VAL = "\r";
    public static final String LINE_NEXT_VAL = "\n";
    public static final int MAX_ACTIVE_TERMINALS = 100;
    public static ChannelGroup channelGroup = new DefaultChannelGroup((EventExecutor)GlobalEventExecutor.INSTANCE);
    public static Map<String, ChannelShell> MAP_SSH_SESSION = Collections.synchronizedMap(new ConcurrentHashMap());
    public static Map<String, StringBuilder> MAP_CMD_BUFFER = Collections.synchronizedMap(new ConcurrentHashMap());
    public static Map<String, Boolean> MAP_READER_STARTED = Collections.synchronizedMap(new ConcurrentHashMap());
    private static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(16);

    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        String channelId = ctx.channel().id().toString();
        String msgJSonStr = msg.text();
        if (StringUtils.isEmpty((CharSequence)msgJSonStr)) {
            return;
        }
        JSONObject msgJson = JSONUtil.parseObj((String)msgJSonStr);
        if ("connect".equals(msgJson.getStr(HANDLE_OPERATE))) {
            EXECUTOR.submit(() -> {
                try {
                    Integer sshPort = Integer.valueOf(msgJson.getStr("port"));
                    ChannelShell channelShell = NettytHandler.getSSHChannel(channelId, msgJson.getStr("ip"), msgJson.getStr("user"), msgJson.getStr("pwd"), sshPort, msgJson.getStr("priKeyBasePath"), msgJson.getInt("cols"), msgJson.getInt("rows"));
                    NettytHandler.startReadLoopIfNeeded(ctx, channelShell);
                    NettytHandler.writeInput(ctx, ENTER_VAL);
                }
                catch (Exception e) {
                    ctx.writeAndFlush((Object)new TextWebSocketFrame("\nSSH\u8fde\u63a5\u5931\u8d25: " + e.getMessage()));
                    logger.error("ssh\u7ec8\u7aef\u8fde\u63a5\u9519\u8bef", (Throwable)e);
                }
            });
        } else if ("cmd".equals(msgJson.getStr(HANDLE_OPERATE))) {
            String cmdStr = msgJson.getStr(HANDLE_VALUE);
            if (StringUtils.isEmpty((CharSequence)cmdStr)) {
                return;
            }
            try {
                NettytHandler.appendCommandAudit(channelId, cmdStr);
            }
            catch (Exception e) {
                logger.error("ssh\u547d\u4ee4\u5ba1\u8ba1\u8bb0\u5f55\u9519\u8bef, channelId={}", (Object)channelId, (Object)e);
            }
            EXECUTOR.submit(() -> NettytHandler.writeInput(ctx, cmdStr));
        } else if ("resize".equals(msgJson.getStr(HANDLE_OPERATE))) {
            EXECUTOR.submit(() -> {
                ChannelShell channelShell = MAP_SSH_SESSION.get(channelId);
                if (channelShell == null) {
                    return;
                }
                Integer cols = msgJson.getInt("cols");
                Integer rows = msgJson.getInt("rows");
                if (cols == null || rows == null || cols <= 0 || rows <= 0) {
                    return;
                }
                try {
                    channelShell.setPtySize(cols.intValue(), rows.intValue(), cols * 9, rows * 17);
                }
                catch (Exception e) {
                    logger.error("ssh\u7ec8\u7aefresize\u9519\u8bef", (Throwable)e);
                }
            });
        }
    }

    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        if (channelGroup.size() >= 100) {
            ctx.writeAndFlush((Object)new TextWebSocketFrame("\u5f53\u524dWeb SSH\u8fde\u63a5\u6570\u8fc7\u591a\uff0c\u8bf7\u7a0d\u540e\u91cd\u8bd5\u3002"));
            ctx.close();
            return;
        }
        channelGroup.add(ctx.channel());
        logger.info(ctx.channel().remoteAddress() + "ssh\u7ec8\u7aef\u4e0a\u7ebf\u4e86!");
    }

    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        channelGroup.remove((Object)ctx.channel());
        logger.info(ctx.channel().remoteAddress() + "ssh\u7ec8\u7aef\u65ad\u5f00\u8fde\u63a5");
        NettytHandler.cleanupSession(ctx.channel().id().toString());
    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("web ssh\u8fde\u63a5\u5f02\u5e38, channelId={}", (Object)ctx.channel().id().toString(), (Object)cause);
        NettytHandler.cleanupSession(ctx.channel().id().toString());
        ctx.channel().close();
    }

    private static void cleanupSession(String channelId) {
        try {
            ChannelShell channelShell = MAP_SSH_SESSION.get(channelId);
            if (channelShell != null) {
                Session session = channelShell.getSession();
                if (channelShell != null) {
                    channelShell.disconnect();
                }
                if (session != null) {
                    session.disconnect();
                }
                MAP_SSH_SESSION.remove(channelId);
            }
        }
        catch (Exception e) {
            logger.error("ssh\u4f1a\u8bdd\u6e05\u7406\u9519\u8bef, channelId={}", (Object)channelId, (Object)e);
        }
        finally {
            MAP_CMD_BUFFER.remove(channelId);
            MAP_READER_STARTED.remove(channelId);
        }
    }

    private static ChannelShell getSSHChannel(String channelId, String host, String user, String password, Integer port, String priKeyBasePath, Integer cols, Integer rows) throws JSchException {
        ChannelShell channelShell = MAP_SSH_SESSION.get(channelId);
        if (channelShell != null) {
            return channelShell;
        }
        JSch jsch = new JSch();
        Session session = jsch.getSession(user, host, port.intValue());
        if (!StringUtils.isEmpty((CharSequence)password)) {
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
        } else {
            logger.debug("priKeyBasePath-----------" + StaticKeys.JAR_PATH + "/" + priKeyBasePath);
            jsch.addIdentity(StaticKeys.JAR_PATH + "/" + priKeyBasePath);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
        }
        session.connect(CONNECTION_OUT.intValue());
        session.setTimeout(600000);
        channelShell = (ChannelShell)session.openChannel("shell");
        channelShell.setPty(true);
        channelShell.setPtyType("xterm-256color");
        int ptyCols = cols == null || cols <= 0 ? 120 : cols;
        int ptyRows = rows == null || rows <= 0 ? 30 : rows;
        channelShell.setPtySize(ptyCols, ptyRows, ptyCols * 9, ptyRows * 17);
        channelShell.connect(CONNECTION_OUT.intValue());
        MAP_SSH_SESSION.put(channelId, channelShell);
        return channelShell;
    }

    private static void writeInput(ChannelHandlerContext ctx, String input) {
        try {
            ChannelShell channelShell = MAP_SSH_SESSION.get(ctx.channel().id().toString());
            if (null == channelShell) {
                ctx.writeAndFlush((Object)new TextWebSocketFrame("\r\n~$ \u4f1a\u8bdd\u4e0d\u5b58\u5728\uff0c\u8bf7\u91cd\u65b0\u8fde\u63a5\u3002"));
                return;
            }
            OutputStream outputStream = channelShell.getOutputStream();
            outputStream.write(input.getBytes("UTF-8"));
            outputStream.flush();
        }
        catch (Exception e) {
            logger.error("ssh\u8f93\u5165\u900f\u4f20\u9519\u8bef", (Throwable)e);
        }
    }

    private static void appendCommandAudit(String channelId, String input) {
        if (StringUtils.isEmpty((CharSequence)input) || input.contains("\u001b")) {
            return;
        }
        StringBuilder cmdBuffer = MAP_CMD_BUFFER.get(channelId);
        if (cmdBuffer == null) {
            cmdBuffer = new StringBuilder();
            MAP_CMD_BUFFER.put(channelId, cmdBuffer);
        }
        String[] segments = input.split(ENTER_VAL, -1);
        for (int index = 0; index < segments.length; ++index) {
            String segment = segments[index];
            for (int i = 0; i < segment.length(); ++i) {
                char ch = segment.charAt(i);
                if (ch == '\u007f' || ch == '\b') {
                    if (cmdBuffer.length() <= 0) continue;
                    cmdBuffer.deleteCharAt(cmdBuffer.length() - 1);
                    continue;
                }
                if (ch != '\t' && ch < ' ') continue;
                cmdBuffer.append(ch);
            }
            if (index >= segments.length - 1) continue;
            NettytHandler.logCommand(channelId, cmdBuffer.toString());
            cmdBuffer.setLength(0);
        }
    }

    private static void logCommand(String channelId, String command) {
        String finalCmd = StringUtils.trimToEmpty((String)command);
        if (StringUtils.isEmpty((CharSequence)finalCmd)) {
            return;
        }
        try {
            ChannelShell channelShell = MAP_SSH_SESSION.get(channelId);
            if (channelShell != null && channelShell.getSession() != null) {
                Session session = channelShell.getSession();
                logger.info("web ssh\u6267\u884c\u547d\u4ee4, host={}, user={}, channelId={}, cmd={}", new Object[]{session.getHost(), session.getUserName(), channelId, finalCmd});
                return;
            }
            logger.info("web ssh\u6267\u884c\u547d\u4ee4, channelId={}, cmd={}", (Object)channelId, (Object)finalCmd);
        }
        catch (Exception e) {
            logger.error("ssh\u547d\u4ee4\u65e5\u5fd7\u8f93\u51fa\u9519\u8bef, channelId={}", (Object)channelId, (Object)e);
        }
    }

    private static void startReadLoopIfNeeded(ChannelHandlerContext ctx, ChannelShell channelShell) {
        String channelId = ctx.channel().id().toString();
        if (Boolean.TRUE.equals(MAP_READER_STARTED.get(channelId))) {
            return;
        }
        MAP_READER_STARTED.put(channelId, true);
        EXECUTOR.submit(() -> {
            try {
                InputStream inputStream = channelShell.getInputStream();
                byte[] buffer = new byte[4096];
                while (ctx.channel().isActive() && !channelShell.isClosed()) {
                    int readLen = inputStream.read(buffer);
                    if (readLen < 0) {
                        break;
                    }
                    if (readLen == 0) continue;
                    String showMsg = new String(buffer, 0, readLen, "UTF-8");
                    ctx.writeAndFlush((Object)new TextWebSocketFrame(showMsg));
                }
            }
            catch (Exception e) {
                logger.error("ssh\u8f93\u51fa\u8bfb\u53d6\u9519\u8bef", (Throwable)e);
            }
            finally {
                NettytHandler.cleanupSession(channelId);
            }
        });
    }

    private static void disconnectAllSessions() {
        for (Map.Entry<String, ChannelShell> entry : MAP_SSH_SESSION.entrySet()) {
            NettytHandler.cleanupSession(entry.getKey());
        }
    }

    public static void clearOldData() {
        NettytHandler.disconnectAllSessions();
        MAP_CMD_BUFFER.clear();
        MAP_READER_STARTED.clear();
    }
}


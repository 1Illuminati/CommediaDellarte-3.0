package org.red.minecraft.dellarte.compatibility.dataServer;

import com.google.gson.JsonObject;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshakerFactory;
import io.netty.handler.codec.http.websocketx.WebSocketClientProtocolHandler;
import io.netty.handler.codec.http.websocketx.WebSocketVersion;
import org.bukkit.NamespacedKey;
import org.red.library.data.serialize.SerializeDataMap;
import org.red.minecraft.dellarte.CommediaDellartePlugin;

import java.net.URI;

public class A_DataClient {
    private final String host;
    private final int port;
    private A_DataHandler handler;
    private Channel channel;

    public A_DataClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void startClient() throws InterruptedException {
        // ============================================================
        // 서버와 다르게 클라이언트는 EventLoopGroup이 하나만 필요
        //
        // 서버 : bossGroup (연결 수락) + workerGroup (데이터 처리) → 2개
        // 클라 : workerGroup (연결 + 데이터 처리 모두) → 1개
        //
        // 클라이언트는 연결 요청을 "받는" 역할이 없기 때문
        // ============================================================
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        // ============================================================
        // WebSocketClientHandshaker
        // WebSocket 연결 시 HTTP → WebSocket 업그레이드 핸드셰이크를 처리하는 객체
        //
        // URI         : 접속할 서버 주소 (ws://host:port/path)
        // V13         : WebSocket 프로토콜 버전 (현재 표준은 V13)
        // null        : 서브프로토콜 (사용 안 하면 null)
        // true        : 확장 기능 허용 여부
        // headers     : 핸드셰이크 시 추가할 HTTP 헤더 (없으면 빈 객체)
        // 65536       : 최대 프레임 크기 (bytes)
        // ============================================================
        WebSocketClientHandshaker handshaker = WebSocketClientHandshakerFactory.newHandshaker(
                URI.create(String.format("ws://%s:%d/ws",  host, port)),
                WebSocketVersion.V13,
                null,
                true,
                new DefaultHttpHeaders(),
                65536
        );

        // 실제 메시지를 처리할 핸들러 생성 (handshaker 주입)
        this.handler = new A_DataHandler(handshaker);

        try {
            // ============================================================
            // Bootstrap
            // 서버는 ServerBootstrap, 클라이언트는 Bootstrap (일반)
            // ============================================================
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(workerGroup)

                    // NioSocketChannel = 클라이언트용 채널 타입
                    // 서버의 NioServerSocketChannel 과 대응됨
                    .channel(NioSocketChannel.class)

                    // ============================================================
                    // 파이프라인 구성
                    // 서버와 동일한 구조지만 방향이 반대
                    //
                    // [수신] HttpClientCodec → HttpObjectAggregator → MyClientHandler
                    // [송신] MyClientHandler → (역순)
                    // ============================================================
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ChannelPipeline pipeline = ch.pipeline();

                            // 서버는 HttpServerCodec, 클라이언트는 HttpClientCodec
                            pipeline.addLast(new HttpClientCodec());

                            // HTTP 메시지 조각을 하나로 합침 (서버와 동일)
                            pipeline.addLast(new HttpObjectAggregator(65536));

                            // ============================================================
                            // WebSocketClientProtocolHandler
                            // 핸드셰이크 자동 처리 (서버의 WebSocketServerProtocolHandler 와 대응)
                            // ============================================================
                            pipeline.addLast(new WebSocketClientProtocolHandler(handshaker));

                            // 내가 만든 실제 메시지 처리 핸들러
                            pipeline.addLast(handler);
                        }
                    });

            // ============================================================
            // 서버에 연결
            // 서버는 bind() (포트 열기), 클라이언트는 connect() (서버에 접속)
            //
            // sync() = 연결이 완료될 때까지 현재 스레드 대기
            // ============================================================
            this.channel = bootstrap.connect(this.host, this.port).sync().channel();
            CommediaDellartePlugin.sendLog("DataServer 서버 연결 완료");

            // ============================================================
            // 핸드셰이크가 완료될 때까지 대기
            // connect() 직후에는 아직 WebSocket 업그레이드가 안 됐을 수 있음
            // 이 Future가 완료되어야 실제로 메시지를 주고받을 수 있음
            // ============================================================
            handler.handshakeFuture().sync();
            CommediaDellartePlugin.sendLog("DataServer WebSocket 핸드셰이크 완료 - 메시지 송수신 가능");

            // 채널이 닫힐 때까지 대기
            channel.closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
        }
    }

    public void saveDataMap(NamespacedKey storageKey, String key, SerializeDataMap map) {
        JsonObject obj = new JsonObject();
        obj.addProperty("storage", storageKey.toString());
        obj.addProperty("key", key);
        obj.addProperty("data", map.toString());

        this.handler.send(channel, "SAVE", obj);
    }

    public void loadDataMap(NamespacedKey storageKey, String key) {
        JsonObject obj = new JsonObject();
        obj.addProperty("storage", storageKey.toString());
        obj.addProperty("key", key);

        this.handler.send(channel, "LOAD", obj);
    }
}

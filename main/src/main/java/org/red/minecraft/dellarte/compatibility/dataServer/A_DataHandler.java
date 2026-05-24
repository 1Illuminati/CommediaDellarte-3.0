package org.red.minecraft.dellarte.compatibility.dataServer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.*;
import org.red.minecraft.dellarte.CommediaDellartePlugin;

public class A_DataHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private final WebSocketClientHandshaker handshaker;

    // ============================================================
    // ChannelPromise
    // 핸드셰이크 완료 여부를 외부에서 감지하기 위한 Future
    // main() 에서 handler.handshakeFuture().sync() 로 대기할 때 사용
    // ============================================================
    private ChannelPromise handshakePromise;

    public A_DataHandler(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public ChannelPromise handshakeFuture() {
        return handshakePromise;
    }

    // ============================================================
    // handlerAdded
    // 이 핸들러가 파이프라인에 추가될 때 딱 한 번 호출
    // Promise 초기화는 채널이 생기기 전에 해야 하므로 여기서 수행
    // ============================================================
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakePromise = ctx.newPromise();
    }

    // ============================================================
    // channelActive
    // 서버와 TCP 연결이 맺어졌을 때 호출
    // ============================================================
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        CommediaDellartePlugin.sendLog("DataServer TCP 연결 완료: " + ctx.channel().remoteAddress());
    }

    // ============================================================
    // channelInactive
    // 서버와 연결이 끊어졌을 때 호출
    // ============================================================
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        CommediaDellartePlugin.sendLog("DataServer 연결 종료");
    }

    // ============================================================
    // userEventTriggered
    // 파이프라인 안에서 발생하는 특수 이벤트를 수신
    // WebSocketClientProtocolHandler 가 핸드셰이크 완료 시
    // HandshakeComplete 이벤트를 여기로 보냄
    // ============================================================
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
        if (evt instanceof WebSocketClientProtocolHandler.ClientHandshakeStateEvent stateEvent) {
            if (stateEvent == WebSocketClientProtocolHandler.ClientHandshakeStateEvent.HANDSHAKE_COMPLETE) {
                CommediaDellartePlugin.sendLog("DataServer HandShake 이벤트 수신");
                // main() 의 handshakeFuture().sync() 를 풀어줌
                handshakePromise.setSuccess();
            }
        }
    }

    // ============================================================
    // channelRead0
    // 서버로부터 메시지가 왔을 때 호출
    // ============================================================
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) {

        // ── 메시지 수신 예제 ──────────────────────────────────────
        if (frame instanceof TextWebSocketFrame textFrame) {
            // 텍스트 메시지 처리
            String raw = textFrame.text();
            JsonObject packet = new JsonParser().parse(raw).getAsJsonObject();

            String type = packet.get("type").getAsString();
            JsonObject data = packet.getAsJsonObject("data");

            switch (type) {
                case "UPDATE" -> {

                }
                case "DATA" -> {
                    String message = data.get("message").getAsString();
                }
                default -> CommediaDellartePlugin.sendLog("DataServer 알 수 없는 수신 - " + type);
            }

        } else if (frame instanceof PongWebSocketFrame) {
            // WebSocket 프로토콜 레벨의 Pong 프레임 (하트비트용)
            CommediaDellartePlugin.sendLog("DataServer WebSocket Pong 수신");

        } else if (frame instanceof CloseWebSocketFrame) {
            CommediaDellartePlugin.sendLog("DataServer 연결종료 수신");
            ctx.channel().close();
        }
        // ─────────────────────────────────────────────────────────
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        CommediaDellartePlugin.sendErrorLog("DataServer 에러 발생: " + cause.getMessage());
        // handshake 이전에 에러가 나면 Promise를 실패로 마킹
        if (!handshakePromise.isDone()) {
            handshakePromise.setFailure(cause);
        }
        ctx.close();
    }


    // ==============================================================
    // ██████████████████████████████████████████████████████████████
    //
    //   메시지 전송 방법 예제
    //
    // ██████████████████████████████████████████████████████████████
    // ==============================================================

    // ── 예제 1 : 기본 전송 ────────────────────────────────────────
    //
    //   send(channel, "TYPE", data)
    //
    //   channel : main() 에서 connect() 로 얻은 채널
    //   type    : 메시지 타입 문자열
    //   data    : 전송할 JsonObject 데이터
    //
    public void send(Channel channel, String type, JsonObject data) {
        if (!channel.isActive()) {
            CommediaDellartePlugin.sendErrorLog("DataServer 채널이 비활성 상태 - 전송 불가");
            return;
        }

        JsonObject packet = new JsonObject();
        packet.addProperty("type", type);
        packet.add("data", data);

        // TextWebSocketFrame 으로 감싸서 전송
        channel.writeAndFlush(new TextWebSocketFrame(packet.toString()));
    }

    // ── 예제 2 : ctx 로 바로 전송 (핸들러 내부에서 사용) ─────────
    //
    //   채널을 따로 보관하지 않아도 ctx 가 있으면 바로 전송 가능
    //   channelRead0 내부에서 응답을 보낼 때 유용
    //
    public void sendWithCtx(ChannelHandlerContext ctx, String type, JsonObject data) {
        JsonObject packet = new JsonObject();
        packet.addProperty("type", type);
        packet.add("data", data);

        ctx.writeAndFlush(new TextWebSocketFrame(packet.toString()));
    }

    // ── 예제 3 : WebSocket 프로토콜 레벨 Ping 전송 ───────────────
    //
    //   응용 레벨 PING/PONG (JSON) 과 다른 WebSocket 자체 하트비트
    //   일반적으로 연결 유지 확인용으로 사용
    //
    public void sendPing(Channel channel) {
        channel.writeAndFlush(new PingWebSocketFrame());
    }

    // ── 예제 4 : 연결 정상 종료 요청 ─────────────────────────────
    //
    //   그냥 channel.close() 하면 비정상 종료
    //   CloseWebSocketFrame 을 먼저 보내야 WebSocket 규약에 맞는 정상 종료
    //
    public void close(Channel channel) {
        channel.writeAndFlush(new CloseWebSocketFrame());
    }

    // ── 실제 사용 예시 ────────────────────────────────────────────
    //
    //   JsonObject data = new JsonObject();
    //   data.addProperty("name", "lobby");
    //   data.addProperty("count", 5);
    //   data.addProperty("flag", true);
    //
    //   send(channel, "REGISTER", data);   // 채널로 전송
    //   sendWithCtx(ctx, "PONG", data);    // 핸들러 내부에서 즉시 전송
    //   sendPing(channel);                 // 하트비트
    //   close(channel);                    // 정상 종료
    //
    // ─────────────────────────────────────────────────────────────
}

/*    */ package net.minecraft.server.jsonrpc.websocket;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonParser;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToMessageDecoder;
/*    */ import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
/*    */ import java.util.List;
/*    */ 
/*    */ public class WebSocketToJsonCodec
/*    */   extends MessageToMessageDecoder<TextWebSocketFrame>
/*    */ {
/*    */   protected void decode(ChannelHandlerContext ctx, TextWebSocketFrame msg, List<Object> out) {
/* 14 */     JsonElement json = JsonParser.parseString(msg.text());
/* 15 */     out.add(json);
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\websocket\WebSocketToJsonCodec.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.server.jsonrpc;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.jsonrpc.methods.ClientInfo;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class JsonRpcLogger
/*    */ {
/* 12 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   private static final String PREFIX = "RPC Connection #{}: ";
/*    */   
/*    */   public void log(ClientInfo clientInfo, String message, Object... args) {
/* 16 */     if (args.length == 0) {
/* 17 */       LOGGER.info("RPC Connection #{}: " + message, clientInfo.connectionId());
/*    */     } else {
/* 19 */       List<Object> list = new ArrayList<Object>(Arrays.asList(args));
/* 20 */       list.addFirst(clientInfo.connectionId());
/* 21 */       LOGGER.info("RPC Connection #{}: " + message, list.toArray());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\JsonRpcLogger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.server.jsonrpc.methods;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.protocol.status.ServerStatus;
/*    */ import net.minecraft.server.jsonrpc.api.PlayerDto;
/*    */ 
/*    */ public final class ServerState extends Record {
/*    */   private final boolean started;
/*    */   private final List<PlayerDto> players;
/*    */   private final ServerStatus.Version version;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState; }
/*    */   
/* 16 */   public ServerState(boolean started, List<PlayerDto> players, ServerStatus.Version version) { this.started = started; this.players = players; this.version = version; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/jsonrpc/methods/ServerStateService$ServerState;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public boolean started() { return this.started; } public List<PlayerDto> players() { return this.players; } public ServerStatus.Version version() { return this.version; }
/* 17 */   public static final Codec<ServerState> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.BOOL
/* 18 */         .fieldOf("started").forGetter(ServerState::started), PlayerDto.CODEC
/* 19 */         .codec().listOf().lenientOptionalFieldOf("players", List.of()).forGetter(ServerState::players), ServerStatus.Version.CODEC
/* 20 */         .fieldOf("version").forGetter(ServerState::version))
/* 21 */       .apply(i, ServerState::new));
/*    */   
/* 23 */   public static final ServerState NOT_STARTED = new ServerState(false, List.of(), ServerStatus.Version.current());
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\jsonrpc\methods\ServerStateService$ServerState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
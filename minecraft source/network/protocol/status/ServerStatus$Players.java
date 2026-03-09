/*    */ package net.minecraft.network.protocol.status;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.players.NameAndId;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class Players
/*    */   extends Record
/*    */ {
/*    */   private final int max;
/*    */   private final int online;
/*    */   private final List<NameAndId> sample;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/status/ServerStatus$Players;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Players; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/status/ServerStatus$Players;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Players; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/status/ServerStatus$Players;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #28	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/status/ServerStatus$Players;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 28 */   public Players(int max, int online, List<NameAndId> sample) { this.max = max; this.online = online; this.sample = sample; } public int max() { return this.max; } public int online() { return this.online; } public List<NameAndId> sample() { return this.sample; }
/* 29 */   public static final Codec<Players> CODEC = RecordCodecBuilder.create(i -> i.group(Codec.INT
/* 30 */         .fieldOf("max").forGetter(Players::max), Codec.INT
/* 31 */         .fieldOf("online").forGetter(Players::online), NameAndId.CODEC
/* 32 */         .listOf().lenientOptionalFieldOf("sample", List.of()).forGetter(Players::sample))
/* 33 */       .apply(i, Players::new));
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\protocol\status\ServerStatus$Players.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
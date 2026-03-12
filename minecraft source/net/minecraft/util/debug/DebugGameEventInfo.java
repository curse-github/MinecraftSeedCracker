/*    */ package net.minecraft.util.debug;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class DebugGameEventInfo extends Record {
/*    */   private final Holder<GameEvent> event;
/*    */   private final Vec3 pos;
/*    */   
/* 11 */   public DebugGameEventInfo(Holder<GameEvent> event, Vec3 pos) { this.event = event; this.pos = pos; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugGameEventInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugGameEventInfo; } public Holder<GameEvent> event() { return this.event; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugGameEventInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugGameEventInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugGameEventInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugGameEventInfo;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public Vec3 pos() { return this.pos; }
/*    */ 
/*    */ 
/*    */   
/* 15 */   public static final StreamCodec<RegistryFriendlyByteBuf, DebugGameEventInfo> STREAM_CODEC = StreamCodec.composite(
/* 16 */       ByteBufCodecs.holderRegistry(Registries.GAME_EVENT), DebugGameEventInfo::event, Vec3.STREAM_CODEC, DebugGameEventInfo::pos, DebugGameEventInfo::new);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\debug\DebugGameEventInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
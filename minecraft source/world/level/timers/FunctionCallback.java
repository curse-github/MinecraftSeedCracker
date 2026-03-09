/*    */ package net.minecraft.world.level.timers;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.ServerFunctionManager;
/*    */ 
/*    */ public final class FunctionCallback extends Record implements TimerCallback<MinecraftServer> {
/*    */   private final Identifier functionId;
/*    */   
/*  9 */   public FunctionCallback(Identifier functionId) { this.functionId = functionId; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/timers/FunctionCallback;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/world/level/timers/FunctionCallback; } public Identifier functionId() { return this.functionId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/timers/FunctionCallback;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/timers/FunctionCallback; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/timers/FunctionCallback;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/timers/FunctionCallback;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 10 */   public static final MapCodec<FunctionCallback> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Identifier.CODEC
/* 11 */         .fieldOf("Name").forGetter(FunctionCallback::functionId))
/* 12 */       .apply(i, FunctionCallback::new));
/*    */ 
/*    */   
/*    */   public void handle(MinecraftServer server, TimerQueue<MinecraftServer> queue, long time) {
/* 16 */     ServerFunctionManager functionManager = server.getFunctions();
/* 17 */     functionManager.get(this.functionId).ifPresent(function -> functionManager.execute(function, functionManager.getGameLoopSender()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 22 */   public MapCodec<FunctionCallback> codec() { return CODEC; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\timers\FunctionCallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
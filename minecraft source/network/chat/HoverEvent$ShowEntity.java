/*    */ package net.minecraft.network.chat;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ShowEntity
/*    */   extends Record
/*    */   implements HoverEvent
/*    */ {
/*    */   private final HoverEvent.EntityTooltipInfo entity;
/*    */   
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/HoverEvent$ShowEntity;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #59	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowEntity; }
/*    */   
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/HoverEvent$ShowEntity;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #59	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowEntity; }
/*    */   
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/HoverEvent$ShowEntity;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #59	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowEntity;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/*    */   
/* 59 */   public ShowEntity(HoverEvent.EntityTooltipInfo entity) { this.entity = entity; } public HoverEvent.EntityTooltipInfo entity() { return this.entity; }
/* 60 */   public static final MapCodec<ShowEntity> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(HoverEvent.EntityTooltipInfo.CODEC
/* 61 */         .forGetter(ShowEntity::entity))
/* 62 */       .apply(i, ShowEntity::new));
/*    */ 
/*    */ 
/*    */   
/* 66 */   public HoverEvent.Action action() { return HoverEvent.Action.SHOW_ENTITY; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\network\chat\HoverEvent$ShowEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
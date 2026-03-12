/*    */ package net.minecraft.server.commands;
/*    */ 
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface LookAt {
/*    */   void perform(CommandSourceStack paramCommandSourceStack, Entity paramEntity);
/*    */   
/*    */   public static final class LookAtEntity extends Record implements LookAt { private final Entity entity;
/*    */     private final EntityAnchorArgument.Anchor anchor;
/*    */     
/* 13 */     public LookAtEntity(Entity entity, EntityAnchorArgument.Anchor anchor) { this.entity = entity; this.anchor = anchor; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/commands/LookAt$LookAtEntity;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #13	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 13 */       //   0	7	0	this	Lnet/minecraft/server/commands/LookAt$LookAtEntity; } public Entity entity() { return this.entity; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/commands/LookAt$LookAtEntity;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #13	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/commands/LookAt$LookAtEntity; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/commands/LookAt$LookAtEntity;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #13	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/commands/LookAt$LookAtEntity;
/* 13 */       //   0	8	1	o	Ljava/lang/Object; } public EntityAnchorArgument.Anchor anchor() { return this.anchor; }
/*    */     
/*    */     public void perform(CommandSourceStack source, Entity target) {
/* 16 */       if (target instanceof ServerPlayer) { ServerPlayer targetPlayer = (ServerPlayer)target;
/* 17 */         targetPlayer.lookAt(source.getAnchor(), this.entity, this.anchor); }
/*    */       else
/* 19 */       { target.lookAt(source.getAnchor(), this.anchor.apply(this.entity)); }
/*    */     
/*    */     } }
/*    */   public static final class LookAtPosition extends Record implements LookAt { private final Vec3 position;
/*    */     
/* 24 */     public LookAtPosition(Vec3 position) { this.position = position; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/commands/LookAt$LookAtPosition;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/commands/LookAt$LookAtPosition; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/commands/LookAt$LookAtPosition;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/commands/LookAt$LookAtPosition; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/commands/LookAt$LookAtPosition;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/commands/LookAt$LookAtPosition;
/* 24 */       //   0	8	1	o	Ljava/lang/Object; } public Vec3 position() { return this.position; }
/*    */ 
/*    */     
/* 27 */     public void perform(CommandSourceStack source, Entity target) { target.lookAt(source.getAnchor(), this.position); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\server\commands\LookAt.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
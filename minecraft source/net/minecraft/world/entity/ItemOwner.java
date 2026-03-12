/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface ItemOwner
/*    */ {
/*    */   Level level();
/*    */   
/*    */   Vec3 position();
/*    */   
/*    */   float getVisualRotationYInDegrees();
/*    */   
/* 14 */   default LivingEntity asLivingEntity() { return null; }
/*    */ 
/*    */ 
/*    */   
/* 18 */   static ItemOwner offsetFromOwner(ItemOwner owner, Vec3 offset) { return new OffsetFromOwner(owner, offset); }
/*    */   public static final class OffsetFromOwner extends Record implements ItemOwner { private final ItemOwner owner; private final Vec3 offset;
/*    */     
/* 21 */     public OffsetFromOwner(ItemOwner owner, Vec3 offset) { this.owner = owner; this.offset = offset; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/ItemOwner$OffsetFromOwner;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 21 */       //   0	7	0	this	Lnet/minecraft/world/entity/ItemOwner$OffsetFromOwner; } public ItemOwner owner() { return this.owner; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/ItemOwner$OffsetFromOwner;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/entity/ItemOwner$OffsetFromOwner; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/ItemOwner$OffsetFromOwner;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/entity/ItemOwner$OffsetFromOwner;
/* 21 */       //   0	8	1	o	Ljava/lang/Object; } public Vec3 offset() { return this.offset; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 27 */     public Level level() { return this.owner.level(); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 32 */     public Vec3 position() { return this.owner.position().add(this.offset); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 37 */     public float getVisualRotationYInDegrees() { return this.owner.getVisualRotationYInDegrees(); }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     public LivingEntity asLivingEntity() { return this.owner.asLivingEntity(); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ItemOwner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
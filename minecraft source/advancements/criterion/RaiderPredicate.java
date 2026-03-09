/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ 
/*    */ public final class RaiderPredicate extends Record implements EntitySubPredicate {
/*    */   private final boolean hasRaid;
/*    */   private final boolean isCaptain;
/*    */   
/* 12 */   public RaiderPredicate(boolean hasRaid, boolean isCaptain) { this.hasRaid = hasRaid; this.isCaptain = isCaptain; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/RaiderPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/RaiderPredicate; } public boolean hasRaid() { return this.hasRaid; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/RaiderPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/RaiderPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/RaiderPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/RaiderPredicate;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public boolean isCaptain() { return this.isCaptain; }
/* 13 */   public static final MapCodec<RaiderPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.BOOL
/* 14 */         .optionalFieldOf("has_raid", Boolean.valueOf(false)).forGetter(RaiderPredicate::hasRaid), Codec.BOOL
/* 15 */         .optionalFieldOf("is_captain", Boolean.valueOf(false)).forGetter(RaiderPredicate::isCaptain))
/* 16 */       .apply(i, RaiderPredicate::new));
/*    */   
/* 18 */   public static final RaiderPredicate CAPTAIN_WITHOUT_RAID = new RaiderPredicate(false, true);
/*    */ 
/*    */ 
/*    */   
/* 22 */   public MapCodec<RaiderPredicate> codec() { return EntitySubPredicates.RAIDER; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/* 27 */     if (entity instanceof Raider) { Raider raider = (Raider)entity;
/* 28 */       return (raider.hasRaid() == this.hasRaid && raider.isCaptain() == this.isCaptain); }
/*    */     
/* 30 */     return false;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\RaiderPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
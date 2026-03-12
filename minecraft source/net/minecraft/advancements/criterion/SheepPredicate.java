/*    */ package net.minecraft.advancements.criterion;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.animal.sheep.Sheep;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class SheepPredicate extends Record implements EntitySubPredicate {
/*    */   private final Optional<Boolean> sheared;
/*    */   
/* 14 */   public SheepPredicate(Optional<Boolean> sheared) { this.sheared = sheared; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/SheepPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 14 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/SheepPredicate; } public Optional<Boolean> sheared() { return this.sheared; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/SheepPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/SheepPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/SheepPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/SheepPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object; }
/* 15 */   public static final MapCodec<SheepPredicate> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.BOOL
/* 16 */         .optionalFieldOf("sheared").forGetter(SheepPredicate::sheared))
/* 17 */       .apply(i, SheepPredicate::new));
/*    */ 
/*    */ 
/*    */   
/* 21 */   public MapCodec<SheepPredicate> codec() { return EntitySubPredicates.SHEEP; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean matches(Entity entity, ServerLevel level, Vec3 position) {
/* 26 */     if (entity instanceof Sheep) { Sheep sheep = (Sheep)entity;
/* 27 */       if (this.sheared.isPresent() && sheep.isSheared() != ((Boolean)this.sheared.get()).booleanValue()) {
/* 28 */         return false;
/*    */       }
/* 30 */       return true; }
/*    */     
/* 32 */     return false;
/*    */   }
/*    */ 
/*    */   
/* 36 */   public static SheepPredicate hasWool() { return new SheepPredicate(Optional.of(Boolean.valueOf(false))); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\advancements\criterion\SheepPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
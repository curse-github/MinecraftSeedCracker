/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.criterion.LocationPredicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class LocationCheck extends Record implements LootItemCondition {
/*    */   private final Optional<LocationPredicate> predicate;
/*    */   private final BlockPos offset;
/*    */   
/* 16 */   public LocationCheck(Optional<LocationPredicate> predicate, BlockPos offset) { this.predicate = predicate; this.offset = offset; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck; } public Optional<LocationPredicate> predicate() { return this.predicate; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/predicates/LocationCheck;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public BlockPos offset() { return this.offset; }
/*    */ 
/*    */ 
/*    */   
/* 20 */   private static final MapCodec<BlockPos> OFFSET_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(Codec.INT
/* 21 */         .optionalFieldOf("offsetX", Integer.valueOf(0)).forGetter(Vec3i::getX), Codec.INT
/* 22 */         .optionalFieldOf("offsetY", Integer.valueOf(0)).forGetter(Vec3i::getY), Codec.INT
/* 23 */         .optionalFieldOf("offsetZ", Integer.valueOf(0)).forGetter(Vec3i::getZ))
/* 24 */       .apply(i, BlockPos::new));
/*    */   
/* 26 */   public static final MapCodec<LocationCheck> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(LocationPredicate.CODEC
/* 27 */         .optionalFieldOf("predicate").forGetter(LocationCheck::predicate), OFFSET_CODEC
/* 28 */         .forGetter(LocationCheck::offset))
/* 29 */       .apply(i, LocationCheck::new));
/*    */ 
/*    */ 
/*    */   
/* 33 */   public LootItemConditionType getType() { return LootItemConditions.LOCATION_CHECK; }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(LootContext context) {
/* 38 */     Vec3 pos = (Vec3)context.getOptionalParameter(LootContextParams.ORIGIN);
/* 39 */     return (pos != null && (this.predicate.isEmpty() || ((LocationPredicate)this.predicate.get()).matches(context.getLevel(), pos.x() + this.offset.getX(), pos.y() + this.offset.getY(), pos.z() + this.offset.getZ())));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 44 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(LootContextParams.ORIGIN); }
/*    */ 
/*    */ 
/*    */   
/* 48 */   public static LootItemCondition.Builder checkLocation(LocationPredicate.Builder predicate) { return () -> new LocationCheck(Optional.of(predicate.build()), BlockPos.ZERO); }
/*    */ 
/*    */ 
/*    */   
/* 52 */   public static LootItemCondition.Builder checkLocation(LocationPredicate.Builder predicate, BlockPos offset) { return () -> new LocationCheck(Optional.of(predicate.build()), offset); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LocationCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
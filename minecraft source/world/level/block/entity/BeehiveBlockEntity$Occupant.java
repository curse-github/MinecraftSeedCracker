/*     */ package net.minecraft.world.level.block.entity;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.nbt.CompoundTag;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.tags.EntityTypeTags;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityProcessor;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.animal.bee.Bee;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.storage.TagValueOutput;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class Occupant
/*     */   extends Record
/*     */ {
/*     */   private final TypedEntityData<EntityType<?>> entityData;
/*     */   private final int ticksInHive;
/*     */   private final int minTicksInHive;
/*     */   
/*     */   public final String toString() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #363	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant; }
/*     */   
/*     */   public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #363	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant; }
/*     */   
/* 363 */   public Occupant(TypedEntityData<EntityType<?>> entityData, int ticksInHive, int minTicksInHive) { this.entityData = entityData; this.ticksInHive = ticksInHive; this.minTicksInHive = minTicksInHive; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #363	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/world/level/block/entity/BeehiveBlockEntity$Occupant;
/* 363 */     //   0	8	1	o	Ljava/lang/Object; } public TypedEntityData<EntityType<?>> entityData() { return this.entityData; } public int ticksInHive() { return this.ticksInHive; } public int minTicksInHive() { return this.minTicksInHive; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 368 */   public static final Codec<Occupant> CODEC = RecordCodecBuilder.create(i -> i.group(
/* 369 */         TypedEntityData.codec(EntityType.CODEC).fieldOf("entity_data").forGetter(Occupant::entityData), Codec.INT
/* 370 */         .fieldOf("ticks_in_hive").forGetter(Occupant::ticksInHive), Codec.INT
/* 371 */         .fieldOf("min_ticks_in_hive").forGetter(Occupant::minTicksInHive))
/* 372 */       .apply(i, Occupant::new));
/*     */   
/* 374 */   public static final Codec<List<Occupant>> LIST_CODEC = CODEC.listOf();
/*     */   
/* 376 */   public static final StreamCodec<RegistryFriendlyByteBuf, Occupant> STREAM_CODEC = StreamCodec.composite(
/* 377 */       TypedEntityData.streamCodec(EntityType.STREAM_CODEC), Occupant::entityData, ByteBufCodecs.VAR_INT, Occupant::ticksInHive, ByteBufCodecs.VAR_INT, Occupant::minTicksInHive, Occupant::new);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Occupant of(Entity entity)
/*     */   {
/* 384 */     ProblemReporter.ScopedCollector reporter = new ProblemReporter.ScopedCollector(entity.problemPath(), BeehiveBlockEntity.LOGGER); 
/* 385 */     try { TagValueOutput output = TagValueOutput.createWithContext(reporter, entity.registryAccess());
/* 386 */       entity.save(output);
/* 387 */       Objects.requireNonNull(output); BeehiveBlockEntity.IGNORED_BEE_TAGS.forEach(output::discard);
/* 388 */       CompoundTag entityTag = output.buildResult();
/* 389 */       boolean hasNectar = entityTag.getBooleanOr("HasNectar", false);
/* 390 */       Occupant occupant = new Occupant(TypedEntityData.of(entity.getType(), entityTag), 0, hasNectar ? 2400 : 600);
/* 391 */       reporter.close(); return occupant; }
/*     */     catch (Throwable throwable) { try { reporter.close(); }
/*     */       catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */        throw throwable; }
/* 395 */      } public static Occupant create(int ticksInHive) { return new Occupant(TypedEntityData.of(EntityType.BEE, new CompoundTag()), ticksInHive, 600); }
/*     */ 
/*     */   
/*     */   public Entity createEntity(Level level, BlockPos hivePos) {
/* 399 */     CompoundTag entityTag = this.entityData.copyTagWithoutId();
/*     */     
/* 401 */     Objects.requireNonNull(entityTag); BeehiveBlockEntity.IGNORED_BEE_TAGS.forEach(entityTag::remove);
/*     */     
/* 403 */     Entity entity = EntityType.loadEntityRecursive((EntityType)this.entityData.type(), entityTag, level, EntitySpawnReason.LOAD, EntityProcessor.NOP);
/* 404 */     if (entity == null || !entity.getType().is(EntityTypeTags.BEEHIVE_INHABITORS)) {
/* 405 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 409 */     entity.setNoGravity(true);
/*     */     
/* 411 */     if (entity instanceof Bee) { Bee bee = (Bee)entity;
/* 412 */       bee.setHivePos(hivePos);
/* 413 */       setBeeReleaseData(this.ticksInHive, bee); }
/*     */ 
/*     */     
/* 416 */     return entity;
/*     */   }
/*     */   
/*     */   private static void setBeeReleaseData(int ticksInHive, Bee bee) {
/* 420 */     int age = bee.getAge();
/* 421 */     if (age < 0) {
/* 422 */       bee.setAge(Math.min(0, age + ticksInHive));
/* 423 */     } else if (age > 0) {
/* 424 */       bee.setAge(Math.max(0, age - ticksInHive));
/*     */     } 
/* 426 */     bee.setInLoveTime(Math.max(0, bee.getInLoveTime() - ticksInHive));
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\entity\BeehiveBlockEntity$Occupant.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
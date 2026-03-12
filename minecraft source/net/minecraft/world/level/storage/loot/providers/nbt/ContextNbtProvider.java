/*    */ package net.minecraft.world.level.storage.loot.providers.nbt;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.criterion.NbtPredicate;
/*    */ import net.minecraft.nbt.Tag;
/*    */ import net.minecraft.util.context.ContextKey;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.LootContextArg;
/*    */ 
/*    */ public class ContextNbtProvider implements NbtProvider {
/* 18 */   private static final Codec<LootContextArg<Tag>> GETTER_CODEC = LootContextArg.createArgCodec(builder -> builder
/* 19 */       .anyBlockEntity(BlockEntitySource::new)
/* 20 */       .anyEntity(EntitySource::new));
/*    */ 
/*    */   
/* 23 */   public static final MapCodec<ContextNbtProvider> MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(GETTER_CODEC
/* 24 */         .fieldOf("target").forGetter(()))
/* 25 */       .apply(i, ContextNbtProvider::new));
/*    */   
/* 27 */   public static final Codec<ContextNbtProvider> INLINE_CODEC = GETTER_CODEC.xmap(ContextNbtProvider::new, p -> p.source);
/*    */   
/*    */   private final LootContextArg<Tag> source;
/*    */ 
/*    */   
/* 32 */   private ContextNbtProvider(LootContextArg<Tag> source) { this.source = source; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 37 */   public LootNbtProviderType getType() { return NbtProviders.CONTEXT; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 42 */   public Tag get(LootContext context) { return (Tag)this.source.get(context); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 47 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(this.source.contextParam()); }
/*    */ 
/*    */ 
/*    */   
/* 51 */   public static NbtProvider forContextEntity(LootContext.EntityTarget source) { return new ContextNbtProvider(new EntitySource(source.contextParam())); }
/*    */   private static final class BlockEntitySource extends Record implements LootContextArg.Getter<BlockEntity, Tag> { private final ContextKey<? extends BlockEntity> contextParam;
/*    */     
/* 54 */     private BlockEntitySource(ContextKey<? extends BlockEntity> contextParam) { this.contextParam = contextParam; } public ContextKey<? extends BlockEntity> contextParam() { return this.contextParam; }
/*    */     public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #54	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #54	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource; }
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #54	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$BlockEntitySource;
/*    */       //   0	8	1	o	Ljava/lang/Object; }
/*    */     
/* 59 */     public Tag get(BlockEntity blockEntity) { return blockEntity.saveWithFullMetadata(blockEntity.getLevel().registryAccess()); } }
/*    */   
/*    */   private static final class EntitySource extends Record implements LootContextArg.Getter<Entity, Tag> { private final ContextKey<? extends Entity> contextParam;
/*    */     
/* 63 */     private EntitySource(ContextKey<? extends Entity> contextParam) { this.contextParam = contextParam; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #63	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 63 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource; } public ContextKey<? extends Entity> contextParam() { return this.contextParam; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #63	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource; }
/*    */     
/*    */     public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #63	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/nbt/ContextNbtProvider$EntitySource;
/*    */       //   0	8	1	o	Ljava/lang/Object; }
/*    */     
/* 68 */     public Tag get(Entity entity) { return NbtPredicate.getEntityTagToCompare(entity); } }
/*    */ 
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\providers\nbt\ContextNbtProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
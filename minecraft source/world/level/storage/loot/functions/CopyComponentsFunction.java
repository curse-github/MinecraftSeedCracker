/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.component.DataComponentGetter;
/*     */ import net.minecraft.core.component.DataComponentMap;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.TypedDataComponent;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.LootContextArg;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class CopyComponentsFunction extends LootItemConditionalFunction {
/*  30 */   private static final Codec<LootContextArg<DataComponentGetter>> GETTER_CODEC = LootContextArg.createArgCodec(builder -> builder
/*  31 */       .anyEntity(DirectSource::new)
/*  32 */       .anyBlockEntity(BlockEntitySource::new)
/*  33 */       .anyItemStack(DirectSource::new));
/*     */ 
/*     */   
/*  36 */   public static final MapCodec<CopyComponentsFunction> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(GETTER_CODEC
/*  37 */           .fieldOf("source").forGetter(()), DataComponentType.CODEC
/*  38 */           .listOf().optionalFieldOf("include").forGetter(()), DataComponentType.CODEC
/*  39 */           .listOf().optionalFieldOf("exclude").forGetter(())))
/*  40 */       .apply(i, CopyComponentsFunction::new));
/*     */   
/*     */   private final LootContextArg<DataComponentGetter> source;
/*     */   
/*     */   private final Optional<List<DataComponentType<?>>> include;
/*     */   private final Optional<List<DataComponentType<?>>> exclude;
/*     */   private final Predicate<DataComponentType<?>> bakedPredicate;
/*     */   
/*     */   private CopyComponentsFunction(List<LootItemCondition> predicates, LootContextArg<DataComponentGetter> source, Optional<List<DataComponentType<?>>> include, Optional<List<DataComponentType<?>>> exclude) {
/*  49 */     super(predicates);
/*  50 */     this.source = source;
/*  51 */     this.include = include.map(List::copyOf);
/*  52 */     this.exclude = exclude.map(List::copyOf);
/*     */     
/*  54 */     List<Predicate<DataComponentType<?>>> componentPredicates = new ArrayList<Predicate<DataComponentType<?>>>(2);
/*  55 */     exclude.ifPresent(s -> componentPredicates.add(()));
/*  56 */     include.ifPresent(s -> { Objects.requireNonNull(s); componentPredicates.add(s::contains);
/*  57 */         }); this.bakedPredicate = Util.allOf(componentPredicates);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  62 */   public LootItemFunctionType<CopyComponentsFunction> getType() { return LootItemFunctions.COPY_COMPONENTS; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  67 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(this.source.contextParam()); }
/*     */ 
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  72 */     DataComponentGetter data = (DataComponentGetter)this.source.get(context);
/*  73 */     if (data != null) {
/*  74 */       if (data instanceof DataComponentMap) { DataComponentMap sourceComponents = (DataComponentMap)data;
/*  75 */         itemStack.applyComponents(sourceComponents.filter(this.bakedPredicate)); }
/*     */       else
/*  77 */       { Collection<DataComponentType<?>> exclude = (Collection)this.exclude.orElse(List.of());
/*     */         
/*  79 */         ((Stream)this.include.map(Collection::stream)
/*  80 */           .orElse(BuiltInRegistries.DATA_COMPONENT_TYPE.listElements().map(Holder::value)))
/*  81 */           .forEach(componentType -> {
/*  82 */               if (exclude.contains(componentType)) {
/*     */                 return;
/*     */               }
/*  85 */               TypedDataComponent<?> value = data.getTyped(componentType);
/*  86 */               if (value != null) {
/*  87 */                 itemStack.set(value);
/*     */               }
/*     */             }); }
/*     */     
/*     */     }
/*  92 */     return itemStack;
/*     */   }
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> { private final LootContextArg<DataComponentGetter> source; private Optional<ImmutableList.Builder<DataComponentType<?>>> include; private Optional<ImmutableList.Builder<DataComponentType<?>>> exclude;
/*     */     
/*     */     private Builder(LootContextArg<DataComponentGetter> source) {
/*  97 */       this.include = Optional.empty();
/*  98 */       this.exclude = Optional.empty();
/*     */ 
/*     */       
/* 101 */       this.source = source;
/*     */     }
/*     */     
/*     */     public Builder include(DataComponentType<?> type) {
/* 105 */       if (this.include.isEmpty()) {
/* 106 */         this.include = Optional.of(ImmutableList.builder());
/*     */       }
/* 108 */       ((ImmutableList.Builder)this.include.get()).add(type);
/* 109 */       return this;
/*     */     }
/*     */     
/*     */     public Builder exclude(DataComponentType<?> type) {
/* 113 */       if (this.exclude.isEmpty()) {
/* 114 */         this.exclude = Optional.of(ImmutableList.builder());
/*     */       }
/*     */       
/* 117 */       ((ImmutableList.Builder)this.exclude.get()).add(type);
/* 118 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 123 */     protected Builder getThis() { return this; }
/*     */ 
/*     */ 
/*     */     
/*     */     public LootItemFunction build() {
/* 128 */       return new CopyComponentsFunction(
/* 129 */           getConditions(), this.source, this.include
/*     */           
/* 131 */           .map(ImmutableList.Builder::build), this.exclude
/* 132 */           .map(ImmutableList.Builder::build));
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 138 */   public static Builder copyComponentsFromEntity(ContextKey<? extends Entity> source) { return new Builder(new DirectSource(source)); }
/*     */ 
/*     */ 
/*     */   
/* 142 */   public static Builder copyComponentsFromBlockEntity(ContextKey<? extends BlockEntity> source) { return new Builder(new BlockEntitySource(source)); }
/*     */   private static final class DirectSource<T extends DataComponentGetter> extends Record implements LootContextArg.Getter<T, DataComponentGetter> { private final ContextKey<? extends T> contextParam;
/*     */     
/* 145 */     private DirectSource(ContextKey<? extends T> contextParam) { this.contextParam = contextParam; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #145	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 145 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource<TT;>; } public ContextKey<? extends T> contextParam() { return this.contextParam; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #145	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource<TT;>; }
/*     */     
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #145	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$DirectSource<TT;>; }
/*     */     
/* 150 */     public DataComponentGetter get(T value) { return value; } }
/*     */   
/*     */   private static final class BlockEntitySource extends Record implements LootContextArg.Getter<BlockEntity, DataComponentGetter> { private final ContextKey<? extends BlockEntity> contextParam;
/*     */     
/* 154 */     private BlockEntitySource(ContextKey<? extends BlockEntity> contextParam) { this.contextParam = contextParam; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$BlockEntitySource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #154	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 154 */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$BlockEntitySource; } public ContextKey<? extends BlockEntity> contextParam() { return this.contextParam; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$BlockEntitySource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #154	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$BlockEntitySource; }
/*     */     
/*     */     public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$BlockEntitySource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #154	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/functions/CopyComponentsFunction$BlockEntitySource;
/*     */       //   0	8	1	o	Ljava/lang/Object; }
/*     */     
/* 159 */     public DataComponentGetter get(BlockEntity blockEntity) { return blockEntity.collectComponents(); } }
/*     */ 
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\CopyComponentsFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
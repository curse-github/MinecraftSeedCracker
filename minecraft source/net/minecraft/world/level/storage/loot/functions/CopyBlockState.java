/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ 
/*     */ public class CopyBlockState extends LootItemConditionalFunction {
/*  26 */   public static final MapCodec<CopyBlockState> CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group(BuiltInRegistries.BLOCK
/*  27 */           .holderByNameCodec().fieldOf("block").forGetter(()), Codec.STRING
/*  28 */           .listOf().fieldOf("properties").forGetter(())))
/*  29 */       .apply(i, CopyBlockState::new));
/*     */   
/*     */   private final Holder<Block> block;
/*     */   private final Set<Property<?>> properties;
/*     */   
/*     */   private CopyBlockState(List<LootItemCondition> predicates, Holder<Block> block, Set<Property<?>> properties) {
/*  35 */     super(predicates);
/*  36 */     this.block = block;
/*  37 */     this.properties = properties;
/*     */   }
/*     */   
/*     */   private CopyBlockState(List<LootItemCondition> predicates, Holder<Block> block, List<String> propertyNames) {
/*  41 */     this(predicates, block, (Set)propertyNames.stream()
/*  42 */         .map(((Block)block.value()).getStateDefinition()::getProperty)
/*  43 */         .filter(Objects::nonNull)
/*  44 */         .collect(Collectors.toSet()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   public LootItemFunctionType<CopyBlockState> getType() { return LootItemFunctions.COPY_STATE; }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  55 */   public Set<ContextKey<?>> getReferencedContextParams() { return Set.of(LootContextParams.BLOCK_STATE); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected ItemStack run(ItemStack itemStack, LootContext context) {
/*  60 */     BlockState state = (BlockState)context.getOptionalParameter(LootContextParams.BLOCK_STATE);
/*  61 */     if (state != null) {
/*  62 */       itemStack.update(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY, itemState -> {
/*  63 */             for (Property<?> property : this.properties) {
/*  64 */               if (state.hasProperty(property)) {
/*  65 */                 itemState = itemState.with(property, state);
/*     */               }
/*     */             } 
/*  68 */             return itemState;
/*     */           });
/*     */     }
/*     */     
/*  72 */     return itemStack;
/*     */   }
/*     */   public static class Builder extends LootItemConditionalFunction.Builder<Builder> { private final Holder<Block> block;
/*     */     
/*     */     private Builder(Block block) {
/*  77 */       this.properties = ImmutableSet.builder();
/*     */ 
/*     */       
/*  80 */       this.block = block.builtInRegistryHolder();
/*     */     }
/*     */     private final ImmutableSet.Builder<Property<?>> properties;
/*     */     public Builder copy(Property<?> property) {
/*  84 */       if (!((Block)this.block.value()).getStateDefinition().getProperties().contains(property)) {
/*  85 */         throw new IllegalStateException("Property " + String.valueOf(property) + " is not present on block " + String.valueOf(this.block));
/*     */       }
/*  87 */       this.properties.add(property);
/*  88 */       return this;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*  93 */     protected Builder getThis() { return this; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     public LootItemFunction build() { return new CopyBlockState(getConditions(), this.block, this.properties.build()); } }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 103 */   public static Builder copyState(Block block) { return new Builder(block); }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\CopyBlockState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
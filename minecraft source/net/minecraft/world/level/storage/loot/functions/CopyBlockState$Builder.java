/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
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
/*    */ public class Builder
/*    */   extends LootItemConditionalFunction.Builder<CopyBlockState.Builder>
/*    */ {
/*    */   private final Holder<Block> block;
/*    */   private final ImmutableSet.Builder<Property<?>> properties;
/*    */   
/*    */   private Builder(Block block) {
/* 77 */     this.properties = ImmutableSet.builder();
/*    */ 
/*    */     
/* 80 */     this.block = block.builtInRegistryHolder();
/*    */   }
/*    */   
/*    */   public Builder copy(Property<?> property) {
/* 84 */     if (!((Block)this.block.value()).getStateDefinition().getProperties().contains(property)) {
/* 85 */       throw new IllegalStateException("Property " + String.valueOf(property) + " is not present on block " + String.valueOf(this.block));
/*    */     }
/* 87 */     this.properties.add(property);
/* 88 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 93 */   protected Builder getThis() { return this; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 98 */   public LootItemFunction build() { return new CopyBlockState(getConditions(), this.block, this.properties.build()); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\functions\CopyBlockState$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
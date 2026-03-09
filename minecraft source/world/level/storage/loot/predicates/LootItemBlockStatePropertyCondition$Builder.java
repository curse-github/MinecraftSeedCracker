/*    */ package net.minecraft.world.level.storage.loot.predicates;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.advancements.criterion.StatePropertiesPredicate;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.world.level.block.Block;
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
/*    */   implements LootItemCondition.Builder
/*    */ {
/*    */   private final Holder<Block> block;
/*    */   private Optional<StatePropertiesPredicate> properties;
/*    */   
/*    */   public Builder(Block block) {
/* 52 */     this.properties = Optional.empty();
/*    */ 
/*    */     
/* 55 */     this.block = block.builtInRegistryHolder();
/*    */   }
/*    */   
/*    */   public Builder setProperties(StatePropertiesPredicate.Builder properties) {
/* 59 */     this.properties = properties.build();
/* 60 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 65 */   public LootItemCondition build() { return new LootItemBlockStatePropertyCondition(this.block, this.properties); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\loot\predicates\LootItemBlockStatePropertyCondition$Builder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
/*    */ package net.minecraft.world.level.block.state.predicate;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Map;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BlockStatePredicate extends Object implements Predicate<BlockState> {
/*    */   public static final Predicate<BlockState> ANY = input -> true;
/*    */   private final StateDefinition<Block, BlockState> definition;
/*    */   private final Map<Property<?>, Predicate<Object>> properties;
/*    */   
/*    */   private BlockStatePredicate(StateDefinition<Block, BlockState> definition) {
/* 17 */     this.properties = Maps.newHashMap();
/*    */ 
/*    */     
/* 20 */     this.definition = definition;
/*    */   }
/*    */ 
/*    */   
/* 24 */   public static BlockStatePredicate forBlock(Block block) { return new BlockStatePredicate(block.getStateDefinition()); }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(BlockState input) {
/* 29 */     if (input == null || !input.getBlock().equals(this.definition.getOwner())) {
/* 30 */       return false;
/*    */     }
/*    */     
/* 33 */     if (this.properties.isEmpty()) {
/* 34 */       return true;
/*    */     }
/*    */     
/* 37 */     for (Map.Entry<Property<?>, Predicate<Object>> entry : this.properties.entrySet()) {
/* 38 */       if (!applies(input, (Property)entry.getKey(), (Predicate)entry.getValue())) {
/* 39 */         return false;
/*    */       }
/*    */     } 
/*    */     
/* 43 */     return true;
/*    */   }
/*    */   
/*    */   protected <T extends Comparable<T>> boolean applies(BlockState input, Property<T> key, Predicate<Object> predicate) {
/* 47 */     T value = (T)input.getValue(key);
/* 48 */     return predicate.test(value);
/*    */   }
/*    */   
/*    */   public <V extends Comparable<V>> BlockStatePredicate where(Property<V> property, Predicate<Object> predicate) {
/* 52 */     if (!this.definition.getProperties().contains(property)) {
/* 53 */       throw new IllegalArgumentException(String.valueOf(this.definition) + " cannot support property " + String.valueOf(this.definition));
/*    */     }
/* 55 */     this.properties.put(property, predicate);
/* 56 */     return this;
/*    */   }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\predicate\BlockStatePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
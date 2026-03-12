/*    */ package net.minecraft.world.level.block.state;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
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
/*    */ class null
/*    */   extends Object
/*    */   implements Function<Map.Entry<Property<?>, Comparable<?>>, String>
/*    */ {
/*    */   public String apply(Map.Entry<Property<?>, Comparable<?>> entry) {
/* 25 */     if (entry == null) {
/* 26 */       return "<NULL>";
/*    */     }
/*    */     
/* 29 */     Property<?> property = (Property)entry.getKey();
/* 30 */     return property.getName() + "=" + property.getName();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 35 */   private <T extends Comparable<T>> String getName(Property<T> property, Comparable<?> value) { return property.getName(value); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\block\state\StateHolder$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
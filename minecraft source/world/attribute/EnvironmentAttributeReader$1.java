/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class null
/*    */   implements EnvironmentAttributeReader
/*    */ {
/* 11 */   public <Value> Value getDimensionValue(EnvironmentAttribute<Value> attribute) { return (Value)attribute.defaultValue(); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 16 */   public <Value> Value getValue(EnvironmentAttribute<Value> attribute, Vec3 pos, SpatialAttributeInterpolator biomeInterpolator) { return (Value)attribute.defaultValue(); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\EnvironmentAttributeReader$1.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
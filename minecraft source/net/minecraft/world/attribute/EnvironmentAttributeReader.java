/*    */ package net.minecraft.world.attribute;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface EnvironmentAttributeReader
/*    */ {
/*  8 */   public static final EnvironmentAttributeReader EMPTY = new EnvironmentAttributeReader()
/*    */     {
/*    */       public <Value> Value getDimensionValue(EnvironmentAttribute<Value> attribute) {
/* 11 */         return (Value)attribute.defaultValue();
/*    */       }
/*    */ 
/*    */ 
/*    */       
/* 16 */       public <Value> Value getValue(EnvironmentAttribute<Value> attribute, Vec3 pos, SpatialAttributeInterpolator biomeInterpolator) { return (Value)attribute.defaultValue(); }
/*    */     };
/*    */ 
/*    */   
/*    */   <Value> Value getDimensionValue(EnvironmentAttribute<Value> paramEnvironmentAttribute);
/*    */ 
/*    */   
/* 23 */   default <Value> Value getValue(EnvironmentAttribute<Value> attribute, BlockPos pos) { return (Value)getValue(attribute, Vec3.atCenterOf(pos)); }
/*    */ 
/*    */ 
/*    */   
/* 27 */   default <Value> Value getValue(EnvironmentAttribute<Value> attribute, Vec3 pos) { return (Value)getValue(attribute, pos, null); }
/*    */   
/*    */   <Value> Value getValue(EnvironmentAttribute<Value> paramEnvironmentAttribute, Vec3 paramVec3, SpatialAttributeInterpolator paramSpatialAttributeInterpolator);
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\attribute\EnvironmentAttributeReader.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
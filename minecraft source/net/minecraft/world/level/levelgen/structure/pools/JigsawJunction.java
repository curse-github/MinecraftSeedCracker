/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ 
/*    */ public class JigsawJunction {
/*    */   private final int sourceX;
/*    */   private final int sourceGroundY;
/*    */   private final int sourceZ;
/*    */   private final int deltaY;
/*    */   private final StructureTemplatePool.Projection destProjection;
/*    */   
/*    */   public JigsawJunction(int sourceX, int sourceGroundY, int sourceZ, int deltaY, StructureTemplatePool.Projection destProjection) {
/* 15 */     this.sourceX = sourceX;
/* 16 */     this.sourceGroundY = sourceGroundY;
/* 17 */     this.sourceZ = sourceZ;
/* 18 */     this.deltaY = deltaY;
/* 19 */     this.destProjection = destProjection;
/*    */   }
/*    */ 
/*    */   
/* 23 */   public int getSourceX() { return this.sourceX; }
/*    */ 
/*    */ 
/*    */   
/* 27 */   public int getSourceGroundY() { return this.sourceGroundY; }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public int getSourceZ() { return this.sourceZ; }
/*    */ 
/*    */ 
/*    */   
/* 35 */   public int getDeltaY() { return this.deltaY; }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public StructureTemplatePool.Projection getDestProjection() { return this.destProjection; }
/*    */ 
/*    */   
/*    */   public <T> Dynamic<T> serialize(DynamicOps<T> ops) {
/* 43 */     ImmutableMap.Builder<T, T> builder = ImmutableMap.builder();
/* 44 */     builder
/* 45 */       .put(ops.createString("source_x"), ops.createInt(this.sourceX))
/* 46 */       .put(ops.createString("source_ground_y"), ops.createInt(this.sourceGroundY))
/* 47 */       .put(ops.createString("source_z"), ops.createInt(this.sourceZ))
/* 48 */       .put(ops.createString("delta_y"), ops.createInt(this.deltaY))
/* 49 */       .put(ops.createString("dest_proj"), ops.createString(this.destProjection.getName()));
/*    */     
/* 51 */     return new Dynamic(ops, ops.createMap(builder.build()));
/*    */   }
/*    */   
/*    */   public static <T> JigsawJunction deserialize(Dynamic<T> input) {
/* 55 */     return new JigsawJunction(input
/* 56 */         .get("source_x").asInt(0), input
/* 57 */         .get("source_ground_y").asInt(0), input
/* 58 */         .get("source_z").asInt(0), input
/* 59 */         .get("delta_y").asInt(0), 
/* 60 */         StructureTemplatePool.Projection.byName(input.get("dest_proj").asString("")));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 66 */     if (this == o) {
/* 67 */       return true;
/*    */     }
/* 69 */     if (o == null || getClass() != o.getClass()) {
/* 70 */       return false;
/*    */     }
/*    */     
/* 73 */     JigsawJunction that = (JigsawJunction)o;
/*    */     
/* 75 */     if (this.sourceX != that.sourceX) {
/* 76 */       return false;
/*    */     }
/* 78 */     if (this.sourceZ != that.sourceZ) {
/* 79 */       return false;
/*    */     }
/* 81 */     if (this.deltaY != that.deltaY) {
/* 82 */       return false;
/*    */     }
/* 84 */     return (this.destProjection == that.destProjection);
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 89 */     result = this.sourceX;
/* 90 */     result = 31 * result + this.sourceGroundY;
/* 91 */     result = 31 * result + this.sourceZ;
/* 92 */     result = 31 * result + this.deltaY;
/* 93 */     return 31 * result + this.destProjection.hashCode();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 99 */   public String toString() { return "JigsawJunction{sourceX=" + this.sourceX + ", sourceGroundY=" + this.sourceGroundY + ", sourceZ=" + this.sourceZ + ", deltaY=" + this.deltaY + ", destProjection=" + String.valueOf(this.destProjection) + "}"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\JigsawJunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
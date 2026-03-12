/*    */ package net.minecraft.world.level.levelgen.structure.pools;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.GravityProcessor;
/*    */ import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
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
/*    */ public static enum Projection
/*    */   implements StringRepresentable
/*    */ {
/*    */   public static final StringRepresentable.EnumCodec<Projection> CODEC;
/* 44 */   TERRAIN_MATCHING("terrain_matching", 
/*    */     
/* 46 */     ImmutableList.of(new GravityProcessor(Heightmap.Types.WORLD_SURFACE_WG, -1))),
/*    */   
/* 48 */   RIGID("rigid", 
/*    */     
/* 50 */     ImmutableList.of());
/*    */   
/*    */   static  {
/* 53 */     CODEC = StringRepresentable.fromEnum(Projection::values);
/*    */   }
/*    */   
/*    */   private final String name;
/*    */   
/*    */   Projection(String name, ImmutableList<StructureProcessor> processors) {
/* 59 */     this.name = name;
/* 60 */     this.processors = processors;
/*    */   }
/*    */   private final ImmutableList<StructureProcessor> processors;
/*    */   
/* 64 */   public String getName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 68 */   public static Projection byName(String name) { return (Projection)CODEC.byName(name); }
/*    */ 
/*    */ 
/*    */   
/* 72 */   public ImmutableList<StructureProcessor> getProcessors() { return this.processors; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 77 */   public String getSerializedName() { return this.name; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\levelgen\structure\pools\StructureTemplatePool$Projection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
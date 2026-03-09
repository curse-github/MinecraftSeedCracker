/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public static enum MobCategory implements StringRepresentable {
/*  7 */   MONSTER("monster", 70, false, false, 128),
/*  8 */   CREATURE("creature", 10, true, true, 128),
/*  9 */   AMBIENT("ambient", 15, true, false, 128),
/*    */   
/* 11 */   AXOLOTLS("axolotls", 5, true, false, 128),
/* 12 */   UNDERGROUND_WATER_CREATURE("underground_water_creature", 5, true, false, 128),
/* 13 */   WATER_CREATURE("water_creature", 5, true, false, 128),
/* 14 */   WATER_AMBIENT("water_ambient", 20, true, false, 64),
/* 15 */   MISC("misc", -1, true, true, 128);
/*    */   
/*    */   static  {
/* 18 */     CODEC = StringRepresentable.fromEnum(MobCategory::values);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   private final int noDespawnDistance = 32; public static final Codec<MobCategory> CODEC; private final int max;
/*    */   private final boolean isFriendly;
/*    */   
/*    */   MobCategory(String name, int max, boolean isFriendly, boolean isPersistent, int despawnDistance) {
/* 28 */     this.name = name;
/* 29 */     this.max = max;
/* 30 */     this.isFriendly = isFriendly;
/* 31 */     this.isPersistent = isPersistent;
/* 32 */     this.despawnDistance = despawnDistance;
/*    */   }
/*    */   private final boolean isPersistent; private final String name; private final int despawnDistance;
/*    */   
/* 36 */   public String getName() { return this.name; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 41 */   public String getSerializedName() { return this.name; }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public int getMaxInstancesPerChunk() { return this.max; }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public boolean isFriendly() { return this.isFriendly; }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public boolean isPersistent() { return this.isPersistent; }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public int getDespawnDistance() { return this.despawnDistance; }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public int getNoDespawnDistance() { return 32; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\MobCategory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
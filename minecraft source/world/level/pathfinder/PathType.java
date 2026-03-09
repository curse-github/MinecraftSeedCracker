/*    */ package net.minecraft.world.level.pathfinder;
/*    */ 
/*    */ public static enum PathType {
/*  4 */   BLOCKED(-1.0F),
/*  5 */   OPEN(0.0F),
/*  6 */   WALKABLE(0.0F),
/*  7 */   WALKABLE_DOOR(0.0F),
/*  8 */   TRAPDOOR(0.0F),
/*  9 */   POWDER_SNOW(-1.0F),
/* 10 */   DANGER_POWDER_SNOW(0.0F),
/* 11 */   FENCE(-1.0F),
/* 12 */   LAVA(-1.0F),
/* 13 */   WATER(8.0F),
/* 14 */   WATER_BORDER(8.0F),
/* 15 */   RAIL(0.0F),
/* 16 */   UNPASSABLE_RAIL(-1.0F),
/* 17 */   DANGER_FIRE(8.0F),
/* 18 */   DAMAGE_FIRE(16.0F),
/* 19 */   DANGER_OTHER(8.0F),
/* 20 */   DAMAGE_OTHER(-1.0F),
/* 21 */   DOOR_OPEN(0.0F),
/* 22 */   DOOR_WOOD_CLOSED(-1.0F),
/* 23 */   DOOR_IRON_CLOSED(-1.0F),
/* 24 */   BREACH(4.0F),
/* 25 */   LEAVES(-1.0F),
/* 26 */   STICKY_HONEY(8.0F),
/* 27 */   COCOA(0.0F),
/* 28 */   DAMAGE_CAUTIOUS(0.0F),
/* 29 */   DANGER_TRAPDOOR(0.0F);
/*    */ 
/*    */   
/*    */   private final float malus;
/*    */ 
/*    */   
/* 35 */   PathType(float defaultCost) { this.malus = defaultCost; }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public float getMalus() { return this.malus; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\pathfinder\PathType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
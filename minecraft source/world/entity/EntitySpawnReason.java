/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ public static enum EntitySpawnReason {
/*  4 */   NATURAL,
/*  5 */   CHUNK_GENERATION,
/*  6 */   SPAWNER,
/*  7 */   STRUCTURE,
/*  8 */   BREEDING,
/*  9 */   MOB_SUMMONED,
/* 10 */   JOCKEY,
/* 11 */   EVENT,
/* 12 */   CONVERSION,
/* 13 */   REINFORCEMENT,
/* 14 */   TRIGGERED,
/* 15 */   BUCKET,
/* 16 */   SPAWN_ITEM_USE,
/* 17 */   COMMAND,
/* 18 */   DISPENSER,
/* 19 */   PATROL,
/* 20 */   TRIAL_SPAWNER,
/* 21 */   LOAD,
/* 22 */   DIMENSION_TRAVEL;
/*    */ 
/*    */ 
/*    */   
/* 26 */   public static boolean isSpawner(EntitySpawnReason reason) { return (reason == SPAWNER || reason == TRIAL_SPAWNER); }
/*    */ 
/*    */ 
/*    */   
/* 30 */   public static boolean ignoresLightRequirements(EntitySpawnReason reason) { return (reason == TRIAL_SPAWNER); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\EntitySpawnReason.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
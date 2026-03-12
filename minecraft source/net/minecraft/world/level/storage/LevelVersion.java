/*    */ package net.minecraft.world.level.storage;
/*    */ 
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import com.mojang.serialization.OptionalDynamic;
/*    */ import net.minecraft.SharedConstants;
/*    */ 
/*    */ public class LevelVersion {
/*    */   private final int levelDataVersion;
/*    */   private final long lastPlayed;
/*    */   private final String minecraftVersionName;
/*    */   private final DataVersion minecraftVersion;
/*    */   private final boolean snapshot;
/*    */   
/*    */   private LevelVersion(int levelDataVersion, long lastPlayed, String minecraftVersionName, int minecraftVersion, String series, boolean snapshot) {
/* 15 */     this.levelDataVersion = levelDataVersion;
/* 16 */     this.lastPlayed = lastPlayed;
/* 17 */     this.minecraftVersionName = minecraftVersionName;
/* 18 */     this.minecraftVersion = new DataVersion(minecraftVersion, series);
/* 19 */     this.snapshot = snapshot;
/*    */   }
/*    */   
/*    */   public static LevelVersion parse(Dynamic<?> input) {
/* 23 */     int levelDataVersion = input.get("version").asInt(0);
/* 24 */     long lastPlayed = input.get("LastPlayed").asLong(0L);
/* 25 */     OptionalDynamic<?> version = input.get("Version");
/*    */     
/* 27 */     if (version.result().isPresent()) {
/* 28 */       return new LevelVersion(levelDataVersion, lastPlayed, version
/*    */ 
/*    */           
/* 31 */           .get("Name").asString(SharedConstants.getCurrentVersion().name()), version
/* 32 */           .get("Id").asInt(SharedConstants.getCurrentVersion().dataVersion().version()), version
/* 33 */           .get("Series").asString("main"), version
/* 34 */           .get("Snapshot").asBoolean(!SharedConstants.getCurrentVersion().stable()));
/*    */     }
/*    */     
/* 37 */     return new LevelVersion(levelDataVersion, lastPlayed, "", 0, "main", false);
/*    */   }
/*    */ 
/*    */   
/* 41 */   public int levelDataVersion() { return this.levelDataVersion; }
/*    */ 
/*    */ 
/*    */   
/* 45 */   public long lastPlayed() { return this.lastPlayed; }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public String minecraftVersionName() { return this.minecraftVersionName; }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public DataVersion minecraftVersion() { return this.minecraftVersion; }
/*    */ 
/*    */ 
/*    */   
/* 57 */   public boolean snapshot() { return this.snapshot; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\level\storage\LevelVersion.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
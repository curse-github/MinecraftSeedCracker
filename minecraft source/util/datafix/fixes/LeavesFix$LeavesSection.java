/*     */ package net.minecraft.util.datafix.fixes;
/*     */ 
/*     */ import com.mojang.datafixers.Typed;
/*     */ import com.mojang.datafixers.schemas.Schema;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.util.datafix.PackedBitStorage;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LeavesSection
/*     */   extends LeavesFix.Section
/*     */ {
/*     */   private static final String PERSISTENT = "persistent";
/*     */   private static final String DECAYABLE = "decayable";
/*     */   private static final String DISTANCE = "distance";
/*     */   private IntSet leaveIds;
/*     */   private IntSet logIds;
/*     */   private Int2IntMap stateToIdMap;
/*     */   
/* 264 */   public LeavesSection(Typed<?> section, Schema inputSchema) { super(section, inputSchema); }
/*     */ 
/*     */ 
/*     */   
/*     */   protected boolean skippable() {
/* 269 */     this.leaveIds = new IntOpenHashSet();
/* 270 */     this.logIds = new IntOpenHashSet();
/* 271 */     this.stateToIdMap = new Int2IntOpenHashMap();
/*     */     
/* 273 */     for (int i = 0; i < this.palette.size(); i++) {
/* 274 */       Dynamic<?> paletteTag = (Dynamic)this.palette.get(i);
/* 275 */       String blockName = paletteTag.get("Name").asString("");
/* 276 */       if (LeavesFix.LEAVES.containsKey(blockName)) {
/* 277 */         boolean persistent = Objects.equals(paletteTag.get("Properties").get("decayable").asString(""), "false");
/* 278 */         this.leaveIds.add(i);
/* 279 */         this.stateToIdMap.put(getStateId(blockName, persistent, 7), i);
/* 280 */         this.palette.set(i, makeLeafTag(paletteTag, blockName, persistent, 7));
/*     */       } 
/* 282 */       if (LeavesFix.LOGS.contains(blockName)) {
/* 283 */         this.logIds.add(i);
/*     */       }
/*     */     } 
/*     */     
/* 287 */     return (this.leaveIds.isEmpty() && this.logIds.isEmpty());
/*     */   }
/*     */   
/*     */   private Dynamic<?> makeLeafTag(Dynamic<?> input, String blockName, boolean persistent, int distance) {
/* 291 */     Dynamic<?> properties = input.emptyMap();
/* 292 */     properties = properties.set("persistent", properties.createString(persistent ? "true" : "false"));
/* 293 */     properties = properties.set("distance", properties.createString(Integer.toString(distance)));
/*     */     
/* 295 */     tag = input.emptyMap();
/* 296 */     tag = tag.set("Properties", properties);
/* 297 */     return tag.set("Name", tag.createString(blockName));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/* 302 */   public boolean isLog(int block) { return this.logIds.contains(block); }
/*     */ 
/*     */ 
/*     */   
/* 306 */   public boolean isLeaf(int block) { return this.leaveIds.contains(block); }
/*     */ 
/*     */   
/*     */   private int getDistance(int block) {
/* 310 */     if (isLog(block)) {
/* 311 */       return 0;
/*     */     }
/* 313 */     return Integer.parseInt(((Dynamic)this.palette.get(block)).get("Properties").get("distance").asString(""));
/*     */   }
/*     */   
/*     */   private void setDistance(int pos, int block, int distance) {
/* 317 */     Dynamic<?> baseTag = (Dynamic)this.palette.get(block);
/* 318 */     String blockName = baseTag.get("Name").asString("");
/* 319 */     boolean persistent = Objects.equals(baseTag.get("Properties").get("persistent").asString(""), "true");
/* 320 */     int stateId = getStateId(blockName, persistent, distance);
/*     */     
/* 322 */     if (!this.stateToIdMap.containsKey(stateId)) {
/* 323 */       int id = this.palette.size();
/* 324 */       this.leaveIds.add(id);
/* 325 */       this.stateToIdMap.put(stateId, id);
/* 326 */       this.palette.add(makeLeafTag(baseTag, blockName, persistent, distance));
/*     */     } 
/*     */     
/* 329 */     int id = this.stateToIdMap.get(stateId);
/* 330 */     if (1 << this.storage.getBits() <= id) {
/* 331 */       PackedBitStorage newStorage = new PackedBitStorage(this.storage.getBits() + 1, 4096);
/* 332 */       for (int i = 0; i < 4096; i++) {
/* 333 */         newStorage.set(i, this.storage.get(i));
/*     */       }
/* 335 */       this.storage = newStorage;
/*     */     } 
/* 337 */     this.storage.set(pos, id);
/*     */   }
/*     */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraf\\util\datafix\fixes\LeavesFix$LeavesSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
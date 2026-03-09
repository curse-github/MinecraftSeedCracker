/*    */ package net.minecraft.world.entity.ai.gossip;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import java.util.UUID;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.stream.Stream;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class EntityGossips
/*    */ {
/* 50 */   private final Object2IntMap<GossipType> entries = new Object2IntOpenHashMap();
/*    */ 
/*    */   
/* 53 */   public int weightedValue(Predicate<GossipType> types) { return this.entries.object2IntEntrySet()
/* 54 */       .stream()
/* 55 */       .filter(e -> types.test((GossipType)e.getKey()))
/* 56 */       .mapToInt(e -> e.getIntValue() * ((GossipType)e.getKey()).weight)
/* 57 */       .sum(); }
/*    */ 
/*    */ 
/*    */   
/* 61 */   public Stream<GossipContainer.GossipEntry> unpack(UUID target) { return this.entries.object2IntEntrySet().stream().map(e -> new GossipContainer.GossipEntry(target, (GossipType)e.getKey(), e.getIntValue())); }
/*    */ 
/*    */   
/*    */   public void decay() {
/* 65 */     ObjectIterator<Object2IntMap.Entry<GossipType>> it = this.entries.object2IntEntrySet().iterator();
/* 66 */     while (it.hasNext()) {
/* 67 */       Object2IntMap.Entry<GossipType> gossip = (Object2IntMap.Entry)it.next();
/* 68 */       int newValue = gossip.getIntValue() - ((GossipType)gossip.getKey()).decayPerDay;
/* 69 */       if (newValue < 2) {
/* 70 */         it.remove(); continue;
/*    */       } 
/* 72 */       gossip.setValue(newValue);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/* 78 */   public boolean isEmpty() { return this.entries.isEmpty(); }
/*    */ 
/*    */   
/*    */   public void makeSureValueIsntTooLowOrTooHigh(GossipType type) {
/* 82 */     int value = this.entries.getInt(type);
/* 83 */     if (value > type.max) {
/* 84 */       this.entries.put(type, type.max);
/*    */     }
/* 86 */     if (value < 2) {
/* 87 */       remove(type);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/* 92 */   public void remove(GossipType type) { this.entries.removeInt(type); }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\world\entity\ai\gossip\GossipContainer$EntityGossips.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
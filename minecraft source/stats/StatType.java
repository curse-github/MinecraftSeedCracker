/*    */ package net.minecraft.stats;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class StatType<T> extends Object implements Iterable<Stat<T>> {
/*    */   private final Registry<T> registry;
/*    */   private final Map<T, Stat<T>> map;
/*    */   
/*    */   public StatType(Registry<T> registry, Component displayName) {
/* 15 */     this.map = new IdentityHashMap();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 21 */     this.registry = registry;
/* 22 */     this.displayName = displayName;
/* 23 */     this.streamCodec = ByteBufCodecs.registry(registry.key()).map(this::get, Stat::getValue);
/*    */   }
/*    */   private final Component displayName; private final StreamCodec<RegistryFriendlyByteBuf, Stat<T>> streamCodec;
/*    */   
/* 27 */   public StreamCodec<RegistryFriendlyByteBuf, Stat<T>> streamCodec() { return this.streamCodec; }
/*    */ 
/*    */ 
/*    */   
/* 31 */   public boolean contains(T key) { return this.map.containsKey(key); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 36 */   public Stat<T> get(T argument, StatFormatter formatter) { return (Stat)this.map.computeIfAbsent(argument, t -> new Stat(this, t, formatter)); }
/*    */ 
/*    */ 
/*    */   
/* 40 */   public Registry<T> getRegistry() { return this.registry; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 45 */   public Iterator<Stat<T>> iterator() { return this.map.values().iterator(); }
/*    */ 
/*    */ 
/*    */   
/* 49 */   public Stat<T> get(T argument) { return get(argument, StatFormatter.DEFAULT); }
/*    */ 
/*    */ 
/*    */   
/* 53 */   public Component getDisplayName() { return this.displayName; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\stats\StatType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */
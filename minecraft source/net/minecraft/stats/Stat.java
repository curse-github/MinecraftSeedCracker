/*    */ package net.minecraft.stats;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*    */ 
/*    */ public class Stat<T>
/*    */   extends ObjectiveCriteria
/*    */ {
/* 15 */   public static final StreamCodec<RegistryFriendlyByteBuf, Stat<?>> STREAM_CODEC = ByteBufCodecs.registry(Registries.STAT_TYPE).dispatch(Stat::getType, StatType::streamCodec);
/*    */   
/*    */   private final StatFormatter formatter;
/*    */   private final T value;
/*    */   private final StatType<T> type;
/*    */   
/*    */   protected Stat(StatType<T> type, T value, StatFormatter formatter) {
/* 22 */     super(buildName(type, value));
/* 23 */     this.type = type;
/* 24 */     this.formatter = formatter;
/* 25 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/* 29 */   public static <T> String buildName(StatType<T> type, T value) { return locationToKey(BuiltInRegistries.STAT_TYPE.getKey(type)) + ":" + locationToKey(BuiltInRegistries.STAT_TYPE.getKey(type)); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 35 */   private static String locationToKey(Identifier location) { return location.toString().replace(':', '.'); }
/*    */ 
/*    */ 
/*    */   
/* 39 */   public StatType<T> getType() { return this.type; }
/*    */ 
/*    */ 
/*    */   
/* 43 */   public T getValue() { return (T)this.value; }
/*    */ 
/*    */ 
/*    */   
/* 47 */   public String format(int value) { return this.formatter.format(value); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 52 */   public boolean equals(Object o) { return (this == o || (o instanceof Stat && Objects.equals(getName(), ((Stat)o).getName()))); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 57 */   public int hashCode() { return getName().hashCode(); }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 63 */   public String toString() { return "Stat{name=" + getName() + ", formatter=" + String.valueOf(this.formatter) + "}"; }
/*    */ }


/* Location:              C:\Users\Curse\Desktop\servers\test\versions\1.21.11_unobfuscated\server-1.21.11_unobfuscated.jar!\net\minecraft\stats\Stat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.0.7
 */